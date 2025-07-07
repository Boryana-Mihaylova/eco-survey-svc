package app.model.repository;

import app.model.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, UUID> {

    Optional<Survey> findByUserId(UUID userId);

    @Query("SELECT s.subject, COUNT(s) FROM Survey s GROUP BY s.subject")
    List<Object[]> countVotesBySubject();

}
