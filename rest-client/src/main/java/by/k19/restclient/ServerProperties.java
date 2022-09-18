package by.k19.restclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("service")
public class ServerProperties {
    private String url;
    private String basePath;
}
