package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ummisco.gamaSenseIt.springServer.data.model.user.UserGroup;

@Repository
public interface IUserGroupRepository  extends CrudRepository<UserGroup, Long> {
}

