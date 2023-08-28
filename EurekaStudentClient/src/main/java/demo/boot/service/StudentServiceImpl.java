package demo.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import demo.boot.model.Student;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class StudentServiceImpl implements StudentService {

    private final RestTemplate restTemplate;

    @Autowired
    public StudentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private DiscoveryClient discoveryClient;
    
    private String getBaseUrl(){
    	List<ServiceInstance> instances = discoveryClient.getInstances("StudentService");
    	String url = instances.get(0).getUri().toString();
        return url+"/student";
    }

    @Override
    public ResponseEntity<List<Student>> getAllStudents() {
        ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
        ResponseEntity<List<Student>> responseEntity = restTemplate.exchange(getBaseUrl(), HttpMethod.GET, null, responseType);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Student> getStudentById(Long id) {
        return restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
    }

    @Override
    public ResponseEntity<Student> createStudent(Student student) {
        return restTemplate.postForEntity(getBaseUrl(), student, Student.class);
    }

    @Override
    public ResponseEntity<Student> updateStudent(Long id, Student student) {
        return restTemplate.exchange(getBaseUrl() + "/" + id, HttpMethod.PUT, new HttpEntity<>(student), Student.class);
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Long id) {
        restTemplate.delete(getBaseUrl() + "/" + id);
        return ResponseEntity.ok().build();
    }
    
    
}
