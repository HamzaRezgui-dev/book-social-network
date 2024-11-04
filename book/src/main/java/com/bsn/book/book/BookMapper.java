package com.bsn.book.book;

import org.springframework.stereotype.Service;

import com.bsn.book.file.FileUtils;
import com.bsn.book.history.BookTransactionHistory;

@Service
public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
            .id(request.bookId())
            .title(request.title())
            .authorName(request.authorName())
            .isbn(request.isbn())
            .synopsis(request.synopsis())
            .archived(false)
            .shareable(request.shareable())
            .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
            .bookId(book.getId())
            .title(book.getTitle())
            .authorName(book.getAuthorName())
            .isbn(book.getIsbn())
            .synopsis(book.getSynopsis())
            .owner(book.getOwner().getUsername())
            .cover(FileUtils.readFileFromLocation(book.getBookCover()))
            .rate(book.getRate())
            .archived(book.isArchived())
            .shareable(book.isShareable())
            .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
            .bookId(history.getBook().getId())
            .title(history.getBook().getTitle())
            .authorName(history.getBook().getAuthorName())
            .isbn(history.getBook().getIsbn())
            .rate(history.getBook().getRate())
            .returned(history.isReturned())
            .returnApproved(history.isReturnApproved())
            .build();
    }

}
