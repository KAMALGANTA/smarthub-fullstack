package com.noticeboard.service;

import com.noticeboard.dto.NoticeDTO;
import com.noticeboard.model.Category;
import com.noticeboard.model.Notice;
import com.noticeboard.model.Notice.Priority;
import com.noticeboard.model.User;
import com.noticeboard.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<NoticeDTO> getAllActiveNotices() {
        return noticeRepository.findAllActiveNonExpired(LocalDateTime.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findByIsActiveTrueOrderByIsPinnedDescCreatedAtDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NoticeDTO getNoticeById(Long id) {
        Notice notice = findById(id);
        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);
        return toDTO(notice);
    }

    public List<NoticeDTO> getNoticesByCategory(Long categoryId) {
        return noticeRepository.findByCategoryIdAndIsActiveTrueOrderByCreatedAtDesc(categoryId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NoticeDTO> getPinnedNotices() {
        return noticeRepository.findByIsPinnedTrueAndIsActiveTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NoticeDTO> getNoticesByPriority(Priority priority) {
        return noticeRepository.findByPriorityAndIsActiveTrue(priority)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NoticeDTO> searchNotices(String keyword) {
        return noticeRepository.searchByKeyword(keyword)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NoticeDTO createNotice(NoticeDTO dto, String username) {
        Category category = categoryService.getCategoryById(dto.getCategoryId());
        User user = userService.getUserByUsername(username);

        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(category)
                .createdBy(user)
                .priority(dto.getPriority() != null ? dto.getPriority() : Priority.NORMAL)
                .isPinned(dto.isPinned())
                .isActive(true)
                .expiryDate(dto.getExpiryDate())
                .attachmentUrl(dto.getAttachmentUrl())
                .build();

        return toDTO(noticeRepository.save(notice));
    }

    public NoticeDTO updateNotice(Long id, NoticeDTO dto) {
        Notice existing = findById(id);
        Category category = categoryService.getCategoryById(dto.getCategoryId());

        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());
        existing.setCategory(category);
        existing.setPriority(dto.getPriority() != null ? dto.getPriority() : Priority.NORMAL);
        existing.setPinned(dto.isPinned());
        existing.setActive(dto.isActive());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setAttachmentUrl(dto.getAttachmentUrl());

        return toDTO(noticeRepository.save(existing));
    }

    public void deleteNotice(Long id) {
        Notice notice = findById(id);
        notice.setActive(false);
        noticeRepository.save(notice);
    }

    public void hardDeleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    private Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
    }

    private NoticeDTO toDTO(Notice notice) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setPriority(notice.getPriority());
        dto.setPinned(notice.isPinned());
        dto.setActive(notice.isActive());
        dto.setExpiryDate(notice.getExpiryDate());
        dto.setAttachmentUrl(notice.getAttachmentUrl());
        dto.setViewCount(notice.getViewCount());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setUpdatedAt(notice.getUpdatedAt());
        if (notice.getCategory() != null) {
            dto.setCategoryId(notice.getCategory().getId());
            dto.setCategoryName(notice.getCategory().getName());
        }
        if (notice.getCreatedBy() != null) {
            dto.setCreatedByUsername(notice.getCreatedBy().getUsername());
        }
        return dto;
    }
}