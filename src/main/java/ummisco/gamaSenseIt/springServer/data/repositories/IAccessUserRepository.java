package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessUser;

@Repository
public interface IAccessUserRepository extends CrudRepository<AccessUser, AccessUser.AccessUserPK> {
}
