package models;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Validatable, Serializable {
    private float x;
    private Long y; //Поле не может быть null

    public Coordinates(Float x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String s) {
        try {
            try {
                this.x = Float.parseFloat(s.split(";")[0]);
                this.y = Long.parseLong(s.split(";")[1]);
            } catch (NumberFormatException e) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Валидирует правильность полей.
     *
     * @return true, если все верно, иначе false
     */
    @Override
    public String validate() {
        if (y == null) return "y";
        return "";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Float.compare(x, that.x) == 0 && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }
}
