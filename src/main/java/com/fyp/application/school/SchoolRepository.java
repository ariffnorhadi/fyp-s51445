package com.fyp.application.school;

import com.fyp.application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

  List<School> findSchoolsByPrincipal(User principal);

  School findSchoolByPeople(User people);
}
