package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.user.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    User findByMail(String mail);
}
