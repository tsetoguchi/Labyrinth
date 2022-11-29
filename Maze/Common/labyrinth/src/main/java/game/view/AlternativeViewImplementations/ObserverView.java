package game.view.AlternativeViewImplementations;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ObserverView extends JFrame {

  private final Controls controls;
  private final GameCanvas canvas;

  private static final int cellSize = 80;

  public ObserverView(List<ObserverGameProjection> states) {
    this.controls = new Controls(states);
    this.canvas = new GameCanvas(states, cellSize);

    this.add(this.controls);

    this.add(this.createStatePanel());
    this.initialize();

  }

  private void initialize() {
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().setBackground(Color.WHITE);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLocationRelativeTo(null);

    this.pack();
    this.setVisible(true);
  }

  private JPanel createStatePanel() {
    JScrollPane container = new JScrollPane(this.canvas,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JPanel statePanel = new JPanel();
    statePanel.setLayout(new BoxLayout(statePanel, BoxLayout.X_AXIS));
    statePanel.add(container);

    return statePanel;
  }

  public void update() {
    this.controls.update();
    this.canvas.repaint();
  }
}
