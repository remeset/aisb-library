package persistence.dao.volume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import persistence.api.volume.VolumeEntity;

public interface VolumeDAO extends CrudRepository<VolumeEntity, Long> {

    /**
     * Please note: There is a small cheat here: as the query would become more complex
     */
    @Query("SELECT DISTINCT volume FROM VolumeEntity volume JOIN volume.authors author WHERE "
            + "LOWER(volume.title) LIKE LOWER(CONCAT ('%', :title, '%')) AND "
            + "LOWER(author) LIKE LOWER(CONCAT('%', :author, '%')) AND "
            + "LOWER(volume.isbn) LIKE LOWER(CONCAT('%', :isbn, '%'))")
    Page<VolumeEntity> findByTitleAndAuthorsAndISBN(@Param("title") String title, @Param("author") String author, @Param("isbn") String isbn, Pageable pageable);

    VolumeEntity findOneByIsbn(String isbn);

    VolumeEntity findOneByRemoteId(String remoteId);
}
