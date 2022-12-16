package model.data.builder;

import model.data.Comparator;

import java.util.Random;

public class DoubleObjectBuilder implements UserTypeBuilder {

    @Override
    public String typeName() {
        return "Double";
    }

    @Override
    public Double create() {
        return new Random().nextDouble();
    }

    @Override
    public Double createFromString(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public String toString(Object object) {
        return object.toString();
    }

    @Override
    public Comparator getComparator() {
        return (o1, o2) -> (Double)o1 - (Double) o2;
    }

}
