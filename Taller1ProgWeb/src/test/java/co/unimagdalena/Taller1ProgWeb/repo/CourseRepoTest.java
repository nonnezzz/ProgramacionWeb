package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Course;
import co.unimagdalena.Taller1ProgWeb.model.Instructor;
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
 * Pruebas de integración para CourseRepo utilizando Testcontainers con PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CourseRepoTest {

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
    private CourseRepo courseRepo;

    @Autowired
    private InstructorRepo instructorRepo;

    @Test
    public void testFindByTitle() {
        // Paso 1: Crear un instructor para el curso
        Instructor instructor = new Instructor();
        instructor.setFullName("Profesor Ejemplo");
        instructor.setEmail("profesor@example.com");
        instructorRepo.save(instructor);

        // Paso 2: Crear un curso de prueba
        Course course = new Course();
        course.setTitle("Curso de Prueba");
        course.setInstructor(instructor);
        course.setStatus("ACTIVE");
        courseRepo.save(course);

        // Paso 3: Buscar por título
        Optional<Course> found = courseRepo.findByTitle("Curso de Prueba");

        // Paso 4: Verificar que se encontró
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Curso de Prueba");
    }
}