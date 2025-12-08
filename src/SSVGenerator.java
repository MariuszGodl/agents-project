// jade imports
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
// java imports
import java.util.*;

public class SSVGenerator extends Agent {
    private MFN mfn;
    private double epsilon;
    private double delta;
    private SSVGeneratorGui gui;

    protected void setup() {
        // Get agent arguments
        Object[] args = getArguments();
        System.out.println("SSVGenerator Agent "+getLocalName()+" started.");
        if (args != null && args.length == 2) {
            try {
                epsilon = Double.parseDouble((String) args[0]);
                delta = Double.parseDouble((String) args[1]);
                if (epsilon < 0 || epsilon > 1 || delta < 0 || delta > 1) {
                    System.out.println("Epsilon and Delta must be in the range [0, 1]. Terminating agent.");
                    doDelete();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid arguments. Terminating agent.");
                doDelete();
                return;
            }
        } else {
            System.out.println("Invalid arguments. Terminating agent.");
                doDelete();
                return;
        }
        // Create and show the GUI
        gui = new SSVGeneratorGui(this);
        gui.showGui();

        // Register the agent with the DF
        //registerAgent();
    }

    public void handleGuiInput(String path, int m, int[] W, double[] C, int[] L, double[] R, double[] rh) {
        this.mfn = new MFN(m, W, C, L, R, rh);
        this.mfn.getMPs(path);
        mfn.printMPs();
        System.out.println(mfn);
        int N = 5; 
        double[][]arPDF = mfn.arPMF(); 
        double[][] arCDF = mfn.CDF(arPDF);
        double[][] ssv = mfn.randomSSV(N, arCDF);

        for ( double[] s : ssv) { System.out.println(Arrays.toString(s));}
    }
}