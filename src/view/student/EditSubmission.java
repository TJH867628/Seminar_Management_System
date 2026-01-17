package view.student;

import dao.SubmissionDAO;
import java.awt.*;
import java.io.File;
import java.util.List;
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
    private Submission submission;

    public EditSubmission(Student student, int submissionID) {
        this.student = student;

        Submission found = null;
        List<Submission> list =
                SubmissionDAO.getSubmissionsByStudent(student.getStudentID());

        for (Submission s : list) {
            if (s.getSubmissionID() == submissionID) {
                found = s;
                break;
            }
        }

        if (found == null) {
            JOptionPane.showMessageDialog(this, "Submission not found.");
            dispose();
            return;
        }

        this.submission = found;

        setTitle("Edit Submission");
        setSize(550, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadData();

        setVisible(true);
    }

    private void initUI() {

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // ===== Research Title =====
        main.add(new JLabel("Research Title"));
        txtTitle = new JTextField();
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        main.add(txtTitle);
        main.add(Box.createVerticalStrut(15));

        // ===== Abstract =====
        main.add(new JLabel("Abstract"));
        txtAbstract = new JTextArea(5, 20);
        JScrollPane sp = new JScrollPane(txtAbstract);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        main.add(sp);
        main.add(Box.createVerticalStrut(15));

        // ===== File =====
        main.add(new JLabel("Presentation File"));
        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browse());

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        filePanel.add(txtFilePath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        main.add(filePanel);
        main.add(Box.createVerticalStrut(15));

        // ===== Supervisor =====
        main.add(new JLabel("Supervisor Name"));
        txtSupervisor = new JTextField();
        txtSupervisor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        main.add(txtSupervisor);
        main.add(Box.createVerticalStrut(15));

        // ===== Type =====
        main.add(new JLabel("Presentation Type"));

        rbOral = new JRadioButton("Oral");
        rbPoster = new JRadioButton("Poster");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbOral);
        bg.add(rbPoster);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(rbOral);
        typePanel.add(rbPoster);
        main.add(typePanel);
        main.add(Box.createVerticalStrut(20));

        // ===== Buttons =====
        JButton btnBack = new JButton("Back");
        JButton btnSave = new JButton("Save Changes");

        btnBack.addActionListener(e -> {
            new ManageSubmission(student);
            dispose();
        });

        btnSave.addActionListener(e -> save());

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnBack);
        btnPanel.add(btnSave);

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

        // 🔒 LOCK if evaluated
        if (!submission.getStatus().equalsIgnoreCase("submitted")) {
            txtTitle.setEnabled(false);
            txtAbstract.setEnabled(false);
            txtSupervisor.setEnabled(false);
            txtFilePath.setEnabled(false);
            rbOral.setEnabled(false);
            rbPoster.setEnabled(false);
        }
    }

    private void browse() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            txtFilePath.setText(f.getAbsolutePath());
        }
    }

    private void save() {

        if (!submission.getStatus().equalsIgnoreCase("submitted")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This submission can no longer be edited.",
                    "Locked",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (txtTitle.getText().isBlank()
                || txtSupervisor.getText().isBlank()
                || txtAbstract.getText().isBlank()
                || txtFilePath.getText().isBlank()) {

            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        String type = rbOral.isSelected() ? "Oral" : "Poster";

        Submission updated = new Submission(
                submission.getSubmissionID(),
                txtTitle.getText().trim(),
                txtFilePath.getText().trim(),
                student.getStudentID(),
                txtAbstract.getText().trim(),
                txtSupervisor.getText().trim(),
                type,
                submission.getStatus() // 👈 status untouched
        );

        if (SubmissionDAO.updateSubmission(updated)) {
            JOptionPane.showMessageDialog(this, "Submission updated.");
            new ManageSubmission(student);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }
}
