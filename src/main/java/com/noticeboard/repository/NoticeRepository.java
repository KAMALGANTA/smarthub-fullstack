package com.noticeboard.repository;

import com.noticeboard.model.Notice;
import com.noticeboard.model.Notice.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByIsActiveTrueOrderByIsPinnedDescCreatedAtDesc();
    List<Notice> findByCategoryIdAndIsActiveTrueOrderByCreatedAtDesc(Long categoryId);
    List<Notice> findByCreatedByIdOrderByCreatedAtDesc(Long userId);
    List<Notice> findByIsPinnedTrueAndIsActiveTrue();
    List<Notice> findByPriorityAndIsActiveTrue(Priority priority);

    @Query("SELECT n FROM Notice n WHERE n.isActive = true AND " +
           "(n.expiryDate IS NULL OR n.expiryDate > :now) " +
           "ORDER BY n.isPinned DESC, n.createdAt DESC")
    List<Notice> findAllActiveNonExpired(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notice n WHERE n.isActive = true AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Notice> searchByKeyword(@Param("keyword") String keyword);

    long countByIsActiveTrue();
    long countByCategoryId(Long categoryId);
}