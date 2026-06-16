package com.upc.matchpoint.coaches.domain.model.entities;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coach_services")
@Getter
@Setter
@NoArgsConstructor
public class CoachService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;


    public CoachService(Coach coach, String name, String description, Double price) {
        this.coach = coach;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Manual getters and setters just in case lombok fails
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Coach getCoach() { return coach; }
    public void setCoach(Coach coach) { this.coach = coach; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
