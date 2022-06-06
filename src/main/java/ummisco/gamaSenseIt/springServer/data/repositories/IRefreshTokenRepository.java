package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.user.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository  extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
