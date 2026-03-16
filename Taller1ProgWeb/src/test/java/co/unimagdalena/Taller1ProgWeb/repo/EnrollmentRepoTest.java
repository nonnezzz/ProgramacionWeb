package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Course;
import co.unimagdalena.Taller1ProgWeb.model.Enrollment;
import co.unimagdalena.Taller1ProgWeb.model.Instructor;
import co.unimagdalena.Taller1ProgWeb.model.Student;
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
 * Pruebas de integración para EnrollmentRepo utilizando Testcontainers con PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class EnrollmentRepoTest {

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
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private InstructorRepo instructorRepo;

    @Test
    public void testFindByStudentId() {
        // Paso 1: Crear estudiante
        Student student = new Student();
        student.setFullName("Estudiante Uno");
        student.setEmail("uno@example.com");
        studentRepo.save(student);

        // Paso 2: Crear instructor y curso
        Instructor instructor = new Instructor();
        instructor.setFullName("Instructor Uno");
        instructor.setEmail("instuno@example.com");
        instructorRepo.save(instructor);

        Course course = new Course();
        course.setTitle("Curso Uno");
        course.setInstructor(instructor);
        course.setStatus("ACTIVE");
        courseRepo.save(course);

        // Paso 3: Crear matrícula
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");
        enrollmentRepo.save(enrollment);

        // Paso 4: Buscar matrículas por estudiante
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(student.getId());

        // Paso 5: Verificar
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getStatus()).isEqualTo("ENROLLED");
    }
}