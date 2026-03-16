package co.unimagdalena.Taller1ProgWeb.service;

import co.unimagdalena.Taller1ProgWeb.model.Enrollment;
import co.unimagdalena.Taller1ProgWeb.model.Student;
import co.unimagdalena.Taller1ProgWeb.repo.EnrollmentRepo;
import co.unimagdalena.Taller1ProgWeb.repo.StudentRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la clase StudentService.
 * Verifica la lógica de negocio relacionada con estudiantes.
 */
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private EnrollmentRepo enrollmentRepo;

    @InjectMocks
    private StudentService studentService;

    /**
     * Prueba el método getSubscribedStudents().
     * Verifica que devuelve estudiantes únicos que tienen al menos una matrícula.
     */
    @Test
    public void testGetSubscribedStudents() {
        // Paso 1: Crear estudiantes mock
        Student student1 = new Student();
        student1.setFullName("Estudiante Suscrito");
        student1.setEmail("suscrito@example.com");

        Student student2 = new Student();
        student2.setFullName("Estudiante No Suscrito");
        student2.setEmail("nosuscrito@example.com");

        // Paso 2: Crear enrollments mock
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student1); // Mismo estudiante en dos cursos

        // Paso 3: Mockear el comportamiento del repo
        when(enrollmentRepo.findAll()).thenReturn(Arrays.asList(enrollment1, enrollment2));

        // Paso 4: Llamar al servicio
        List<Student> subscribedStudents = studentService.getSubscribedStudents();

        // Paso 5: Verificar que devuelve estudiantes únicos suscritos
        assertThat(subscribedStudents).hasSize(1);
        assertThat(subscribedStudents.get(0).getFullName()).isEqualTo("Estudiante Suscrito");
    }
}