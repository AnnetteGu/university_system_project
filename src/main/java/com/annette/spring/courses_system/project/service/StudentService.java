package com.annette.spring.courses_system.project.service;

import java.util.List;
import java.util.Map;

import com.annette.spring.courses_system.project.entity.Student;

public interface StudentService {

    public List<Map<String, Object>> getAllStudents();

    public Map<String, Object> getStudent(int id);

    public Student saveStudent(String data);

    public Student updateStudent(String data);

    public void deleteStudent(int id);

}
