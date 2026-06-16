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


    public Coach(String name, String expertise, String phone) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
        this.availability = "Lunes a Viernes 09:00 - 18:00"; // default value
    }

    public void updateCoach(String name, String expertise, String phone, String availability) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
        this.availability = availability;
    }

    // Manual getters
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getExpertise() {
        return this.expertise;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAvailability() {
        return this.availability;
    }

    // Manual setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}