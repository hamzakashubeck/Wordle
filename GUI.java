import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Wordle game GUI.
 *
 * @author Hamza Kashubeck
 *
 */
public class GUI {

    /**
     * The JFrame for the GUI.
     */
    private JFrame frame;

    /**
     * The JPanel for the GUI.
     */
    private JPanel panel;

    /**
     * Initializes the GUI.
     */
    public GUI() {
        this.frame = new JFrame();

        this.frame.setSize(450, 550);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Wordle");

        this.panel = new JPanel();
        this.panel.setBackground(Color.BLACK);
        this.frame.add(this.panel);

        this.panel.setLayout(null);
        JLabel label = new JLabel("Guess: ");
        label.setForeground(Color.WHITE);
        label.setBounds(10, 10, 100, 50);
        this.panel.add(label);

        this.frame.setVisible(true);
    }

    /**
     * Adds the given element into the gui.
     *
     * @param input
     *            the element to be added
     */
    public void addToPanel(JComponent input) {
        this.panel.add(input);
    }

    /**
     * Refresh the GUI from outside the class.
     */
    public void refreshDisplay() {
        this.frame.repaint();
    }

    /**
     * Closes the old game window and boots up a new one.
     */
    public void restartGame() {
        this.frame.dispose();
        new WordleGame(new GUI());
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        new WordleGame(new GUI());
    }

}
