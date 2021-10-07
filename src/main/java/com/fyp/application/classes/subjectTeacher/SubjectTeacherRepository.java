package com.fyp.application.classes.subjectTeacher;

import com.fyp.application.classes.Classes;
import com.fyp.application.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTeacherRepository extends JpaRepository<SubjectTeacher, Long>,
    CrudRepository<SubjectTeacher, Long> {

  List<SubjectTeacher> findByClasses(Classes classes);

  List<SubjectTeacher> findByClassesAndSubject(Classes classes, Subject subject);

}
