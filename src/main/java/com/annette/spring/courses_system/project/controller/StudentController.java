package com.annette.spring.courses_system.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annette.spring.courses_system.project.service.StudentService;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Map<String, Object>> getAllStudents() {

        return studentService.getAllStudents();
        
    }

    @GetMapping("/students/{id}")
    public Map<String, Object> getStudent(@PathVariable int id) {

        return studentService.getStudent(id);

    }

    @PostMapping("/students")
    public Map<String, Object> saveStudent(@RequestBody String data) {

        return studentService.saveStudent(data);
        
    }

    @PatchMapping("/students")
    public Map<String, Object> updateStudent(@RequestBody String data) {

        return studentService.updateStudent(data);

    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable int id) {

        studentService.deleteStudent(id);

        return "Студент с id = " + id + " удалён";

    }

}
