package GUI;
import GUI.JRButton;
import member.GRADESEC;
import member.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import member.*;
import gradesection.*;
import tasks.Attendance;
import tasks.AttendanceHistory;
import tasks.DateChecker;


public class TeacherDashboard extends JFrame {
    private Teacher teacher;

    private JRPanel mainPage;
    private JRPanel sideBar;
    private JRPanel profileBar;
    private JRPanel profileImageBar; // Added for profile picture
    private JRPanel profileLabelBar; // Added for labels
    private JRPanel navBar;
    private JRPanel bottomPanel; // Spacer
    private JRPanel mainTopPanel;
    private JPanel mainBottomPanel; // Will use CardLayout
    private CardLayout mainBottomCardLayout;


    private JLabel teacherNameLabel; // Renamed
    private JLabel teacherSectionLabel; // Renamed
    private JLabel teacherIDLabel; // Renamed
    private JLabel teacherAbsentDaysLabel; // Renamed
    private JLabel teacherRedFlagLabel; // Renamed
    private JLabel profilePicHolder; // Added

    private JRButton takeStudentAttendanceButton;
    private JRButton reviewStudentAppealsButton;
    private JRButton viewStudentAbsencesButton;
    private JRButton logOutButton; // Added

    // Components for Review Appeals Panel
    private JPanel reviewAppealsPanel;
    private JTable appealsTable;
    private DefaultTableModel appealsTableModel;
    private JRButton approveAppealButton;
    private JRButton denyAppealButton;

    // Components for View Student Absences Panel
    private JPanel viewClassAbsencesPanel;
    private JComboBox<LocalDate> classAbsenceDateComboBox;
    private JTextArea classAbsencesTextArea;
    private JRButton searchClassAbsencesButton;


    private ImageIcon profilePic;

    private final Color GOLD_COLOR = new Color(218, 165, 32);
    private final Color PURPLE_COLOR = new Color(72, 0, 72);
    private final Color PRESSED_PURPLE_COLOR = new Color(65, 0, 60);
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)");

    // Card names
    private static final String DEFAULT_TEACHER_PANEL = "DefaultTeacherPanel";
    private static final String REVIEW_APPEALS_PANEL = "ReviewAppealsPanel";
    private static final String VIEW_CLASS_ABSENCES_PANEL = "ViewClassAbsencesPanel";


    public TeacherDashboard(Teacher teacher){
        this.teacher = teacher;
        setSize(900,600);
        setLayout(new BorderLayout(10,10));
        setResizable(false);
        setTitle("Teacher Dashboard - " + teacher.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponent();
        setUpComponents();
        addActionListeners();

        mainBottomCardLayout.show(mainBottomPanel, DEFAULT_TEACHER_PANEL);
        updateTeacherTopPanelInfo();
    }

    public void initComponent(){
        sideBar = new JRPanel();
        mainPage = new JRPanel();

        profileBar = new JRPanel();
        profileImageBar = new JRPanel();
        profileLabelBar = new JRPanel();
        navBar = new JRPanel();
        bottomPanel = new JRPanel();
        mainTopPanel = new JRPanel();

        mainBottomCardLayout = new CardLayout();
        mainBottomPanel = new JPanel(mainBottomCardLayout);

        teacherNameLabel = new JLabel();
        teacherSectionLabel = new JLabel();
        teacherIDLabel = new JLabel();
        teacherAbsentDaysLabel = new JLabel();
        teacherRedFlagLabel = new JLabel();
        profilePicHolder = new JLabel();

        takeStudentAttendanceButton = sideBarButtons("Take Student Attendance");
        reviewStudentAppealsButton = sideBarButtons("Review Student Appeals");
        viewStudentAbsencesButton = sideBarButtons("View Class Absences");
        logOutButton = sideBarButtons("Log Out");


        profilePic = new ImageIcon("././Images/teacher.png"); // Teacher icon
        if(profilePic.getImage() != null) {
            profilePic.setImage(profilePic.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH));
        }
        profilePicHolder.setIcon(profilePic);
        profilePicHolder.setHorizontalAlignment(SwingConstants.CENTER);


        sideBar.setBackground(PURPLE_COLOR);
        sideBar.setPreferredSize(new Dimension(320, 600));
        sideBar.setLayout(new GridLayout(3,1,0,0));

        mainPage.setBackground(Color.white);
        mainPage.setPreferredSize(new Dimension(getWidth() - sideBar.getWidth() - 20, 600));
        mainPage.setLayout(new BorderLayout(0,5));

        profileBar.setBackground(PURPLE_COLOR);
        profileBar.setPreferredSize(new Dimension(sideBar.getWidth(), 180));
        profileBar.setLayout(new BorderLayout());

        profileImageBar.setPreferredSize(new Dimension(300,100));
        profileImageBar.setBackground(PURPLE_COLOR);
        profileImageBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        profileLabelBar.setBackground(PURPLE_COLOR);
        profileLabelBar.setPreferredSize(new Dimension(300,80));
        profileLabelBar.setLayout(new GridLayout(3,1,0,1));
        profileLabelBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        navBar.setBackground(PURPLE_COLOR);
        navBar.setPreferredSize(new Dimension(sideBar.getWidth(), 220)); // For 4 buttons
        navBar.setLayout(new GridLayout(4,1,0,0));

        bottomPanel.setBackground(PURPLE_COLOR);
        bottomPanel.setPreferredSize(new Dimension(sideBar.getWidth(), 200));

        mainTopPanel.setLayout(new GridLayout(2,1,10,5));
        mainTopPanel.setPreferredSize(new Dimension(380,100));
        mainTopPanel.setBackground(GOLD_COLOR);
        mainTopPanel.setBorderThickness(0);
        mainTopPanel.setBorderRadius(0);
        mainTopPanel.setBorderBottomRightRadius(20);
        mainTopPanel.setBorderBottomLeftRadius(20);
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        mainBottomPanel.setBackground(Color.WHITE);

        teacherNameLabel.setFont(new Font("Segoe UI",Font.BOLD,16));
        teacherNameLabel.setText("Name: " + teacher.getName());
        teacherNameLabel.setForeground(Color.WHITE);

        String sectionText = teacher.getGradeSec().toString();
        teacherSectionLabel.setFont(new Font("Segoe UI",Font.PLAIN,15));
        teacherSectionLabel.setText("Home Room: "+ sectionText);
        teacherSectionLabel.setForeground(Color.WHITE);

        teacherIDLabel.setFont(new Font("Segoe UI",Font.PLAIN,15));
        teacherIDLabel.setText("ID: "+teacher.getID());
        teacherIDLabel.setForeground(Color.WHITE);

        teacherAbsentDaysLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        teacherAbsentDaysLabel.setForeground(PURPLE_COLOR);
        teacherRedFlagLabel.setFont(new Font("Segoe UI",Font.ITALIC,16));
        teacherRedFlagLabel.setForeground(PURPLE_COLOR);

        // Initialize panels
        createReviewAppealsPanel();
        createViewClassAbsencesPanel();

        JPanel defaultPanel = new JPanel(new BorderLayout());
        defaultPanel.setBackground(Color.WHITE);
        JLabel welcomeMsg = new JLabel("Welcome, " + teacher.getName() + "! Select an option.", SwingConstants.CENTER);
        welcomeMsg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        defaultPanel.add(welcomeMsg, BorderLayout.CENTER);

        mainBottomPanel.add(defaultPanel, DEFAULT_TEACHER_PANEL);
        mainBottomPanel.add(reviewAppealsPanel, REVIEW_APPEALS_PANEL);
        mainBottomPanel.add(viewClassAbsencesPanel, VIEW_CLASS_ABSENCES_PANEL);
    }

    private void updateTeacherTopPanelInfo() {
        int absentCount = teacher.getAbsentDay();
        teacherAbsentDaysLabel.setText("Your Absent Days: "+ absentCount);
        if(absentCount >= 3){
            teacherRedFlagLabel.setText("Status: Red Flagged (3+ absences)");
            teacherRedFlagLabel.setForeground(Color.RED.darker());
        } else {
            teacherRedFlagLabel.setText("Status: Good Standing");
            teacherRedFlagLabel.setForeground(new Color(0, 100, 0)); // Dark Green
        }
    }


    public JRButton sideBarButtons(String text){
        JRButton button = new JRButton(text);
        button.setPressedBackgroundColor(GOLD_COLOR);
        button.setHoverBackgroundColor(PRESSED_PURPLE_COLOR);
        button.setBorderThickness(0);
        button.setForeground(Color.white);
        button.setBackground(PURPLE_COLOR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setPreferredSize(new Dimension(sideBar.getWidth(),50));
        button.setFont(new Font("Segoe UI",Font.PLAIN,16));
        return button;
    }

    public void setUpComponents(){
        profileImageBar.add(profilePicHolder);
        profileLabelBar.add(teacherNameLabel);
        profileLabelBar.add(teacherIDLabel);
        profileLabelBar.add(teacherSectionLabel);

        profileBar.add(profileImageBar,BorderLayout.NORTH);
        profileBar.add(profileLabelBar, BorderLayout.CENTER);

        navBar.add(takeStudentAttendanceButton);
        navBar.add(reviewStudentAppealsButton);
        navBar.add(viewStudentAbsencesButton);
        navBar.add(logOutButton);

        mainTopPanel.add(teacherAbsentDaysLabel);
        mainTopPanel.add(teacherRedFlagLabel);

        sideBar.add(profileBar);
        sideBar.add(navBar);
        sideBar.add(bottomPanel);

        mainPage.add(mainTopPanel, BorderLayout.NORTH);
        mainPage.add(mainBottomPanel, BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
        add(mainPage, BorderLayout.CENTER);
    }

    private void createReviewAppealsPanel() {
        reviewAppealsPanel = new JPanel(new BorderLayout(10,10));
        reviewAppealsPanel.setBackground(Color.WHITE);
        reviewAppealsPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel title = new JLabel("Review Student Appeals (" + (teacher.getGradeSec() == GRADESEC.none ? "N/A" : teacher.getGradeSec().toString()) + ")", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        reviewAppealsPanel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Student ID", "Student Name", "Date of Absence", "Appeal Reason"};
        appealsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        appealsTable = new JTable(appealsTableModel);
        appealsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appealsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        appealsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appealsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(appealsTable);
        reviewAppealsPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        approveAppealButton = new JRButton("Approve Selected");
        styleJRButton(approveAppealButton, new Color(0,150,0), Color.WHITE); // Darker Green
        denyAppealButton = new JRButton("Deny Selected");
        styleJRButton(denyAppealButton, Color.RED.darker(), Color.WHITE);
        buttonPanel.add(denyAppealButton);
        buttonPanel.add(approveAppealButton);
        reviewAppealsPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createViewClassAbsencesPanel() {
        viewClassAbsencesPanel = new JPanel(new BorderLayout(10,10));
        viewClassAbsencesPanel.setBackground(Color.WHITE);
        viewClassAbsencesPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel title = new JLabel("View Class Absences (" + (teacher.getGradeSec() == GRADESEC.none ? "N/A" : teacher.getGradeSec().toString()) + ")", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        viewClassAbsencesPanel.add(title, BorderLayout.NORTH);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topControls.setBackground(Color.WHITE);
        topControls.add(new JLabel("Select Date:"));
        classAbsenceDateComboBox = new JComboBox<>();
        styleComboBox(classAbsenceDateComboBox);
        topControls.add(classAbsenceDateComboBox);
        searchClassAbsencesButton = new JRButton("Search");
        styleJRButton(searchClassAbsencesButton, GOLD_COLOR, Color.WHITE);
        topControls.add(searchClassAbsencesButton);
        viewClassAbsencesPanel.add(topControls, BorderLayout.NORTH); // Re-add title to main panel NORTH after this

        //Re-add title to main panel NORTH as topControls replaced it.
        viewClassAbsencesPanel.remove(title); // remove previous title
        JPanel headerPanel = new JPanel(new BorderLayout()); // new panel for title and controls
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(topControls, BorderLayout.CENTER);
        viewClassAbsencesPanel.add(headerPanel, BorderLayout.NORTH);


        classAbsencesTextArea = new JTextArea();
        classAbsencesTextArea.setEditable(false);
        classAbsencesTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(classAbsencesTextArea);
        viewClassAbsencesPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void styleJRButton(JRButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorderRadius(10);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 35));
    }

    private void styleComboBox(JComboBox<LocalDate> comboBox) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LocalDate) {
                    setText(((LocalDate) value).format(DATE_FORMATTER));
                }
                return this;
            }
        });
        comboBox.setPreferredSize(new Dimension(200, 30));
        comboBox.setBackground(Color.WHITE);
    }


    private void addActionListeners() {
        takeStudentAttendanceButton.addActionListener(e -> {
            if (teacher.getGradeSec() == GRADESEC.none) {
                JOptionPane.showMessageDialog(this, "You are not assigned as a home room teacher for any class.", "Action Not Allowed", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Check if attendance for this class for the current date has already been taken
            if (AttendanceHistory.checkDuplication(LocalDate.now(), teacher.getGradeSec())) {
                JOptionPane.showMessageDialog(this,
                        "Attendance for " + teacher.getGradeSec().toString() + " on " + LocalDate.now().format(DATE_FORMATTER) + " has already been recorded.",
                        "Attendance Logged",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                AttendancePage attendancePage = new AttendancePage(teacher.getGradeSec());
                attendancePage.setLocationRelativeTo(this);
                attendancePage.setVisible(true);
            }
        });

        reviewStudentAppealsButton.addActionListener(e -> {
            if (teacher.getGradeSec() == GRADESEC.none) {
                JOptionPane.showMessageDialog(this, "You are not a home room teacher. Cannot review appeals.", "Action Not Allowed", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            loadPendingAppeals();
            mainBottomCardLayout.show(mainBottomPanel, REVIEW_APPEALS_PANEL);
        });

        viewStudentAbsencesButton.addActionListener(e -> {
            if (teacher.getGradeSec() == GRADESEC.none) {
                JOptionPane.showMessageDialog(this, "You are not a home room teacher. Cannot view class absences.", "Action Not Allowed", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            populateClassAbsenceDateComboBox();
            mainBottomCardLayout.show(mainBottomPanel, VIEW_CLASS_ABSENCES_PANEL);
        });

        logOutButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginPanel().setVisible(true));
        });

        approveAppealButton.addActionListener(e -> processAppeal(true));
        denyAppealButton.addActionListener(e -> processAppeal(false));
        searchClassAbsencesButton.addActionListener(e -> displayClassAbsencesForSelectedDate());
    }


    private void loadPendingAppeals() {
        appealsTableModel.setRowCount(0); // Clear existing rows
        if (teacher.getGradeSec() == GRADESEC.none) {
            appealsTableModel.addRow(new Object[]{"N/A", "You are not a home room teacher.", "", ""});
            return;
        }

        Map<String, Map<LocalDate, String>> pendingAppeals = AttendanceHistory.getPendingStudentAppealsForSection(teacher.getGradeSec());

        if (pendingAppeals.isEmpty()) {
            appealsTableModel.addRow(new Object[]{"", "No pending appeals found for your class.", "", ""});
            approveAppealButton.setEnabled(false);
            denyAppealButton.setEnabled(false);
        } else {
            pendingAppeals.forEach((studentId, dateAppealMap) -> {
                Student student = AttendanceHistory.getStudentById(studentId); // Helper method needed in AttendanceHistory
                if (student != null) {
                    dateAppealMap.forEach((date, appealText) -> {
                        appealsTableModel.addRow(new Object[]{student.getID(), student.getName(), date.format(DATE_FORMATTER), appealText});
                    });
                }
            });
            approveAppealButton.setEnabled(true);
            denyAppealButton.setEnabled(true);
        }
    }

    private void processAppeal(boolean isApproved) {
        int selectedRow = appealsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appeal to process.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) appealsTableModel.getValueAt(selectedRow, 0);
        //String studentName = (String) appealsTableModel.getValueAt(selectedRow, 1);
        String dateStrWithDay = (String) appealsTableModel.getValueAt(selectedRow, 2);
        // Parse date string like "2023-01-01 (Mon)" back to LocalDate
        LocalDate absenceDate = LocalDate.parse(dateStrWithDay.substring(0, 10), DateTimeFormatter.ISO_DATE);

        Student student = AttendanceHistory.getStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Could not find student with ID: " + studentId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (isApproved) {
            // Remove student from absent list for that day
            AttendanceHistory.removeAbsentFromHistory(absenceDate, student);
            // Update appeal status (or remove it by setting to "No appeal yet")
            AttendanceHistory.setAppealStatus(student, absenceDate, teacher.getGradeSec(), "Approved by " + teacher.getName());
            JOptionPane.showMessageDialog(this, "Appeal approved. Student marked present for " + absenceDate.format(DATE_FORMATTER), "Appeal Approved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Update appeal status to "Denied"
            String originalAppeal = (String) appealsTableModel.getValueAt(selectedRow, 3);
            AttendanceHistory.setAppealStatus(student, absenceDate, teacher.getGradeSec(), "Denied by " + teacher.getName() + ". Reason: " + originalAppeal);
            JOptionPane.showMessageDialog(this, "Appeal denied for " + absenceDate.format(DATE_FORMATTER), "Appeal Denied", JOptionPane.INFORMATION_MESSAGE);
        }
        loadPendingAppeals(); // Refresh the table
        updateTeacherTopPanelInfo(); // In case alert status changes due to absence removal
    }

    private void populateClassAbsenceDateComboBox() {
        classAbsenceDateComboBox.removeAllItems();
        if (teacher.getGradeSec() == GRADESEC.none) {
            classAbsenceDateComboBox.setEnabled(false);
            searchClassAbsencesButton.setEnabled(false);
            classAbsencesTextArea.setText("You are not a home room teacher.");
            return;
        }

        Map<LocalDate, String> allAbsencesForClass; // Not directly used, but concept is to get all dates with records
        List<LocalDate> recordedDatesForClass = new ArrayList<>();
        for (DateChecker dc : AttendanceHistory.recordedStudentsDates) {
            if (dc.returnGradeSec() != null && dc.returnGradeSec().equals(teacher.getGradeSec())) {
                if (!recordedDatesForClass.contains(dc.returnDate())) {
                    recordedDatesForClass.add(dc.returnDate());
                }
            }
        }
        Collections.sort(recordedDatesForClass, Collections.reverseOrder());

        for(LocalDate date : recordedDatesForClass) {
            classAbsenceDateComboBox.addItem(date);
        }

        if (recordedDatesForClass.isEmpty()) {
            classAbsenceDateComboBox.setEnabled(false);
            searchClassAbsencesButton.setEnabled(false);
            classAbsencesTextArea.setText("No attendance records found for your class: " + teacher.getGradeSec());
        } else {
            classAbsenceDateComboBox.setEnabled(true);
            searchClassAbsencesButton.setEnabled(true);
            classAbsencesTextArea.setText("Select a date and click Search.");
        }
    }

    private void displayClassAbsencesForSelectedDate() {
        LocalDate selectedDate = (LocalDate) classAbsenceDateComboBox.getSelectedItem();
        if (selectedDate == null || teacher.getGradeSec() == GRADESEC.none) {
            classAbsencesTextArea.setText("Please select a date or ensure you are a home room teacher.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Absent Students in ").append(teacher.getGradeSec().toString()).append(" on ")
                .append(selectedDate.format(DATE_FORMATTER)).append(":\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-8s %-25s %-15s %s\n", "ID", "Name", "Phone", "Appeal"));
        sb.append("--------------------------------------------------\n");

        boolean foundRecord = false;
        for (DateChecker dc : AttendanceHistory.recordedStudentsDates) {
            if (dc.returnDate().equals(selectedDate) && dc.returnGradeSec().equals(teacher.getGradeSec())) {
                foundRecord = true;
                if (dc.unatendees.isEmpty()) {
                    sb.append("No students were absent in this class on this date.\n");
                } else {
                    for (Student student : dc.unatendees) {
                        String appeal = dc.getStudentAppeal(student.getID());
                        sb.append(String.format("%-8s %-25s %-15s %s\n",
                                student.getID(), student.getName(), student.getPhone(),
                                (appeal == null || appeal.equals(dc.getNoAppealString()) ? "No Appeal" : appeal)));
                    }
                }
                break;
            }
        }
        if (!foundRecord) {
            sb.append("No attendance record found for this class on this date.\n");
        }
        classAbsencesTextArea.setText(sb.toString());
        classAbsencesTextArea.setCaretPosition(0);
    }



}