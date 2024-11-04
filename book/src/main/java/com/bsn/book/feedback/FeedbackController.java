package com.bsn.book.feedback;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsn.book.common.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
@Tag(name = "Feedback", description = "Feedback API")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> createFeedback(@Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.createFeedback(request, connectedUser));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByBook(@PathVariable Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.findFeedbacksByBook(bookId, page, size, connectedUser));
    }
}
