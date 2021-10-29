package edu.txstate.ctl_parser.model;

public class Atom {
    private final char name;

    public Atom(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    @Override
    public String toString() {
        return Character.toString(name);
    }
}
