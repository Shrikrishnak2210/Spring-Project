package demo.boot.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import demo.boot.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class StudentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Student> getAllStudents() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    public Student getStudentById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public Student createStudent(Student student) {
        entityManager.persist(student);
        return student;
    }

    public Student updateStudent(Student student) {
        return entityManager.merge(student);
    }

    public void deleteStudent(Student student) {
        entityManager.remove(student);
    }
}
