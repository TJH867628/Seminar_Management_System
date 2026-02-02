package view.coordinator;

import model.Session;
import model.EvaluationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


 // AnalyticsForm contains the reusable panels:StackedBarChartPanel，PieChartPanel，ReportSummaryPanel，Top3StudentsPanel
public class AnalyticsForm {

    // Tab 1: Horizontal Stacked Bar Chart
    public static class StackedBarChartPanel extends JPanel {
        private List<Session> sessions;

        public StackedBarChartPanel(List<Session> sessions) {
            this.sessions = new ArrayList<>(sessions);
            this.sessions.sort(Comparator.comparing(Session::getDate));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int paddingLeft = 230, paddingTop = 120, spacing = 90, barHeight = 32;
            int maxBarWidth = getWidth() - paddingLeft - 120;
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            int y = paddingTop;

            for (Session s : sessions) {
                String title = s.getSessionType() + " " + s.getDate()
                        + " (Session " + s.getSessionID() + ")";
                g2.setColor(Color.BLACK);
                g2.drawString(title, paddingLeft, y);
                y += 18;

                List<Integer> lowSubs = new ArrayList<>();
                List<Integer> midSubs = new ArrayList<>();
                List<Integer> highSubs = new ArrayList<>();

                for (EvaluationResult er : s.getEvaluationResults()) {
                    int score = er.getTotalScore();
                    int submissionId = er.getSubmissionID();
                    if (score <= 16) lowSubs.add(submissionId);
                    else if (score <= 32) midSubs.add(submissionId);
                    else highSubs.add(submissionId);
                }

                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.drawString("0-16 : " + (lowSubs.isEmpty() ? "" : lowSubs), paddingLeft, y);
                y += 15;
                g2.drawString("17-32: " + (midSubs.isEmpty() ? "" : midSubs), paddingLeft, y);
                y += 15;
                g2.drawString("33-40: " + (highSubs.isEmpty() ? "" : highSubs), paddingLeft, y);
                y += 20;

                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                int total = s.getEvaluationResults().size();
                if (total == 0) total = 1;
                int widthTotal = (int) ((double) s.getEvaluationResults().size() / getMaxEvaluations() * maxBarWidth);

                int wLow = (int) ((double) lowSubs.size() / total * widthTotal);
                int wMid = (int) ((double) midSubs.size() / total * widthTotal);
                int wHigh = (int) ((double) highSubs.size() / total * widthTotal);

                int x = paddingLeft;

                if (wLow > 0) {
                    g2.setColor(new Color(192, 80, 77));
                    g2.fillRect(x, y, wLow, barHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, wLow, barHeight);
                    g2.setColor(Color.WHITE);
                    g2.drawString(String.valueOf(lowSubs.size()), x + 6, y + 22);
                    x += wLow;
                }
                if (wMid > 0) {
                    g2.setColor(new Color(255, 192, 0));
                    g2.fillRect(x, y, wMid, barHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, wMid, barHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(midSubs.size()), x + 6, y + 22);
                    x += wMid;
                }
                if (wHigh > 0) {
                    g2.setColor(new Color(79, 129, 189));
                    g2.fillRect(x, y, wHigh, barHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, wHigh, barHeight);
                    g2.setColor(Color.WHITE);
                    g2.drawString(String.valueOf(highSubs.size()), x + 6, y + 22);
                }
                y += barHeight + spacing;
            }

            // Legend
            int lx = getWidth() - 200;
            int ly = 60;
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(Color.BLACK);
            g2.drawString("Legend (Score / 40)", lx, ly);
            ly += 18;
            drawLegend(g2, lx, ly, new Color(192, 80, 77), "0-16");
            ly += 18;
            drawLegend(g2, lx, ly, new Color(255, 192, 0), "17-32");
            ly += 18;
            drawLegend(g2, lx, ly, new Color(79, 129, 189), "33-40");
        }

        private void drawLegend(Graphics2D g2, int x, int y, Color c, String text) {
            g2.setColor(c);
            g2.fillRect(x, y - 12, 18, 14);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y - 12, 18, 14);
            g2.drawString(text, x + 24, y);
        }

        private int getMaxEvaluations() {
            int max = 1;
            for (Session s : sessions) {
                max = Math.max(max, s.getEvaluationResults().size());
            }
            return max;
        }
    }

    // Tab 2: Pie Charts 
    public static class PieChartPanel extends JPanel {
        private List<Session> sessions;

        public PieChartPanel(List<Session> sessions) {
            this.sessions = sessions;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setDoubleBuffered(true);

            for (Session s : sessions) {
                add(createSessionPiePanel(s));
                add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        private JPanel createSessionPiePanel(Session s) {
            JPanel panel = new JPanel() {
                private BufferedImage chartImage;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chartImage == null) createChartImage();
                    g.drawImage(chartImage, 0, 0, null);
                }

                private void createChartImage() {
                    chartImage = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = chartImage.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int padding = 50;
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, chartImage.getWidth(), chartImage.getHeight());

                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Arial", Font.BOLD, 16));
                    String title = s.getSessionType() + " " + s.getDate()
                            + " (Session " + s.getSessionID() + ")";
                    g2.drawString(title, padding, 25);

                    Map<String, Integer> counts = new LinkedHashMap<>();
                    counts.put("0-16", 0);
                    counts.put("17-32", 0);
                    counts.put("33-40", 0);
                    for (EvaluationResult er : s.getEvaluationResults()) {
                        int score = er.getTotalScore();
                        if (score <= 16) counts.put("0-16", counts.get("0-16") + 1);
                        else if (score <= 32) counts.put("17-32", counts.get("17-32") + 1);
                        else counts.put("33-40", counts.get("33-40") + 1);
                    }

                    int total = s.getEvaluationResults().size();
                    if (total == 0) total = 1;

                    int pieX = padding, pieY = 60, pieWidth = 300, pieHeight = 300;
                    int startAngle = 0;
                    Color[] colors = {new Color(192, 80, 77), new Color(255, 192, 0), new Color(79, 129, 189)};
                    int i = 0;
                    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                        int angle = (int) Math.round(entry.getValue() * 360.0 / total);
                        g2.setColor(colors[i]);
                        g2.fillArc(pieX, pieY, pieWidth, pieHeight, startAngle, angle);
                        startAngle += angle;
                        i++;
                    }

                    int lx = pieX + pieWidth + 30, ly = pieY + 10;
                    g2.setFont(new Font("Arial", Font.PLAIN, 12));
                    i = 0;
                    for (String key : counts.keySet()) {
                        g2.setColor(colors[i]);
                        g2.fillRect(lx, ly - 12, 18, 14);
                        g2.setColor(Color.BLACK);
                        g2.drawRect(lx, ly - 12, 18, 14);
                        g2.drawString(key + " (" + counts.get(key) + ")", lx + 24, ly);
                        ly += 20;
                        i++;
                    }

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(600, 400);
                }
            };
            panel.setLayout(new BorderLayout());
            return panel;
        }
    }

    // Report Summary Panel 
    public static class ReportSummaryPanel extends JPanel {
        public ReportSummaryPanel(List<Session> sessions) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            Map<String, List<Session>> grouped = sessions.stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getSessionType() + " " + s.getDate() + " (Session " + s.getSessionID() + ")",
                            LinkedHashMap::new, Collectors.toList()));

            for (String key : grouped.keySet()) {
                add(createTablePanel(key, grouped.get(key)));
                add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        private JPanel createTablePanel(String title, List<Session> sessions) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(lblTitle, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Submission ID", "Evaluator ID", "Total Score", "Comments"}, 0
            );

            for (Session s : sessions) {
                for (EvaluationResult er : s.getEvaluationResults()) {
                    model.addRow(new Object[]{
                            er.getSubmissionID(),
                            er.getEvaluatorID(),
                            er.getTotalScore(),
                            er.getComments()
                    });
                }
            }

            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);
            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(800, Math.min(200 + sessions.size() * 50, 400)));
            return panel;
        }
    }

    // Top 3 Students Panel 
    public static class Top3StudentsPanel extends JPanel {
        public Top3StudentsPanel(List<Session> sessions) {
            setLayout(new BorderLayout()); 

            // Collect all results and take top 3
            List<EvaluationResult> allResults = new ArrayList<>();
            for (Session s : sessions) allResults.addAll(s.getEvaluationResults());
            allResults.sort(Comparator.comparing(EvaluationResult::getTotalScore).reversed());
            List<EvaluationResult> top3 = allResults.stream().limit(3).collect(Collectors.toList());

            // Table for Top 3 students
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Submission ID", "Evaluator ID", "Total Score", "Comments"}, 0
            );
            for (EvaluationResult er : top3) {
                model.addRow(new Object[]{
                        er.getSubmissionID(),
                        er.getEvaluatorID(),
                        er.getTotalScore(),
                        er.getComments()
                });
            }

            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);

            // Put table in scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);

            // Red disclaimer
            JLabel lblDisclaimer = new JLabel(
                    "Note: The top 3 students are sorted by total score only. " +
                    "The final award results, including People's Choice, are determined after evaluator nominations."
            );
            lblDisclaimer.setFont(new Font("Arial", Font.ITALIC, 12));
            lblDisclaimer.setForeground(Color.RED);
            lblDisclaimer.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

            add(lblDisclaimer, BorderLayout.SOUTH);
        }
    }
}