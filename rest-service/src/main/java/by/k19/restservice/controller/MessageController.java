package by.k19.restservice.controller;

import by.k19.restservice.repos.MessageRepository;
import by.k19.restservice.validation.MessageError;
import by.k19.restservice.validation.MessageErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/messages")
public class MessageController extends AbstractController{

    private final MessageRepository repository;

    @Autowired
    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Message>> findAll(HttpServletRequest request) {
        log.info("findAll Messages for " + getRemoteAddress(request));
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Message> message = repository.findById(id);
        if (message.isPresent()) {
            log.info("findByIf " + id + " for " + getRemoteAddress(request) + " | " + message.get());
            return ResponseEntity.ok(message.get());
        } else {
            log.info("findByIf " + id + " for " + getRemoteAddress(request) + " | Not found");
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<?> saveOrUpdate(@Valid @RequestBody Message message, HttpServletRequest request, Errors errors) {
        if (errors.hasErrors()) {
            MessageError error = MessageErrorBuilder.fromBinfingErrors(errors);
            log.info("saveOrUpdate " + getRemoteAddress(request) + " | Errors: " + error);
            return ResponseEntity.badRequest().body(error);
        }
        Message savedMessage = null;
        if (message.getId() != null) {
            Optional<Message> saved = repository.findById(message.getId());
            if (saved.isPresent()) {
                savedMessage = saved.get();
                savedMessage.setText(message.getText());
                savedMessage.setRead(message.isRead());
                repository.saveAndFlush(savedMessage);
            }
        }
        if (savedMessage == null)
            savedMessage = repository.saveAndFlush(message);
        log.info("saveOrUpdate " + getRemoteAddress(request) + " | " + savedMessage);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedMessage.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, HttpServletRequest request) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("deleteById " + id + " " + getRemoteAddress(request));
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError exceptionHandler(Exception exception) {
        return new MessageError(exception.getMessage());
    }
}
