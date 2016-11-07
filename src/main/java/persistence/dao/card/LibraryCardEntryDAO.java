package persistence.dao.card;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import persistence.api.card.LibraryCardEntryEntity;

public interface LibraryCardEntryDAO extends CrudRepository<LibraryCardEntryEntity, Long> {
    @Query("SELECT COALESCE(SUM(entry.amount), 0) FROM LibraryCardEntryEntity entry JOIN entry.volume volume WHERE volume.id = :volumeId")
    long findAmountSumByVolumeId(@Param("volumeId") Long volumeId);
}
