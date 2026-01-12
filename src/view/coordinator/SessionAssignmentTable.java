package view.coordinator;

import controller.CoordinatorController;
import model.SessionAssignment;
import model.Coordinator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import app.AppNavigator;

import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

public class SessionAssignmentTable extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private CoordinatorController controller;
    private List<SessionAssignment> assignments;
    private Coordinator coordinator;
    private JPanel buttonPanel;
    private JButton backBtn;
    private JScrollPane tablePanel;

    public SessionAssignmentTable(Coordinator coordinator) {

        this.coordinator = coordinator;
        controller = new CoordinatorController(coordinator);

        setTitle("Session Assignment Management");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {
                "Session ID",
                "Venue",
                "Session Type",
                "Time Slot",
                "Submissions",
                "Evaluators",
                "Action"
        };

        //Make only the manage button can be clicked
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);

        loadData();

        table.getColumn("Action")
                .setCellRenderer(new ButtonRenderer("Manage"));
        table.getColumn("Action")
                .setCellEditor(new ButtonEditor(new JCheckBox()));

        tablePanel = new JScrollPane(table);

        buttonPanel = new JPanel();
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            dispose();
            AppNavigator.openDashboard(coordinator);
        });
        add(tablePanel, BorderLayout.CENTER);

        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        assignments = controller.getAllSessionAssignments();

        for (SessionAssignment sa : assignments) {
            model.addRow(new Object[]{
                    sa.getSessionId(),
                    sa.getVenue(),
                    sa.getSessionType(),
                    sa.getTimeSlot(),
                    sa.getSubmissionCount(),
                    sa.getEvaluatorCount(),
                    "Manage"
            });
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer(String text) {
            setText(text);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Manage");

            button.addActionListener(e -> openManagePage());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            currentRow = row;
            return button;
        }

        private void openManagePage() {
            int sessionId = (int) model.getValueAt(currentRow, 0);

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(table);
            AppNavigator.openManageSeminarSessionsAssignmentsDetails(parent,coordinator, sessionId);
            dispose();
        }
    }
}