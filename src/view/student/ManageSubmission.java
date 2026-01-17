package view.student;

import app.AppNavigator;
import dao.SubmissionDAO;
import model.Student;
import model.Submission;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageSubmission extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private final Student student;

    public ManageSubmission(Student student) {
        this.student = student;

        setTitle("Manage Submissions");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadData();

        setVisible(true);
    }

    private void initUI() {

        String[] cols = {
                "ID",
                "Research Title",
                "Supervisor",
                "Type",
                "Status",
                "File Path"
        };

        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnEdit = new JButton("View / Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnBack = new JButton("Back");

        btnEdit.addActionListener(e -> edit());
        btnDelete.addActionListener(e -> delete());
        btnBack.addActionListener(e -> {
            AppNavigator.openDashboard(student);
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.add(btnEdit);
        bottom.add(btnDelete);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);

        List<Submission> list =
                SubmissionDAO.getSubmissionsByStudent(student.getStudentID());

        for (Submission s : list) {
            model.addRow(new Object[]{
                    s.getSubmissionID(),
                    s.getResearchTitle(),
                    s.getSupervisorName(),
                    s.getPresentationType(),
                    s.getStatus(),
                    s.getFilePath()
            });
        }
    }

    private void edit() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a submission first.");
            return;
        }

        String status = model.getValueAt(row, 4).toString();

        if (!status.equalsIgnoreCase("submitted")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This submission can no longer be edited.",
                    "Locked",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        new EditSubmission(student, id);
        dispose();
    }

    private void delete() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a submission first.");
            return;
        }

        String status = model.getValueAt(row, 4).toString();
        if (!status.equalsIgnoreCase("submitted")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete after evaluation starts.",
                    "Blocked",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this submission permanently?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);

        if (SubmissionDAO.deleteSubmission(id)) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }
}
