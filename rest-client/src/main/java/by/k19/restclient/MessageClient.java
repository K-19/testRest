package by.k19.restclient;

import lombok.extern.slf4j.Slf4j;
import model.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MessageClient {

    private final RestTemplate restTemplate;
    private final String url;

    public MessageClient(ServerProperties serverProperties) {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new MessageErrorHandler());
        this.url = serverProperties.getUrl() + serverProperties.getBasePath();
    }

    public Iterable<Message> findAll() throws URISyntaxException {
        RequestEntity<Iterable<Message>> request = new RequestEntity<>(HttpMethod.GET, new URI(url));
        ResponseEntity<Iterable<Message>> response = restTemplate.exchange(request, new ParameterizedTypeReference<Iterable<Message>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("findAll | " + response.getBody());
            return response.getBody();
        }
        return null;
    }

    public Message findById(Long id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id.toString());
        Message message = restTemplate.getForObject(url + "/{id}", Message.class, params);
        log.info("findById " + id + " " + message);
        if (message == null)
            message = new Message();
        return message;
    }

    public Message save(Message message) throws URISyntaxException {
        RequestEntity<Message> request = new RequestEntity<>(message, HttpMethod.POST, new URI(url));
        ResponseEntity<Message> response = restTemplate.exchange(request, new ParameterizedTypeReference<Message>() {});
        if (response.getStatusCode() == HttpStatus.CREATED) {
            log.info("save " + message);
            return restTemplate.getForObject(response.getHeaders().getLocation(), Message.class);
        }
        return null;
    }

    public void deleteById(Long id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id.toString());
        restTemplate.delete(url + "/{id}", params);
        log.info("deleteById " + id);
    }
}
