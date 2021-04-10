package com.anshuman.spring.reactive.errorHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

// Customizing the Global Error Response Attributes:
// We can customize the Global Web Exception handler so that when the exception is thrown,
// it will be automatically translated to an HTTP status and a JSON error body.
// Overriding getErrorAttributes() of the DefaultErrorAttributes class will allow the customization
@Component
@Getter
@Setter
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private HttpStatus status;
    private String message;

    public GlobalErrorAttributes()
    {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = "please provide a name";
    }

    public GlobalErrorAttributes(HttpStatus status, String message)
    {
        this.status = status;
        this.message = message;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("status", getStatus());
        map.put("message", getMessage());
        return map;
    }
}
