package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractUser;

public interface IInteractUserRepository extends CrudRepository<InteractUser, InteractUser.InteractUserPK> {

}
