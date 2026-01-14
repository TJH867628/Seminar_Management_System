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
        totalLabel.setText("Total Mark: " + total);
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) return 0;
        return text.trim().split("\\s+").length;
    }

    // =========================
    // Constructor
    // =========================
    public EvaluationForm(AssignedEvaluation assignedEvaluation, Evaluator evaluator) {

        setTitle("Evaluation Form");
        setMinimumSize(new Dimension(520, 600)); // 🔥 IMPORTANT
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================
        // Header Panel
        // =========================
        JPanel headerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Evaluation Form", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblName = new JLabel("Student: " + assignedEvaluation.getStudentName());
        JLabel lblSession = new JLabel("Session ID: " + assignedEvaluation.getSessionID());
        JLabel lblSubmissionID = new JLabel("Submission ID: " + assignedEvaluation.getSubmissionID());

        JButton btnOpen = new JButton("Open Submission File");
        btnOpen.addActionListener(e -> {
            try {
                String rawPath = assignedEvaluation.getFilePath();
                if (rawPath == null || rawPath.trim().isEmpty()) {
                    DialogUtil.showErrorDialog(this, "Error", "File path is invalid.");
                    return;
                }

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
        // Form Panel (GridBagLayout)
        // =========================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        // Spinners
        JSpinner pc = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner m  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner r  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner p  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JTextArea comments = new JTextArea(8, 30); // 🔥 rows > 1
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);

        JScrollPane commentScroll = new JScrollPane(
                comments,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        JLabel totalLabel = new JLabel("Total Mark: 0");
        JLabel wordCountLabel = new JLabel("Words: 0 / 200");

        JButton saveDraft = new JButton("Save Draft");
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Back");

        int row = 0;

        // ---------- Problem Clarity ----------
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Problem Clarity (0–10)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(pc, gbc);
        row++;

        // ---------- Methodology ----------
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(new JLabel("Methodology (0–10)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(m, gbc);
        row++;

        // ---------- Results ----------
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(new JLabel("Results (0–10)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(r, gbc);
        row++;

        // ---------- Presentation ----------
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(new JLabel("Presentation (0–10)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(p, gbc);
        row++;

        // ---------- COMMENTS (THIS IS THE FIX) ----------
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Comments"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;              // 🔥 MUST HAVE
        gbc.weighty = 1.0;              // 🔥 MUST HAVE
        gbc.fill = GridBagConstraints.BOTH; // 🔥 MUST HAVE
        formPanel.add(commentScroll, gbc);
        row++;

        // ---------- Reset after comments ----------
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---------- Word count & total ----------
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(wordCountLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(totalLabel, gbc);
        row++;

        // ---------- Buttons ----------
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(back, gbc);
        gbc.gridx = 1;
        formPanel.add(saveDraft, gbc);
        row++;

        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(submit, gbc);

        add(formPanel, BorderLayout.CENTER);

        // =========================
        // Live updates
        // =========================
        pc.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        m.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        r.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));
        p.addChangeListener(e -> updateTotal(pc, m, r, p, totalLabel));

        comments.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                int count = countWords(comments.getText());
                wordCountLabel.setText("Words: " + count + " / 200");
                wordCountLabel.setForeground(count > 200 ? Color.RED : Color.BLACK);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        // =========================
        // DAO & status logic
        // =========================
        EvaluationDAO dao = new EvaluationDAO();

        if ("COMPLETED".equals(assignedEvaluation.getStatus())) {
            saveDraft.setVisible(false);
            submit.setEnabled(false);
            pc.setEnabled(false);
            m.setEnabled(false);
            r.setEnabled(false);
            p.setEnabled(false);
            comments.setEditable(false);
        }

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

        saveDraft.addActionListener(e -> {
            dao.saveDraft(
                    new Evaluation(
                            (int) pc.getValue(),
                            (int) m.getValue(),
                            (int) r.getValue(),
                            (int) p.getValue(),
                            comments.getText()
                    ),
                    assignedEvaluation.getSubmissionID(),
                    evaluator.getEvaluatorID()
            );
            DialogUtil.showInfoDialog(this, "Saved", "Draft saved successfully.");
        });

        submit.addActionListener(e -> {
            if (countWords(comments.getText()) > 200) {
                DialogUtil.showErrorDialog(this, "Error", "Comments exceed 200 words.");
                return;
            }

            if (dao.saveEvaluation(
                    new Evaluation(
                            (int) pc.getValue(),
                            (int) m.getValue(),
                            (int) r.getValue(),
                            (int) p.getValue(),
                            comments.getText()
                    ),
                    assignedEvaluation.getSubmissionID(),
                    evaluator.getEvaluatorID()
            )) {
                DialogUtil.showInfoDialog(this, "Success", "Evaluation submitted successfully.");
                AppNavigator.openAssignedEvaluations(this, evaluator);
            }
        });

        back.addActionListener(e -> AppNavigator.openAssignedEvaluations(this, evaluator));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
