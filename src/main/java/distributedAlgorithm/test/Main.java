package distributedAlgorithm.test;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] ii = new int[10];
        System.out.println(Arrays.toString(ii));
        int[] jj = ii.clone();
        System.out.println(Arrays.toString(jj));
        jj[0] = 1;
        System.out.println(Arrays.toString(ii));
    }
}
