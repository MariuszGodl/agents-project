
import java.util.ArrayList;
import java.util.Arrays;

public class MFN {
    private int m ;// number_of_links
    private int[] W; // component number vector
    private double[] C; // component capacity vector
    private int[] L; // lead time vector
    private double[] R; // component reliability vector
    private double[] rho; // vector of the correlation between the faults of the components
    private double[] beta; // beta vector
    private ArrayList<int[]> MPs; // list of minimal paths

    MFN(int m, int[] W, double[] C, int[] L, double[] R, double[] rho) {
        validateInput(m, W, C, L, R, rho);
        //check whether all values of R and rho are between 0 and 1;
        validateRvalues(R);
        this.m = m;
        this.W = W;
        this.C = C;
        this.L = L;
        this.R = R;
        this.rho = rho;
        //create the beta vector if the above-mentioned conditions are satisfied.
        this.beta = calculateBeta(R, rho);
    }

    private void validateInput(int m, int[] W, double[] C, int[] L, double[] R, double[] rho) {
        //check whether the length of vectors W, C, L, R, and rho is equal to m;
        if ( W.length != m) {throw new IllegalArgumentException("W has incorrect size: " + W.length + ", expected: " + m);}
        if ( C.length != m) {throw new IllegalArgumentException("C has incorrect size: " + C.length + ", expected: " + m);}
        if ( L.length != m) {throw new IllegalArgumentException("L has incorrect size: " + L.length + ", expected: " + m);}
        if ( R.length != m) {throw new IllegalArgumentException("R has incorrect size: " + R.length + ", expected: " + m);}
        if ( rho.length != m) {throw new IllegalArgumentException("Rho has incorrect size: " + rho.length + ", expected: " + m);}
    }

    private void validateRvalues(double[] R) {
        for ( int i = 0 ; i < R.length; i++) {
            if ( !( R[i] >= 0 && R[i] <= 1)){
                throw new IllegalArgumentException("R has incorrect value: " + R[i] + " at position: " + i);
            }
        }
    }

    private double[] calculateBeta(double[] R, double[] rho) {
        if ( R.length != rho.length) { throw new IllegalArgumentException("Rho and R have different size R: " + R.length + ", Rho: " + rho.length); }
        double[] beta = new double[R.length];
        for (int i = 0; i < R.length; i++) {
            beta[i] = 1 + (rho[i] * ( 1 - R[i] )) / R[i];
        }
        return beta;
    }

    // tmp constructor
    MFN(){
        Combinatorial.tests();
    }

    private static class  Combinatorial {

        public static long binomial(int n, int k) {
            if (k < 0 || k > n) {
                System.out.println("Error: Incorrect input for binomial, n: " + n + " k: " + k);
                return -1;
            }

            return factorial(n) / (factorial(k) * factorial(n - k));
        }

        public static long doubleFactorial(int n) {
            return factorial(n, 2);
        }

        public static long factorial(int n) {
            return factorial(n, 1);
        }

        public static long factorial(int n, int step) {

            if (n < -1) {
                System.out.println("Error: Incorrect input in factorial, n: " + n + ", step: " + step);
                return -1;
            }
            if (n == 0 || n == 1) return 1;
            if (n == -1 && step == 2) return 1;
            return n * factorial(n - step, step);
        }

        private static void factorialTest() {
            if (factorial(0) != 1) System.out.println("fac(0) != 1");
            if (factorial(1) != 1) System.out.println("fac(1) != 1");
            if (factorial(2) != 2) System.out.println("fac(2) != 2");
            if (factorial(3) != 6) System.out.println("fac(3) != 6");
            if (factorial(5) != 120) System.out.println("fac(5) != 120");
            if (factorial(20) != 2432902008176640000L) {
                System.out.println("fac(20) != 2432902008176640000");
            }
        }

        private static void doubleFactorialTest() {
            if (doubleFactorial(0) != 1) System.out.println("dfac(0) != 1");
            if (doubleFactorial(1) != 1) System.out.println("dfac(1) != 1");
            if (doubleFactorial(2) != 2) System.out.println("dfac(2) != 2");
            if (doubleFactorial(3) != 3) System.out.println("dfac(3) != 3");
            if (doubleFactorial(5) != 15) System.out.println("dfac(5) != 15");
            if (doubleFactorial(6) != 48) System.out.println("dfac(6) != 48");
        }

        private static void binomialTest() {
            if (binomial(0,0) != 1) System.out.println("binomial(0,0) != 1");
            if (binomial(1,0) != 1) System.out.println("binomial(1,0) != 1");
            if (binomial(1,1) != 1) System.out.println("binomial(1,1) != 1");
            if (binomial(2,1) != 2) System.out.println("binomial(2,1) != 2");
            if (binomial(3,1) != 3) System.out.println("binomial(3,1) != 3");
            if (binomial(3,2) != 3) System.out.println("binomial(3,2) != 3");
            if (binomial(4,2) != 6) System.out.println("binomial(4,2) != 6");
            if (binomial(5,2) != 10) System.out.println("binomial(5,2) != 10");
            if (binomial(6,3) != 20) System.out.println("binomial(6,3) != 20");
            System.out.println("This error is expected");
            if (binomial(3,5) != -1) System.out.println("binomial(3,5) should return -1");
            System.out.println("This error is expected");
            if (binomial(-1,1) != -1) System.out.println("binomial(-1,1) should return -1");
            System.out.println("This error is expected");
            if (binomial(4,-2) != -1) System.out.println("binomial(4,-2) should return -1");
        }

        public static void tests() {
            factorialTest();
            doubleFactorialTest();
            binomialTest();
        }
    }

}
