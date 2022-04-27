package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractAccess;

public interface IInteractAccessRepository extends CrudRepository<InteractAccess, InteractAccess.InteractAccessPK> {
}
