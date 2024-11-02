package com.bsn.book.book;

import org.springframework.stereotype.Service;

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

}
