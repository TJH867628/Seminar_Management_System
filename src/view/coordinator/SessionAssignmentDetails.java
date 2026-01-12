package view.coordinator;

import controller.CoordinatorController;
import model.Coordinator;
import model.Evaluator;
import model.Submission;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import app.AppNavigator;

import java.awt.*;

public class SessionAssignmentDetails extends JFrame {

    private int sessionId;
    private CoordinatorController controller;

    private JTable assignedEvalTable, availableEvalTable;
    private JTable assignedSubTable, availableSubTable;

    private DefaultTableModel assignedEvalModel, availableEvalModel;
    private DefaultTableModel assignedSubModel, availableSubModel;
    private Coordinator coordinator;

    public SessionAssignmentDetails(Coordinator coordinator, int sessionId) {
        this.sessionId = sessionId;
        this.coordinator = coordinator;
        this.controller = new CoordinatorController(coordinator);

        setTitle("Manage Session Assignment - Session " + sessionId);
        setSize(900, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadEvaluators();
        loadSubmissions();

        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createEvaluatorPanel());
        panel.add(createSubmissionPanel());

        return panel;
    }

    private JPanel createEvaluatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Evaluators"));

        assignedEvalModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Expertise"}, 0);
        assignedEvalTable = new JTable(assignedEvalModel);

        availableEvalModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Expertise"}, 0);
        availableEvalTable = new JTable(availableEvalModel);

        JButton btnAssign = new JButton("Assign >");
        JButton btnRemove = new JButton("< Remove");

        btnAssign.addActionListener(e -> assignEvaluator());
        btnRemove.addActionListener(e -> removeEvaluator());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnAssign);

        JPanel leftPanel = new JPanel(new BorderLayout(5,5));
        leftPanel.add(new JLabel("Available Evaluators", SwingConstants.CENTER), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(availableEvalTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5,5));
        rightPanel.add(new JLabel("Assigned Evaluators", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(assignedEvalTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                rightPanel
        );
        splitPane.setResizeWeight(0.5);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSubmissionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Submissions"));

        assignedSubModel = new DefaultTableModel(
                new String[]{"ID", "Title"}, 0);
        assignedSubTable = new JTable(assignedSubModel);

        availableSubModel = new DefaultTableModel(
                new String[]{"ID", "Title"}, 0);
        availableSubTable = new JTable(availableSubModel);

        JButton btnAssign = new JButton("Assign >");
        JButton btnRemove = new JButton("< Remove");

        btnAssign.addActionListener(e -> assignSubmission());
        btnRemove.addActionListener(e -> removeSubmission());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnAssign);

        JPanel leftPanel = new JPanel(new BorderLayout(5,5));
        leftPanel.add(new JLabel("Available Submissions", SwingConstants.CENTER), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(availableSubTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5,5));
        rightPanel.add(new JLabel("Assigned Submissions", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(assignedSubTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                rightPanel
        );
        splitPane.setResizeWeight(0.45);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> AppNavigator.openManageSeminarSessionsAssignments(this, coordinator));
        panel.add(backBtn);
        return panel;
    }

    private void loadEvaluators() {
        assignedEvalModel.setRowCount(0);
        availableEvalModel.setRowCount(0);

        for (Evaluator e : controller.getAssignedEvaluators(sessionId)) {
            assignedEvalModel.addRow(new Object[]{
                    e.getEvaluatorID(),
                    e.getName(),
                    e.getExpertise()
            });
        }

        for (Evaluator e : controller.getAvailableEvaluators(sessionId)) {
            availableEvalModel.addRow(new Object[]{
                    e.getEvaluatorID(),
                    e.getName(),
                    e.getExpertise()
            });
        }
    }

    private void loadSubmissions() {
        assignedSubModel.setRowCount(0);
        availableSubModel.setRowCount(0);

        for (Submission s : controller.getAssigedSubmissions(sessionId)) {
            assignedSubModel.addRow(new Object[]{
                    s.getSubmissionID(),
                    s.getResearchTitle(),
            });
        }

        for (Submission s : controller.getAvailableSubmissions(sessionId)) {
            availableSubModel.addRow(new Object[]{
                    s.getSubmissionID(),
                    s.getResearchTitle(),
            });
        }
    }

    private void assignEvaluator() {
        int row = availableEvalTable.getSelectedRow();
        if (row == -1) return;

        int evaluatorId = (int) availableEvalModel.getValueAt(row, 0);
        controller.assignEvaluatorToSession(sessionId, evaluatorId);
        loadEvaluators();
    }

    private void removeEvaluator() {
        int row = assignedEvalTable.getSelectedRow();
        if (row == -1) return;

        int evaluatorId = (int) assignedEvalModel.getValueAt(row, 0);
        controller.removeEvaluatorFromSession(sessionId, evaluatorId);
        loadEvaluators();
    }

    private void assignSubmission() {
        int row = availableSubTable.getSelectedRow();
        if (row == -1) return;

        int submissionId = (int) availableSubModel.getValueAt(row, 0);
        controller.assignSubmissionToSession(sessionId, submissionId);
        loadSubmissions();
    }

    private void removeSubmission() {
        int row = assignedSubTable.getSelectedRow();
        if (row == -1) return;

        int submissionId = (int) assignedSubModel.getValueAt(row, 0);
        controller.removeSubmissionFromSession(sessionId, submissionId);
        loadSubmissions();
    }
}