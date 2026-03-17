package com.noticeboard.dto;

import com.noticeboard.model.Notice.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;
    private Priority priority;
    private boolean isPinned;
    private boolean isActive;
    private LocalDateTime expiryDate;
    private String attachmentUrl;
    private int viewCount;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}