package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.io.Serializable;
import java.util.Arrays;

public class Vector implements Serializable {

    int[] v;

    public Vector(int[] v) {
        this.v = v;
    }

    public Vector(int length) {
        v = new int[length];
    }

    public void merge(Vector v) {

    }

    public void addOne(int processorId) {
        this.v[processorId] += 1;
    }

    public boolean bigOrEqualThan(Vector vector) {
        int[] v = vector.v;
        boolean flag = true;
        for(int i = 0; i < this.v.length; i++) {
            if (this.v[i] < v[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        return "{" +
                "vector=" + Arrays.toString(v) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(v, vector.v);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(v);
    }
}
