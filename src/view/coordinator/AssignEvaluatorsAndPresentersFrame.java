package view.coordinator;

import dao.CoordinatorDAO;
import model.Coordinator;
import model.Evaluator;
import model.Student;
import model.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import app.AppNavigator;

public class AssignEvaluatorsAndPresentersFrame extends JFrame {

    private Coordinator coordinator;

    private JComboBox<Session> sessionBox;
    private JComboBox<Evaluator> evaluatorBox;
    private JComboBox<Student> studentBox;

    private CoordinatorDAO dao = new CoordinatorDAO();

    public AssignEvaluatorsAndPresentersFrame(Coordinator coordinator) {
        this.coordinator = coordinator;

        setTitle("Assign Evaluator & Presenter");
        pack();
        setMinimumSize(new Dimension(480, 360));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================
        // Header
        // =========================
        JLabel titleLabel = new JLabel("Assign Evaluator & Presenter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        add(titleLabel, BorderLayout.NORTH);

        // =========================
        // Form Panel (Center)
        // =========================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        sessionBox = new JComboBox<>();
        evaluatorBox = new JComboBox<>();
        studentBox = new JComboBox<>();

        loadData();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // IMPORTANT: don't steal vertical space

        // Session
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Session:"), gbc);

        gbc.gridx = 1;
        formPanel.add(sessionBox, gbc);

        // Evaluator
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Evaluator:"), gbc);

        gbc.gridx = 1;
        formPanel.add(evaluatorBox, gbc);

        // Presenter
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Presenter (Student):"), gbc);

        gbc.gridx = 1;
        formPanel.add(studentBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // =========================
        // Buttons (Bottom)
        // =========================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton assignEvaluatorBtn = new JButton("Assign Evaluator");
        JButton backBtn = new JButton("Back");

        assignEvaluatorBtn.setPreferredSize(new Dimension(150, 30));
        backBtn.setPreferredSize(new Dimension(90, 30));

        assignEvaluatorBtn.addActionListener(e -> assignEvaluator());
        backBtn.addActionListener(e -> back());

        buttonPanel.add(assignEvaluatorBtn);
        buttonPanel.add(backBtn);
        buttonPanel.setPreferredSize(new Dimension(450, 50));

        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        List<Session> sessions = dao.getAllSessions();
        List<Evaluator> evaluators = dao.getAllEvaluators();
        List<Student> students = dao.getAllStudents();

        sessions.forEach(sessionBox::addItem);
        evaluators.forEach(evaluatorBox::addItem);
        students.forEach(studentBox::addItem);
    }

    private void assignEvaluator() {
        Session session = (Session) sessionBox.getSelectedItem();
        Evaluator evaluator = (Evaluator) evaluatorBox.getSelectedItem();

        if (session == null || evaluator == null) {
            JOptionPane.showMessageDialog(this, "Please select both session and evaluator.");
            return;
        }

        dao.assignEvaluator(session.getSessionID(), evaluator.getEvaluatorID());
        JOptionPane.showMessageDialog(this, "Evaluator assigned successfully.");
    }

    private void back() {
        AppNavigator.openDashboard(coordinator);
        dispose();
    }
}