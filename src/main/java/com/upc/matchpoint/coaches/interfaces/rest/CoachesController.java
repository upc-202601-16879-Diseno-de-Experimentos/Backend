package com.upc.matchpoint.coaches.interfaces.rest;

import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachServiceCommand;
import com.upc.matchpoint.coaches.domain.model.commands.UpdateCoachServiceCommand;
import com.upc.matchpoint.coaches.domain.model.queries.*;
import com.upc.matchpoint.coaches.domain.services.CoachCommandService;
import com.upc.matchpoint.coaches.domain.services.CoachQueryService;
import com.upc.matchpoint.coaches.interfaces.rest.resources.*;
import com.upc.matchpoint.coaches.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/coaches", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Coaches", description = "Coach Management Endpoints")
public class CoachesController {
    private final CoachCommandService coachCommandService;
    private final CoachQueryService coachQueryService;

    public CoachesController(CoachCommandService coachCommandService, CoachQueryService coachQueryService) {
        this.coachCommandService = coachCommandService;
        this.coachQueryService = coachQueryService;
    }

    @PostMapping
    public ResponseEntity<CoachResource> createCoach(@RequestBody CreateCoachResource resource) {
        var command = CreateCoachCommandFromResourceAssembler.toCommandFromResource(resource);
        var coach = coachCommandService.handle(command);
        return coach.map(c -> new ResponseEntity<>(CoachResourceFromEntityAssembler.toResourceFromEntity(c), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<CoachResource>> getAllCoaches() {
        var query = new GetAllCoachesQuery();
        var coaches = coachQueryService.handle(query);
        var coachResources = coaches.stream()
                .map(CoachResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(coachResources);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CoachResource>> searchCoaches(
            @RequestParam(required = false) String sportType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating) {
        var query = new SearchCoachesQuery(sportType, location, maxPrice, minRating);
        var coaches = coachQueryService.handle(query);
        var coachResources = coaches.stream()
                .map(CoachResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(coachResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachResource> getCoachById(@PathVariable Long id) {
        var query = new GetCoachByIdQuery(id);
        var coach = coachQueryService.handle(query);
        return coach.map(c -> ResponseEntity.ok(CoachResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachResource> updateCoach(@PathVariable Long id, @RequestBody UpdateCoachResource resource) {
        var command = UpdateCoachCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedCoach = coachCommandService.handle(command);
        return updatedCoach.map(c -> ResponseEntity.ok(CoachResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        var command = new DeleteCoachCommand(id);
        coachCommandService.handle(command);
        return ResponseEntity.ok("Coach deleted successfully.");
    }

    // ---- Coach Services ----

    @PostMapping("/{coachId}/services")
    public ResponseEntity<CoachServiceResource> createCoachService(@PathVariable Long coachId, @RequestBody CreateCoachServiceResource resource) {
        var command = CreateCoachServiceCommandFromResourceAssembler.toCommandFromResource(coachId, resource);
        var service = coachCommandService.handle(command);
        return service.map(s -> new ResponseEntity<>(CoachServiceResourceFromEntityAssembler.toResourceFromEntity(s), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{coachId}/services")
    public ResponseEntity<List<CoachServiceResource>> getCoachServices(@PathVariable Long coachId) {
        var query = new GetCoachServicesByCoachIdQuery(coachId);
        var services = coachQueryService.handle(query);
        var resources = services.stream()
                .map(CoachServiceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/services/{serviceId}")
    public ResponseEntity<CoachServiceResource> updateCoachService(@PathVariable Long serviceId, @RequestBody UpdateCoachServiceResource resource) {
        var command = new UpdateCoachServiceCommand(serviceId, resource.name(), resource.description(), resource.price());
        var updated = coachCommandService.handle(command);
        return updated.map(s -> ResponseEntity.ok(CoachServiceResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<?> deleteCoachService(@PathVariable Long serviceId) {
        var command = new DeleteCoachServiceCommand(serviceId);
        coachCommandService.handle(command);
        return ResponseEntity.ok("Service deleted successfully.");
    }

    // ---- Coach Stats ----

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getCoachStats(@PathVariable Long id) {
        var coachOpt = coachQueryService.handle(new GetCoachByIdQuery(id));
        if (coachOpt.isEmpty()) return ResponseEntity.notFound().build();
        var coach = coachOpt.get();
        var stats = new java.util.HashMap<String, Object>();
        stats.put("coachId", coach.getId());
        stats.put("name", coach.getName());
        stats.put("rating", coach.getRating());
        stats.put("totalReviews", coach.getTotalReviews());
        stats.put("sportType", coach.getSportType());
        stats.put("pricePerHour", coach.getPricePerHour());
        stats.put("isAvailable", coach.getIsAvailable());
        return ResponseEntity.ok(stats);
    }
}
