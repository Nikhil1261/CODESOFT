import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Student implements Serializable {
    private String name;
    private String rollNo;
    private String grade;
    private int age;

    public Student(String name, String rollNo, String grade, int age) {
        this.name = name;
        this.rollNo = rollNo;
        this.grade = grade;
        this.age = age;
    }

    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public String getGrade() { return grade; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setAge(int age) { this.age = age; }
}

class StudentDatabase {
    private List<Student> students;
    private final String FILE_NAME = "students.dat";

    public StudentDatabase() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(String rollNo) {
        students.removeIf(s -> s.getRollNo().equals(rollNo));
        saveStudents();
    }

    public Student searchStudent(String rollNo) {
        for (Student s : students) {
            if (s.getRollNo().equals(rollNo)) return s;
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            students = new ArrayList<>();
        }
    }
}

public class StudentManagementSystem extends JFrame {
    private StudentDatabase db;
    private JTable table;
    private DefaultTableModel model;

    public StudentManagementSystem() {
        db = new StudentDatabase();

        setTitle("Student Management System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Name", "Roll No", "Grade", "Age"}, 0);
        table = new JTable(model);
        loadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(searchBtn);
        panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addStudent());
        editBtn.addActionListener(e -> editStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        searchBtn.addActionListener(e -> searchStudent());
        refreshBtn.addActionListener(e -> loadTable());

        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Student s : db.getAllStudents()) {
            model.addRow(new Object[]{s.getName(), s.getRollNo(), s.getGrade(), s.getAge()});
        }
    }

    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField rollField = new JTextField();
        JTextField gradeField = new JTextField();
        JTextField ageField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Roll No:", rollField,
                "Grade:", gradeField,
                "Age:", ageField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String roll = rollField.getText().trim();
                String grade = gradeField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());

                if (name.isEmpty() || roll.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!");
                    return;
                }

                db.addStudent(new Student(name, roll, grade, age));
                loadTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number!");
            }
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to edit!");
            return;
        }

        String rollNo = (String) model.getValueAt(row, 1);
        Student s = db.searchStudent(rollNo);

        if (s != null) {
            JTextField nameField = new JTextField(s.getName());
            JTextField gradeField = new JTextField(s.getGrade());
            JTextField ageField = new JTextField(String.valueOf(s.getAge()));

            Object[] message = {
                    "Name:", nameField,
                    "Grade:", gradeField,
                    "Age:", ageField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    s.setName(nameField.getText().trim());
                    s.setGrade(gradeField.getText().trim());
                    s.setAge(Integer.parseInt(ageField.getText().trim()));
                    loadTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be a number!");
                }
            }
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete!");
            return;
        }

        String rollNo = (String) model.getValueAt(row, 1);
        db.removeStudent(rollNo);
        loadTable();
    }

    private void searchStudent() {
        String rollNo = JOptionPane.showInputDialog(this, "Enter Roll No to search:");
        if (rollNo != null && !rollNo.trim().isEmpty()) {
            Student s = db.searchStudent(rollNo);
            if (s != null) {
                JOptionPane.showMessageDialog(this,
                        "Name: " + s.getName() +
                                "\nRoll No: " + s.getRollNo() +
                                "\nGrade: " + s.getGrade() +
                                "\nAge: " + s.getAge(),
                        "Student Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}
