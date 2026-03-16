package co.unimagdalena.Taller1ProgWeb.service;

import co.unimagdalena.Taller1ProgWeb.model.Enrollment;
import co.unimagdalena.Taller1ProgWeb.model.Student;
import co.unimagdalena.Taller1ProgWeb.repo.EnrollmentRepo;
import co.unimagdalena.Taller1ProgWeb.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la lógica de negocio relacionada con estudiantes.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    /**
     * Obtiene la lista de estudiantes que tienen al menos una matrícula (suscritos).
     * Devuelve estudiantes únicos.
     *
     * @return Lista de estudiantes suscritos
     */
    public List<Student> getSubscribedStudents() {
        return enrollmentRepo.findAll()
                .stream()
                .map(Enrollment::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }
}