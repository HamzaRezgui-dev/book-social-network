package com.bsn.book.book;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Tag(name = "Book", description = "Book API")
public class BookController {

    private final BookService service;

    @PostMapping("path")
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid  BookRequest request, Authentication connectedUser) {
        //TODO: process POST request
        
        return ResponseEntity.ok(service.save(request, connectedUser));
    }
    

}
