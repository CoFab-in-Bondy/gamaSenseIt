package fr.ummisco.gamasenseit.server.data.repositories;

import fr.ummisco.gamasenseit.server.data.model.preference.InteractUser;
import org.springframework.data.repository.CrudRepository;

public interface IInteractUserRepository extends CrudRepository<InteractUser, InteractUser.InteractUserPK> {

}
