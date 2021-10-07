package com.fyp.application.subject;

import com.fyp.application.school.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>,
    CrudRepository<Subject, Long> {

  List<Subject> findBySchool(School school);
}
