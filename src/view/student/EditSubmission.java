package view.student;

import dao.SubmissionDAO;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import model.Student;
import model.Submission;

public class EditSubmission extends JFrame {

    private final Student student;
    private final Submission submission;

    private JTextField txtTitle;
    private JTextArea txtAbstract;
    private JTextField txtSupervisor;
    private JTextField txtFilePath;
    private JRadioButton rbOral;
    private JRadioButton rbPoster;

    public EditSubmission(Student student, Submission submission) {
        this.student = student;
        this.submission = submission;

        setTitle("Edit Submission");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Research Title:"), gbc);
        txtTitle = new JTextField(submission.getResearchTitle(), 30);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Abstract:"), gbc);
        txtAbstract = new JTextArea(submission.getAbstracts(), 4, 30);
        gbc.gridx = 1;
        panel.add(new JScrollPane(txtAbstract), gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Presentation File:"), gbc);
        txtFilePath = new JTextField(submission.getFilePath(), 22);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browse());

        JPanel filePanel = new JPanel();
        filePanel.add(txtFilePath);
        filePanel.add(btnBrowse);

        gbc.gridx = 1;
        panel.add(filePanel, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Supervisor Name:"), gbc);
        txtSupervisor = new JTextField(submission.getSupervisorName(), 30);
        gbc.gridx = 1;
        panel.add(txtSupervisor, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Presentation Type:"), gbc);

        rbOral = new JRadioButton("Oral");
        rbPoster = new JRadioButton("Poster");

        if ("Oral".equalsIgnoreCase(submission.getStatus())) {
            rbOral.setSelected(true);
        } else {
            rbPoster.setSelected(true);
        }

        ButtonGroup g = new ButtonGroup();
        g.add(rbOral);
        g.add(rbPoster);

        JPanel typePanel = new JPanel();
        typePanel.add(rbOral);
        typePanel.add(rbPoster);

        gbc.gridx = 1;
        panel.add(typePanel, gbc);

        JButton btnUpdate = new JButton("Update");
        JButton btnBack = new JButton("Back");

        btnBack.addActionListener(e -> {
            new ManageSubmission(student);
            dispose();
        });

        btnUpdate.addActionListener(e -> update());

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnBack);
        btnPanel.add(btnUpdate);

        panel.add(btnPanel, gbc);
        add(panel);
    }

    private void browse() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            txtFilePath.setText(f.getAbsolutePath());
        }
    }

    private void update() {

        Submission updated = new Submission(
                submission.getSubmissionID(),
                txtTitle.getText().trim(),
                txtFilePath.getText().trim(),
                submission.getStudentID(),
                txtAbstract.getText().trim(),
                txtSupervisor.getText().trim(),
                rbOral.isSelected() ? "Oral" : "Poster"
        );

        boolean ok = SubmissionDAO.updateSubmission(updated);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Submission updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            new ManageSubmission(student);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Update failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
