// jade imports
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;
// java imports
import java.io.FileWriter;
import java.io.IOException;

public class TT extends Agent {
    private double d; // number of units of flow
    private double T; // max transmission time

    protected void setup() {
        // Get agent arguments
        Object[] args = getArguments();
        System.out.println("TT Agent "+getLocalName()+" started.");
        if (args != null && args.length == 2) {
            try {
                d = Double.parseDouble((String) args[0]);
                T = Double.parseDouble((String) args[1]);
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

        register();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    process(msg);
                } else {
                    block();
                }
            }
        });
    }

    private void register() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("TT-agent");
        sd.setName("TransmissionTimeAgent");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void process(ACLMessage msg) {
        try {
            SSV_data data = (SSV_data) msg.getContentObject();

            writeSSV(data.ssv);

            int success = 0;
            for (double[] X : data.ssv) {
                double TdX = computeTdX(X);
                if (TdX <= T)
                    success++;
            }

            double reliability = (double) success / data.ssv.length;

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent(String.valueOf(reliability));
            send(reply);

            doDelete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeSSV(double[][] ssv) throws IOException {
        FileWriter fw = new FileWriter("SSV.csv");
        for (double[] row : ssv) {
            for (int i = 0; i < row.length; i++) {
                fw.write(row[i] + (i < row.length - 1 ? "," : ""));
            }
            fw.write("\n");
        }
        fw.close();
    }

    private double computeTdX(double[] X) {
        double sum = 0;
        for (double x : X)
            sum += x;
        return sum / d;
    }
}