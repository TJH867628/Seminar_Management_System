package view.student;

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
        setSize(550, 520);
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
        JLabel lblTitle = new JLabel("Research Title");
        txtTitle = new JTextField();
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(txtTitle);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Abstract =====
        JLabel lblAbstract = new JLabel("Abstract");
        txtAbstract = new JTextArea(5, 20);
        JScrollPane abstractScroll = new JScrollPane(txtAbstract);
        abstractScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        mainPanel.add(lblAbstract);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(abstractScroll);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== File Upload =====
        JLabel lblFile = new JLabel("Presentation File");

        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseFile());

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        filePanel.add(txtFilePath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        mainPanel.add(lblFile);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(filePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Supervisor Name =====
        JLabel lblSupervisor = new JLabel("Supervisor Name");
        txtSupervisor = new JTextField();
        txtSupervisor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        mainPanel.add(lblSupervisor);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(txtSupervisor);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== Presentation Type =====
        JLabel lblType = new JLabel("Presentation Type");

        rbOral = new JRadioButton("Oral");
        rbPoster = new JRadioButton("Poster");

        ButtonGroup group = new ButtonGroup();
        group.add(rbOral);
        group.add(rbPoster);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(rbOral);
        typePanel.add(rbPoster);

        mainPanel.add(lblType);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(typePanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // ===== Submit Button =====
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubmit.addActionListener(e -> submit());

        mainPanel.add(btnSubmit);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            txtFilePath.setText(selectedFile.getAbsolutePath());
        }
    }

    private void submit() {

        String title = txtTitle.getText().trim();
        String supervisor = txtSupervisor.getText().trim();
        String abs = txtAbstract.getText().trim();
        String filePath = txtFilePath.getText().trim();

        if (title.isEmpty() || supervisor.isEmpty() || abs.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please upload your presentation file.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!rbOral.isSelected() && !rbPoster.isSelected()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a presentation type (Oral or Poster).",
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
            JOptionPane.showMessageDialog(
                    this,
                    "Submission created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to create submission.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
