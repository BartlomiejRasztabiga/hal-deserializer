package entities;

public class Student {

    private Long id;

    private Long studentNumber;

    private String studentName;

    private String studentSurname;

    private Boolean isOnDuty;

    public Long getId() {
        return id;
    }

    public Long getStudentNumber() {
        return studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public Boolean isOnDuty() {
        return isOnDuty;
    }

    @Override
    public String toString() {
        return "entities.Student{" +
                "id=" + id +
                ", studentNumber=" + studentNumber +
                ", studentName='" + studentName + '\'' +
                ", studentSurname='" + studentSurname + '\'' +
                ", isOnDuty=" + isOnDuty +
                '}';
    }
}
