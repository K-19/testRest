package by.k19.restservice.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class MessageError {
    private String errorText;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<String> errors = new ArrayList<>();

    public MessageError(String errorText) {
        this.errorText = errorText;
    }

    public void addError(String error) {
        errors.add(error);
    }
}
