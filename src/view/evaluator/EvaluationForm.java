package view.evaluator;

import model.AssignedEvaluation;
import model.Evaluation;
import model.Evaluator;

import util.DialogUtil;

import javax.swing.*;

import app.AppNavigator;

import java.awt.*;

public class EvaluationForm extends JFrame {

    private void updateTotal(JSpinner pc, JSpinner m, JSpinner r, JSpinner p,JLabel totalLabel) {
        int total = (int) pc.getValue() + (int) m.getValue() + (int) r.getValue() + (int) p.getValue();

        totalLabel.setText("Total: " + total);
    }

    public EvaluationForm(AssignedEvaluation assignedEvaluation, Evaluator evaluator) {

        setTitle("Evaluation Form");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(5, 1));
        JLabel lblTitle = new JLabel("Evaluation Form", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblName = new JLabel("Student: " + assignedEvaluation.getStudentName());
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblSession = new JLabel("Session ID: " + assignedEvaluation.getSessionID());
        lblSession.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblSubmissionID = new JLabel("Submission ID: " + assignedEvaluation.getSubmissionID());
        lblSubmissionID.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton btnOpen = new JButton("Open File");
        btnOpen.addActionListener(e -> {
            String filePath = assignedEvaluation.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                try {
                    Desktop.getDesktop().open(new java.io.File(filePath));
                } catch (Exception ex) {
                    DialogUtil.showErrorDialog(this, "Error", "Unable to open file.");
                }
            } else {
                DialogUtil.showErrorDialog(this, "Error", "File path is invalid.");
            }
        });

        headerPanel.add(lblTitle);
        headerPanel.add(lblName);
        headerPanel.add(lblSession);
        headerPanel.add(lblSubmissionID);
        headerPanel.add(btnOpen);

        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2));

        JSpinner pc = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner m  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner r  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner p  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JTextArea comments = new JTextArea(3, 20);
        JLabel totalLabel = new JLabel("Total: 0");
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Back");

        pc.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        m.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        r.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        p.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        back.addActionListener(e -> AppNavigator.openAssignedEvaluations(this, evaluator));

        formPanel.add(new JLabel("Problem Clarity (0 - 10)")); formPanel.add(pc);
        formPanel.add(new JLabel("Methodology (0 - 10)")); formPanel.add(m);
        formPanel.add(new JLabel("Results (0 - 10)")); formPanel.add(r);
        formPanel.add(new JLabel("Presentation (0 - 10)")); formPanel.add(p);
        formPanel.add(new JLabel("Comments (0 - 10)")); formPanel.add(new JScrollPane(comments));
        formPanel.add(totalLabel); formPanel.add(submit);
        formPanel.add(back);

        add(formPanel, BorderLayout.CENTER);

        submit.addActionListener(e -> {
            Evaluation eval = new Evaluation(
                    (int) pc.getValue(),
                    (int) m.getValue(),
                    (int) r.getValue(),
                    (int) p.getValue(),
                    comments.getText()
            );
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}