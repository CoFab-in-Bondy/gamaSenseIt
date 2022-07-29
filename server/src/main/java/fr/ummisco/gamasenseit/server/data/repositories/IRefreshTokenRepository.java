package fr.ummisco.gamasenseit.server.data.repositories;

import fr.ummisco.gamasenseit.server.data.model.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRefreshTokenRepository  extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokensByTokenEquals(String token);
}
