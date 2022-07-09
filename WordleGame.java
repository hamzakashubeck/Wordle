import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Processes the core Wordle game simulation.
 *
 * @author Hamza Kashubeck
 *
 */
public class WordleGame {

    /**
     * The solution word (to be generated during the game simulation).
     */
    private String solution;

    /**
     * The GUI interface (to display the guesses).
     */
    private GUI gui;

    /**
     * The y coordinate at which to place the next guess on the GUI.
     */
    private int guessYCoord;

    /**
     * The number of words in the word bank.
     */
    private int entries = 928;

    /**
     * Construct a Wordle game with the default word length of 5.
     *
     */
    public WordleGame(GUI g) {
        this.gui = g;
        this.guessYCoord = 100;
        this.play();
    }

    /**
     * Handles the core Wordle game.
     */
    private void play() {
        try {
            this.solution = this.getNewWord(this.entries);
        } catch (Exception e) {

        }

        final JTextField submission = new JTextField();
        submission.setBounds(105, 10, 200, 50);
        this.gui.addToPanel(submission);

        JButton button = new JButton("Submit");
        button.setBounds(315, 10, 100, 50);
        this.gui.addToPanel(button);

        this.gui.refreshDisplay();

        button.addActionListener(new ActionListener() {
            private int guesses = 5;

            @Override
            public void actionPerformed(ActionEvent e) {
                String guess = submission.getText();

                if (guess.length() != 5
                        || !WordleGame.this.isValidWord(guess)) {
                    WordleGame.this.invalidGuess();
                    submission.setText("");
                    return;
                }

                if (WordleGame.this.processGuess(guess,
                        WordleGame.this.guessYCoord)) {
                    WordleGame.this.endGameSuccess();
                    return;
                }

                WordleGame.this.guessYCoord += 50;
                submission.setText("");
                this.guesses--;

                if (this.guesses == 0) {
                    WordleGame.this.endGameFailure();
                    return;
                }
            }
        });

    }

    /**
     * Provides a solution word of the desired length.
     *
     * @param entries
     *            the number of words in the solutions bank
     * @return the solution word
     */
    public String getNewWord(int entries) throws IOException {
        int entry = (int) (Math.random() * (entries - 1));

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("solutionsBank.txt"));
        } catch (IOException e) {
            System.err.println("Error opening file to read.");
            return null;
        }

        String word = "";
        try {
            String s = in.readLine();
            while (s != null && entry != 0) {
                s = in.readLine();
                entry--;
            }
            word = in.readLine();
        } catch (IOException e) {
            System.err.println("Error processing file.");
        }

        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Error closing input file");
        }
        
        return word;
    }

    /**
     * Determines the validity of the user's guess.
     *
     * @param word
     *            the guess supplied by the player
     * @return boolean value that states if the guess is valid
     */
    public boolean isValidWord(String word) {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("guessableWords.txt"));
        } catch (IOException e) {
            System.err.println("Error opening file to read.");
            return false;
        }

        boolean valid = false;
        try {
            String s = in.readLine();
            while (s != null) {
                s = in.readLine();
                if (word.equals(s)) {
                    valid = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing file.");
        }

        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Error closing input file");
        }

        return valid;
    }

    /**
     * Handles output of one guess.
     *
     * @param guess
     *            the input guess from the user
     *
     * @param y
     *            the y coordinate at which to display the color-coded guess on
     *            the GUI
     *
     * @return returns true when the guess is correct
     */
    private boolean processGuess(String guess, int y) {
        int xCoord = 100;
        for (int i = 0; i < guess.length(); i++) {
            JLabel label = new JLabel(guess.substring(i, i + 1).toUpperCase());
            label.setBounds(xCoord, y, 50, 50);
            label.setFont(new Font("Monospaced", Font.BOLD, 30));
            xCoord += 50;
            if (guess.charAt(i) == this.solution.charAt(i)) {
                label.setForeground(Color.GREEN);
            } else if (this.solution.contains(guess.substring(i, i + 1))) {
                label.setForeground(Color.YELLOW);
            } else {
                label.setForeground(Color.RED);
            }
            this.gui.addToPanel(label);
        }
        this.gui.refreshDisplay();
        return guess.equals(this.solution);
    }

    /**
     * Prints invalid guess pop-up message, and clears the input box.
     */
    public void invalidGuess() {
        JOptionPane.showMessageDialog(null, "Invalid guess!");
    }

    /**
     * Prints success message and terminates the simulation.
     */
    public void endGameSuccess() {
        JOptionPane.showMessageDialog(null, "You win! Play again?");

        this.gui.restartGame();
    }

    /**
     * Prints failure message and terminates the simulation.
     */
    public void endGameFailure() {
        JOptionPane.showMessageDialog(null,
                "You lose! The word was '" + this.solution + "'. Try again?");

        this.gui.restartGame();
    }

}
