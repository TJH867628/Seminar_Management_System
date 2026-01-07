package view.coordinator;

import model.Session;
import util.DialogUtil;
import model.Coordinator;
import app.AppNavigator;
import dao.SeminarSessionDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class AddNewSeminarSession extends JFrame {
    private static SeminarSessionDAO sao = new SeminarSessionDAO();

    public AddNewSeminarSession(Coordinator coordinator) {

        setTitle("Add New Seminar Session");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Add New Seminar Session", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JTextField txtVenue = new JTextField();
        JTextField txtType = new JTextField();     
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));

        form.add(new JLabel("Venue"));
        form.add(txtVenue);

        form.add(new JLabel("Session Type"));
        form.add(txtType);

        form.add(new JLabel("Date (YYYY-MM-DD)"));
        form.add(dateSpinner);

        form.add(new JLabel("Time (HH:MM:SS)"));
        form.add(timeSpinner);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Create");
        JButton backButton = new JButton("Back");

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            try {
                Date date = new Date(((java.util.Date) dateSpinner.getValue()).getTime());
                Time time = new Time(((java.util.Date) timeSpinner.getValue()).getTime());

                Session newSession = new Session(
                        0,
                        txtVenue.getText(),
                        txtType.getText(),
                        date,
                        time
                );

                boolean success = sao.AddNewSeminarSession(newSession);
                if (success) {
                    DialogUtil.showInfoDialog(this,"Session Add Successful" ,"New seminar session added successfully.");
                    dispose();
                    AppNavigator.openSeminarSessions(this, coordinator);
                } else {
                    DialogUtil.showErrorDialog(this, "Session Add Failed", "Failed to add new seminar session. Please try again.");
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid input. Please check date/time format.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        backButton.addActionListener(e -> AppNavigator.openSeminarSessions(this, coordinator));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}