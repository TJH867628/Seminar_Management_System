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

public class EditSeminarSession extends JFrame {
    SeminarSessionDAO dao = new SeminarSessionDAO();
    private JComboBox sessionTypeDropdown, venueDropdown;
    private String[] sessionTypes = { "Type 1" , "Type 2", "Type 3" };
    private String[] venues = { "Room A", "Room B", "Room C", "Room D" };

    public EditSeminarSession(Coordinator coordinator,int sessionID) {

        setTitle("Edit Seminar Session");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Edit Seminar Session", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        Session session = dao.getSeminarSessionById(sessionID);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        sessionTypeDropdown = new JComboBox<>(sessionTypes);
        venueDropdown = new JComboBox<>(venues);
        venueDropdown.setSelectedItem(session.getVenue());
        sessionTypeDropdown.setSelectedItem(session.getSessionType());
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setValue(new java.util.Date(session.getDate().getTime()));

        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
        dateSpinner.setValue(new java.util.Date(session.getDate().getTime()));

        form.add(new JLabel("Venue"));
        form.add(venueDropdown);

        form.add(new JLabel("Session Type"));
        form.add(sessionTypeDropdown);

        form.add(new JLabel("Date (YYYY-MM-DD)"));
        form.add(dateSpinner);

        form.add(new JLabel("Time (HH:MM:SS)"));
        form.add(timeSpinner);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Edit");
        JButton backButton = new JButton("Back");

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            try {
                Date date = new Date(((java.util.Date) dateSpinner.getValue()).getTime());
                Time time = new Time(((java.util.Date) timeSpinner.getValue()).getTime());
                System.out.println(venueDropdown.getSelectedItem().toString());
                System.out.println(sessionTypeDropdown.getSelectedItem().toString());

                Session editedSession = new Session(
                        session.getSessionID(),
                        venueDropdown.getSelectedItem().toString(),
                        sessionTypeDropdown.getSelectedItem().toString(),
                        date,
                        time
                );

                boolean success = dao.updateSeminarSession(editedSession);
                if (success) {
                    DialogUtil.showInfoDialog(this,"Session Edit Successful" ,"Seminar session edited successfully.");
                    dispose();
                    AppNavigator.openSeminarSessions(this, coordinator);
                } else {
                    DialogUtil.showErrorDialog(this, "Session Edit Failed", "Failed to edited seminar session. Please try again.");
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