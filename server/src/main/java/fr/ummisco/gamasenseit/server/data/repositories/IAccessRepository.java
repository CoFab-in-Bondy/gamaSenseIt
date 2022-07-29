package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.user.Access;

@Repository
public interface IAccessRepository extends CrudRepository<Access, Long> {
    // acu.privilege MANAGE - VIEW
    /*
    @Query("""
    SELECT u FROM Access ac
        JOIN AccessUser acu1 ON (acu1.accessId = ac.id)
        JOIN AccessUser acu2 ON (acu2.accessId = ac.id)
        JOIN User u ON (u.id = acu2.userId)
        WHERE acu1.userId = :userId AND acu1.privilege = 0 AND ac.id = :accessId
            AND LOWER(u.name) LIKE LOWER(:query)
    """)
    List<User> searchUserInByName(long userId, long accessId, String query);

    @Query("""
    SELECT u FROM User u
        WHERE LOWER(u.name) LIKE LOWER(:query)
    EXCEPT
    SELECT u FROM Access ac
        JOIN AccessUser acu1 ON (acu1.accessId = ac.id)
        JOIN AccessUser acu2 ON (acu2.accessId = ac.id)
        JOIN User u ON (u.id = acu2.user)
        WHERE acu1.userId = :userId AND acu1.privilege = 0 AND ac.id = :accessId
    """)
    List<User> searchUserOutByName(long userId, long accessId, String query);

    @Query("""
    SELECT s FROM Access ac
        JOIN AccessUser acu ON (acu.accessId = ac.id)
        JOIN AccessSensor acs ON (acs.accessId = ac.id)
        JOIN Sensor s ON (s.id = acs.sensorId)
        WHERE acu.userId = :userId AND acu.privilege = 0 AND ac.id = :accessId
            AND LOWER(s.name) LIKE LOWER(:query)
    """)
    List<User> searchSensorInByName(long userId, long access, String query);

    @Query("""
    SELECT s FROM Sensor
        JOIN accessSensor acs ON (acs.accessId = ac.id)
        JOIN access ac ON (acs)
        WHERE LOWER(s.name) LIKE LOWER(:query)
        
    EXCEPT
    SELECT s FROM Access ac
        JOIN AccessUser acu ON (acu.accessId = ac.id)
        JOIN AccessSensor acs ON (acs.accessId = ac.id)
        JOIN Sensor s ON (s.id = acs.sensorId)
        WHERE acu.userId = :userId AND acu.privilege = 0 AND ac.id = :accessId
    """)
    List<User> searchSensorOutByName(long userId, long access, String query);
    */

}
