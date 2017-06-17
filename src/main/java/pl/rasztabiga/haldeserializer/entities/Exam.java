package pl.rasztabiga.haldeserializer.entities;

import java.util.Date;

public class Exam {

    private Long id;

    private Date date;

    private String subject;

    private String description;

    private Boolean isVisible;

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", date=" + date +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", isVisible=" + isVisible +
                '}';
    }
}
