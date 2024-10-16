package com.annette.spring.courses_system.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annette.spring.courses_system.project.service.CourseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public List<Map<String, Object>> getAllCourses() {

        return courseService.getAllCourses();

    }

    @GetMapping("/courses/{id}")
    public Map<String, Object> getCourse(@PathVariable int id) {

        return courseService.getCourse(id);

    }

    @PostMapping("/courses")
    public Map<String, Object> saveCourse(@RequestBody String data) {

        return courseService.saveCourse(data);

    }

    @PatchMapping("/courses")
    public Map<String, Object> updateCourse(@RequestBody String data) {

        return courseService.updateCourse(data);

    }

    @DeleteMapping("/courses/{id}")
    public String deleteCourse(@PathVariable int id) {

        courseService.deleteCourse(id);

        return "Курс с id = " + id + " был удалён";

    }

    @PostMapping("/courses/add")
    public String addStudentOnCourse(@RequestBody String data) {

        return courseService.addStudentOnCourse(data);

    }

}
