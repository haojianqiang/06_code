package pub.types;

import java.io.Serializable;

public class ValidationError extends RuntimeException implements Serializable {
    public ValidationError(String message) {
        super(message);
    }
}
