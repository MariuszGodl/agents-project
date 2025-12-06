import java.util.Arrays;

public class Main {
    // move it to const file later
    private static double epsilon_small = 1.0e-8;
    private static double epsilon_medium = 1.0 * 1.0e-4; 
    public static void main(String[] args) {
        testMFN();
    }


    private static void testMFN() {
                
        int m1 = 5;
        int[] W = {4, 3, 2, 3, 2};
        double[] C = {10, 15, 5, 15, 20};
        int[] L = {5, 7, 6, 25, 8};
        double[] R = {0.7, 0.65, 0.67, 0.71, 0.75};
        double[] rho = {0.1, 0.3, 0.5, 0.7, 0.9};

        //1,2,
        // 1,3,5
        // 4,3,2
        // 4,5,

        MFN instance = new MFN(m1, W, C, L, R, rho);
        instance.printMPs();
        
        instance.getMPs("data/MPs0.csv");
        calculateLeadTimeForAlltestMFN(instance);
        maxTransmitionForAlltestMFN(instance);
        transimtionTimeForAlltestMFN(instance);
        arPMFtestMFN(instance);
        minimumTransmissionTime(instance);
        CDFtestMFN(instance);
        normalCDFtestMFN(instance);
        normalICDFtestMFN(instance);
        worstCaseNormalSampleSizetestMFN(instance);
    }
        private static void calculateLeadTimeForAlltestMFN(MFN instance) {
        int[] lead = instance.calculateLeadTimeForAll();
        int[] lead_correct = {12, 19, 38, 33};
        for (int i = 0; i < lead.length; i++) { 
            if ( lead[i] != lead_correct[i]){
                System.out.println("Error: calculateLeadTimeForAll works incorrect for i:" + i + " l: " + lead[i] + " correct: " + lead_correct[i]);
            }
        }
    }
    
    private static void maxTransmitionForAlltestMFN(MFN instance) {
        double[] max_transimitions = instance.maxTransmitionForAll();
        double[] m_t_correct = {40, 10, 10, 40};
        for (int i = 0; i < max_transimitions.length; i++) { 
            if ( max_transimitions[i] != m_t_correct[i]){
                System.out.println("Error: maxTransmitionForAll works incorrect for i:" + i + " t: " + max_transimitions[i] + " correct: " + m_t_correct[i]);
            }
        }
    }

    private static void transimtionTimeForAlltestMFN(MFN instance) {
        int[] transimtion_time = instance.transimtionTimeForAll(512);
        int[] transimtion_time_correct = {25, 71, 90, 46};
        for (int i = 0; i < transimtion_time.length; i++) { 
            if ( transimtion_time[i] != transimtion_time_correct[i]){
                System.out.println("Error: transimtionTimeForAlltestMFN works incorrect for i:" + i + " t: " + transimtion_time[i] + " correct: " + transimtion_time_correct[i]);
            }
        }
    }

    private static void minimumTransmissionTime(MFN instance) {
        int d = 512;
        int minimum_transimition_t = instance.minimumTransmissionTime(d);

        int expected = 25;
        if (minimum_transimition_t != expected) {
            int[] transimtions_t = instance.transimtionTimeForAll(d);
            System.out.println("Error: minimumTransmissionTime works incorrect for min t:" + minimum_transimition_t + 
                                " ex: " + expected + " full_list: " + Arrays.toString(transimtions_t));
        }
    }

    private static void arPMFtestMFN(MFN instance) {
        double[][] arPMF = instance.arPMF();
        for (int i = 0; i < arPMF.length; i++ ) { 
            double test = 1;
            for (int j = 0; j < arPMF[i].length; j++) { test -= arPMF[i][j]; }
            if ( test > epsilon_small ) { 
                System.out.println("Error: arPMFtestMFN works incorrect for i:" + i + " cum_p: " + (1 - test));
            }
        }
    }

    // it is primitive test
    private static void CDFtestMFN(MFN instance) {
        double[][] arPMF = instance.arPMF();
        double[][] cdf = instance.CDF(arPMF);
        for (int i = 0; i < cdf.length; i++ ) { 
            int length = cdf[i].length - 1; 
            if ( cdf[i][length] < (1 - epsilon_small) ) { 
                System.out.println("Error: CDFtestMFN works incorrect for i:" + i + " cum_p: " + cdf[i][length]);
            }
        }
    }

    private static void normalCDFtestMFN(MFN instance) {
        double z1 = 2;
        double p_expected = 0.97725;
        double p1 = instance.normalCDF(z1);
        if ( Math.abs(p1) < (p_expected - epsilon_medium) ) { 
            System.out.println("Error: normalCDFtestMFN works incorrect for p:" + p1 + " p_expected: " + p_expected);
        }

        z1 = 0;
        p_expected = 0.5;
        p1 = instance.normalCDF(z1);
        if ( Math.abs(p1) < (p_expected - epsilon_medium) ) { 
            System.out.println("Error: normalCDFtestMFN works incorrect for p:" + p1 + " p_expected: " + p_expected);
        }

        z1 = -1.96;
        p_expected = 0.024998;
        p1 = instance.normalCDF(z1);
        if ( Math.abs(p1) < (p_expected - epsilon_medium) ) { 
            System.out.println("Error: normalCDFtestMFN works incorrect for p:" + p1 + " p_expected: " + p_expected);
        }
    }

    private static void normalICDFtestMFN(MFN instance) {
        double error_rate = 1.0e-1;
        double p1 = 0.5;
        double z1_expected = 0;
        double z1 = instance.normalICDF(p1);
        if ( Math.abs(z1 - z1_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for z:" + z1 + " z_expected: " + z1_expected);
        }
        
        double p2 = 0.2;
        double z2_expected = -0.84162;
        double z2 = instance.normalICDF(p2);
        if ( Math.abs(z2 - z2_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for p=" + p2 + ", z:" + z2 + " z_expected: " + z2_expected);
        }

        double p3 = 0.8;
        double z3_expected = 0.84162;
        double z3 = instance.normalICDF(p3);
        if ( Math.abs(z3 - z3_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for p=" + p3 + ", z:" + z3 + " z_expected: " + z3_expected);
        }

        double p4 = 0.9;
        double z4_expected = 1.28155;
        double z4 = instance.normalICDF(p4);
        if ( Math.abs(z4 - z4_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for p=" + p4 + ", z:" + z4 + " z_expected: " + z4_expected);
        }

        double p5 = 0.05;
        double z5_expected = -1.64485;
        double z5 = instance.normalICDF(p5);
        if ( Math.abs(z5 - z5_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for p=" + p5 + ", z:" + z5 + " z_expected: " + z5_expected);
        }

        double p6 = 0.975;
        double z6_expected = 1.95996;
        double z6 = instance.normalICDF(p6);
        
        // UWAGA: Przy p=0.975 przybliżenie Winitzkiego zwraca ok. 1.956. 
        // Przy error_rate = 0.001 test by nie przeszedł.
        if ( Math.abs(z6 - z6_expected) > error_rate ){
            System.out.println("Error: normalICDFtestMFN works incorrect for p=" + p6 + ", z:" + z6 + " z_expected: " + z6_expected);
        }
    }

    private static void worstCaseNormalSampleSizetestMFN(MFN instance) {
        double error_rate = 1.0e-2;
        // Case 1: Standard (95% Confidence, 5% Error)
        double err = 0.05;
        double diviation = 0.05;
        int n_expected = 385;
        int n = instance.worstCaseNormalSampleSize(diviation, err);
        
        if ( Math.abs((double)n  / n_expected -1) > error_rate ) { 
            System.out.println("Error: worstCaseNormalSampleSizetestMFN Case 1 works incorrect for n:" + n + " p_expected: " + n_expected);
        }

        // Case 2: High Confidence (99% Confidence, 5% Error)
        err = 0.05;
        diviation = 0.01;
        n_expected = 664;
        n = instance.worstCaseNormalSampleSize(diviation, err);
        if ( Math.abs((double)n  / n_expected -1) > error_rate )  { 
            System.out.println("Error: worstCaseNormalSampleSizetestMFN Case 2 works incorrect for n:" + n + " p_expected: " + n_expected);
        }

        // Case 3: High Precision (95% Confidence, 1% Error)
        err = 0.01;
        diviation = 0.05;
        n_expected = 9604;
        n = instance.worstCaseNormalSampleSize(diviation, err);

        if ( Math.abs((double)n  / n_expected -1) > error_rate  ) { 
            System.out.println("Error: worstCaseNormalSampleSizetestMFN Case 3 works incorrect for n:" + n + " p_expected: " + n_expected);
        }

        // Case 4: Low Confidence (90% Confidence, 10% Error)
        err = 0.10;
        diviation = 0.10;
        n_expected = 68;
        n = instance.worstCaseNormalSampleSize(diviation, err);

        if ( Math.abs((double)n  / n_expected -1) > error_rate  ) { 
            System.out.println("Error: worstCaseNormalSampleSizetestMFN Case 4 works incorrect for n:" + n + " p_expected: " + n_expected);
        }
    }
}