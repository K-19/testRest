package model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean read;

    @PrePersist
    private void prePersist() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.modified = LocalDateTime.now();
    }
}
