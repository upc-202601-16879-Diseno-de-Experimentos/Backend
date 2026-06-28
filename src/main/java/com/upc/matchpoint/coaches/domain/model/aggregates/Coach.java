package com.upc.matchpoint.coaches.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String expertise;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String availability;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String sportType;

    @Column(nullable = true)
    private Double pricePerHour;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true, length = 1000)
    private String description;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Integer totalReviews = 0;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Column(nullable = true)
    private Integer experienceYears;

    public Coach(String name, String expertise, String phone) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
        this.availability = "Lunes a Viernes 09:00 - 18:00";
        this.rating = 0.0;
        this.totalReviews = 0;
        this.isAvailable = true;
    }

    public Coach(String name, String expertise, String phone, String email, String sportType,
                 Double pricePerHour, String location, String description, String imageUrl,
                 Boolean isAvailable, Integer experienceYears) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
        this.email = email;
        this.sportType = sportType;
        this.pricePerHour = pricePerHour;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable != null ? isAvailable : true;
        this.experienceYears = experienceYears;
        this.availability = "Lunes a Viernes 09:00 - 18:00";
        this.rating = 0.0;
        this.totalReviews = 0;
    }

    public void updateCoach(String name, String expertise, String phone, String availability,
                             String email, String sportType, Double pricePerHour, String location,
                             String description, String imageUrl, Boolean isAvailable, Integer experienceYears) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
        this.availability = availability;
        this.email = email;
        this.sportType = sportType;
        this.pricePerHour = pricePerHour;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
        if (isAvailable != null) this.isAvailable = isAvailable;
        this.experienceYears = experienceYears;
    }

    public void updateRating(Double newRating) {
        this.totalReviews = this.totalReviews + 1;
        this.rating = ((this.rating * (this.totalReviews - 1)) + newRating) / this.totalReviews;
    }

    // Manual getters
    public Long getId() { return this.id; }
    public String getName() { return this.name; }
    public String getExpertise() { return this.expertise; }
    public String getPhone() { return this.phone; }
    public String getAvailability() { return this.availability; }
    public String getEmail() { return this.email; }
    public String getSportType() { return this.sportType; }
    public Double getPricePerHour() { return this.pricePerHour; }
    public String getLocation() { return this.location; }
    public String getDescription() { return this.description; }
    public String getImageUrl() { return this.imageUrl; }
    public Double getRating() { return this.rating; }
    public Integer getTotalReviews() { return this.totalReviews; }
    public Boolean getIsAvailable() { return this.isAvailable; }
    public Integer getExperienceYears() { return this.experienceYears; }

    // Manual setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvailability(String availability) { this.availability = availability; }
    public void setEmail(String email) { this.email = email; }
    public void setSportType(String sportType) { this.sportType = sportType; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(Double rating) { this.rating = rating; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
}