package com.upc.matchpoint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MatchpointApplicationTests {

    @Test
    @DisplayName("Debería cargar el contexto de la aplicación")
    void contextLoads() {
        // integrationTest
    }

}
