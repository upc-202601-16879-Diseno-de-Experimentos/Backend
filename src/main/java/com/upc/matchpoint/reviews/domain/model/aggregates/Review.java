package com.upc.matchpoint.reviews.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long coachId;

    @Column(nullable = false)
    private Long userProfileId;

    @Column(nullable = false)
    private Integer rating; // 1-5

    @Column(nullable = true, length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Review(Long coachId, Long userProfileId, Integer rating, String comment) {
        this.coachId = coachId;
        this.userProfileId = userProfileId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public Long getCoachId() { return coachId; }
    public Long getUserProfileId() { return userProfileId; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }
    public void setUserProfileId(Long userProfileId) { this.userProfileId = userProfileId; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
