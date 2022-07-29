package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import fr.ummisco.gamasenseit.server.data.model.user.DownloadToken;

import java.util.Optional;

public interface IDownloadTokenRepository extends CrudRepository<DownloadToken, Long> {
    Optional<DownloadToken> findDownloadTokenByTokenEquals(String token);
}