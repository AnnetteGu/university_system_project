package com.annette.spring.courses_system.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annette.spring.courses_system.project.entity.Student;
import com.annette.spring.courses_system.project.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Map<String, Object>> getAllStudents() {

        List<Student> studentList = studentRepository.findAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new LinkedHashMap<>();

        for (Student student : studentList) {
            resultMap.put("id", student.getId());
            resultMap.put("name", student.getName());
            resultMap.put("surname", student.getSurname());

            resultList.add(resultMap);

            resultMap = new LinkedHashMap<>();
        }

        return resultList;

    }

    @Override
    public Map<String, Object> getStudent(int id) {

        Student student = studentRepository.findById(id).get();
        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("id", student.getId());
        resultMap.put("name", student.getName());
        resultMap.put("surname", student.getSurname());

        return resultMap;
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public Student saveStudent(String data) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = new LinkedHashMap<>();

        try {
            resultMap = objectMapper.readValue(data, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Student student = new Student();

        student.setName((String) resultMap.get("name"));
        student.setSurname((String) resultMap.get("surname"));

        return studentRepository.save(student);

    }

    @SuppressWarnings("unchecked")
    @Override
    public Student updateStudent(String data) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = new LinkedHashMap<>();

        try {
            resultMap = objectMapper.readValue(data, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        int id = (Integer) resultMap.get("id");

        Student student = studentRepository.findById(id).get();

        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            switch (entry.getKey()) {
                case "name":
                    student.setName((String) entry.getValue());
                    break;
                case "surname":
                    student.setSurname((String) entry.getValue());
                    break;
                default:
                    break;
            }
        }

        return studentRepository.save(student);

    }

    @Override
    public void deleteStudent(int id) {
        
        studentRepository.deleteById(id);

    }

}
