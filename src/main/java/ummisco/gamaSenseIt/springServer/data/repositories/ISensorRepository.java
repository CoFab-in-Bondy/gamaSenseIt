package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.util.List;

//@Repository
@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "sensors_rest", path = "sensors_rest")
@Repository
public interface ISensorRepository extends CrudRepository<Sensor, Long> {
    List<Sensor> findByName(String name);

    @Query(nativeQuery = true, value = """
        SELECT s.* FROM sensor s
            WHERE s.last_capture_date IS NOT NULL
                AND s.last_capture_date < NOW() - INTERVAL 1 DAY
                AND s.notifier = true""")
    List<Sensor> findPowerOffNotAlreadyNotified();
}
