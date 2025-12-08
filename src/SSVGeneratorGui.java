import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;

public class SSVGeneratorGui extends JFrame {
    private final SSVGenerator agent;

    private JTextField num_of_links_Field;
    private JTextField components_number_v_Field;
    private JTextField components_capacities_v_Field;
    private JTextField lead_time_v_Field;
    private JTextField component_reliability_v_Field;
    private JTextField correlation_between_faults_v_Field;
    private JTextField fileField;

    public SSVGeneratorGui(SSVGenerator agent) {
        super(agent.getLocalName());
        this.agent = agent;

        setupGui();
    }

    private void setupGui() {
        setTitle("SSV Generator");
        setSize(700, 500);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // File selection panel
        JPanel filePanel = new JPanel(new BorderLayout());
        fileField = new JTextField();
        fileField.setEditable(false);

        JButton chooseBtn = new JButton("Choose MPs CSV");
        chooseBtn.addActionListener(e -> chooseFile());

        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(chooseBtn, BorderLayout.EAST);

        // MFN parameters panel
        JPanel paramsPanel = new JPanel(new GridLayout(6, 2));
        
        paramsPanel.add(new JLabel("The number of links:"));
        num_of_links_Field = new JTextField();
        paramsPanel.add(num_of_links_Field);

        paramsPanel.add(new JLabel("The component numbers vector:"));
        components_number_v_Field = new JTextField();
        paramsPanel.add(components_number_v_Field);

        paramsPanel.add(new JLabel("The component capacities vector:"));
        components_capacities_v_Field = new JTextField();
        paramsPanel.add(components_capacities_v_Field);

        paramsPanel.add(new JLabel("The lead time vector:"));
        lead_time_v_Field = new JTextField();
        paramsPanel.add(lead_time_v_Field);

        paramsPanel.add(new JLabel("The component reliabilities vector:"));
        component_reliability_v_Field = new JTextField();
        paramsPanel.add(component_reliability_v_Field);

        paramsPanel.add(new JLabel("The vector of the correlation between the faults of the components:"));
        correlation_between_faults_v_Field = new JTextField();
        paramsPanel.add(correlation_between_faults_v_Field);


        // Send button
        JButton runBtn = new JButton("Send Data");
        runBtn.addActionListener(e -> sendToAgent());

        add(filePanel);
        add(paramsPanel);
        add(runBtn);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            fileField.setText(selected.getAbsolutePath());
        }
    }

    private void sendToAgent() {
        try {
            /* fields in MFN.java
            private int m ;// number_of_links
            private int[] W; // component number vector
            private double[] C; // component capacity vector
            private int[] L; // lead time vector
            private double[] R; // component reliability vector
            private double[] rho; // vector of the correlation between the faults of the components
            */
            int m = Integer.parseInt(num_of_links_Field.getText());
            int[] W = Arrays.stream(components_number_v_Field.getText().split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
            double[] C = Arrays.stream(components_capacities_v_Field.getText().split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
            int[] L = Arrays.stream(lead_time_v_Field.getText().split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
            double[] R = Arrays.stream(component_reliability_v_Field.getText().split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
            double[] rh = Arrays.stream(correlation_between_faults_v_Field.getText().split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
            String path = fileField.getText();

            if (path == null || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Choose a CSV file first.");
                return;
            }

            agent.handleGuiInput(path, m, W, C, L, R, rh);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid data");
        }
    }

    public void showGui() {
        setVisible(true);
    }
}
