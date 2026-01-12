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
    private JTextField txtSupervisor;
    private JTextArea txtAbstract;
    private JTextField txtFilePath;
    private JRadioButton rbOral;
    private JRadioButton rbPoster;

    private Student student;

    public CreateSubmission(Student student) {
        this.student = student;

        setTitle("Create Submission");
        setSize(550, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // ===== Research Title =====
        mainPanel.add(new JLabel("Research Title"));
        txtTitle = new JTextField();
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(txtTitle);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Abstract =====
        mainPanel.add(new JLabel("Abstract"));
        txtAbstract = new JTextArea(5, 20);
        JScrollPane abstractScroll = new JScrollPane(txtAbstract);
        abstractScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(abstractScroll);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== File Upload =====
        mainPanel.add(new JLabel("Presentation File"));

        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseFile());

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        filePanel.add(txtFilePath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(filePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Supervisor =====
        mainPanel.add(new JLabel("Supervisor Name"));
        txtSupervisor = new JTextField();
        txtSupervisor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(txtSupervisor);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Presentation Type =====
        mainPanel.add(new JLabel("Presentation Type"));

        rbOral = new JRadioButton("Oral");
        rbPoster = new JRadioButton("Poster");

        ButtonGroup group = new ButtonGroup();
        group.add(rbOral);
        group.add(rbPoster);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(rbOral);
        typePanel.add(rbPoster);

        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(typePanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // ===== Buttons Panel =====
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

        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
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
        String supervisor = txtSupervisor.getText().trim();
        String abs = txtAbstract.getText().trim();
        String filePath = txtFilePath.getText().trim();

        if (title.isEmpty() || supervisor.isEmpty() || abs.isEmpty() || filePath.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "All fields including file upload are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!rbOral.isSelected() && !rbPoster.isSelected()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a presentation type.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
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
