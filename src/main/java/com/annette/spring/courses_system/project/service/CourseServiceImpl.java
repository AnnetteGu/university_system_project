package com.annette.spring.courses_system.project.service;

import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annette.spring.courses_system.project.entity.Course;
import com.annette.spring.courses_system.project.entity.Student;
import com.annette.spring.courses_system.project.exception_handling.NoPlacesOnCourseException;
import com.annette.spring.courses_system.project.exception_handling.StudentAlreadyOnCourseException;
import com.annette.spring.courses_system.project.exception_handling.WrongTimeException;
import com.annette.spring.courses_system.project.repository.CourseRepository;
import com.annette.spring.courses_system.project.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    // получение всех курсов
    public List<Map<String, Object>> getAllCourses() {
        
        // получаем список всех курсов
        List<Course> courseList = courseRepository.findAll();
        // создаём список словарей для вывода курсов без поля со связью
        List<Map<String, Object>> resultList = new ArrayList<>();
        // словарь для данных о курсе
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // идём по списку курсов
        for (Course course : courseList) {
            // заполняем словарь
            resultMap = fillMap(course);
            // добавляем словарь с список
            resultList.add(resultMap);
            // обновляем словарь
            resultMap = new LinkedHashMap<>();

        }

        return resultList;

    }

    @Override
    // получение одного курса по id
    public Map<String, Object> getCourse(int id) {
        // получаем курс
        Course course = courseRepository.findById(id).get();
        // создаем словарь для данных о курсе (избегаем полей со связью)
        Map<String, Object> resultMap = new LinkedHashMap<>();
        // собираем данные в словарь
        resultMap = fillMap(course);

        return resultMap;

    }

    @Override
    // сохранение курса в бд
    public Map<String, Object> saveCourse(String data) {
        // парсим пришедший json
        Map<String, Object> courseMap = jsonParse(data);
        // словарь для вывода данных о сохранённом курсе
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // создаём новый курс
        Course course = new Course();

        // задаём ему название и количество мест
        course.setName((String) courseMap.get("name"));
        course.setPlacesAmount((Integer) courseMap.get("placesAmount"));

        // сохраняем курс
        courseRepository.save(course);

        // собираем данные по сохранённому курсу
        resultMap = fillMap(course);

        return resultMap;

    }

    @Override
    // обновление курса
    public Map<String, Object> updateCourse(String data) {
    
        // парсим json
        Map<String, Object> courseMap = jsonParse(data);
        // словарь для вывода
        Map<String, Object> resultMap = new LinkedHashMap<>();
        // получаем id курса
        int id = (Integer) courseMap.get("id");
        // получаем курс по id
        Course course = courseRepository.findById(id).get();

        // идём по полученным данным из json
        for (Map.Entry<String, Object> entry : courseMap.entrySet()) {
            // в зависимости от пришедших параметров меняем курс
            switch (entry.getKey()) {
                case "name":
                    course.setName((String) entry.getValue());
                    break;
                case "placesAmount":
                    course.setPlacesAmount((Integer) entry.getValue());
                    break;
                default:
                    break;
            }
        }

        // собираем данные об обновлённом курсе
        resultMap = fillMap(course);
        // сохраняем курс
        courseRepository.save(course);

        return resultMap;

    }

    @Override
    // удаление курса по id
    public void deleteCourse(int id) {
        
        // удаляем курс
        courseRepository.deleteById(id);    

    }

    @SuppressWarnings("null")
    @Override
    // добавление студента на курс
    public String addStudentOnCourse(String data) {
        
        // парсим пришедший json
        Map<String, Object> resultMap = jsonParse(data);
        
        // получаем id студента
        int studentId = (Integer) resultMap.get("student_id");
        
        // получаем данные о студенте по id
        Student student = studentRepository.findById(studentId).get();

        // задаём данные для временного окна
        String startDateString = "2024-10-14T09:00:00";
        String endDateString = "2024-10-18T23:59:59";

        // получаем текущую дату и переводим её в строку
        String currentDateString = LocalDateTime.now().toString();

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        LocalDateTime currentUserDate = null;

        // адаптируем эти даты для часового пояса пользователя
        try {
            startDate = getActualTimeZoneDate(startDateString, student.getTimeZone());
            endDate = getActualTimeZoneDate(endDateString, student.getTimeZone());
            currentUserDate = getActualTimeZoneDate(currentDateString, student.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // если текущая дата меньше стартовой
        if (currentUserDate.compareTo(startDate) == -1) {

            // ошибка, что запись ещё не началась
            throw new WrongTimeException("Запись на курсы ещё не началась");

        // если текущая дата больше конечной
        } else if (currentUserDate.compareTo(endDate) == 1) {

            // ошибка, что запись уже закончилась
            throw new WrongTimeException("Запись на курсы уже закрыта");

        // иначе (т.е. мы попали в интервал)
        } else {

            // получаем id курса
            int courseId = (Integer) resultMap.get("course_id");

            // получаем данные о курсе
            Course course = courseRepository.findById(courseId).get();
        
            // если мест на курсе нет
            if (course.getPlacesAmount() == 0) {

                // ошибка, что места на курсе кончились
                throw new NoPlacesOnCourseException(
                "На этом курсе мест больше нет");

            //  иначе (т.е. места есть)
            } else {

                // получаем список студентов на курсе
                List<Student> studentsOnCourse = course.getStudentsOnCourse();

                // если студента ещё нет в списке участников
                if (studentsOnCourse.indexOf(student) == -1) {

                    // добавляем туда студента
                    studentsOnCourse.add(student);

                    // уменьшаем места на 1
                    int placesAmount = course.getPlacesAmount() - 1;

                    // обновляем количество мест и список участников
                    course.setStudentsOnCourse(studentsOnCourse);
                    course.setPlacesAmount(placesAmount);

                    // сохраняем изменения в базе
                    courseRepository.save(course);

                    // выводим, что студент успешно зачислен на выбранный курс
                    // и показываем оставшиеся места на этом курсе
                    return "Студент " + student.getName() + 
                        " " + student.getSurname() + 
                        " был успешно записан на курс " + course.getName() +
                        ", оставшихся мест на курсе: " + course.getPlacesAmount();

                // иначе
                } else {

                    // ошибка, что студент уже есть на курсе
                    throw new StudentAlreadyOnCourseException(
                        "Этот студент уже был записан на этот курс");

                }
                
            }

        }

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

    // метод для вывода даты, согласно часовому поясу
    private LocalDateTime getActualTimeZoneDate(String data, String userTimeZone) throws ParseException {

        // парсим дату из строки
        LocalDateTime dateTime = LocalDateTime.parse(data);

        // прибавляем к дате часы, согласно часовому поясу
        dateTime = dateTime.plusHours(getTimeZoneHours(userTimeZone));

        return dateTime;

    }

    // метод для вывода количества часов, на которые надо поменять дату
    private int getTimeZoneHours(String timeZone) {

        StringBuilder hours = new StringBuilder(); 

        for (int i = 0; i < timeZone.length(); i++) {

            // знак минуса нужен для отстающих часовых поясов от текущего
            if (timeZone.charAt(i) == '-') {
                hours.append(timeZone.charAt(i));
            }

            // ну и ищем саму цифру
            if (Character.isDigit(timeZone.charAt(i))) {
                hours.append(timeZone.charAt(i));
            }

        }

        // превращаем StringBuilder в строку и парсим её в int
        return Integer.parseInt(hours.toString());

    }

    // метод для заполнения словаря вывода
    private Map<String, Object> fillMap(Course course) {

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("id", course.getId());
        map.put("name", course.getName());
        map.put("placeAmount", course.getPlacesAmount());

        return map;

    }

}
