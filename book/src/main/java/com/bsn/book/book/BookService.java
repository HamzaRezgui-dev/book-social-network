package com.bsn.book.book;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bsn.book.common.PageResponse;
import com.bsn.book.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    private final BookMapper bookMapper;

    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return repository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return repository.findById(bookId).map(bookMapper::toBookResponse).orElseThrow(() -> new EntityNotFoundException());
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
            books.isLast()
        );
    
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
            books.isLast()
        );
        
    }
}
