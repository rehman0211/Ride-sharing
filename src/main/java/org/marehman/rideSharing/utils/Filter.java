package org.marehman.rideSharing.utils;

public class Filter {
    public String field;
    public Object value;
    public String comparator="=";

    public Filter(String field, Object value, String comparator) {
        this.field = field;
        this.value = value;
        this.comparator = comparator;
    }

    public Filter() {
    }
}
