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


}