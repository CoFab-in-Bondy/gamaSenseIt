package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import ummisco.gamaSenseIt.springServer.data.model.SensoredBulkData;

import java.util.Date;
import java.util.List;

@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "bulkData", path = "bulkData")
@Repository
public interface ISensoredBulkDataRepository extends CrudRepository<SensoredBulkData, Long> {
    List<SensoredBulkData> findByCaptureDate(Date date);
}
