package view.evaluator;

import model.AssignedEvaluation;
import model.Evaluator;
import dao.EvaluatorDAO;

import javax.swing.*;
import java.awt.*;

public class VotingForm extends JFrame {

    private AssignedEvaluation project;
    private Evaluator evaluator;
    private EvaluatorDAO dao;

    public VotingForm(AssignedEvaluation project, Evaluator evaluator) {
        this.project = project;
        this.evaluator = evaluator;
        this.dao = new EvaluatorDAO();

        setTitle("Vote Project");
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Vote for " + project.getStudentName() + "?", SwingConstants.CENTER);
        JButton btnVote = new JButton("Vote");

        btnVote.addActionListener(e -> submitVote());

        setLayout(new BorderLayout());
        add(lbl, BorderLayout.CENTER);
        add(btnVote, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void submitVote() {
        boolean success = dao.castVote(evaluator.getEvaluatorID(), project.getSubmissionID());
        if (success) {
            JOptionPane.showMessageDialog(this, "Vote counted successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "You have already voted for this project.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
