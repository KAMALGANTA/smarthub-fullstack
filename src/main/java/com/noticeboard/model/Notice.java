package com.noticeboard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "is_pinned")
    private boolean isPinned = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority = Priority.NORMAL;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Priority {
        LOW, NORMAL, HIGH, URGENT
    }
}