package model.data.builder;

import model.data.Comparator;

public interface UserTypeBuilder {
    String typeName();

    Object create();

    Comparator getComparator();

    Object createFromString(String s);

    String toString(Object object);
}
