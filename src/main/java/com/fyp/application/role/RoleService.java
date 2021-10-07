package com.fyp.application.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RoleService {

  ArrayList<Long> rolesByUserId(Long id);

  void save(Role role);

  Optional<Role> findRoleById(Long id);

  List<Role> ROLE_LIST();
}
