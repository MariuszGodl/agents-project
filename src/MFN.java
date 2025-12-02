
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

    MFN(int m, int[] W, double[] C, int[] L, double[] R, double[] rho){
        //check whether the length of vectors W, C, L, R, and rho is equal to m;
        //check whether all values of R and rho are between 0 and 1;
        //create the beta vector if the above-mentioned conditions are satisfied.
    }
    // tmp
    MFN(){
        Combinatorial tmp = new Combinatorial();
        tmp.tests();
    }

    class Combinatorial{
        Combinatorial(){}

        // do we need to do it for int or double as well
        public double binomial(int n, int k) {
            if (k > n || n < 0 || k < 0){
                System.out.println("Error: Incorrect input for binomial, n: " + n + " k: "+ k);
                return -1;
            }
            return (double) factorial(n) / (factorial(k) * factorial(n-k));
        }

        public long doubleFactorial(int n){
            return factorial(n, 2);
        }

        public long factorial(int n){ return factorial(n, 1); }
        // might need to implement cache
        public long factorial(int n, int step) {
            // might be a need to handle the error ndk rn
            if ((n < -1) ) {
                System.out.println("Error: Incorrect input value in factorial, n: " + n + ",  step: " + step);
                return -1;
            }
            if (n == 0 || n == 1 || (n == -1 && step == 2)) { return 1; }

           return (n * factorial(n-step, step));
        }

        private void factorialTest(){
            if (factorial(0) != 1) {
                System.out.println("fac(0) != 1");
            }
            if (factorial(1) != 1) {
                System.out.println("fac(1) != 1");
            }
            if (factorial(2) != 2) {
                System.out.println("fac(2) != 2");
            }
            if (factorial(3) != 6) {
                System.out.println("fac(3) != 6");
            }
            if (factorial(5) != 120) {
                System.out.println("fac(5) != 120");
            }
        }

        private void doubleFactorialTest(){
            if (doubleFactorial(0) != 1) {
                System.out.println("dfac(0) != 1");
            }
            if (doubleFactorial(1) != 1) {
                System.out.println("dfac(1) != 1");
            }
            if (doubleFactorial(2) != 2) {
                System.out.println("dfac(2) != 2");
            }
            if (doubleFactorial(3) != 3) {
                System.out.println("dfac(3) != 3");
            }
            if (doubleFactorial(5) != 15) {
                System.out.println("dfac(5) != 15");
            }
            if (doubleFactorial(6) != 48) {
                System.out.println("dfac(6) != 48");
            }
        }

        private void binomialTest() {

            // Basic cases
            if (binomial(0, 0) != 1) {
                System.out.println("binomial(0,0) != 1");
            }
            if (binomial(1, 0) != 1) {
                System.out.println("binomial(1,0) != 1");
            }
            if (binomial(1, 1) != 1) {
                System.out.println("binomial(1,1) != 1");
            }

            // Common values
            if (binomial(2, 1) != 2) {
                System.out.println("binomial(2,1) != 2");
            }
            if (binomial(3, 1) != 3) {
                System.out.println("binomial(3,1) != 3");
            }
            if (binomial(3, 2) != 3) {
                System.out.println("binomial(3,2) != 3");
            }
            if (binomial(4, 2) != 6) {
                System.out.println("binomial(4,2) != 6");
            }
            if (binomial(5, 2) != 10) {
                System.out.println("binomial(5,2) != 10");
            }
            if (binomial(6, 3) != 20) {
                System.out.println("binomial(6,3) != 20");
            }

            // k > n (invalid)
            if (binomial(3, 5) != -1) {
                System.out.println("binomial(3,5) should return -1 for invalid input");
            }

            // Negative values (invalid)
            if (binomial(-1, 1) != -1) {
                System.out.println("binomial(-1,1) should return -1 for invalid input");
            }
            if (binomial(4, -2) != -1) {
                System.out.println("binomial(4,-2) should return -1 for invalid input");
            }
        }


        public void tests(){
            factorialTest();
            doubleFactorialTest();
            binomialTest();
        }

    }



}
