package com.noticeboard.controller;

import com.noticeboard.dto.NoticeDTO;
import com.noticeboard.model.Notice.Priority;
import com.noticeboard.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllActiveNotices());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NoticeDTO>> getAllNoticesAdmin() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<NoticeDTO>> getNoticesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(noticeService.getNoticesByCategory(categoryId));
    }

    @GetMapping("/pinned")
    public ResponseEntity<List<NoticeDTO>> getPinnedNotices() {
        return ResponseEntity.ok(noticeService.getPinnedNotices());
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<NoticeDTO>> getByPriority(@PathVariable Priority priority) {
        return ResponseEntity.ok(noticeService.getNoticesByPriority(priority));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoticeDTO>> searchNotices(@RequestParam String keyword) {
        return ResponseEntity.ok(noticeService.searchNotices(keyword));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NoticeDTO> createNotice(@Valid @RequestBody NoticeDTO dto,
                                                   Authentication authentication) {
        return ResponseEntity.status(201).body(
                noticeService.createNotice(dto, authentication.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable Long id,
                                                   @Valid @RequestBody NoticeDTO dto) {
        return ResponseEntity.ok(noticeService.updateNotice(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok(Map.of("message", "Notice deactivated successfully"));
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> hardDeleteNotice(@PathVariable Long id) {
        noticeService.hardDeleteNotice(id);
        return ResponseEntity.ok(Map.of("message", "Notice permanently deleted"));
    }
}