package ummisco.gamaSenseIt.springServer.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ummisco.gamaSenseIt.springServer.data.model.user.Role;

public interface IRoleRepository extends CrudRepository<Role, Long> {  
}