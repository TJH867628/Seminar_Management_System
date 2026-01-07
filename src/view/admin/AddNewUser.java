package view.admin;

import model.Admin;
import util.DialogUtil;
import util.EmailUtil;
import controller.AdminController;

import javax.swing.*;

import app.AppNavigator;

import java.awt.*;

public class AddNewUser extends JFrame {

    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword;

    private JComboBox<String> cmbRole;

    // Student fields
    private JTextField txtProgram;
    private JSpinner spnYear;

    // Coordinator field
    private JTextField txtDepartment;

    // Evaluator field
    private JTextField txtExpertise;

    private JPanel dynamicPanel;

    private String[] programs = {
        "Software Engineering",
        "Computer Science",
        "Information Technology",
        "Data Science"
    };

    Admin admin;

    AdminController adminController = new AdminController();

    public AddNewUser(Admin admin) {

        this.admin = admin;

        setTitle("Admin - Add New User");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        cmbRole = new JComboBox<>(new String[]{
            "Student", "Evaluator", "Coordinator"
        });

        form.add(new JLabel("Name"));
        form.add(txtName);

        form.add(new JLabel("Email"));
        form.add(txtEmail);

        form.add(new JLabel("Password"));
        form.add(txtPassword);

        form.add(new JLabel("Role"));
        form.add(cmbRole);

        add(form, BorderLayout.NORTH);

        //Use CardLayout() to switch between different role details
        dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.setBorder(
            BorderFactory.createTitledBorder("Role Details")
        );

        dynamicPanel.add(createStudentPanel(), "Student");
        dynamicPanel.add(createEvaluatorPanel(), "Evaluator");
        dynamicPanel.add(createCoordinatorPanel(), "Coordinator");

        add(dynamicPanel, BorderLayout.CENTER);

        JButton btnCreate = new JButton("Create User");
        JButton btnCancel = new JButton("Cancel");

        JPanel bottom = new JPanel();
        bottom.add(btnCreate);
        bottom.add(btnCancel);

        add(bottom, BorderLayout.SOUTH);

        cmbRole.addActionListener(e -> switchRolePanel());

        btnCreate.addActionListener(e -> handleCreate());

        btnCancel.addActionListener(e -> dispose());

        switchRolePanel();
        setVisible(true);
    }

    private JPanel createStudentPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        txtProgram = new JTextField();

        spnYear = new JSpinner(
            new SpinnerNumberModel(2025, 2000, 2100, 1)
        );

        p.add(new JLabel("Program"));
        p.add(txtProgram);
        p.add(new JLabel("Year"));
        p.add(spnYear);
        return p;
    }

    private JPanel createEvaluatorPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 8));
        txtExpertise = new JTextField();

        p.add(new JLabel("Expertise"));
        p.add(txtExpertise);
        return p;
    }

    private JPanel createCoordinatorPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 8));
        txtDepartment = new JTextField();

        p.add(new JLabel("Department"));
        p.add(txtDepartment);
        return p;
    }

    private void switchRolePanel() {
        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, cmbRole.getSelectedItem().toString());
    }

    private void handleCreate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = cmbRole.getSelectedItem().toString();

        if(!EmailUtil.isValidEmail(email)){
            DialogUtil.showErrorDialog(this,
                    "Validation Error",
                    "Please enter a valid email address.");
            return;
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            DialogUtil.showErrorDialog(this,
                    "Validation Error",
                    "Name, Email and Password are required.");
            return;
        }

        switch (role) {
            case "Student":
                if (txtProgram.getText().trim().isEmpty()) {
                    DialogUtil.showErrorDialog(this,
                            "Validation Error",
                            "Program is required for students.");
                    return;
                }
                String program = txtProgram.getText().trim();
                int year = (int) spnYear.getValue();

                adminController.createNewStudent(name,email,password,program,year);

                break;

            case "Evaluator":
                if (txtExpertise.getText().trim().isEmpty()) {
                    DialogUtil.showErrorDialog(this,
                            "Validation Error",
                            "Expertise is required for evaluators.");
                    return;
                }

                String expertise = txtExpertise.getText().trim();
                adminController.createNewEvaluator(name,email,password,expertise);
                break;

            case "Coordinator":
                if (txtDepartment.getText().trim().isEmpty()) {
                    DialogUtil.showErrorDialog(this,
                            "Validation Error",
                            "Department is required for coordinators.");
                    return;
                }
                String department = txtDepartment.getText().trim();
                adminController.createNewCoordinator(name,email,password,department);
                break;
        }

        DialogUtil.showInfoDialog(this, "Success", "User created successfully");

        AppNavigator.openManageUser(this,admin);
    }
}