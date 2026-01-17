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

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadTable();

        JButton addNewButton = new JButton("Add New Session");
        JButton deleteButton = new JButton("Delete Session");
        JButton editButton = new JButton("Edit Session");
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
            AppNavigator.openEditSeminarSession(this, coordinator,(int) model.getValueAt(table.getSelectedRow(), 0));
        });

        refreshBtn.addActionListener(e -> loadTable());
        backBtn.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(addNewButton);
        bottom.add(deleteButton);
        bottom.add(editButton);
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

}