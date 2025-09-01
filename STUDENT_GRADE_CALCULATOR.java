import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Student {
    private int[] marks;
    private int total;
    private double average;
    private char grade;

    
    public Student(int[] marks) {
        this.marks = marks;
        calculateResults();
    }

    private void calculateResults() {
        total = 0;
        for (int m : marks) {
            total += m;
        }
        average = (double) total / marks.length;

        if (average >= 90) grade = 'A';
        else if (average >= 80) grade = 'B';
        else if (average >= 70) grade = 'C';
        else if (average >= 60) grade = 'D';
        else grade = 'F';
    }

    public int getTotal() { return total; }
    public double getAverage() { return average; }
    public char getGrade() { return grade; }
}

public class StudentGradeCalculator extends JFrame implements ActionListener {
    JTextField[] markFields;
    JButton calculateButton;
    JLabel resultLabel;

    public StudentGradeCalculator(int numSubjects) {
        setTitle("Student Grade Calculator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(numSubjects + 2, 2, 10, 10));

        markFields = new JTextField[numSubjects];
        for (int i = 0; i < numSubjects; i++) {
            add(new JLabel("Enter marks for Subject " + (i + 1) + ":"));
            markFields[i] = new JTextField();
            add(markFields[i]);
        }

        calculateButton = new JButton("Calculate Grade");
        calculateButton.addActionListener(this);
        add(calculateButton);

        resultLabel = new JLabel("Results will be displayed here.");
        add(resultLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int[] marks = new int[markFields.length];
            for (int i = 0; i < markFields.length; i++) {
                marks[i] = Integer.parseInt(markFields[i].getText());
                if (marks[i] < 0 || marks[i] > 100) {
                    throw new NumberFormatException();
                }
            }

            

            Student student = new Student(marks);

            resultLabel.setText("<html>Total: " + student.getTotal() +
                    "<br>Average: " + String.format("%.2f", student.getAverage()) + "%" +
                    "<br>Grade: " + student.getGrade() + "</html>");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers between 0 and 100!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter number of subjects:");
        int numSubjects = Integer.parseInt(input);

        new StudentGradeCalculator(numSubjects);
    }
}
