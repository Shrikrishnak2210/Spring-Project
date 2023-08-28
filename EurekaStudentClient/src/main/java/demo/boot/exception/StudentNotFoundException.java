package demo.boot.exception;

// check the error - 500
// explicitly set 404 error by using @ResponseStatus
//@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {

	public StudentNotFoundException(String message) {
		super(message);
	}

}
