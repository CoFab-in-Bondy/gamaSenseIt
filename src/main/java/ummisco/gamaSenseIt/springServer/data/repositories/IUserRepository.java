package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.user.User;


public interface IUser extends CrudRepository<User, Long> {
}
