package com.annette.spring.courses_system.project.service;

import java.util.List;
import java.util.Map;

public interface StudentService {

    public List<Map<String, Object>> getAllStudents();

    public Map<String, Object> getStudent(int id);

    public Map<String, Object> saveStudent(String data);

    public Map<String, Object> updateStudent(String data);

    public void deleteStudent(int id);

}
