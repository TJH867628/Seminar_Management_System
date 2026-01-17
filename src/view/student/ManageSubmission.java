package view.student;

import app.AppNavigator;
import dao.SubmissionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Student;
import model.Submission;

public class ManageSubmission extends JFrame {

    private final Student student;
    private JTable table;
    private DefaultTableModel model;

    public ManageSubmission(Student student) {
        this.student = student;

        setTitle("Manage Submission");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadData();
        setVisible(true);
    }

    private void initUI() {

        model = new DefaultTableModel(
                new String[]{"ID", "Research Title", "Supervisor", "Type"},
                0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnEdit = new JButton("View / Edit");
        JButton btnBack = new JButton("Back");

        btnEdit.addActionListener(e -> openSelected());
        btnBack.addActionListener(e -> {
            AppNavigator.openDashboard(student);
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.add(btnEdit);
        bottom.add(btnBack);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadData() {

        model.setRowCount(0);

        List<Submission> submissions =
                SubmissionDAO.getSubmissionsByStudent(student.getStudentID());

        for (Submission s : submissions) {
            model.addRow(new Object[]{
                    s.getSubmissionID(),
                    s.getResearchTitle(),
                    s.getSupervisorName(),
                    s.getStatus()
            });
        }
    }

    private void openSelected() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a submission.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int submissionID = (int) model.getValueAt(row, 0);
        Submission submission = SubmissionDAO.getSubmissionByID(submissionID);

        if (submission != null) {
            new EditSubmission(student, submission);
            dispose();
        }
    }
}
