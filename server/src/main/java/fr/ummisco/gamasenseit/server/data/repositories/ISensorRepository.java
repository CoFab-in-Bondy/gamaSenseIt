package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ISensorRepository extends CrudRepository<Sensor, Long> {
    Optional<Sensor> findByToken(String token);
    List<Sensor> findByName(String name);

    @Query(nativeQuery = true, value = """
            SELECT s.* FROM sensor s
                WHERE s.last_capture_date IS NOT NULL
                    AND s.last_capture_date < CURRENT_TIMESTAMP - :ms
                    AND s.notified = false""")
    List<Sensor> findPowerOffNotAlreadyNotified(long ms);


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

    @Query("""
                SELECT DISTINCT s FROM Sensor s
                    JOIN AccessSensor acs ON (acs.sensorId = s.id)
                    JOIN Access ac ON (ac.id = acs.accessId)
                    JOIN AccessUser acu ON (acu.accessId = ac.id)
                        WHERE acu.userId = :userId
                            AND ac.privilege = 0
                            AND s.id = :sensorId
            """)
    Sensor findManageableSensor(long userId, long sensorId);

}
