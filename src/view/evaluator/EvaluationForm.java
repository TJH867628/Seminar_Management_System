package view.evaluator;
import model.Evaluation;

import javax.swing.*;
import java.awt.*;

public class EvaluationForm extends JFrame {

    public EvaluationForm() {
        setTitle("Evaluation Form");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        JSpinner pc = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner m  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner r  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner p  = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JTextArea comments = new JTextArea(3, 20);
        JLabel totalLabel = new JLabel("Total: 0");

        JButton submit = new JButton("Submit");

        add(new JLabel("Problem Clarity")); add(pc);
        add(new JLabel("Methodology")); add(m);
        add(new JLabel("Results")); add(r);
        add(new JLabel("Presentation")); add(p);
        add(new JLabel("Comments")); add(new JScrollPane(comments));
        add(totalLabel); add(submit);

        submit.addActionListener(e -> {
            Evaluation eval = new Evaluation(
                (int) pc.getValue(),
                (int) m.getValue(),
                (int) r.getValue(),
                (int) p.getValue(),
                comments.getText()
            );
            totalLabel.setText("Total: " + eval.getTotalScore());
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
