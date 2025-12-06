import java.io.BufferedReader;
import java.io.FileReader;
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
    private ArrayList<int[]> MPs = new ArrayList<>(); // list of minimal paths

    MFN(int m, int[] W, double[] C, int[] L, double[] R, double[] rho) {
        //check whether the length of vectors W, C, L, R, and rho is equal to m;
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
        if ( m < 0 ) {throw new IllegalArgumentException("m is negative: " + m);}
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

    private double[] probabilityThatLinkAExhibitsCapacity(int edge_idx) {

        double local_beta = this.beta[edge_idx];
        int local_w = this.W[edge_idx];
        double local_r = this.R[edge_idx];

        double[] res = new double[local_w + 1];

        for ( int k = 0; k <= local_w; k++) {
            if ( k >= 1 ) {
                res[k] = (1.0 / local_beta) *
                        ( Combinatorial.binomial( local_w, k )) *
                        Math.pow( local_r * local_beta, k ) *
                        Math.pow( 1 - local_r * local_beta, local_w - k);
            } else if ( k == 0 ) {
                res[k] = 1 - 1 / local_beta *
                        ( 1 - Math.pow( 1 - local_r * local_beta, local_w));
            }
        }

        return res;
    }

    public double[][] arPMF() {

        double[][] res = new double[m][];
        for ( int edge_idx = 0; edge_idx < m; edge_idx++) {
            res[edge_idx] = probabilityThatLinkAExhibitsCapacity(edge_idx);
        }
        return res;
    }

    public double[][] CDF(double[][] arPMF) {
        double[][] cdf = new double[m][];

        for( int i = 0; i < m; i++ ) { cdf[i] = new double[ W[i] + 1 ]; }

        for ( int edge = 0; edge < m; edge++ ) {
            cdf[edge][0] = arPMF[edge][0];
            for ( int k_value = 1; k_value < arPMF[edge].length; k_value++) {
                cdf[edge][k_value] = arPMF[edge][k_value] + cdf[edge][k_value - 1];
            }
        }

        return cdf;
    }

    // 4th function in 1
    private int calculateLeadTime(int[] Mps_local) {
        int total_l = 0;
        for ( int edge : Mps_local) { total_l +=  L[edge-1]; }
        return total_l;
    }

    public int[] calculateLeadTimeForAll() {
        int[] total_lead = new int[MPs.size()];
        
        for (int i = 0; i < MPs.size(); i++){
            total_lead[i] = calculateLeadTime(MPs.get(i));
        }

        return total_lead;
    }

    // 5th function in 1 
    // remember to make it for actual SSV not only components * capacity
    private double maxTransmition(int[] Mps_local) {
        double min = Double.POSITIVE_INFINITY;
        for (int edge: Mps_local) { 
            if (min > W[edge-1] * C[edge - 1]) { min = W[edge-1] * C[edge - 1]; }
         }
        return min;
    }

    public double[] maxTransmitionForAll() {
        double[] max_transimitons = new double[MPs.size()];

        for (int i = 0; i < MPs.size(); i++ ){
            max_transimitons[i] = maxTransmition(MPs.get(i));
        }
        return max_transimitons;

    }

    // 3rd function 1
    private int transimtionTime(int[] Mps_local, int d) {
        double cp = maxTransmition(Mps_local);
        
        if ( cp > 0) {
            return calculateLeadTime(Mps_local) + (int) Math.ceil((double) d / maxTransmition(Mps_local));
        } else {
            return 0x7FFFFFFF; // max possible positive int
        }

    }

    // replace d with value given by the system might be int[]
    public int[] transimtionTimeForAll(int d) {
        int[] t = new int[MPs.size()]; 
        
        for ( int i = 0; i < MPs.size(); i++) {
            t[i] = transimtionTime(MPs.get(i), d);
        }

        return t;
    }

    // 8th function 1
    // not sure if it answer the question as we search the smallest price acreoss the all MPS 
    public int minimumTransmissionTime(int d) {
        int[] transimtions_t = transimtionTimeForAll(d);
        int min_transimition_t = 0x7FFFFFFF;
        for (int t : transimtions_t) { 
            if ( t < min_transimition_t) { 
                min_transimition_t = t; 
            } 
        }
        return min_transimition_t;
    }

    private double SeriesElement(double z, int k) {
        double nominator = Math.pow(z, 2 * k + 1);
        double denominator = Combinatorial.doubleFactorial(2*k + 1);
        return nominator / denominator;
    }

    private double sumApproximation(double z) {
        int degree_of_apoximation_n = 20;//100;
        double sum = 0;
        for (int k = 0; k < degree_of_apoximation_n; k++) {
            sum += SeriesElement(z, k);
        }

        return sum;
    }

    public double normalCDF(double z) { 

        double res = 0.5 + 1 / Math.sqrt(2 * Math.PI) * 
                    Math.pow( Math.E, -1 * z * z * 0.5) * 
                    sumApproximation(z);
        return res;
    }

    // DESMOS : \sqrt{2}\cdot\operatorname{sgn}\left(2x-1\right)\sqrt{\sqrt{\left(\frac{2}{\pi\cdot1.47}+\frac{\ln\left(1-\left(2x-1\right)^{2}\right)}{2}\right)^{2}-\frac{\ln\left(1-\left(2x-1\right)^{2}\right)}{1.47}}-\left(\frac{2}{\pi\cdot1.47}+\frac{\ln\left(1-\left(2x-1\right)^{2}\right)}{2}\right)}
    // https://www.academia.edu/9730974/A_handy_approximation_for_the_error_function_and_its_inverse
    
    public double normalICDF(double u) {
        double first_compontent = Math.sqrt(2) * Math.signum(2 * u - 1);
        double inner_log = Math.log( 1 - ( Math.pow( 2*u - 1, 2) ) );
        double a = 0.147;
        double first_root_elemetnt = Math.pow(2 / (Math.PI * a) + inner_log / 2, 2) - inner_log / a;
        double second_root_element = 2 / (Math.PI * a) + inner_log / 2;

        return first_compontent * Math.sqrt( Math.sqrt(first_root_elemetnt)  - second_root_element);
    }

    
    public void test() {
        validataInputTest();
        validateRvaluesTest();
        calculateBetaTest();
        Combinatorial.tests();
    }

    private static class  Combinatorial {

        public static long binomial(int n, int k) {
            if (k < 0 || k > n) {
                throw new IllegalArgumentException("Error: Incorrect input for binomial, n: " + n + " k: " + k);
            }

            return factorial(n) / (factorial(k) * factorial(n - k));
        }

        public static long doubleFactorial(int n) {
            return factorial(n, 2);
        }

        public static long factorial(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Error: Incorrect input for factorial, n: " + n);
            }
            return factorial(n, 1);
        }


        public static long factorial(int n, int step) {
            if (n < -1) {
                throw new IllegalArgumentException("Error: Incorrect input for factorial, n: " + n + " step: " + step);
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
            try {
                factorial(-1);
                System.out.println("factorial(-1) should throw an exception");
            } catch (IllegalArgumentException ignored) {}
        }

        private static void doubleFactorialTest() {
            if (doubleFactorial(0) != 1) System.out.println("dfac(0) != 1");
            if (doubleFactorial(1) != 1) System.out.println("dfac(1) != 1");
            if (doubleFactorial(2) != 2) System.out.println("dfac(2) != 2");
            if (doubleFactorial(3) != 3) System.out.println("dfac(3) != 3");
            if (doubleFactorial(5) != 15) System.out.println("dfac(5) != 15");
            if (doubleFactorial(6) != 48) System.out.println("dfac(6) != 48");
            try {
                doubleFactorial(-2);
                System.out.println("doubleFactorial(-2) should throw an exception");
            } catch (IllegalArgumentException ignored) {}
            try {
                doubleFactorial(-3);
                System.out.println("doubleFactorial(-3) should throw an exception");
            } catch (IllegalArgumentException ignored) {}
        }

        private static void binomialTest() {
            if (binomial(0, 0) != 1) System.out.println("binomial(0,0) != 1");
            if (binomial(1, 0) != 1) System.out.println("binomial(1,0) != 1");
            if (binomial(1, 1) != 1) System.out.println("binomial(1,1) != 1");
            if (binomial(2, 1) != 2) System.out.println("binomial(2,1) != 2");
            if (binomial(3, 1) != 3) System.out.println("binomial(3,1) != 3");
            if (binomial(3, 2) != 3) System.out.println("binomial(3,2) != 3");
            if (binomial(4, 2) != 6) System.out.println("binomial(4,2) != 6");
            if (binomial(5, 2) != 10) System.out.println("binomial(5,2) != 10");
            if (binomial(6, 3) != 20) System.out.println("binomial(6,3) != 20");

            // Test invalid inputs (expect exceptions)
            try {
                binomial(3, 5);
                System.out.println("binomial(3,5) should throw an exception");
            } catch (IllegalArgumentException ignored) {}

            try {
                binomial(-1, 1);
                System.out.println("binomial(-1,1) should throw an exception");
            } catch (IllegalArgumentException ignored) {}

            try {
                binomial(4, -2);
                System.out.println("binomial(4,-2) should throw an exception");
            } catch (IllegalArgumentException ignored) {}
        }

        public static void tests() {
            factorialTest();
            doubleFactorialTest();
            binomialTest();
        }
    }
    
    public void getMPs(String fileName) {
        //MPs.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skipping empty lines
                String[] parts = line.split(",");

                // convert to ints
                ArrayList<Integer> temp = new ArrayList<>();
                for (String p: parts) {
                    p = p.trim();
                    if (!p.isEmpty()) {
                        temp.add(Integer.parseInt(p));
                    }
                }

                // move into int[]
                int[] arr = new int[temp.size()];
                for (int i = 0; i < temp.size(); i++) {
                    arr[i] = temp.get(i);
                }

                MPs.add(arr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printMPs() {
        for (int[] arr: MPs) {
            System.out.println(Arrays.toString(arr));
        }
    }


    private void validataInputTest(){
        // int m, int[] W, double[] C, int[] L, double[] R, double[] rho
        int correct_m = 3;
        int[] correct_W = { 1, 2, 3};
        double[] correct_C = { 2.1, 3.2, 4.1};
        int[] correct_L = {1, 2, 3};
        double[] correct_R = {0.1, 0, 1};
        double[] correct_rho = {1, 2, 3};

        try {
            validateInput(correct_m, correct_W, correct_C, correct_L, correct_R, correct_rho);
        } catch ( IllegalArgumentException e) {
            System.out.println("validataInputTest should NOT throw an exception for correct input");
        }
        // test m
        try {
            validateInput(1, correct_W, correct_C, correct_L, correct_R, correct_rho);
            System.out.println("validataInputTest should throw an exception for m ");
        } catch ( IllegalArgumentException ignore) {}
        try {
            int[] tmp_w = {1, 2};
            validateInput(correct_m, tmp_w, correct_C, correct_L, correct_R, correct_rho);
            System.out.println("validataInputTest should throw an exception for W ");
        } catch ( IllegalArgumentException ignore) {}
        try {
            double[] tmp_c = {1, 2};
            validateInput(correct_m, correct_W, tmp_c, correct_L, correct_R, correct_rho);
            System.out.println("validataInputTest should throw an exception for C ");
        } catch ( IllegalArgumentException ignore) {}
        try {
            int[] tmp_L = {1, 2};
            validateInput(correct_m, correct_W, correct_C, tmp_L, correct_R, correct_rho);
            System.out.println("validataInputTest should throw an exception for L ");
        } catch ( IllegalArgumentException ignore) {}
        try {
            double[] tmp_R = {1, 2};
            validateInput(correct_m, correct_W, correct_C, correct_L, tmp_R, correct_rho);
            System.out.println("validataInputTest should throw an exception for R ");
        } catch ( IllegalArgumentException ignore) {}
        try {
            double[] tmp_rho = {1, 2};
            validateInput(correct_m, correct_W, correct_C, correct_L, correct_R, tmp_rho);
            System.out.println("validataInputTest should throw an exception for Rho");
        } catch ( IllegalArgumentException ignore) {}
    }

    private void validateRvaluesTest() {
        double[] R_correct = {0.0, 0.5, 1.0};
        try {
            validateRvalues(R_correct);
        } catch (IllegalArgumentException e) {
            System.out.println("validateRvaluesTest FAILED: should NOT throw exception for valid values");
        }

        double[] R_negative = {-0.1, 0.5, 0.7};
        try {
            validateRvalues(R_negative);
            System.out.println("validateRvaluesTest FAILED: exception expected for value < 0");
        } catch (IllegalArgumentException ignore) {}

        double[] R_over = {0.2, 1.1, 0.5};
        try {
            validateRvalues(R_over);
            System.out.println("validateRvaluesTest FAILED: exception expected for value > 1");
        } catch (IllegalArgumentException ignore) {}

        // Edge case: all values at the boundaries
        double[] R_boundary = {0.0, 1.0};
        try {
            validateRvalues(R_boundary);
        } catch (IllegalArgumentException e) {
            System.out.println("validateRvaluesTest FAILED: should NOT throw exception for boundary values");
        }
    }

    private void calculateBetaTest() {
        // Correct input
        double[] R = {0.1, 0.5, 1.0};
        double[] rho = {1.0, 2.0, 3.0};
        double[] expectedBeta = new double[R.length];

        for (int i = 0; i < R.length; i++) {
            expectedBeta[i] = 1 + (rho[i] * (1 - R[i])) / R[i];
        }

        try {
            double[] beta = calculateBeta(R, rho);

            boolean correct = true;
            for (int i = 0; i < beta.length; i++) {
                if (Math.abs(beta[i] - expectedBeta[i]) > 1e-9) { // allow small floating point error
                    correct = false;
                    break;
                }
            }

            if (!correct) {System.out.println("calculateBetaTest FAILED: output does not match expected values");}

        } catch (IllegalArgumentException e) {
            System.out.println("calculateBetaTest should NOT throw an exception for correct input");
        }

        double[] R_wrong = {0.1, 0.5};
        double[] rho_wrong = {1.0, 2.0, 3.0};

        try {
            double[] beta = calculateBeta(R_wrong, rho_wrong);
            System.out.println("calculateBetaTest FAILED: exception expected for mismatched array lengths");
        } catch (IllegalArgumentException ignore) {}
    }

}
