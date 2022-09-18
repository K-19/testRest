package by.k19.restservice.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class MessageErrorBuilder {

    public static MessageError fromBinfingErrors(Errors errors) {
        MessageError error = new MessageError("Validation error. " + errors.getErrorCount() + " errors detected");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addError(objectError.getDefaultMessage());
        }
        return error;
    }
}
