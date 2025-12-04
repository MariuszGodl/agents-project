import java.util.Arrays;

public class Main {
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

        MFN instance1 = new MFN(m1, W, C, L, R, rho);
        instance1.printMPs();
        
        instance1.getMPs("MPs0.csv");
        int[] lead = instance1.calculateLeadTimeForAll();
        int[] lead_correct = {12, 19, 38, 33};
        for (int i = 0; i < lead.length; i++) { 
            if ( lead[i] != lead_correct[i]){
                System.out.println("Error: calculateLeadTimeForAll works incorrect for i:" + i + " l: " + lead[i] + " correct: " + lead_correct[i]);
            }
        }

        double[] max_transimitions = instance1.maxTransmitionForAll();
        double[] m_t_correct = {40, 10, 10, 40};
        for (int i = 0; i < max_transimitions.length; i++) { 
            if ( max_transimitions[i] != m_t_correct[i]){
                System.out.println("Error: maxTransmitionForAll works incorrect for i:" + i + " t: " + max_transimitions[i] + " correct: " + m_t_correct[i]);
            }
        }

        int[] transimtion_time = instance1.transimtionTimeForAll(512);
        int[] transimtion_time_correct = {25, 71, 90, 46};
        for (int i = 0; i < transimtion_time.length; i++) { 
            if ( transimtion_time[i] != transimtion_time_correct[i]){
                System.out.println("Error: maxTransmitionForAll works incorrect for i:" + i + " t: " + transimtion_time[i] + " correct: " + transimtion_time_correct[i]);
            }
        }

    }



}