package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.user.User;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByMail(String mail);
}
