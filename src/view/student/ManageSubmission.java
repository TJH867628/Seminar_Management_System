package view.student;

import app.AppNavigator;
import dao.EvaluationDAO;
import dao.SubmissionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Evaluation;
import model.Student;
import model.Submission;

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
        JButton btnResult = new JButton("View Result");
        JButton btnBack = new JButton("Back");

        btnEdit.addActionListener(e -> edit());
        btnDelete.addActionListener(e -> delete());
        btnResult.addActionListener(e -> viewResult());
        btnBack.addActionListener(e -> {
            AppNavigator.openDashboard(student);
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.add(btnEdit);
        bottom.add(btnDelete);
        bottom.add(btnResult);
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

    private boolean isEditable(Submission s) {
        long diffMs = System.currentTimeMillis() - s.getCreatedDate().getTime();
        return diffMs <= 86_400_000;
    }

    private void edit() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a submission first.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Submission s = SubmissionDAO.getSubmissionById(id);

        if (!isEditable(s)) {
            JOptionPane.showMessageDialog(
                    this,
                    "You can only edit a submission within 24 hours.",
                    "Edit Locked",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        new EditSubmission(student, id);
        dispose();
    }

    private void delete() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a submission first.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Submission s = SubmissionDAO.getSubmissionById(id);

        if (!isEditable(s)) {
            JOptionPane.showMessageDialog(
                    this,
                    "You can only delete a submission within 24 hours.",
                    "Delete Locked",
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

        if (SubmissionDAO.deleteSubmission(id)) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void viewResult() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a submission first.");
            return;
        }

        int submissionID = (int) model.getValueAt(row, 0);
        Evaluation eval = EvaluationDAO.getFinalEvaluation(submissionID);

        if (eval == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Evaluation not available yet.",
                    "Pending",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Problem Clarity: " + eval.getProblemClarity() +
                "\nMethodology: " + eval.getMethodology() +
                "\nResults: " + eval.getResults() +
                "\nPresentation: " + eval.getPresentation() +
                "\n\nTotal Score: " + eval.getTotalScore() +
                "\n\nComments:\n" + eval.getComments(),
                "Evaluation Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
