package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Worker extends Element<Worker> implements Validatable, Serializable, Comparable<Worker> {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int salary; //Значение поля должно быть больше 0
    private LocalDate endDate; //Поле может быть null
    private Position position; //Поле не может быть null
    private Status status; //Поле не может быть null
    private Person person; //Поле не может быть null

    @Override
    public String validate() {
        StringBuilder s = new StringBuilder();
        if (name == null || name.isEmpty()) s.append("name; ");
        if (coordinates == null || !coordinates.validate().isEmpty()) s.append("coordinates; ");
        if (creationDate == null) s.append("creationDate; ");
        if (salary <= 0) s.append("salary; ");
        if (endDate == null) s.append("endDate; ");
        if (position == null) s.append("position; ");
        if (status == null) s.append("status; ");
        if (person == null) s.append("person; ");
        if (person == null) {
            s.append("person=null");
        } else if (!person.validate().isEmpty()) {
            s.append("person: ").append(person.validate());
        }
        return s.toString();
    }

    public Worker(String name, Coordinates coordinates, LocalDateTime creationDate, int salary, LocalDate endDate, Position position, Status status, Person person) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.endDate = endDate;
        this.position = position;
        this.status = status;
        this.person = person;

    }

    public Worker(int id, String name, Coordinates coordinates, LocalDateTime creationDate, int salary, LocalDate endDate, Position position, Status status, Person person) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.endDate = endDate;
        this.position = position;
        this.status = status;
        this.person = person;

    }

    public static Worker fromArray(String[] line) throws NullPointerException {
        Integer id;
        String name;
        Coordinates coordinates;
        LocalDateTime creationDate;
        Integer salary;
        LocalDate endDate;
        Position position;
        Status status;
        Person person;
        try {
            id = Integer.parseInt(line[0]);
        } catch (NumberFormatException e) {
            id = null;
        }
        name = line[1];
        coordinates = new Coordinates(line[2]);
        try {
            creationDate = LocalDateTime.parse(line[3], DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            creationDate = null;
        }
        try {
            salary = Integer.parseInt(line[4]);
        } catch (NumberFormatException e) {
            salary = null;
        }
        try {
            endDate = LocalDate.parse(line[5], DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            endDate = null;
        }
        try {
            position = line[6].equals("null") ? null : Position.valueOf(line[6]);
        } catch (NullPointerException | IllegalArgumentException e) {
            position = null;
        }
        try {
            status = line[7].equals("null") ? null : Status.valueOf(line[7]);
        } catch (NullPointerException | IllegalArgumentException e) {
            status = null;
        }
        person = new Person(line[8]);
        return new Worker(id, name, coordinates, creationDate, salary, endDate, position, status, person);
    }

    /**
     * private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
     * private String name; //Поле не может быть null, Строка не может быть пустой
     * private Coordinates coordinates; //Поле не может быть null
     * private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
     * private int salary; //Значение поля должно быть больше 0
     * private LocalDate endDate; //Поле может быть null
     * private Position position; //Поле не может быть null
     * private Status status; //Поле не может быть null
     * private Person person; //Поле не может быть null
     */
    public static String[] toArray(Worker worker) {

        List<String> list = new ArrayList<String>();
        list.add("" + worker.getId());
        list.add(worker.getName());
        list.add(worker.getCoordinates().toString());
        list.add(worker.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME));
        list.add("" + worker.getSalary());
        list.add(worker.getEndDate().format(DateTimeFormatter.ISO_DATE));
        list.add(worker.getPosition() == null ? "null" : worker.getPosition().toString());
        list.add(worker.getStatus() == null ? "null" : worker.getStatus().toString());
        list.add(worker.getPerson() == null ? "null" : worker.getPerson().toString());
        String[] array = new String[list.size()];
        for (var i = 0; i < list.size(); i++) {
            array[i] = (String) list.get(i);
        }
        return array;
    }

    @Override
    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getSalary() {
        return salary;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Position getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id == worker.id && salary == worker.salary && Objects.equals(name, worker.name) &&
                Objects.equals(coordinates, worker.coordinates) && Objects.equals(creationDate, worker.creationDate) &&
                Objects.equals(endDate, worker.endDate) && position == worker.position && status == worker.status
                && Objects.equals(person, worker.person);
    }

    @Override
    public int compareTo(Worker o) {
        if (id != o.id) return (Integer.compare(id, o.id));
        if (salary != o.salary) return Integer.compare(salary, o.salary);
        if (name.compareTo(o.getName()) != 0) return name.compareTo(o.getName());
        if (coordinates.getX() != o.getCoordinates().getX())
            return (int) (coordinates.getX() - o.getCoordinates().getX());
        if (!Objects.equals(coordinates.getY(), o.getCoordinates().getY()))
            return (int) (coordinates.getY() - o.getCoordinates().getY());
        if (creationDate != o.creationDate)
            return (int) (creationDate.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(creationDate)) -
                    (o.creationDate.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(o.creationDate))));
        if (endDate != o.endDate) return (int) (endDate.toEpochDay() - o.endDate.toEpochDay());
        if (position != o.position) return position.ordinal() - o.position.ordinal();
        if (status != o.status) return status.ordinal() - o.status.ordinal();
        if (!Objects.equals(this.getPerson().getWeight(), o.getPerson().getWeight()))
            return (int) (this.getPerson().getWeight() - o.getPerson().getWeight());
        if (this.getPerson().getEyeColor().ordinal() != o.getPerson().getEyeColor().ordinal())
            return (int) (this.getPerson().getEyeColor().ordinal() - o.getPerson().getEyeColor().ordinal());
        if (this.getPerson().getHairColor().ordinal() != o.getPerson().getHairColor().ordinal())
            return (int) (this.getPerson().getHairColor().ordinal() - o.getPerson().getHairColor().ordinal());
        if (this.getPerson().getNationality().ordinal() != o.getPerson().getNationality().ordinal())
            return (int) (this.getPerson().getNationality().ordinal() - o.getPerson().getNationality().ordinal());
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, salary, endDate, position, status, person);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", salary=" + salary +
                ", endDate=" + endDate +
                ", position=" + position +
                ", status=" + status +
                ", person=" + person +
                '}';
    }


}
