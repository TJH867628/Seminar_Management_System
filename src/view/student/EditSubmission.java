package view.student;

import app.AppNavigator;
import dao.SubmissionDAO;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import model.Student;
import model.Submission;

public class EditSubmission extends JFrame {

    private JTextField txtTitle;
    private JTextField txtSupervisor;
    private JTextArea txtAbstract;
    private JTextField txtFilePath;
    private JRadioButton rbOral;
    private JRadioButton rbPoster;

    private final Student student;
    private final Submission submission;

    public EditSubmission(Student student, int submissionID) {

        this.student = student;
        this.submission = SubmissionDAO.getSubmissionById(submissionID);

        if (submission == null) {
            JOptionPane.showMessageDialog(this,
                    "Submission not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Edit Submission");
        setSize(550, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadData();
        setVisible(true);
    }

    private void initUI() {

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        main.add(new JLabel("Research Title"));
        txtTitle = new JTextField();
        main.add(txtTitle);
        main.add(Box.createVerticalStrut(10));

        main.add(new JLabel("Abstract"));
        txtAbstract = new JTextArea(5, 20);
        main.add(new JScrollPane(txtAbstract));
        main.add(Box.createVerticalStrut(10));

        main.add(new JLabel("Presentation File"));
        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseFile());

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.add(txtFilePath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);
        main.add(filePanel);
        main.add(Box.createVerticalStrut(10));

        main.add(new JLabel("Supervisor Name"));
        txtSupervisor = new JTextField();
        main.add(txtSupervisor);
        main.add(Box.createVerticalStrut(10));

        main.add(new JLabel("Presentation Type"));
        rbOral = new JRadioButton("Oral");
        rbPoster = new JRadioButton("Poster");

        ButtonGroup group = new ButtonGroup();
        group.add(rbOral);
        group.add(rbPoster);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(rbOral);
        typePanel.add(rbPoster);
        main.add(typePanel);

        JButton btnBack = new JButton("Back");
        JButton btnSave = new JButton("Save");

        btnBack.addActionListener(e -> {
            AppNavigator.openManageSubmission(this, student);
            dispose();
        });

        btnSave.addActionListener(e -> save());

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnBack);
        btnPanel.add(btnSave);

        main.add(Box.createVerticalStrut(15));
        main.add(btnPanel);

        add(main);
    }

    private void loadData() {
        txtTitle.setText(submission.getResearchTitle());
        txtAbstract.setText(submission.getAbstracts());
        txtSupervisor.setText(submission.getSupervisorName());
        txtFilePath.setText(submission.getFilePath());

        if ("Oral".equalsIgnoreCase(submission.getPresentationType())) {
            rbOral.setSelected(true);
        } else {
            rbPoster.setSelected(true);
        }
    }

    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtFilePath.setText(file.getAbsolutePath());
        }
    }

    private void save() {

        if (!rbOral.isSelected() && !rbPoster.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Select presentation type.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Submission updated = new Submission(
        submission.getSubmissionID(),
        txtTitle.getText().trim(),
        txtFilePath.getText().trim(),
        submission.getStudentID(),
        txtAbstract.getText().trim(),
        txtSupervisor.getText().trim(),
        rbOral.isSelected() ? "Oral" : "Poster",
        submission.getStatus(),
        submission.getCreatedDate()
);

SubmissionDAO.updateSubmission(updated);


        if (SubmissionDAO.updateSubmission(updated)) {
            JOptionPane.showMessageDialog(this,
                    "Submission updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            AppNavigator.openManageSubmission(this, student);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Update failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
