package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.user.DownloadToken;

import java.util.Optional;

public interface IDownloadTokenRepository extends CrudRepository<DownloadToken, Long> {
    Optional<DownloadToken> findByToken(String token);
}