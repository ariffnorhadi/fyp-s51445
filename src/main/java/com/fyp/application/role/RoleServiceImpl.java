package com.fyp.application.role;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository repository;

  public RoleServiceImpl(RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public ArrayList<Long> rolesByUserId(Long id) {
    return this.repository.findRoles(id);
  }

  @Override
  public void save(Role role) {
    this.repository.save(role);
  }

  @Override
  public Optional<Role> findRoleById(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public List<Role> ROLE_LIST() {
    return this.repository.findAll();
  }
}
