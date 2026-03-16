package co.unimagdalena.Taller1ProgWeb.repo;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Evitar configuración automática de DB
@Testcontainers // Anotación para habilitar Testcontainers en el test
public class StudentRepoTest {

    // Contenedor de PostgreSQL para pruebas
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // Configurar propiedades dinámicas para Spring usar la base de datos del contenedor
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Crear y eliminar tablas automáticamente
        // Configuraciones para el pool de conexiones HikariCP para reducir warnings
        registry.add("spring.datasource.hikari.max-lifetime", () -> "30000"); // Máximo tiempo de vida de conexión en ms
        registry.add("spring.datasource.hikari.connection-timeout", () -> "5000"); // Timeout para obtener conexión
        registry.add("spring.datasource.hikari.validation-timeout", () -> "1000"); // Timeout para validar conexión
    }

    @Autowired
    private StudentRepo studentRepo;

    @Test
    public void testFindByEmail() {
        // Paso 1: Crear un estudiante de prueba con datos básicos
        Student student = new Student();
        student.setFullName("Juan Pérez");
        student.setEmail("juan.perez@example.com");

        // Paso 2: Guardar el estudiante en la base de datos usando el repositorio
        studentRepo.save(student);

        // Paso 3: Buscar el estudiante por email usando el método del repositorio
        Optional<Student> found = studentRepo.findByEmail("juan.perez@example.com");

        // Paso 4: Verificar que el estudiante fue encontrado y que los datos coinciden
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Juan Pérez");
    }

    @Test
    public void testFindByFullName() {
        // Paso 1: Crear un estudiante de prueba con datos básicos
        Student student = new Student();
        student.setFullName("María García");
        student.setEmail("maria.garcia@example.com");

        // Paso 2: Guardar el estudiante en la base de datos usando el repositorio
        studentRepo.save(student);

        // Paso 3: Buscar el estudiante por nombre completo usando el método del repositorio
        Optional<Student> found = studentRepo.findByFullName("María García");

        // Paso 4: Verificar que el estudiante fue encontrado y que los datos coinciden
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("maria.garcia@example.com");
    }
}