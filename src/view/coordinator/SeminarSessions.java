package view.coordinator;

import controller.CoordinatorController;
import controller.SeminarSessionsController;
import model.Coordinator;
import model.Session;
import util.DialogUtil;

import javax.swing.*;
import javax.swing.table.*;

import app.AppNavigator;

import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SeminarSessions extends JFrame {

    private Coordinator coordinator;
    private SeminarSessionsController controller;

    private JTable table;
    private DefaultTableModel model;
    private List<Session> sessionList;

    private boolean editMode = false;
    private int editingRow = -1;

    public SeminarSessions(Coordinator coordinator) {
        this.coordinator = coordinator;
        controller = new SeminarSessionsController();

        setTitle("Seminar Sessions");
        setSize(1000, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {
                "Session ID",
                "Date",
                "Venue",
                "Session Type",
                "Time Slot",
                "Last Updated"
        };

        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (!editMode || row != editingRow) return false;

                return column != 0;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor());
        table.getColumnModel().getColumn(4).setCellEditor(new TimeCellEditor());

        loadTable();

        JButton addNewButton = new JButton("Add New Session");
        JButton deleteButton = new JButton("Delete Session");
        JButton editButton = new JButton("Edit Session");
        JButton saveEditButton = new JButton("Save Edit");
        JButton cancelEditButton = new JButton("Cancel Edit");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        addNewButton.addActionListener(e -> {
            AppNavigator.openAddSeminarSession(this, coordinator);
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int sessionID = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete session ID " + sessionID + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = controller.deleteSeminarSession(sessionID);
                        if (success) {
                            DialogUtil.showInfoDialog(this, "Success", "Session deleted successfully.");
                            loadTable();
                        } else {
                            DialogUtil.showErrorDialog(this, "Deletion Failed", "Failed to delete the session. It may be assigned to evaluators or submissions.");
                        }
                    } catch (Exception ex) {
                        DialogUtil.showErrorDialog(this, "Error", "An error occurred: " + ex.getMessage());
                    }   
                }
            } else {
                DialogUtil.showErrorDialog(this, "No Selection","Please select a session to delete.");
            } 
        });
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                editMode = true;
                editingRow = selectedRow;
                editButton.setEnabled(false);
                saveEditButton.setEnabled(true);
                cancelEditButton.setEnabled(true);

                table.repaint();
                table.editCellAt(selectedRow, 1);
            } else {
                DialogUtil.showErrorDialog(this, "No Selection","Please select a session to edit.");
            }
        });
        saveEditButton.setEnabled(false);
        cancelEditButton.setEnabled(false);

        saveEditButton.addActionListener(e -> {
            if (editMode && editingRow >= 0) {
                editButton.setEnabled(true);
                saveEditButton.setEnabled(false);
                cancelEditButton.setEnabled(false);
                
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }

                int sessionID = (int) model.getValueAt(editingRow,0);
                String date = model.getValueAt(editingRow, 1).toString();
                String venue = model.getValueAt(editingRow, 2).toString();
                String sessionType = model.getValueAt(editingRow, 3).toString();
                String timeSlot = model.getValueAt(editingRow, 4).toString();
                Session updatedSession = new Session(sessionID, venue, sessionType, java.sql.Date.valueOf(date), java.sql.Time.valueOf(timeSlot));

                editMode = false;
                editingRow = -1;
                table.clearSelection();
                try {
                    boolean success = controller.updateSeminarSession(updatedSession);
                    if (success) {
                        DialogUtil.showInfoDialog(this, "Success", "Session updated successfully.");
                    } else {
                        DialogUtil.showErrorDialog(this, "Update Failed", "Failed to update the session.");
                    }
                } catch (Exception ex) {
                    DialogUtil.showErrorDialog(this, "Error", "An error occurred: " + ex.getMessage());
                }            
            }
        });
        cancelEditButton.addActionListener(e -> {
            if (editMode) {
                editMode = false;
                editingRow = -1;
                saveEditButton.setEnabled(false);
                cancelEditButton.setEnabled(false);
                editButton.setEnabled(false);

                loadTable();
            }
        });

        refreshBtn.addActionListener(e -> loadTable());
        backBtn.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(addNewButton);
        bottom.add(deleteButton);
        bottom.add(editButton);
        bottom.add(saveEditButton);
        bottom.add(cancelEditButton);
        bottom.add(refreshBtn);
        bottom.add(backBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        sessionList = controller.getAllSeminarSessions();

        for (Session s : sessionList) {
            model.addRow(s.toRow());
        }
    }

    private void back() {
        AppNavigator.openDashboard(coordinator);
        dispose();
    }

    class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JSpinner spinner;
        private final SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd");

        public DateCellEditor() {
            spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        }

        @Override
        public Object getCellEditorValue() {
            return formatter.format((Date) spinner.getValue());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {

            try {
                spinner.setValue(formatter.parse(value.toString()));
            } catch (Exception ignored) {}
            return spinner;
        }
    }

    class TimeCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JSpinner spinner;
        private final SimpleDateFormat formatter =
                new SimpleDateFormat("HH:mm:ss");

        public TimeCellEditor() {
            spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss"));
        }

        @Override
        public Object getCellEditorValue() {
            return formatter.format((Date) spinner.getValue());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {

            try {
                spinner.setValue(formatter.parse(value.toString()));
            } catch (Exception ignored) {}
            return spinner;
        }
    }
}