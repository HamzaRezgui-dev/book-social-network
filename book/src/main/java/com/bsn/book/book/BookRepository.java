package com.bsn.book.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b WHERE b.owner.userId != :userId AND b.archived = false AND b.shareable = true")
    Page<Book> findAllDisplayableBooks(Integer userId, Pageable pageable);

}
