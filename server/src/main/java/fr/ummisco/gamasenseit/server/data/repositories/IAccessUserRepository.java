package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.user.AccessUser;

@Repository
public interface IAccessUserRepository extends CrudRepository<AccessUser, AccessUser.AccessUserPK> {
}
