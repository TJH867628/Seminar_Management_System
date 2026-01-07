package view.evaluator;

import model.AssignedEvaluation;
import model.Evaluation;
import model.Evaluator;

import util.DialogUtil;
import app.AppNavigator;
import dao.EvaluationDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class EvaluationForm extends JFrame {

    // =========================
    // Helper methods
    // =========================

    private void updateTotal(JSpinner pc, JSpinner m, JSpinner r, JSpinner p, JLabel totalLabel) {
        int total = (int) pc.getValue()
                  + (int) m.getValue()
                  + (int) r.getValue()
                  + (int) p.getValue();
        totalLabel.setText("Total: " + total);
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    // =========================
    // Constructor
    // =========================

    public EvaluationForm(AssignedEvaluation assignedEvaluation, Evaluator evaluator) {

        setTitle("Evaluation Form");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================
        // Header panel
        // =========================

        JPanel headerPanel = new JPanel(new GridLayout(5, 1));

        JLabel lblTitle = new JLabel("Evaluation Form", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblName = new JLabel("Student: " + assignedEvaluation.getStudentName());
        JLabel lblSession = new JLabel("Session ID: " + assignedEvaluation.getSessionID());
        JLabel lblSubmissionID = new JLabel("Submission ID: " + assignedEvaluation.getSubmissionID());

        JButton btnOpen = new JButton("Open File");
        btnOpen.addActionListener(e -> {
            try {
                String rawPath = assignedEvaluation.getFilePath();
                if (rawPath == null || rawPath.trim().isEmpty()) {
                    DialogUtil.showErrorDialog(this, "Error", "File path is invalid.");
                    return;
                }

                // remove surrounding quotes if exist
                rawPath = rawPath.trim().replaceAll("^['\"]|['\"]$", "");
                File file = new File(rawPath);

                if (!file.exists()) {
                    DialogUtil.showErrorDialog(this, "Error", "File not found.");
                    return;
                }

                Desktop.getDesktop().open(file);

            } catch (Exception ex) {
                DialogUtil.showErrorDialog(this, "Error", "Unable to open file.");
            }
        });

        headerPanel.add(lblTitle);
        headerPanel.add(lblName);
        headerPanel.add(lblSession);
        headerPanel.add(lblSubmissionID);
        headerPanel.add(btnOpen);

        add(headerPanel, BorderLayout.NORTH);

        // =========================
        // Form panel
        // =========================

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 5, 5));

        // Spinners (0–10 enforced by model)
        JSpinner pc = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner m  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner r  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner p  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JTextArea comments = new JTextArea(5, 20);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);

        JScrollPane commentScroll = new JScrollPane(
            comments,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        JLabel totalLabel = new JLabel("Total: 0");
        JLabel wordCountLabel = new JLabel("Words: 0 / 200");

        JButton saveDraft = new JButton("Save Draft");
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Back");

        // =========================
        // Live total update
        // =========================

        pc.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        m.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        r.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        p.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));

        // =========================
        // Live word counter (NO dialog here)
        // =========================

        comments.getDocument().addDocumentListener(new DocumentListener() {

            private void updateWordCount() {
                int count = countWords(comments.getText());
                wordCountLabel.setText("Words: " + count + " / 200");

                if (count > 200) {
                    wordCountLabel.setForeground(Color.RED);
                } else {
                    wordCountLabel.setForeground(Color.BLACK);
                }
            }

            @Override public void insertUpdate(DocumentEvent e) { updateWordCount(); }
            @Override public void removeUpdate(DocumentEvent e) { updateWordCount(); }
            @Override public void changedUpdate(DocumentEvent e) { updateWordCount(); }
        });

        // =========================
        // Status-based UI control
        // =========================

        String status = assignedEvaluation.getStatus();

        if ("COMPLETED".equals(status)) {

            // Hide Save Draft
            saveDraft.setVisible(false);

            // Disable Submit
            submit.setEnabled(false);

            // Lock inputs
            pc.setEnabled(false);
            m.setEnabled(false);
            r.setEnabled(false);
            p.setEnabled(false);
            comments.setEditable(false);
        }

        // =========================
        // Buttons
        // =========================
        EvaluationDAO dao = new EvaluationDAO();

        // =========================
        // Load draft if IN_PROGRESS
        // =========================

        if ("IN_PROGRESS".equals(assignedEvaluation.getStatus()) || "COMPLETED".equals(assignedEvaluation.getStatus())) {

            Evaluation draft = dao.loadDraft(
                assignedEvaluation.getSubmissionID(),
                evaluator.getEvaluatorID()
            );

            if (draft != null) {
                pc.setValue(draft.getProblemClarity());
                m.setValue(draft.getMethodology());
                r.setValue(draft.getResults());
                p.setValue(draft.getPresentation());
                comments.setText(draft.getComments());
            }
        }

        saveDraft.addActionListener(e -> {

            Evaluation eval = new Evaluation(
                (int) pc.getValue(),
                (int) m.getValue(),
                (int) r.getValue(),
                (int) p.getValue(),
                comments.getText()
            );
        
            dao.saveDraft(
                eval,
                assignedEvaluation.getSubmissionID(),
                evaluator.getEvaluatorID() 
            );
        
            DialogUtil.showInfoDialog(
                this,
                "Saved",
                "Draft saved successfully (IN_PROGRESS)."
            );
        });

        back.addActionListener(e -> AppNavigator.openAssignedEvaluations(this, evaluator));

        submit.addActionListener(e -> {

            int wordCount = countWords(comments.getText());
        
            if (wordCount > 200) {
                DialogUtil.showErrorDialog(
                    this,
                    "Comment Too Long",
                    "Comments cannot exceed 200 words.\nCurrent count: " + wordCount
                );
                return;
            }
        
            Evaluation evaluation = new Evaluation(
                (int) pc.getValue(),
                (int) m.getValue(),
                (int) r.getValue(),
                (int) p.getValue(),
                comments.getText()
            );
        
            boolean success = dao.saveEvaluation(
                evaluation,
                assignedEvaluation.getSubmissionID(),
                evaluator.getEvaluatorID()   // ✅ consistent
            );
        
            if (success) {
                DialogUtil.showInfoDialog(
                    this,
                    "Success",
                    "Evaluation submitted successfully.\nStatus: COMPLETED\nTotal Score: "
                        + evaluation.getTotalScore()
                );
        
                AppNavigator.openAssignedEvaluations(this, evaluator);
        
            } else {
                DialogUtil.showErrorDialog(
                    this,
                    "Error",
                    "Failed to submit evaluation."
                );
            }
        });

        // =========================
        // Layout
        // =========================

        formPanel.add(new JLabel("Problem Clarity (0–10)")); formPanel.add(pc);
        formPanel.add(new JLabel("Methodology (0–10)"));     formPanel.add(m);
        formPanel.add(new JLabel("Results (0–10)"));         formPanel.add(r);
        formPanel.add(new JLabel("Presentation (0–10)"));    formPanel.add(p);
        formPanel.add(new JLabel("Comments"));               formPanel.add(commentScroll);
        formPanel.add(wordCountLabel);                        formPanel.add(totalLabel);
        formPanel.add(back);                                  formPanel.add(saveDraft);
        formPanel.add(new JLabel());                          formPanel.add(submit);

        add(formPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
