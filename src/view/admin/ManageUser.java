package view.admin;

import app.AppNavigator;
import controller.AdminController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Admin;
import model.User;
import util.DialogUtil;

public class ManageUser extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private AdminController controller;
    private boolean editMode = false;
    private int editingRow = -1;

    private JButton editUserBtn;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JButton addUserBtn;
    private JButton refreshBtn;
    private JButton backBtn;

    private JComboBox<String> roleCombo;

    public ManageUser(Admin admin) {

        controller = new AdminController();

        setTitle("Admin - Manage Users");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"User ID", "Name", "Email", "Role"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (!editMode) return false;
                if (row != editingRow) return false;
                return column != 0; // allow edit except ID
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] roles = {"Student", "Evaluator", "Coordinator"};
        roleCombo = new JComboBox<>(roles);
        table.getColumnModel().getColumn(3)
             .setCellEditor(new DefaultCellEditor(roleCombo));

        roleCombo.addActionListener(e -> {
            if (!editMode) {
                roleCombo.hidePopup();
            }
        });

        loadUsers();
        add(new JScrollPane(table), BorderLayout.CENTER);

        addUserBtn = new JButton("Add User");
        editUserBtn = new JButton("Edit");
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        refreshBtn = new JButton("Refresh");
        backBtn = new JButton("Back");

        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        addUserBtn.addActionListener(e -> {
            AppNavigator.openAddNewUser(this, admin);
        });

        editUserBtn.addActionListener(e -> startEdit());
        saveBtn.addActionListener(e -> saveEdit());
        cancelBtn.addActionListener(e -> cancelEdit());

        refreshBtn.addActionListener(e -> loadUsers());
        backBtn.addActionListener(e -> {
            AppNavigator.openDashboard(admin);
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.add(addUserBtn);
        bottom.add(editUserBtn);
        bottom.add(saveBtn);
        bottom.add(cancelBtn);
        bottom.add(refreshBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void startEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            return;
        }

        editMode = true;
        editingRow = selectedRow;

        editUserBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        cancelBtn.setEnabled(true);

        table.repaint();
        table.editCellAt(editingRow, 1);
    }

    private void saveEdit() {
        if (!editMode || editingRow == -1) return;

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        int userID = (int) model.getValueAt(editingRow, 0);
        String name = model.getValueAt(editingRow, 1).toString();
        String email = model.getValueAt(editingRow, 2).toString();
        String role = model.getValueAt(editingRow, 3).toString();

        if (!controller.updateUser(userID, name, email, role)) {
            DialogUtil.showErrorDialog(this, "Update Failed", "Failed to update user details.");
            return;
        }

        exitEditMode();
        loadUsers();
    }

    private void cancelEdit() {
        exitEditMode();
        loadUsers();
    }

    private void exitEditMode() {
        editMode = false;
        editingRow = -1;

        editUserBtn.setEnabled(true);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        table.repaint();
    }

    private void loadUsers() {
        model.setRowCount(0);
        List<User> users = controller.getAllUsers();

        for (User u : users) {
            model.addRow(new Object[]{
                    u.getUserID(),
                    u.getName(),
                    u.getEmail(),
                    u.getRole()
            });
        }
    }
}