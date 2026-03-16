package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Course;
import co.unimagdalena.Taller1ProgWeb.model.Instructor;
import co.unimagdalena.Taller1ProgWeb.model.Lesson;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para LessonRepo utilizando Testcontainers con PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class LessonRepoTest {

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
    private LessonRepo lessonRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private InstructorRepo instructorRepo;

    @Test
    public void testFindByCourseIdOrderByOrderIndexAsc() {
        // Paso 1: Crear instructor y curso
        Instructor instructor = new Instructor();
        instructor.setFullName("Instructor Martínez");
        instructor.setEmail("martinez@example.com");
        instructorRepo.save(instructor);

        Course course = new Course();
        course.setTitle("Curso con Lecciones");
        course.setInstructor(instructor);
        course.setStatus("ACTIVE");
        courseRepo.save(course);

        // Paso 2: Crear lecciones para el curso
        Lesson lesson1 = new Lesson();
        lesson1.setCourse(course);
        lesson1.setTitle("Lección 1");
        lesson1.setOrderIndex(1);
        lessonRepo.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course);
        lesson2.setTitle("Lección 2");
        lesson2.setOrderIndex(2);
        lessonRepo.save(lesson2);

        // Paso 3: Buscar lecciones por curso, ordenadas por índice
        List<Lesson> lessons = lessonRepo.findByCourseIdOrderByOrderIndexAsc(course.getId());

        // Paso 4: Verificar orden
        assertThat(lessons).hasSize(2);
        assertThat(lessons.get(0).getOrderIndex()).isEqualTo(1);
        assertThat(lessons.get(1).getOrderIndex()).isEqualTo(2);
    }
}