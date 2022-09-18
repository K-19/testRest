package by.k19.restclient;

import lombok.extern.slf4j.Slf4j;
import model.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RestClientApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RestClientApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Bean
    public CommandLineRunner process(MessageClient client) {
        return args -> {
            Message message = client.save(new Message("Zdarova"));
            log.info(message.toString());
            log.info(client.findAll().toString());
            log.info(client.findById(message.getId()).toString());
            client.deleteById(message.getId());
            log.info(client.findAll().toString());
            log.info(client.findById(message.getId()).toString());
        };
    }
}
