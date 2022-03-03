package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;

import java.util.List;
import java.util.Set;

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


    @Query("""
        SELECT s.id FROM Sensor s
            JOIN AccessSensor acs ON (acs.sensorId = s.id)
            JOIN Access ac ON (ac.id = acs.accessId)
            JOIN AccessUser acu ON (acu.accessId = ac.id)
                WHERE acu.userId = :userId
    """)
    Set<Long> findReadableSensors(long userId);

    @Query("""
        SELECT s FROM Sensor s
            JOIN AccessSensor acs ON (acs.sensorId = s.id)
            JOIN Access ac ON (ac.id = acs.accessId)
            JOIN AccessUser acu ON (acu.accessId = ac.id)
                WHERE acu.userId = :userId AND s.id = :sensorId
    """)
    Sensor findReadableSensor(long userId, long sensorId);

    @Query("""
        SELECT DISTINCT s FROM Sensor s
            JOIN AccessSensor acs ON (acs.sensorId = s.id)
            JOIN Access ac ON (ac.id = acs.accessId)
            JOIN AccessUser acu ON (acu.accessId = ac.id)
                WHERE acu.userId = :userId
                    AND ac.privilege = 0
    """)
    Set<Sensor> findManageableSensors(long userId);

}
