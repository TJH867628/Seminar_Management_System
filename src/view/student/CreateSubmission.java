package view.student;

import app.AppNavigator;
import dao.SubmissionDAO;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import model.Student;
import model.Submission;

public class CreateSubmission extends JFrame {

    private JTextField txtTitle;
    private JTextArea txtAbstract;
    private JTextField txtFilePath;
    private JTextField txtSupervisor;
    private JRadioButton rbOral;
    private JRadioButton rbPoster;

    private Student student;

    public CreateSubmission(Student student) {
        this.student = student;

        setTitle("Create Submission");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ===== Research Title =====
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Research Title:"), gbc);

        txtTitle = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        // ===== Abstract =====
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Abstract:"), gbc);

        txtAbstract = new JTextArea(4, 30);
        JScrollPane abstractScroll = new JScrollPane(txtAbstract);
        gbc.gridx = 1;
        panel.add(abstractScroll, gbc);

        // ===== Presentation File =====
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("presentation file:"), gbc);

        txtFilePath = new JTextField(22);
        txtFilePath.setEditable(false);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseFile());

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filePanel.add(txtFilePath);
        filePanel.add(btnBrowse);

        gbc.gridx = 1;
        panel.add(filePanel, gbc);

        // ===== Supervisor Name =====
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Supervisor Name:"), gbc);

        txtSupervisor = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtSupervisor, gbc);

        // ===== Presentation Type =====
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("presentation type:"), gbc);

        rbOral = new JRadioButton("oral");
        rbPoster = new JRadioButton("poster");

        ButtonGroup group = new ButtonGroup();
        group.add(rbOral);
        group.add(rbPoster);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        typePanel.add(rbOral);
        typePanel.add(rbPoster);

        gbc.gridx = 1;
        panel.add(typePanel, gbc);

        // ===== Buttons =====
        JButton btnBack = new JButton("Back");
        JButton btnSubmit = new JButton("Submit");

        btnBack.addActionListener(e -> {
            AppNavigator.openDashboard(student);
            dispose();
        });

        btnSubmit.addActionListener(e -> submit());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnSubmit);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtFilePath.setText(file.getAbsolutePath());
        }
    }

    private void submit() {

        String title = txtTitle.getText().trim();
        String abs = txtAbstract.getText().trim();
        String supervisor = txtSupervisor.getText().trim();
        String filePath = txtFilePath.getText().trim();

        if (title.isEmpty() || abs.isEmpty() || supervisor.isEmpty() || filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields including file upload are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!rbOral.isSelected() && !rbPoster.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a presentation type.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String presentationType = rbOral.isSelected() ? "Oral" : "Poster";

        Submission submission = new Submission(
                0,
                title,
                filePath,
                student.getStudentID(),
                abs,
                supervisor,
                presentationType
        );

        boolean success = SubmissionDAO.createSubmission(submission);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Submission created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            AppNavigator.openDashboard(student);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create submission.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
