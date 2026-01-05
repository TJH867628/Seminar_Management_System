package view.evaluator;

import model.AssignedEvaluation;
import model.Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dao.EvaluatorDAO;

import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AssignedEvaluationsFrame extends JFrame {

    private Evaluator evaluator;
    private JTable table;
    private DefaultTableModel model;
    private List<AssignedEvaluation> assignedList;

    public AssignedEvaluationsFrame(Evaluator evaluator) {
        this.evaluator = evaluator;

        setTitle("Assigned Evaluations");
        setSize(800, 400);
        setLocationRelativeTo(null);

        assignedList = loadAssignedEvaluations();

        String[] columns = {"Student", "Session", "Date", "Venue", "File", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (AssignedEvaluation ae : assignedList) {
            model.addRow(ae.toRow());
        }

        // 🔹 Button column setup
        table.getColumn("File").setCellRenderer(new FileButtonRenderer());
        table.getColumn("File").setCellEditor(
            new FileButtonEditor(new JCheckBox(), assignedList)
        );

        JButton evaluateBtn = new JButton("Evaluate");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        evaluateBtn.addActionListener(e -> openEvaluation());
        refreshBtn.addActionListener(e -> refreshTable());
        closeBtn.addActionListener(e -> back());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(evaluateBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(closeBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void openEvaluation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an evaluation.");
            return;
        }

        new EvaluationForm(); // your existing EvaluationForm
        dispose();
    }

    private void refreshTable() {
        model.setRowCount(0);
        assignedList = loadAssignedEvaluations();
        for (AssignedEvaluation ae : assignedList) {
            model.addRow(ae.toRow());
        }
    }

    private void back() {
        new EvaluatorDashboard(evaluator);
        dispose(); // close current window
    }

    // Dummy data (replace with DB later)
    private List<AssignedEvaluation> loadAssignedEvaluations() {
        EvaluatorDAO dao = new EvaluatorDAO();
        return dao.getAssignedEvaluations(evaluator.getUserID());
    }

    // ===============================
    // BUTTON RENDERER
    // ===============================
    class FileButtonRenderer extends JButton implements TableCellRenderer {

        public FileButtonRenderer() {
            setText("Open File");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ===============================
    // BUTTON EDITOR
    // ===============================
    class FileButtonEditor extends DefaultCellEditor {

        private JButton button;
        private AssignedEvaluation currentEval;

        public FileButtonEditor(JCheckBox checkBox, List<AssignedEvaluation> list) {
            super(checkBox);
            button = new JButton("Open File");

            button.addActionListener(e -> {
                try {
                    if (currentEval != null) {
                        Desktop.getDesktop().open(new File(currentEval.getFilePath()));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Cannot open file.\nPlease check file path.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {

            currentEval = assignedList.get(row);
            return button;
        }
    }
}
