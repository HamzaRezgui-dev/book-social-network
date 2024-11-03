package com.bsn.book.book;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bsn.book.common.PageResponse;
import com.bsn.book.exception.OperationNotPermittedException;
import com.bsn.book.history.BookTransactionHistory;
import com.bsn.book.history.BookTransactionHistoryRepository;
import com.bsn.book.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    private final BookTransactionHistoryRepository historyRepository;

    private final BookMapper bookMapper;

    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return repository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return repository.findById(bookId).map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = repository.findAllDisplayableBooks(user.getUserId(), pageable);
        List<BookResponse> content = books.stream().map(bookMapper::toBookResponse).collect(Collectors.toList());

        return new PageResponse<>(
                content,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());

    }

    public PageResponse<BookResponse> findBooksByOwner(Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = repository.findAll(BookSpecification.withOwnerId(user.getUserId()), pageable);
        List<BookResponse> content = books.stream().map(bookMapper::toBookResponse).collect(Collectors.toList());

        return new PageResponse<>(
                content,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(Integer page, Integer size,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = historyRepository.findAllBorrowedBooks(user.getUserId(), pageable);
        List<BorrowedBookResponse> bookResponse = borrowedBooks.stream().map(bookMapper::toBorrowedBookResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookResponse,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast());
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(Integer page, Integer size,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = historyRepository.findAllReturnedBooks(user.getUserId(), pageable);
        List<BorrowedBookResponse> bookResponse = borrowedBooks.stream().map(bookMapper::toBorrowedBookResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookResponse,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast());
    }

    public Integer updateShareable(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException());
        if (!Objects.equals(book.getOwner().getUserId(), user.getUserId())) {
            throw new OperationNotPermittedException("You cannot update books shareable status");
        }
        book.setShareable(!book.isShareable());
        repository.save(book);
        return book.getId();
    }

    public Integer updateArchived(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException());
        if (!Objects.equals(book.getOwner().getUserId(), user.getUserId())) {
            throw new OperationNotPermittedException("You cannot update books archived status");
        }
        book.setArchived(!book.isArchived());
        repository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "The requested book cannot be borrowed since it is archived or not shareable");
        }

        if (Objects.equals(book.getOwner().getUserId(), user.getUserId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowedByUser = historyRepository.isAlreadyBorrowedByUser(bookId,
                user.getUserId());

        if (isAlreadyBorrowedByUser) {
            throw new OperationNotPermittedException(
                    "You already borrowed this book and it is still not returned or the return is not approved by the owner");
        }

        final boolean isAlreadyBorrowedByOtherUser = historyRepository.isAlreadyBorrowed(bookId);

        if (isAlreadyBorrowedByOtherUser) {
            throw new OperationNotPermittedException("Te requested book is already borrowed by another user");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return historyRepository.save(bookTransactionHistory).getId();

    }

    public Integer returnBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        if (Objects.equals(book.getOwner().getUserId(), user.getUserId())) {
            throw new OperationNotPermittedException("You cannot return your own book");
        }

        BookTransactionHistory bookTransactionHistory = historyRepository.findByBookIdAndUserId(bookId,
                user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("No borrowed book found with ID:: " + bookId));

        bookTransactionHistory.setReturned(true);
        historyRepository.save(bookTransactionHistory);
        return bookTransactionHistory.getId();
    }

    public Integer approveReturnBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "The requested book cannot be borrowed since it is archived or not shareable");
        }
        
        if (!Objects.equals(book.getOwner().getUserId(), user.getUserId())) {
            throw new OperationNotPermittedException("You cannot approve return of a book you do not own");
        }

        BookTransactionHistory bookTransactionHistory = historyRepository.findByBookIdAndOwnerId(bookId, user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("No borrowed book found with ID:: " + bookId));

        bookTransactionHistory.setReturnApproved(true);
        historyRepository.save(bookTransactionHistory);
        return bookTransactionHistory.getId();
    }
}
