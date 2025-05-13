package GUI;
import javax.swing.*;
import java.awt.*;
import gradesection.*;
import member.*;
import tasks.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttendancePage extends JFrame {
    private JPanel topBar;
    private JPanel mainBar; // This will be wrapped in a JScrollPane
    private JPanel bottomBar;

    JRButton saveButton;

    private JLabel currentClassLabel; // For section if applicable
    private JLabel currentDateLabel;

    private final Color GOLD_COLOR = new Color(218, 165, 32);
    private final Color PURPLE_COLOR = new Color(72, 0, 72);
    private final Color PRESSED_PURPLE_COLOR = new Color(65, 0, 60);
    private final Color BUTTON_MARKED_COLOR = Color.DARK_GRAY; // Color for selected present/absent
    private final Color BUTTON_UNMARKED_COLOR = GOLD_COLOR; // Original color for present/absent


    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;

    // To store selections: Key is student/teacher ID, Value is Boolean (true for present, false for absent)
    private Map<String, Boolean> attendanceStatusMap;
    private Set<String> markedIndividuals; // Tracks IDs of individuals whose attendance has been set

    private boolean isStudentAttendance = false; // Differentiates between student and teacher attendance mode
    private GRADESEC currentGradeSec; // Store if it's student attendance

    private DateChecker dateChecker; // To store the attendance data

    private int totalToMark = 0;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)");


    // Constructor for Student Attendance
    public AttendancePage(GRADESEC gradeSec){
        this.isStudentAttendance = true;
        this.currentGradeSec = gradeSec;
        setLayout(new BorderLayout(5,5)); // Added gaps
        setSize(new Dimension(500,600));
        setTitle("Take Student Attendance - " + gradeSec.toString());
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose frame on close

        this.students = Attendance.studentsInAGrade(gradeSec);
        this.totalToMark = this.students.size();
        this.attendanceStatusMap = new HashMap<>();
        this.markedIndividuals = new HashSet<>();

        initComponent();
        setUpComponents();
        this.dateChecker = new DateChecker(LocalDate.now(), gradeSec);
        checkAllMarked(); // Initially disable save button if list is not empty
    }

    // Constructor for Teacher Attendance
    public AttendancePage(){
        this.isStudentAttendance = false;
        this.teachers = IncludeTeacher.returnTeachers(); // Get all teachers
        this.totalToMark = this.teachers.size();
        this.attendanceStatusMap = new HashMap<>();
        this.markedIndividuals = new HashSet<>();

        setLayout(new BorderLayout(5,5)); // Added gaps
        setSize(new Dimension(500,600));
        setTitle("Take Teacher Attendance");
        setResizable(false);
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponent();
        setUpComponents();
        this.dateChecker = new DateChecker(LocalDate.now()); // No gradeSec for teacher attendance
        checkAllMarked(); // Initially disable save button if list is not empty
    }

    public void initComponent(){
        currentClassLabel = new JLabel();
        currentDateLabel = new JLabel(LocalDate.now().format(DATE_FORMATTER));
        currentDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentDateLabel.setForeground(PURPLE_COLOR);


        saveButton = new JRButton("Save Attendance"); // More descriptive text

        topBar = new JPanel();
        bottomBar = new JPanel();
        mainBar = new JPanel(); // This panel will hold all student/teacher entries

        topBar.setPreferredSize(new Dimension(getWidth(),60)); // Adjusted height
        topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 20,10)); // Centered with gaps
        topBar.setBackground(GOLD_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        bottomBar.setPreferredSize(new Dimension(getWidth(), 70)); // Adjusted height
        bottomBar.setBackground(Color.WHITE); // Match page background
        bottomBar.setLayout(new FlowLayout(FlowLayout.CENTER,10,10)); // Centered save button

        saveButton.setBackground(PURPLE_COLOR);
        saveButton.setHoverBackgroundColor(PRESSED_PURPLE_COLOR);
        saveButton.setPressedBackgroundColor(GOLD_COLOR.darker());
        saveButton.setBorderThickness(0);
        saveButton.setBorderRadius(10);
        saveButton.setPreferredSize(new Dimension(180,45)); // Made button larger
        saveButton.setText("Save Attendance");
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setEnabled(false); // Initially disabled


        mainBar.setBackground(Color.WHITE);
        mainBar.setLayout(new BoxLayout(mainBar, BoxLayout.Y_AXIS)); // Vertical layout
        mainBar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // Padding around entries

        if(isStudentAttendance){
            currentClassLabel.setText("Class: " + currentGradeSec.toString());
            currentClassLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            currentClassLabel.setForeground(PURPLE_COLOR);
            if (students.isEmpty()) {
                mainBar.add(new JLabel("No students found in this section: " + currentGradeSec.toString()));
                totalToMark = 0; // No one to mark
            } else {
                for (Student student : students) {
                    JPanel studentPanel = createStudentPanel(student);
                    mainBar.add(studentPanel);
                    mainBar.add(Box.createRigidArea(new Dimension(0, 5))); // Small gap between entries
                }
            }
        } else { // Teacher attendance
            currentClassLabel.setText("Marking: All Teachers");
            currentClassLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            currentClassLabel.setForeground(PURPLE_COLOR);

            if (teachers.isEmpty()) {
                mainBar.add(new JLabel("No teachers found in the system."));
                totalToMark = 0;
            } else {
                for (Teacher teacher : teachers) {
                    JPanel teacherPanel = createTeacherPanel(teacher);
                    mainBar.add(teacherPanel);
                    mainBar.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
        }
    }

    public JPanel createStudentPanel(Student student){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Added horizontal gap
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,55)); // Allow horizontal expansion, fixed height
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY)); // Bottom border separator


        JLabel nameLabel = new JLabel(String.format("%-25s (ID: %s)", student.getName(), student.getID()));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setPreferredSize(new Dimension(250,30));


        JRButton presentButton = new JRButton("Present");
        JRButton absentButton = new JRButton("Absent");

        styleAttendanceButton(presentButton);
        styleAttendanceButton(absentButton);

        // Store buttons to reset the other if one is clicked
        Map<String, JRButton> buttonMap = new HashMap<>();
        buttonMap.put("present", presentButton);
        buttonMap.put("absent", absentButton);


        presentButton.addActionListener(e -> {
            handleAttendanceMarking(student.getID(), true, buttonMap);
            presentButton.setBackground(BUTTON_MARKED_COLOR);
            presentButton.setForeground(Color.WHITE);
            absentButton.setBackground(BUTTON_UNMARKED_COLOR);
            absentButton.setForeground(Color.BLACK);
        });

        absentButton.addActionListener(e -> {
            handleAttendanceMarking(student.getID(), false, buttonMap);
            absentButton.setBackground(BUTTON_MARKED_COLOR);
            absentButton.setForeground(Color.WHITE);
            presentButton.setBackground(BUTTON_UNMARKED_COLOR);
            presentButton.setForeground(Color.BLACK);
        });

        panel.add(nameLabel);
        panel.add(presentButton);
        panel.add(absentButton);

        return panel;
    }

    public JPanel createTeacherPanel(Teacher teacher){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,55));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));

        JLabel nameLabel = new JLabel(String.format("%-25s (ID: %s)", teacher.getName(), teacher.getID()));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setPreferredSize(new Dimension(250,30));


        JRButton presentButton = new JRButton("Present");
        JRButton absentButton = new JRButton("Absent");

        styleAttendanceButton(presentButton);
        styleAttendanceButton(absentButton);

        Map<String, JRButton> buttonMap = new HashMap<>();
        buttonMap.put("present", presentButton);
        buttonMap.put("absent", absentButton);

        presentButton.addActionListener(e -> {
            handleAttendanceMarking(teacher.getID(), true, buttonMap);
            presentButton.setBackground(BUTTON_MARKED_COLOR);
            presentButton.setForeground(Color.WHITE);
            absentButton.setBackground(BUTTON_UNMARKED_COLOR);
            absentButton.setForeground(Color.BLACK);
        });
        absentButton.addActionListener(e -> {
            handleAttendanceMarking(teacher.getID(), false, buttonMap);
            absentButton.setBackground(BUTTON_MARKED_COLOR);
            absentButton.setForeground(Color.WHITE);
            presentButton.setBackground(BUTTON_UNMARKED_COLOR);
            presentButton.setForeground(Color.BLACK);
        });

        panel.add(nameLabel);
        panel.add(presentButton);
        panel.add(absentButton);

        return panel;
    }

    private void styleAttendanceButton(JRButton button) {
        button.setBorderRadius(20);
        button.setBorderThickness(1);
        button.setBorderColor(Color.GRAY);
        button.setPreferredSize(new Dimension(90,32));
        button.setBackground(BUTTON_UNMARKED_COLOR); // Initial color
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }


    private void handleAttendanceMarking(String id, boolean isPresent, Map<String, JRButton> buttons) {
        attendanceStatusMap.put(id, isPresent);
        // Only add to markedIndividuals if it's the first time this person is marked
        if (!markedIndividuals.contains(id)) {
            markedIndividuals.add(id);
        }
        checkAllMarked();
    }

    private void checkAllMarked() {
        if (totalToMark == 0) { // If list is empty
            saveButton.setEnabled(false); // No one to save attendance for
            saveButton.setText("No one to mark");
            return;
        }
        if (markedIndividuals.size() == totalToMark) {
            saveButton.setEnabled(true);
            saveButton.setText("Save Attendance");
            saveButton.setBackground(PURPLE_COLOR); // Enable color
        } else {
            saveButton.setEnabled(false);
            saveButton.setText(String.format("Mark %d more", totalToMark - markedIndividuals.size()));
            saveButton.setBackground(Color.LIGHT_GRAY); // Disable color
        }
    }


    public void setUpComponents(){
        topBar.add(currentDateLabel);
        topBar.add(currentClassLabel); // Add class/teacher label to top bar
        bottomBar.add(saveButton);

        JScrollPane scrollPane = new JScrollPane(mainBar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling
        scrollPane.setBorder(null); // Remove scroll pane's own border


        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); // Add scrollPane instead of mainBar directly
        add(bottomBar, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAttendanceData();
                //JOptionPane.showMessageDialog(AttendancePage.this, "Attendance saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                //setVisible(false); // Close after saving
                //dispose();
            }
        });
    }

    private void saveAttendanceData() {
        if (isStudentAttendance) {
            if (AttendanceHistory.checkDuplication(LocalDate.now(), currentGradeSec)) {
                JOptionPane.showMessageDialog(this, "Attendance for this class on this date has already been recorded.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Student student : students) {
                Boolean status = attendanceStatusMap.get(student.getID());
                if (status != null && !status) { // If absent (false)
                    // For now, no appeal is automatically added here. Teacher can edit later or student can appeal.
                    dateChecker.addAbsentStudent(student);
                    student.increaseAlertNumber();
                }
            }
            dateChecker.addDateToHistory(); // This adds to AttendanceHistory
        } else { // Teacher attendance
            if (AttendanceHistory.checkDuplication(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Teacher attendance for this date has already been recorded.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Teacher teacher : teachers) {
                Boolean status = attendanceStatusMap.get(teacher.getID());
                if (status != null && !status) { // If absent
                    dateChecker.addAbsentTeacher(teacher);
                    teacher.increaseAlertNumber();
                }
            }
            dateChecker.addDateToTeacherHistory(); // Adds to teacher history
        }
        JOptionPane.showMessageDialog(this, "Attendance saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Close the window after successful save
    }


   /* public static void main(String[] args){
        // Sample data for testing
        SwingUtilities.invokeLater(() -> {
            // Test student attendance
            new Student("Nathnael", GRADESEC.ten_c, "0953256171");
            new Student("Ayizohibel", GRADESEC.ten_c, "0987654321");
            new Student("Lidya", GRADESEC.ten_c, "1234567890");
            AttendancePage studentAttendancePage = new AttendancePage(GRADESEC.ten_c);
            studentAttendancePage.setVisible(true);

            // Test teacher attendance
            new Teacher("Alemu", GRADESEC.none,"0900321022");
            new Teacher("Getch", GRADESEC.nine_a,"0900321022");
            // AttendancePage teacherAttendancePage = new AttendancePage();
            // teacherAttendancePage.setLocation(500,100); // Offset second window
            // teacherAttendancePage.setVisible(true);
        });
    }*/
}