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
    private double epsilon;
    private double delta;
    private SSVGeneratorGui gui;

    protected void setup() {
        // Get agent arguments
        Object[] args = getArguments();
        if (args != null && args.length == 2) {
            try {
                epsilon = Double.parseDouble((String) args[0]);
                delta = Double.parseDouble((String) args[1]);
                if (epsilon < 0 || epsilon > 1 || delta < 0 || delta > 1) {
                    system.out.println("Epsilon and Delta must be in the range [0, 1]. Terminating agent.");
                    doDelete();
                }
            } catch (NumberFormatException e) {
                system.out.println("Invalid arguments. Terminating agent.");
                doDelete();
            }
        } else {
            system.out.println("Invalid arguments. Terminating agent.");
                doDelete();
        }
        // Create and show the GUI
        gui = new SSVGeneratorGui(this);
        gui.showGui();

        // Register the agent with the DF
        registerAgent();
    }
}