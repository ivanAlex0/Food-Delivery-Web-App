package foodPanda.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Getter
public class APIError {

    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final String message;
    private final String path;

    public APIError(HttpStatus status, Throwable exception, WebRequest request) {
        this.status = status;
        this.message = exception.getMessage();
        this.timestamp = LocalDateTime.now();
        String reqString = request.toString();
        this.path = reqString.substring(reqString.indexOf("/"), reqString.indexOf(";"));
    }


}
