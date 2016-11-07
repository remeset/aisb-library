package persistence.dao.student;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import persistence.api.student.StudentEntity;

public interface StudentDAO extends CrudRepository<StudentEntity, Long> {
    @Query("SELECT DISTINCT student FROM StudentEntity student WHERE "
            + "LOWER(CONCAT(student.firstname, ' ', student.lastname)) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<StudentEntity> findByPattern(@Param("pattern") String pattern);

    StudentEntity findOneByEmail(String email);
}
