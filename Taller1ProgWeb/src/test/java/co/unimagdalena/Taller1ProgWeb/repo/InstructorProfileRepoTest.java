package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Instructor;
import co.unimagdalena.Taller1ProgWeb.model.InstructorProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para InstructorProfileRepo utilizando Testcontainers con PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class InstructorProfileRepoTest {

    @Container	@SuppressWarnings("resource")    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.datasource.hikari.max-lifetime", () -> "30000");
        registry.add("spring.datasource.hikari.connection-timeout", () -> "5000");
        registry.add("spring.datasource.hikari.validation-timeout", () -> "1000");
    }

    @Autowired
    private InstructorProfileRepo instructorProfileRepo;

    @Autowired
    private InstructorRepo instructorRepo;

    @Test
    public void testFindByInstructorId() {
        // Paso 1: Crear un instructor
        Instructor instructor = new Instructor();
        instructor.setFullName("Instructor García");
        instructor.setEmail("garcia@example.com");
        instructorRepo.save(instructor);

        // Paso 2: Crear un perfil para ese instructor
        InstructorProfile profile = new InstructorProfile();
        profile.setInstructor(instructor);
        profile.setPhone("123-456-7890");
        profile.setBio("Bio del instructor");
        instructorProfileRepo.save(profile);

        // Paso 3: Buscar perfil por instructor ID
        Optional<InstructorProfile> found = instructorProfileRepo.findByInstructorId(instructor.getId());

        // Paso 4: Verificar que se encontró
        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("123-456-7890");
    }
}