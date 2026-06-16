package com.upc.matchpoint.bookings.interfaces.rest.transform;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.interfaces.rest.resources.BookingResource;

public class BookingResourceFromEntityAssembler {
    public static BookingResource toResourceFromEntity(Booking entity) {
        BookingResource.UserSummaryResource userSummary = null;
        if (entity.getUser() != null) {
            userSummary = new BookingResource.UserSummaryResource(
                    entity.getUser().getId(),
                    entity.getUser().getName()
            );
        }
        
        BookingResource.CourtSummaryResource courtSummary = null;
        if (entity.getCourt() != null) {
            courtSummary = new BookingResource.CourtSummaryResource(
                    entity.getCourt().getId(),
                    entity.getCourt().getName()
            );
        }
        
        BookingResource.CoachServiceSummaryResource coachServiceSummary = null;
        if (entity.getCoachService() != null) {
            var coachSummary = new BookingResource.CoachSummaryResource(
                    entity.getCoachService().getCoach().getId(),
                    entity.getCoachService().getCoach().getName()
            );
            coachServiceSummary = new BookingResource.CoachServiceSummaryResource(
                    entity.getCoachService().getId(),
                    entity.getCoachService().getName(),
                    entity.getCoachService().getPrice(),
                    coachSummary
            );
        }

        return new BookingResource(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                userSummary,
                courtSummary,
                coachServiceSummary,
                entity.getAmount(),
                entity.getStatus()
        );
    }
}

