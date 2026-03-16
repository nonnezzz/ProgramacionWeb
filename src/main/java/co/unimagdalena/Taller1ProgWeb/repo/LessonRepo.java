package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, UUID> {
    List<Lesson> findByCourseIdOrderByOrderIndexAsc(UUID courseId);
    Optional<Lesson> findByCourseIdAndOrderIndex(UUID courseId, Integer orderIndex);
}
