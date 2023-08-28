package demo.boot.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import demo.boot.model.Student;

public interface StudentService {

	public ResponseEntity<List<Student>> getAllStudents();

	ResponseEntity<Student> getStudentById(Long id);

	ResponseEntity<Student> createStudent(Student student);

	ResponseEntity<Student> updateStudent(Long id, Student student);

	ResponseEntity<Void> deleteStudent(Long id);
}
