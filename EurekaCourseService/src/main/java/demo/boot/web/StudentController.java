package demo.boot.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.boot.dao.StudentDAO;
import demo.boot.exception.StudentNotFoundException;
import demo.boot.model.Student;
import demo.boot.service.KafkaSender;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {

	public static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	KafkaSender kafkaSender;

	private final StudentDAO studentDAO;

	@Autowired
	public StudentController(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}

	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> students = studentDAO.getAllStudents();
		return ResponseEntity.ok(students);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
		logger.info("Fetching User with id {}", id);
		Student student = studentDAO.getStudentById(id);
		if (student == null) {
			// return ResponseEntity.notFound().build()
			throw new StudentNotFoundException("student " + id + " not found");
		}
		return ResponseEntity.ok(student);
	}

	@PostMapping
	public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
		Student createdStudent = studentDAO.createStudent(student);
		kafkaSender.send("student " + student.getStudentName() + " created");
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student updatedStudent) {
		Student existingStudentOptional = studentDAO.getStudentById(id);
		kafkaSender.send("student " + updatedStudent.getStudentName() + " updated");
		if (existingStudentOptional != null) {
			updatedStudent.setId(id);
			Student updated = studentDAO.updateStudent(updatedStudent);
			return ResponseEntity.ok(updated);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		Student student = studentDAO.getStudentById(id);
		if (student != null) {
			kafkaSender.send("student " + student.getStudentName() + " deleted");
			studentDAO.deleteStudent(student);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
