package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, UUID> {
    Optional<Instructor> findByEmail(String email);
    Optional<Instructor> findByFullName(String fullName);
}