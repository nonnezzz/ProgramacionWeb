package co.unimagdalena.Taller1ProgWeb.repo;

import co.unimagdalena.Taller1ProgWeb.model.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorProfileRepo extends JpaRepository<InstructorProfile, UUID> {
    Optional<InstructorProfile> findByInstructorId(UUID instructorId);
    Optional<InstructorProfile> findByPhone(String phone);
}