package com.upc.matchpoint.iam.infrastructure.persistence.seed;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.entities.CoachService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository;
import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.entities.Role;
import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import com.upc.matchpoint.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.matchpoint.reviews.domain.model.aggregates.Review;
import com.upc.matchpoint.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!test")
@Transactional
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptHashingService hashingService;
    private final UserProfileRepository userProfileRepository;
    private final CoachRepository coachRepository;
    private final CoachServiceRepository coachServiceRepository;
    private final CourtRepository courtRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public DataSeeder(RoleRepository roleRepository,
                      UserRepository userRepository,
                      BCryptHashingService hashingService,
                      UserProfileRepository userProfileRepository,
                      CoachRepository coachRepository,
                      CoachServiceRepository coachServiceRepository,
                      CourtRepository courtRepository,
                      BookingRepository bookingRepository,
                      ReviewRepository reviewRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.userProfileRepository = userProfileRepository;
        this.coachRepository = coachRepository;
        this.coachServiceRepository = coachServiceRepository;
        this.courtRepository = courtRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) {
        // Ensure roles exist
        ensureRole(Roles.ROLE_USER);
        ensureRole(Roles.ROLE_ADMIN);
        ensureRole(Roles.ROLE_INSTRUCTOR);

        var userRole = roleRepository.findByName(Roles.ROLE_USER).orElseThrow();
        var instructorRole = roleRepository.findByName(Roles.ROLE_INSTRUCTOR).orElseThrow();
        var adminRole = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow();

        // 1. IAM Users
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", hashingService.encode("admin"), List.of(adminRole)));
        }

        if (!userRepository.existsByUsername("atleta1")) {
            userRepository.save(new User("atleta1", hashingService.encode("123456"), List.of(userRole)));
        }
        if (!userRepository.existsByUsername("atleta2")) {
            userRepository.save(new User("atleta2", hashingService.encode("123456"), List.of(userRole)));
        }
        if (!userRepository.existsByUsername("atleta3")) {
            userRepository.save(new User("atleta3", hashingService.encode("123456"), List.of(userRole)));
        }

        if (!userRepository.existsByUsername("coach1")) {
            userRepository.save(new User("coach1", hashingService.encode("123456"), List.of(instructorRole)));
        }
        if (!userRepository.existsByUsername("coach2")) {
            userRepository.save(new User("coach2", hashingService.encode("123456"), List.of(instructorRole)));
        }

        // 2. User Profiles
        if (userProfileRepository.count() == 0) {
            userProfileRepository.save(new UserProfile("Carlos Mendoza", "atleta1@matchpoint.com", "999-001"));
            userProfileRepository.save(new UserProfile("Ana García", "atleta2@matchpoint.com", "999-002"));
            userProfileRepository.save(new UserProfile("Juan Pérez", "atleta3@matchpoint.com", "999-003"));
        }

        // 3. Coaches
        if (coachRepository.count() == 0) {
            var coach1 = new Coach(
                    "Pedro Sánchez", "Tenis", "998-001", "coach1@matchpoint.com",
                    "TENNIS", 80.0, "Miraflores", "Entrenador certificado con 8 años de experiencia en tenis.",
                    "https://images.unsplash.com/photo-1595152772835-219674b2a8a6", true, 8
            );
            coach1.setRating(4.5);
            coach1.setTotalReviews(1);
            coachRepository.save(coach1);

            var coach2 = new Coach(
                    "María López", "Fútbol", "998-002", "coach2@matchpoint.com",
                    "FOOTBALL", 60.0, "San Isidro", "Especialista en fundamentos técnicos de fútbol infantil y juvenil.",
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb", true, 5
            );
            coach2.setRating(4.2);
            coach2.setTotalReviews(1);
            coachRepository.save(coach2);
        }

        // 4. Coach Services
        if (coachServiceRepository.count() == 0) {
            var pedro = coachRepository.findAll().stream().filter(c -> c.getName().equals("Pedro Sánchez")).findFirst().orElse(null);
            var maria = coachRepository.findAll().stream().filter(c -> c.getName().equals("María López")).findFirst().orElse(null);

            if (pedro != null) {
                coachServiceRepository.save(new CoachService(pedro, "Clase individual", "Entrenamiento personalizado de 1 hora", 80.0));
                coachServiceRepository.save(new CoachService(pedro, "Clase grupal", "Entrenamiento en grupo hasta 4 personas", 50.0));
            }
            if (maria != null) {
                coachServiceRepository.save(new CoachService(maria, "Técnica básica", "Fundamentos y dominio del balón", 60.0));
            }
        }

        // 5. Courts
        if (courtRepository.count() == 0) {
            courtRepository.save(new Court(
                    "Cancha Miraflores 1", "Miraflores", "Indoor", "TENNIS",
                    40.0, "Cancha de tenis techada de superficie dura con iluminación LED.",
                    "https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0", true, "06:00-22:00"
            ));
            courtRepository.save(new Court(
                    "Cancha San Isidro A", "San Isidro", "Outdoor", "FOOTBALL",
                    30.0, "Cancha de fútbol 7 con césped sintético de última generación.",
                    "https://images.unsplash.com/photo-1518063319789-7217e6706b04", true, "07:00-21:00"
            ));
            courtRepository.save(new Court(
                    "Cancha Barranco B", "Barranco", "Outdoor", "VOLLEYBALL",
                    25.0, "Cancha de arena para voley playa de uso recreativo.",
                    "https://images.unsplash.com/photo-1530541930197-ff16ac917b0e", false, "08:00-20:00"
            ));
        }

        // 6. Bookings
        if (bookingRepository.count() == 0) {
            var carlos = userProfileRepository.findAll().stream().filter(u -> u.getName().equals("Carlos Mendoza")).findFirst().orElse(null);
            var ana = userProfileRepository.findAll().stream().filter(u -> u.getName().equals("Ana García")).findFirst().orElse(null);
            var courtTennis = courtRepository.findAll().stream().filter(c -> c.getName().equals("Cancha Miraflores 1")).findFirst().orElse(null);
            var courtFootball = courtRepository.findAll().stream().filter(c -> c.getName().equals("Cancha San Isidro A")).findFirst().orElse(null);
            var serviceFutbol = coachServiceRepository.findAll().stream().filter(s -> s.getName().equals("Técnica básica")).findFirst().orElse(null);

            if (carlos != null && courtTennis != null) {
                var booking1 = new Booking(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                        LocalDateTime.now().plusDays(1).withHour(11).withMinute(0),
                        carlos, courtTennis, null, 40.0);
                booking1.setStatus("PENDING");
                bookingRepository.save(booking1);
            }

            if (ana != null && serviceFutbol != null) {
                var booking2 = new Booking(LocalDateTime.now().plusDays(1).withHour(15).withMinute(0),
                        LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                        ana, null, serviceFutbol, 60.0);
                booking2.setStatus("CONFIRMED");
                bookingRepository.save(booking2);
            }

            if (carlos != null && courtFootball != null) {
                var booking3 = new Booking(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0),
                        LocalDateTime.now().plusDays(2).withHour(10).withMinute(0),
                        carlos, courtFootball, null, 30.0);
                booking3.setStatus("CONFIRMED");
                bookingRepository.save(booking3);
            }
        }

        // 7. Reviews
        if (reviewRepository.count() == 0) {
            var carlos = userProfileRepository.findAll().stream().filter(u -> u.getName().equals("Carlos Mendoza")).findFirst().orElse(null);
            var ana = userProfileRepository.findAll().stream().filter(u -> u.getName().equals("Ana García")).findFirst().orElse(null);
            var pedro = coachRepository.findAll().stream().filter(c -> c.getName().equals("Pedro Sánchez")).findFirst().orElse(null);
            var maria = coachRepository.findAll().stream().filter(c -> c.getName().equals("María López")).findFirst().orElse(null);

            if (carlos != null && pedro != null) {
                reviewRepository.save(new Review(pedro.getId(), carlos.getId(), 5, "Excelente profesor, muy paciente y con gran técnica."));
            }
            if (ana != null && maria != null) {
                reviewRepository.save(new Review(maria.getId(), ana.getId(), 4, "Buena técnica, clases muy dinámicas y recomendadas."));
            }
        }
    }

    private void ensureRole(Roles role) {
        if (!roleRepository.existsByName(role)) {
            roleRepository.save(new Role(role));
        }
    }
}
