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
    // получение всех студентов
    public List<Map<String, Object>> getAllStudents() {

        // получаем список всех студентов
        List<Student> studentList = studentRepository.findAll();
        // создаём список для вывода данных
        List<Map<String, Object>> resultList = new ArrayList<>();
        // словарь для наполнения списка
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // идём по списку студентов
        for (Student student : studentList) {

            // вносим необходимые данные о студенте
            resultMap = fillMap(student);
            // добавляем словарь в список
            resultList.add(resultMap);
            // делаем новый пустой словарь (делается так, чтобы ссылочно не повредить данные)
            resultMap = new LinkedHashMap<>();
        }

        return resultList;

    }

    @Override
    // получение одного студента
    public Map<String, Object> getStudent(int id) {

        //  получаем все данные о студенте
        Student student = studentRepository.findById(id).get();
        // создаем словарь для вывода данных
        Map<String, Object> resultMap = new LinkedHashMap<>();
        // заполняем словарь данными
        resultMap = fillMap(student);

        return resultMap;
        
    }

    @Override
    // сохранение студента
    public Map<String, Object> saveStudent(String data) {
        
        // парсим пришедший json
        Map<String, Object> studentMap = jsonParse(data);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // создаём студента
        Student student = new Student();

        // даём ему имя и фамилию, переданные в json
        student.setName((String) studentMap.get("name"));
        student.setSurname((String) studentMap.get("surname"));

        // сохраняем студента
        studentRepository.save(student);

        // заполняем данные о нём в словарь
        resultMap = fillMap(student);

        return resultMap;

    }

    @Override
    // обновление студента
    public Map<String, Object> updateStudent(String data) {

        // парсим пришедший json
        Map<String, Object> studentMap = jsonParse(data);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // получаем id студента
        int id = (Integer) studentMap.get("id");

        // получаем данные о студенте
        Student student = studentRepository.findById(id).get();

        // идём по словарю с информацией из json
        for (Map.Entry<String, Object> entry : studentMap.entrySet()) {
            // обновляем поля, которые нам попадаются
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

        // заполняем словарь для вывода
        resultMap = fillMap(student);
        // сохраняем изменения в базу
        studentRepository.save(student);

        return resultMap;

    }

    @Override
    public void deleteStudent(int id) {
        
        studentRepository.deleteById(id);

    }

    // метод для заполнения словаря вывода
    private Map<String, Object> fillMap(Student student) {

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("id", student.getId());
        map.put("name", student.getName());
        map.put("surname", student.getSurname());

        return map;

    }

    @SuppressWarnings("unchecked")
    // метод парсинга json в словарь
    private Map<String, Object> jsonParse(String json) {

        // ключевой объект для реализации парсинга
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();

        try {
            // передаём строку с json и указываем, какой класс хотим видеть на выходе
            map = objectMapper.readValue(json, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return map;

    }

}
