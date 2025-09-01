import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGame extends JFrame implements ActionListener {
    private JTextField guessField;
    private JButton guessButton, newGameButton;
    private JLabel feedbackLabel, scoreLabel, attemptsLabel;
    private int randomNumber, attempts, score, maxAttempts = 10;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Guess a number between 1 and 100:"));

        guessField = new JTextField(10);
        add(guessField);

        guessButton = new JButton("Guess");
        add(guessButton);
        guessButton.addActionListener(this);

        newGameButton = new JButton("New Game");
        add(newGameButton);
        newGameButton.addActionListener(this);

        feedbackLabel = new JLabel(" ");
        add(feedbackLabel);

        attemptsLabel = new JLabel("Attempts: 0");
        add(attemptsLabel);

        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel);

        startNewGame();
    }

    private void startNewGame() {
        Random rand = new Random();
        randomNumber = rand.nextInt(100) + 1;
        attempts = 0;
        attemptsLabel.setText("Attempts: 0");
        feedbackLabel.setText("Start guessing!");
        guessField.setText("");
        guessField.setEditable(true);
        guessButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guessButton) {
            try {
                int guess = Integer.parseInt(guessField.getText());
                attempts++;
                attemptsLabel.setText("Attempts: " + attempts);

                if (guess == randomNumber) {
                    int points = (maxAttempts - attempts + 1) * 10;
                    feedbackLabel.setText("Correct! You guessed it!");
                    score += points;
                    scoreLabel.setText("Score: " + score);
                    endRound("You won! Your score: " + points);
                } else if (guess < randomNumber) {
                    feedbackLabel.setText("Too low! Try again.");
                } else {
                    feedbackLabel.setText("Too high! Try again.");
                }

                if (attempts >= maxAttempts && guess != randomNumber) {
                    feedbackLabel.setText("Game over! The number was: " + randomNumber);
                    endRound("Out of attempts! The number was: " + randomNumber);
                }

            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Please enter a valid number!");
            }
        } else if (e.getSource() == newGameButton) {
            startNewGame();
        }
    }

    private void endRound(String message) {
        JOptionPane.showMessageDialog(this, message + "\nDo you want to play again?");
        guessButton.setEnabled(false);
        guessField.setEditable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NumberGuessingGame().setVisible(true);
        });
    }
}
