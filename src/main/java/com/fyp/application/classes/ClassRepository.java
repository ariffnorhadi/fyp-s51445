package com.fyp.application.classes;

import com.fyp.application.school.School;
import com.fyp.application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Classes, Long>,
    CrudRepository<Classes, Long> {

  boolean existsByNameAndSchool(String name, School school);

  List<Classes> findBySchool(School school);

  List<Classes> findClassesByTeacher(User teacher);
}
