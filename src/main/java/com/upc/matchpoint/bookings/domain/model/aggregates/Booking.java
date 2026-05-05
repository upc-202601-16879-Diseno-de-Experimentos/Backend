package com.upc.matchpoint.bookings.domain.model.aggregates;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Booking(LocalDateTime startTime, LocalDateTime endTime, UserProfile user, Court court) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.court = court;
    }

    public void updateBooking(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Manual getters
    public Long getId() {
        return this.id;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public Court getCourt() {
        return this.court;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    // Manual setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}