import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int m1 = 5;
        int[] W = {4, 3, 2, 3, 2};
        double[] C = {10, 15, 25, 15, 20};
        int[] L = {5, 7, 6, 5, 8};
        double[] R = {0.7, 0.65, 0.67, 0.71, 0.75};
        double[] rho = {0.1, 0.3, 0.5, 0.7, 0.9};

        MFN instance1 = new MFN(m1, W, C, L, R, rho);

        double[][] tmp = instance1.arPMF();
        for (double[] v : tmp) { System.out.println(Arrays.toString(v) + "\n"); }
        double[][] tmp2 = instance1.CDF(tmp);
        for (double[] v : tmp2) { System.out.println(Arrays.toString(v) + "\n"); }
    }
}