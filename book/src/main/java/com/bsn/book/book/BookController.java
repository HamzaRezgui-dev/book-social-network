package com.bsn.book.book;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsn.book.common.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Tag(name = "Book", description = "Book API")
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest request, Authentication connectedUser) {

        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @GetMapping("all")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
        @RequestParam(name = "page", defaultValue = "0", required = false) Integer page, 
        @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
        Authentication connectedUser ) {
        return ResponseEntity.ok(service.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getBooksByOwner(
        @RequestParam(name = "page", defaultValue = "0", required = false) Integer page, 
        @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
        Authentication connectedUser ) {
        return ResponseEntity.ok(service.findBooksByOwner(page, size, connectedUser));
    }

}
