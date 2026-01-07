package view.evaluator;

import controller.EvaluatorController;
import model.AssignedEvaluation;
import model.Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.io.File;
import java.util.List;

public class AssignedEvaluationsFrame extends JFrame {

    private Evaluator evaluator;
    private EvaluatorController controller;

    private JTable table;
    private DefaultTableModel model;
    private List<AssignedEvaluation> assignedList;

    public AssignedEvaluationsFrame(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.controller = new EvaluatorController(evaluator);

        setTitle("Assigned Evaluations");
        setSize(1000, 400);
        setLocationRelativeTo(null);

        String[] columns = {"Submission ID", "Student", "Session", "Date", "Venue", "File", "Total Score", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        loadTable();

        table.getColumn("File").setCellRenderer(new FileButtonRenderer());
        table.getColumn("File").setCellEditor(new FileButtonEditor(new JCheckBox()));

        JButton evaluateBtn = new JButton("Evaluate");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        evaluateBtn.addActionListener(e -> openEvaluation());
        refreshBtn.addActionListener(e -> loadTable());
        backBtn.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(evaluateBtn);
        bottom.add(refreshBtn);
        bottom.add(backBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        assignedList = controller.getAssignedEvaluations();

        for (AssignedEvaluation ae : assignedList) {
            model.addRow(ae.toRow());
        }
    }

    private void openEvaluation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        controller.openEvaluationForm(this, assignedList.get(row));
        dispose();
    }

    private void back() {
        controller.backToDashboard();
        dispose();
    }

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

    class FileButtonEditor extends DefaultCellEditor {

        private JButton button;
        private AssignedEvaluation current;

        public FileButtonEditor(JCheckBox box) {
            super(box);
            button = new JButton("Open File");

            button.addActionListener(e -> {
                try {
                    if (current != null && current.getFilePath() != null) {
                
                        // 1️⃣ Get raw path
                        String rawPath = current.getFilePath().trim();
                
                        // 2️⃣ Remove surrounding quotes ( " or ' )
                        if ((rawPath.startsWith("\"") && rawPath.endsWith("\"")) ||
                            (rawPath.startsWith("'") && rawPath.endsWith("'"))) {
                            rawPath = rawPath.substring(1, rawPath.length() - 1);
                        }
                
                        File file = new File(rawPath);
                
                        // 3️⃣ Check file exists
                        if (!file.exists()) {
                            JOptionPane.showMessageDialog(
                                null,
                                "File not found:\n" + rawPath,
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                
                        // 4️⃣ Open file
                        Desktop.getDesktop().open(file);
                    }
                
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Unable to open file.",
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
            current = assignedList.get(row);
            return button;
        }
    }
}