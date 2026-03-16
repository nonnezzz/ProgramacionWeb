package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByStudentId(UUID studentId);
    List<Assessment> findByCourseId(UUID courseId);
    Optional<Assessment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    List<Assessment> findByType(String type);
}