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
import org.springframework.web.bind.annotation.PatchMapping;
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
            Authentication connectedUser) {
        return ResponseEntity.ok(service.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.findBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{bookId}")
    public ResponseEntity<Integer> updateShareable(@PathVariable Integer bookId,
            Authentication connectedUser) {
        
        return ResponseEntity.ok(service.updateShareable(bookId, connectedUser));
    }

    @PatchMapping("/archived/{bookId}")
    public ResponseEntity<Integer> updateArchived(@PathVariable Integer bookId,
            Authentication connectedUser) {
        
        return ResponseEntity.ok(service.updateArchived(bookId, connectedUser));
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<Integer> borrowBook(@PathVariable Integer bookId,
            Authentication connectedUser) {
        
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{bookId}")
    public ResponseEntity<Integer> returnBook(@PathVariable Integer bookId,
            Authentication connectedUser) {
        
        return ResponseEntity.ok(service.returnBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{bookId}")
    public ResponseEntity<Integer> approveReturnBook(@PathVariable Integer bookId,
            Authentication connectedUser) {
        
        return ResponseEntity.ok(service.approveReturnBook(bookId, connectedUser));
    }

}
