package persistence.dao.card;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import persistence.api.card.LibraryCardEntity;
import persistence.api.card.LibraryCardEntryEntity.Status;

public interface LibraryCardDAO extends CrudRepository<LibraryCardEntity, Long> {
    @Query("SELECT DISTINCT card FROM LibraryCardEntity card "
            + "JOIN card.student student "
            + "RIGHT JOIN card.entries entry "
            + "JOIN entry.volume volume "
            + "JOIN volume.authors author WHERE "
                + "entry.status IN (:statuses) AND "
                + "("
                    + "card.creationDate >= :from AND "
                    + "card.expiryDate <= :to"
                + ") AND "
                + "("
                    + "LOWER(CONCAT(student.firstname, ' ', student.lastname)) LIKE LOWER(CONCAT('%', :pattern, '%')) OR "
                    + "LOWER(student.email) LIKE LOWER(CONCAT ('%', :pattern, '%')) OR "
                    + "LOWER(volume.title) LIKE LOWER(CONCAT ('%', :pattern, '%')) OR "
                    + "LOWER(volume.isbn) LIKE LOWER(CONCAT ('%', :pattern, '%')) OR "
                    + "LOWER(author) LIKE LOWER(CONCAT('%', :pattern, '%'))"
                + ")"
                )
    Page<LibraryCardEntity> findByPattern(@Param("pattern") String pattern, @Param("from") Date from, @Param("to") Date to, @Param("statuses") List<Status> statuses, Pageable pageable);

}
