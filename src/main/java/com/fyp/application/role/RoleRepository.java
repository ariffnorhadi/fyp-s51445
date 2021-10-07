package com.fyp.application.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  @Query(value = "select role_id from users_roles n where n.user_id=?1",
      nativeQuery = true)
  ArrayList<Long> findRoles(Long userID);

  Role findByName(String name);
}
