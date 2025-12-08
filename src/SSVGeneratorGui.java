import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SSVGeneratorGui extends JFrame {
    private final SSVGenerator agent;

    private JTextField epsilonField;
    private JTextField deltaField;
    private JTextField fileField;

    public SSVGeneratorGui(SSVGenerator agent) {
        super(agent.getLocalName());
        this.agent = agent;

        setupGui();
    }

    private void setupGui() {
        setTitle("SSV Generator");
        setSize(400, 200);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- FILE PICKER ---
        JPanel filePanel = new JPanel(new BorderLayout());
        fileField = new JTextField();
        fileField.setEditable(false);

        JButton chooseBtn = new JButton("Choose MPs CSV");
        chooseBtn.addActionListener(e -> chooseFile());

        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(chooseBtn, BorderLayout.EAST);

        // --- EPSILON / DELTA FIELDS ---
        JPanel paramsPanel = new JPanel(new GridLayout(2, 2));
        paramsPanel.add(new JLabel("Epsilon:"));
        epsilonField = new JTextField();
        paramsPanel.add(epsilonField);

        paramsPanel.add(new JLabel("Delta:"));
        deltaField = new JTextField();
        paramsPanel.add(deltaField);

        // --- RUN BUTTON ---
        JButton runBtn = new JButton("Run MFN");
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
            double eps = Double.parseDouble(epsilonField.getText());
            double del = Double.parseDouble(deltaField.getText());
            String path = fileField.getText();

            if (path == null || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Choose a CSV file first.");
                return;
            }

            // This calls a method in the agent — YOU create this method.
            agent.handleGuiInput(path, eps, del);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid epsilon/delta.");
        }
    }

    public void showGui() {
        setVisible(true);
    }
}
