package fr.ummisco.gamasenseit.server.data.repositories;

import fr.ummisco.gamasenseit.server.data.model.preference.InteractAccess;
import org.springframework.data.repository.CrudRepository;

public interface IInteractAccessRepository extends CrudRepository<InteractAccess, InteractAccess.InteractAccessPK> {
}
