package com.annette.spring.courses_system.project.service;

import java.util.List;
import java.util.Map;

public interface CourseService {

    public List<Map<String, Object>> getAllCourses();

    public Map<String, Object> getCourse(int id);
    
    public Map<String, Object> saveCourse(String data);

    public Map<String, Object> updateCourse(String data);

    public void deleteCourse(int id);

    public String addStudentOnCourse(String data);

}
