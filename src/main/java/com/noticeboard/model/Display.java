package com.noticeboard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "displays")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Display {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String location;

    @Column(name = "screen_type", length = 50)
    private String screenType;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "last_ping")
    private LocalDateTime lastPing;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "resolution", length = 50)
    private String resolution;

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
}