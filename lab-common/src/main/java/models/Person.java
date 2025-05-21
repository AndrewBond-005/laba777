package models;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Validatable, Serializable {
    private Double weight; //Поле не может быть null, Значение поля должно быть больше 0
    private EyeColor eyeColor; //Поле может быть null
    private HairColor hairColor; //Поле может быть null
    private Country nationality; //Поле не может быть null

    public Person(Double weight, EyeColor eyeColor, HairColor hairColor, Country nationality) {
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }


    public Person(String str) {
        var line = str.split(";");
        try {
            weight = Double.parseDouble(line[0]);
        } catch (NumberFormatException e) {
            weight = null;
        }
        try {
            eyeColor = line[1].equals("null") ? null : EyeColor.valueOf(line[1]);
        } catch (NullPointerException | IllegalArgumentException e) {
            eyeColor = null;
        }
        try {
            hairColor = line[2].equals("null") ? null : HairColor.valueOf(line[2]);
        } catch (NullPointerException | IllegalArgumentException e) {
            hairColor = null;
        }
        try {
            nationality = line[3].equals("null") ? null : Country.valueOf(line[3]);
        } catch (NullPointerException | IllegalArgumentException e) {
            nationality = null;
        }
    }

    @Override
    public String validate() {
        StringBuilder s = new StringBuilder();
        if (weight == null || weight <= 0) s.append("weight; ");
        if (eyeColor == null) s.append("eyeColor; ");
        if (hairColor == null) s.append("hairColor; ");
        if (nationality == null) s.append("nationality; ");
        return s.toString();
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }


    public Double getWeight() {
        return weight;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(weight, person.weight) && eyeColor == person.eyeColor && hairColor == person.hairColor && nationality == person.nationality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, eyeColor, hairColor, nationality);
    }

    @Override
    public String toString() {
        return weight + ";" + eyeColor +
                ";" + hairColor + ";" + nationality;
    }
}
