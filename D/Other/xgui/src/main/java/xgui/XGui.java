package xgui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

/**
 * Displays the inputted characters as graphical cells.
 */
public class XGui {
    private static Map<String, String> verticalDirections;
    private static Map<String, String> horizontalDirections;
    private static int CELL_SIDE_LEN = 80; // in pixels

    static {
        verticalDirections = new HashMap<>();
        horizontalDirections = new HashMap<>();
        verticalDirections.put("┘", "UP");
        verticalDirections.put("┐", "DOWN");
        verticalDirections.put("└", "UP");
        verticalDirections.put("┌", "DOWN");
        horizontalDirections.put("┘", "LEFT");
        horizontalDirections.put("┐", "LEFT");
        horizontalDirections.put("└", "RIGHT");
        horizontalDirections.put("┌", "RIGHT");
    }

    public static void main(String[] args) {
        List<String> rows = getRows();
        if (rows.size() == 0) {
            System.out.println("Pointless test case detected.");
            System.exit(1);
        }
        JFrame frame = new JFrame();
        GridLayout layout = new GridLayout(rows.size(), rows.get(0).length());
        frame.setLayout(layout);

        for (String row : rows) {
            for (JComponent cell : createRow(row)) {
                frame.add(cell);
            }
        }

        Mouse m = new Mouse();
        frame.addMouseListener(m);
        frame.pack();
        frame.setVisible(true);
    }

    private static String readInput() {
        StringBuilder input = new StringBuilder();
        Scanner inputScanner = new Scanner(System.in);
        while (inputScanner.hasNext()) {
            input.append(inputScanner.next());
        }
        return input.toString();
    }

    private static List<String> getRows() {
        String input = readInput();

        List<String> rows = new ArrayList<>();
        String remainingInput = "" + input;
        while (remainingInput.length() > 0) {
            if (!remainingInput.matches(".*\".*\".*")) {
                return rows; // because we can assume valid input, everything after last
            }
            int firstQuoteIndex = remainingInput.indexOf("\"");
            int secondQuoteIndex = remainingInput.indexOf("\"", firstQuoteIndex+1);
            rows.add(remainingInput.substring(firstQuoteIndex+1, secondQuoteIndex));
            remainingInput = remainingInput.substring(secondQuoteIndex+1);
        }
        return rows;
    }

    private static List<JComponent> createRow(String row) {
        List<JComponent> cells = new ArrayList<>();
        for (int i = 0; i < row.length(); i++) {
            String character = row.substring(i, i+1);
            JComponent cell = createCell(character);
            cells.add(cell);
        }
        return cells;
    }

    private static JComponent createCell(String character) {
        JPanel panel = new JPanel();
        panel.setSize(CELL_SIDE_LEN, CELL_SIDE_LEN);
        panel.add(new Cell(verticalDirections.get(character), horizontalDirections.get(character)));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        return panel;
    }

    private static class Cell extends JComponent {

        private final String vertical;
        private final String horizontal;

        public Cell(String vertical, String horizontal) {
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CELL_SIDE_LEN, CELL_SIDE_LEN);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(CELL_SIDE_LEN, CELL_SIDE_LEN);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(CELL_SIDE_LEN, CELL_SIDE_LEN);
        }

        @Override
        protected void paintComponent(Graphics g) {
            int lineWidth = 10;
            int offset = 5;
            if (this.vertical.equals("UP")) {
                g.setColor(Color.MAGENTA);
                g.fillRect(getX() - offset + CELL_SIDE_LEN / 2 - lineWidth / 2, getY() - offset, lineWidth, CELL_SIDE_LEN / 2 + lineWidth / 2);
            }
            if (this.vertical.equals("DOWN")) {
                g.setColor(Color.MAGENTA);
                g.fillRect(getX() - offset + CELL_SIDE_LEN / 2 - lineWidth / 2, getY() - offset + CELL_SIDE_LEN / 2 - lineWidth / 2, lineWidth, CELL_SIDE_LEN / 2);
            }
            if (this.horizontal.equals("LEFT")) {
                g.setColor(Color.MAGENTA);
                g.fillRect(getX() - offset, getY() - offset + CELL_SIDE_LEN / 2 - lineWidth / 2, CELL_SIDE_LEN / 2, lineWidth);
            }
            if (this.horizontal.equals("RIGHT")) {
                g.setColor(Color.MAGENTA);
                g.fillRect(getX() - offset + CELL_SIDE_LEN / 2, getY() - offset + CELL_SIDE_LEN / 2 - lineWidth / 2, CELL_SIDE_LEN / 2, lineWidth);
            }
        }
    }

    private static class Mouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getX() + ", " + e.getY());
            System.exit(0);
        }
    }
}
