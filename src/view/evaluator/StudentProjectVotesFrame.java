package view.evaluator;

import controller.EvaluatorController;
import model.AssignedEvaluation;
import model.Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.io.File;
import java.util.List;

public class StudentProjectVotesFrame extends JFrame {

    private Evaluator evaluator;
    private EvaluatorController controller;

    private JTable table;
    private DefaultTableModel model;
    private List<AssignedEvaluation> projectList;

    public StudentProjectVotesFrame(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.controller = new EvaluatorController(evaluator);

        setTitle("Student Projects Voting");
        setSize(1000, 400);
        setLocationRelativeTo(null);

        String[] columns = {"Submission ID", "Student", "Session", "Date", "Venue", "File", "Total Score", "Status", "Vote"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 8; // File or Vote
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);

        loadTable();

        // Set renderers and editors
        table.getColumn("File").setCellRenderer(new FileButtonRenderer());
        table.getColumn("File").setCellEditor(new FileButtonEditor(new JCheckBox()));

        table.getColumn("Status").setCellRenderer(new StatusCellRenderer());

        table.getColumn("Vote").setCellRenderer(new VoteButtonRenderer());
        table.getColumn("Vote").setCellEditor(new VoteButtonEditor(new JCheckBox()));

        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        refreshBtn.addActionListener(e -> loadTable());
        backBtn.addActionListener(e -> back());

        // Info label
        JLabel infoLabel = new JLabel("Please refresh the page to see the latest voting status.", SwingConstants.CENTER);
        infoLabel.setForeground(Color.RED);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(refreshBtn);
        buttonsPanel.add(backBtn);
        bottom.add(infoLabel, BorderLayout.NORTH);
        bottom.add(buttonsPanel, BorderLayout.SOUTH);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        projectList = controller.getStudentProjectVotes();

        for (AssignedEvaluation p : projectList) {
            model.addRow(p.toRowForVoting());
        }
        model.fireTableDataChanged();
    }

    private void back() {
        controller.backToDashboard();
        dispose();
    }

    // ----------------- Renderers & Editors -----------------
    class FileButtonRenderer extends JButton implements TableCellRenderer {
        public FileButtonRenderer() { setText("Open File"); }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
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
                        String path = current.getFilePath().trim();
                        File file = new File(path);
                        if (!file.exists()) {
                            JOptionPane.showMessageDialog(null, "File not found:\n" + path, "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Desktop.getDesktop().open(file);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Unable to open file.", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            current = projectList.get(row);
            return button;
        }
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null && !isSelected) {
                String status = value.toString();
                if ("COMPLETED".equalsIgnoreCase(status)) c.setForeground(new Color(0,128,0));
                else if ("DRAFT".equalsIgnoreCase(status)) c.setForeground(Color.ORANGE);
                else if ("VOTED".equalsIgnoreCase(status)) c.setForeground(Color.RED);
                else c.setForeground(Color.BLUE);
            }
            return c;
        }
    }

    // ----------------- Voting Column -----------------
    class VoteButtonRenderer extends JButton implements TableCellRenderer {
        public VoteButtonRenderer() { setText("Vote"); }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            AssignedEvaluation p = projectList.get(row);
            if ("VOTED".equalsIgnoreCase(p.getStatus())) {
                setText("Voted");
                setEnabled(false);
            } else {
                setText("Vote");
                setEnabled(true);
            }
            return this;
        }
    }

    class VoteButtonEditor extends DefaultCellEditor {
        private JButton button;
        private AssignedEvaluation current;

        public VoteButtonEditor(JCheckBox box) {
            super(box);
            button = new JButton();

            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) return;

                current = projectList.get(row);

                // Check if evaluator has already voted for this submission
                if (controller.hasVoted(current.getSubmissionID())) {
                    current.setStatus("VOTED");
                    button.setText("Voted");
                    button.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "You have already voted for this project.", "Warning", JOptionPane.WARNING_MESSAGE);
                    fireEditingStopped();
                    return;
                }

                // Check if evaluator has already voted for any other submission
                boolean alreadyVoted = false;
                for (AssignedEvaluation p : projectList) {
                    if ("VOTED".equalsIgnoreCase(p.getStatus())) {
                        alreadyVoted = true;
                        break;
                    }
                }
                if (alreadyVoted) {
                    JOptionPane.showMessageDialog(null, "You can only vote once. You cannot vote on other projects.", "Warning", JOptionPane.WARNING_MESSAGE);
                    fireEditingStopped();
                    return;
                }

                boolean success = controller.castVote(current.getSubmissionID());
                if (success) {
                    current.setStatus("VOTED");
                    button.setText("Voted");
                    button.setEnabled(false);
                    model.fireTableRowsUpdated(row, row);
                    JOptionPane.showMessageDialog(null, "Vote counted successfully!");
                }

                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            current = projectList.get(row);

            if ("VOTED".equalsIgnoreCase(current.getStatus()) || controller.hasVoted(current.getSubmissionID())) {
                button.setText("Voted");
                button.setEnabled(false);
            } else {
                button.setText("Vote");
                button.setEnabled(true);
            }

            return button;
        }
    }
}
