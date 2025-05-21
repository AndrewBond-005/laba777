package models;

public abstract class Element<E extends Element> implements Comparable<E>, Validatable {
    abstract public int getId();
}
