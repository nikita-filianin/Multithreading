package org.example.task3;

public class Mark {
    private double mark;
    private String addedBy;

    public Mark(double mark, String addedBy) {
        this.mark = mark;
        this.addedBy = addedBy;
    }

    @Override
    public String toString() {
        return mark + " (" + addedBy + ")";
    }
}
