package com.fyp.application.classes.subjectTeacher;

import com.fyp.application.classes.Classes;
import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.subject.Subject;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "class_subject_teacher")
public class SubjectTeacher {

  @Id
  @SequenceGenerator(
      name = "class_sub_teacher_seq",
      sequenceName = "class_sub_teacher_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "class_sub_teacher_seq"
  )
  @Column(name = "class_sub_teacher_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_id")
  private Classes classes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id")
  private Subject subject;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  private User teacherInCharge;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectTeacher")
  private Set<Topic> topics;

  public SubjectTeacher() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Classes getClasses() {
    return classes;
  }

  public void setClasses(Classes classes) {
    this.classes = classes;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public User getTeacherInCharge() {
    return teacherInCharge;
  }

  public void setTeacherInCharge(User teacherInCharge) {
    this.teacherInCharge = teacherInCharge;
  }

  public Set<Topic> getTopics() {
    return topics;
  }

  public void setTopics(Set<Topic> topics) {
    this.topics = topics;
  }
}
