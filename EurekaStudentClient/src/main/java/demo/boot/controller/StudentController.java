package demo.boot.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import demo.boot.model.Student;
import demo.boot.service.StudentServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	RestTemplate restTemplate;

	private final StudentServiceImpl studentServiceImpl;

	@Autowired
	public StudentController(StudentServiceImpl studentServiceImpl) {
		this.studentServiceImpl = studentServiceImpl;
	}

	@GetMapping
    @CircuitBreaker(name="StudentService",fallbackMethod="fallbackMethod")
	public ResponseEntity<List<Student>> getAllStudents() {
		return studentServiceImpl.getAllStudents();
	}

	@GetMapping("/{id}")
    @CircuitBreaker(name="StudentService",fallbackMethod="fallbackMethod")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
		return studentServiceImpl.getStudentById(id);
	}

	@PostMapping
    @CircuitBreaker(name="StudentService",fallbackMethod="fallbackMethod")
	public ResponseEntity<?> createStudent(@RequestBody Student student) {
		return studentServiceImpl.createStudent(student);
	}

	@PutMapping("/{id}")
    @CircuitBreaker(name="StudentService",fallbackMethod="fallbackMethod")
	public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
		return studentServiceImpl.updateStudent(id, student);
	}

	@DeleteMapping("/{id}")
    @CircuitBreaker(name="StudentService",fallbackMethod="fallbackMethod")
	public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
		return studentServiceImpl.deleteStudent(id);
	}

	public ResponseEntity<Void> fallbackMethod(Throwable t) {
		return ResponseEntity.notFound().build();
	}
	
}
