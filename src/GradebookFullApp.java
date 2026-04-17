import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

// 🎓 Student Class
class Student implements Serializable {
    int id;
    String name;
    ArrayList<Integer> marks;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.marks = new ArrayList<>();
    }

    double getAverage() {
        if (marks.isEmpty()) return 0;
        int sum = 0;
        for (int m : marks) sum += m;
        return (double) sum / marks.size();
    }

    String getGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A";
        else if (avg >= 75) return "B";
        else if (avg >= 50) return "C";
        else return "Fail";
    }
}

// 🚀 Main App
public class GradebookFullApp {

    static ArrayList<Student> students = new ArrayList<>();
    static DefaultTableModel model;

    public static void main(String[] args) {

        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {}

        loadFromFile();

        JFrame frame = new JFrame("🎓 Gradebook Pro");
        frame.setSize(950, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 🔷 Sidebar
        JPanel sidebar = new JPanel(new GridLayout(7, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("➕ Add");
        JButton marksBtn = new JButton("📝 Marks");
        JButton viewBtn = new JButton("📊 View");
        JButton searchBtn = new JButton("🔍 Search");
        JButton editBtn = new JButton("✏ Edit");
        JButton deleteBtn = new JButton("🗑 Delete");
        JButton saveBtn = new JButton("💾 Save");

        sidebar.add(addBtn);
        sidebar.add(marksBtn);
        sidebar.add(viewBtn);
        sidebar.add(searchBtn);
        sidebar.add(editBtn);
        sidebar.add(deleteBtn);
        sidebar.add(saveBtn);

        // 📊 Table
        String[] cols = {"ID", "Name", "Marks", "Average", "Grade"};
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // ➕ Add Student
        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID"));
                String name = JOptionPane.showInputDialog("Enter Name");

                students.add(new Student(id, name));
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input");
            }
        });

        // 📝 Add Marks
        marksBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID"));
                Student s = findStudent(id);

                if (s == null) {
                    JOptionPane.showMessageDialog(frame, "Not found");
                    return;
                }

                int n = Integer.parseInt(JOptionPane.showInputDialog("Subjects"));
                for (int i = 0; i < n; i++) {
                    int m = Integer.parseInt(JOptionPane.showInputDialog("Mark"));
                    s.marks.add(m);
                }

                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input");
            }
        });

        // 📊 View
        viewBtn.addActionListener(e -> refreshTable());

        // 🔍 Search
        searchBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Name to Search");

            model.setRowCount(0);
            for (Student s : students) {
                if (s.name.equalsIgnoreCase(name)) {
                    model.addRow(new Object[]{
                            s.id, s.name, s.marks, s.getAverage(), s.getGrade()
                    });
                }
            }
        });

        // ✏ Edit
        editBtn.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID"));
            Student s = findStudent(id);

            if (s != null) {
                String newName = JOptionPane.showInputDialog("New Name");
                s.name = newName;
                refreshTable();
            }
        });

        // 🗑 Delete
        deleteBtn.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID"));
            Student s = findStudent(id);

            if (s != null) {
                students.remove(s);
                refreshTable();
            }
        });

        // 💾 Save
        saveBtn.addActionListener(e -> {
            saveToFile();
            JOptionPane.showMessageDialog(frame, "Saved!");
        });

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(scroll, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // 🔄 Refresh Table
    static void refreshTable() {
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.id, s.name, s.marks, s.getAverage(), s.getGrade()
            });
        }
    }

    // 🔍 Find
    static Student findStudent(int id) {
        for (Student s : students)
            if (s.id == id) return s;
        return null;
    }

    // 💾 Save
    static void saveToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.dat"));
            oos.writeObject(students);
            oos.close();
        } catch (Exception e) {}
    }

    // 📂 Load
    static void loadFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.dat"));
            students = (ArrayList<Student>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            students = new ArrayList<>();
        }
    }
}