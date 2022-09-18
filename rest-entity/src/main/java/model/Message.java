package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean read;

    public Message(String text) {
        this.text = text;
    }

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
