package com.fyp.application.school;

import com.fyp.application.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SchoolService {

  void saveSchool(School school);

  void saveSchool(CreateSchoolParameters parameters);

  void editSchool(Long schoolId, EditSchoolParameters parameters);

  Optional<School> getOne(Long schoolId);

  List<School> SCHOOL_LIST();

  Page<School> getAllSchoolsWithPage(Pageable pageable);

    /*
    TODO: Create method to sort schools by date created
     */

  List<School> findSchoolsByPrincipal(User principal);

  School findSchoolByPeople(User user);

}
