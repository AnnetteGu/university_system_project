package com.annette.spring.courses_system.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.annette.spring.courses_system.project.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
