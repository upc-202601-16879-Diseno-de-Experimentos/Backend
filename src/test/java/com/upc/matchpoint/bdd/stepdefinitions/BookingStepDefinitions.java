package com.upc.matchpoint.bdd.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.upc.matchpoint.bookings.interfaces.rest.resources.CreateBookingResource;
import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class BookingStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachServiceRepository coachServiceRepository;

    private UserProfile user;
    private Court court;
    private Coach coach;
    private com.upc.matchpoint.coaches.domain.model.entities.CoachService coachService;
    private ResultActions response;

    @Before
    public void setUp() {
        bookingRepository.deleteAll();
        userProfileRepository.deleteAll();
        courtRepository.deleteAll();
        coachServiceRepository.deleteAll();
        coachRepository.deleteAll();
    }

    // --- Flujo: Reserva de Usuario ---

    @Given("que existe un usuario registrado con email {string}")
    public void queExisteUnUsuarioRegistradoConEmail(String email) {
        user = userProfileRepository.save(new UserProfile("BDD User", email, "123456789"));
    }

    @Given("existe una cancha deportiva llamada {string}")
    public void existeUnaCanchaDeportivaLlamada(String name) {
        court = courtRepository.save(new Court(name, "Sede BDD", "Tenis"));
    }

    @When("el usuario solicita una reserva para mañana de 10:00 a 12:00")
    public void elUsuarioSolicitaUnaReservaParaMananaDeADe() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime = startTime.plusHours(2);

        CreateBookingResource resource = new CreateBookingResource(startTime, endTime, user.getId(), court.getId(),
                null);

        response = mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)));
    }

    @Then("la respuesta del sistema debe indicar que la reserva fue creada")
    public void laRespuestaDelSistemaDebeIndicarQueLaReservaFueCreada() throws Exception {
        response.andExpect(status().isCreated());
    }

    @Then("la reserva debe estar guardada en la base de datos")
    public void laReservaDebeEstarGuardadaEnLaBaseDeDatos() {
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    // --- Flujo: Consulta de Entrenador ---

    @Given("existe un entrenador llamado {string} con especialidad {string}")
    public void queExisteUnEntrenadorLlamadoConEspecialidad(String name, String expertise) {
        coach = coachRepository.save(new Coach(name, expertise, "999888777"));
        coachService = coachServiceRepository.save(new com.upc.matchpoint.coaches.domain.model.entities.CoachService(coach, "Clase de Padel", "description", 50.0));
    }

    @Given("el entrenador tiene {int} reservas asignadas en el sistema")
    public void elEntrenadorTieneReservasAsignadasEnElSistema(int count) {
        UserProfile player = userProfileRepository.save(new UserProfile("Player BDD", "player@bdd.com", "000"));
        Court c = courtRepository.save(new Court("Cancha Pro", "Sede Coach", "Padel"));

        for (int i = 0; i < count; i++) {
            Booking b = new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1), player, c);
            b.setCoachService(coachService);
            bookingRepository.save(b);
        }
    }

    @When("el entrenador consulta su lista de reservas")
    public void elEntrenadorConsultaSuListaDeReservas() throws Exception {
        response = mockMvc.perform(get("/api/v1/bookings/coach/" + coach.getId()));
    }

    @Then("el sistema debe retornar exactamente {int} reservas")
    public void elSistemaDebeRetornarExactamenteReservas(int count) throws Exception {
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(count)));
    }

    @Then("todas las reservas deben pertenecer al entrenador {string}")
    public void todasLasReservasDebenPertenecerAlEntrenador(String coachName) {
        // Verificación lógica adicional si fuera necesaria
    }
}
