package GUI;
import GUI.JRButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import member.*;
import gradesection.*;
import tasks.Attendance;
import tasks.AttendanceHistory;
import tasks.DateChecker;


public class AdminDashboard extends JFrame {
    private SchoolOfficial schoolOfficial; // Assuming this might be used later

    // Components for Add Student
    private JRTextField studentNameField;
    private JRTextField studentPhoneField;
    private JComboBox<GRADESEC> studentGradeSecComboBox;
    private JRButton studentSaveButton;
    private JRButton studentDiscardButton;
    private JPanel addStudentBar;

    // Components for Add Teacher
    private JRTextField teacherNameField;
    private JRTextField teacherPhoneField;
    private JComboBox<GRADESEC> teacherGradeSecComboBox;
    private JRButton teacherSaveButton;
    private JRButton teacherDiscardButton;
    private JPanel addTeacherBar;

    // Components for Show Absents
    private JPanel showAbsentsBar;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private ButtonGroup roleButtonGroup;
    private JComboBox<LocalDate> dateComboBox;
    private JComboBox<GRADESEC> absentGradeSecComboBox;
    private JLabel absentGradeSecLabel;
    private JRButton searchAbsentsButton;
    private JTextArea absentsDisplayArea;


    private JPanel mainPage;
    private JPanel sideBar;
    private JPanel profileBar;
    private JPanel navBar;
    private JPanel bottomPanel;
    private JRPanel mainTopPanel;
    private JPanel mainBottomPanel; // Will use CardLayout
    private CardLayout mainBottomCardLayout;

    private JPanel profileImageBar;
    private JPanel profileLabelBar;

    private JRButton addNewTeacherButton; // Renamed for clarity
    private JRButton takeTeacherAttendanceButton; // Renamed for clarity
    private JRButton viewAttendanceRecordsButton; // Renamed
    private JRButton addNewStudentButton; // Renamed for clarity

    private JLabel Name;
    private JLabel Section; // For Admin, this might be "Role: Admin" or similar
    private JLabel ID;
    private JLabel profilePicHolder;

    private ImageIcon profilePic;

    private final Color GOLD_COLOR = new Color(218, 165, 32);
    private final Color PURPLE_COLOR = new Color(72, 0, 72);
    private final Color PRESSED_PURPLE_COLOR = new Color(65, 0, 60);
    private LocalDate currentDate = LocalDate.now();

    // Card names for mainBottomPanel
    private static final String ADD_STUDENT_PANEL = "AddStudentPanel";
    private static final String ADD_TEACHER_PANEL = "AddTeacherPanel";
    private static final String SHOW_ABSENTS_PANEL = "ShowAbsentsPanel";
    private static final String DEFAULT_EMPTY_PANEL = "DefaultEmptyPanel";


    public AdminDashboard(){
        // Initialize schoolOfficial if necessary, e.g., for validation or specific admin data
        // this.schoolOfficial = new SchoolOfficial("Admin Name", GRADESEC.none, "AdminPhone", 1234); // Example

        setSize(900,600);
        setLayout(new BorderLayout(10,10));
        setResizable(false);
        setTitle("Admin Dashboard - Attendance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Or HIDE_ON_CLOSE if part of larger app

        initComponent();
        setUpComponents();
        actionListeners();

        // Set initial view
        mainBottomCardLayout.show(mainBottomPanel, ADD_STUDENT_PANEL); // Default to add student
    }

    public void initComponent(){
        sideBar = new JRPanel();
        mainPage = new JRPanel();

        profileBar = new JPanel();
        profileImageBar = new JPanel();
        profileLabelBar = new JPanel();

        navBar = new JPanel();
        bottomPanel = new JPanel(); // Serves as a spacer or for future use
        mainTopPanel = new JRPanel();

        mainBottomCardLayout = new CardLayout();
        mainBottomPanel = new JPanel(mainBottomCardLayout); // Applying CardLayout

        addStudentBar = new JPanel();
        addTeacherBar = new JPanel();
        showAbsentsBar = new JPanel();


        addNewStudentButton =  sideBarButtons("Add New Student");
        addNewTeacherButton = sideBarButtons("Add New Teacher");
        viewAttendanceRecordsButton = sideBarButtons("View Attendance Records");
        takeTeacherAttendanceButton = sideBarButtons("Take Teacher Attendance");

        Name = new JLabel();
        Section = new JLabel(); // For Admin, perhaps "Role: Administrator"
        ID = new JLabel();

        profilePic = new ImageIcon("././Images/official.png"); // Using official icon
        profilePicHolder = new JLabel();

        sideBar.setBackground(PURPLE_COLOR);
        sideBar.setPreferredSize(new Dimension(320, 600));
        sideBar.setLayout(new GridLayout(3,1,0,0)); // Profile, Nav, BottomPanel(spacer)

        mainPage.setBackground(Color.white);
        mainPage.setPreferredSize(new Dimension(600, 600)); // Adjusted for frame size
        mainPage.setLayout(new BorderLayout(0,5));

        profilePicHolder.setPreferredSize(new Dimension(100,100));
        if (profilePic.getImage() != null) {
            profilePic.setImage(profilePic.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH));
        }
        profilePicHolder.setIcon(profilePic);
        profilePicHolder.setHorizontalAlignment(SwingConstants.CENTER);


        profileBar.setBackground(PURPLE_COLOR);
        profileBar.setPreferredSize(new Dimension(sideBar.getWidth(), 180)); // Adjusted height
        profileBar.setLayout(new BorderLayout());


        profileImageBar.setPreferredSize(new Dimension(300,100));
        profileImageBar.setBackground(PURPLE_COLOR);
        profileImageBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        profileLabelBar.setBackground(PURPLE_COLOR);
        profileLabelBar.setPreferredSize(new Dimension(300,80)); // Adjusted height
        profileLabelBar.setLayout(new GridLayout(3,1,0,1));
        profileLabelBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add some left padding


        navBar.setBackground(PURPLE_COLOR);
        navBar.setPreferredSize(new Dimension(sideBar.getWidth(), 220)); // Adjusted height
        navBar.setLayout(new GridLayout(4,1,0,0));

        bottomPanel.setBackground(PURPLE_COLOR);
        bottomPanel.setPreferredSize(new Dimension(sideBar.getWidth(), 200)); // Remaining space

        mainTopPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Changed layout
        mainTopPanel.setPreferredSize(new Dimension(getWidth() - sideBar.getWidth() - 20,100)); // Adjusted height
        mainTopPanel.setBackground(GOLD_COLOR);
        mainTopPanel.setBorderThickness(0);
        mainTopPanel.setBorderRadius(0);
        mainTopPanel.setBorderBottomRightRadius(40);
        mainTopPanel.setBorderBottomLeftRadius(40);
        JLabel welcomeLabel = new JLabel("Administrator Control Panel");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        mainTopPanel.add(welcomeLabel);


        mainBottomPanel.setBackground(Color.white);
        // mainBottomPanel.setPreferredSize(new Dimension(380, 400)); // Size managed by BorderLayout.CENTER


        Name.setFont(new Font("Segoe UI",Font.BOLD,15));
        Name.setText("Name: Dr. Alemu"); // Placeholder Admin Name
        Name.setForeground(Color.WHITE);
        Section.setFont(new Font("Segoe UI",Font.PLAIN,15));
        Section.setText("Role: Administrator");
        Section.setForeground(Color.WHITE);
        ID.setFont(new Font("Segoe UI",Font.PLAIN,15));
        ID.setText("ID: ADM-001"); // Placeholder Admin ID
        ID.setForeground(Color.WHITE);

        // Initialize panels for CardLayout
        createAddStudentPanel();
        createAddTeacherPanel();
        createShowAbsentsPanel();

        JPanel defaultEmptyPanel = new JPanel(); // A default panel
        defaultEmptyPanel.setBackground(Color.WHITE);
        defaultEmptyPanel.add(new JLabel("Select an option from the sidebar."));

        mainBottomPanel.add(addStudentBar, ADD_STUDENT_PANEL);
        mainBottomPanel.add(addTeacherBar, ADD_TEACHER_PANEL);
        mainBottomPanel.add(showAbsentsBar, SHOW_ABSENTS_PANEL);
        mainBottomPanel.add(defaultEmptyPanel, DEFAULT_EMPTY_PANEL);
    }

    public JRButton sideBarButtons(String text){
        JRButton button = new JRButton(text);
        button.setPressedBackgroundColor(GOLD_COLOR);
        button.setHoverBackgroundColor(PRESSED_PURPLE_COLOR);
        button.setBorderThickness(0);
        button.setForeground(Color.white);
        button.setBackground(PURPLE_COLOR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15); // Space between icon and text
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Left padding
        button.setPreferredSize(new Dimension(sideBar.getWidth(),50));
        button.setFont(new Font("Segoe UI",Font.PLAIN,16));
        return button;
    }

    public void setUpComponents(){

        profileImageBar.add(profilePicHolder);
        profileLabelBar.add(Name); // Order changed for better visual
        profileLabelBar.add(ID);
        profileLabelBar.add(Section);

        profileBar.add(profileImageBar,BorderLayout.NORTH);
        profileBar.add(profileLabelBar, BorderLayout.CENTER);

        navBar.add(addNewStudentButton);
        navBar.add(addNewTeacherButton);
        navBar.add(viewAttendanceRecordsButton);
        navBar.add(takeTeacherAttendanceButton);


        sideBar.add(profileBar);
        sideBar.add(navBar);
        sideBar.add(bottomPanel); // Spacer
        mainPage.add(mainTopPanel, BorderLayout.NORTH);
        mainPage.add(mainBottomPanel, BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
        add(mainPage, BorderLayout.CENTER);
    }

    private void actionListeners(){
        takeTeacherAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if attendance for teachers for the current date has already been taken
                if (AttendanceHistory.checkDuplication(currentDate)) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Teacher attendance for " + currentDate.format(DateTimeFormatter.ISO_DATE) + " has already been recorded.",
                            "Attendance Logged",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    AttendancePage attendancePage = new AttendancePage(); // For teachers
                    attendancePage.setLocationRelativeTo(AdminDashboard.this); // Center relative to dashboard
                    attendancePage.setVisible(true);
                }
            }
        });

        studentSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });
        studentDiscardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                discardStudentChanges();
            }
        });

        teacherSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTeacher();
            }
        });
        teacherDiscardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                discardTeacherChanges();
            }
        });

        addNewStudentButton.addActionListener(e -> mainBottomCardLayout.show(mainBottomPanel, ADD_STUDENT_PANEL));
        addNewTeacherButton.addActionListener(e -> mainBottomCardLayout.show(mainBottomPanel, ADD_TEACHER_PANEL));
        viewAttendanceRecordsButton.addActionListener(e -> {
            populateDateComboBox(); // Refresh dates when panel is shown
            mainBottomCardLayout.show(mainBottomPanel, SHOW_ABSENTS_PANEL);
        });

        searchAbsentsButton.addActionListener(e -> searchAndDisplayAbsents());

        studentRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                absentGradeSecLabel.setVisible(true);
                absentGradeSecComboBox.setVisible(true);
            }
        });

        teacherRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                absentGradeSecLabel.setVisible(false);
                absentGradeSecComboBox.setVisible(false);
            }
        });
    }

    private void createAddStudentPanel(){
        addStudentBar.setLayout(new GridBagLayout());
        addStudentBar.setBackground(Color.white);
        addStudentBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add New Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addStudentBar.add(title, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        addStudentBar.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        studentNameField = new JRTextField(20);
        styleTextField(studentNameField);
        addStudentBar.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        addStudentBar.add(new JLabel("Grade Section:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        studentGradeSecComboBox = new JComboBox<>(GRADESEC.values());
        studentGradeSecComboBox.removeItem(GRADESEC.none); // Students must have a section
        studentGradeSecComboBox.setBackground(Color.WHITE);
        addStudentBar.add(studentGradeSecComboBox, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        addStudentBar.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        studentPhoneField = new JRTextField(20);
        styleTextField(studentPhoneField);
        addStudentBar.add(studentPhoneField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        studentSaveButton = new JRButton("Save");
        styleSaveButton(studentSaveButton);
        studentDiscardButton = new JRButton("Discard");
        styleDiscardButton(studentDiscardButton);
        buttonPanel.add(studentDiscardButton);
        buttonPanel.add(studentSaveButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        addStudentBar.add(buttonPanel, gbc);
    }

    private void createAddTeacherPanel() {
        addTeacherBar.setLayout(new GridBagLayout());
        addTeacherBar.setBackground(Color.white);
        addTeacherBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add New Teacher");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addTeacherBar.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        addTeacherBar.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        teacherNameField = new JRTextField(20);
        styleTextField(teacherNameField);
        addTeacherBar.add(teacherNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addTeacherBar.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        teacherPhoneField = new JRTextField(20);
        styleTextField(teacherPhoneField);
        addTeacherBar.add(teacherPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addTeacherBar.add(new JLabel("Home Room Section:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        teacherGradeSecComboBox = new JComboBox<>(GRADESEC.values());
        teacherGradeSecComboBox.removeItem(GRADESEC.none); // <<--- MODIFICATION HERE
        // If GRADESEC.none should still be an option for *some* teachers (e.g. subject specialists),
        // then do not remove it. The TeacherDashboard will correctly identify them as non-home room.
        // For the "assume all teachers are home room teachers" for dashboard purposes,
        // removing .none here makes sense for new teacher entries meant for home room duties.
        teacherGradeSecComboBox.setBackground(Color.WHITE);
        addTeacherBar.add(teacherGradeSecComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        teacherSaveButton = new JRButton("Save");
        styleSaveButton(teacherSaveButton);
        teacherDiscardButton = new JRButton("Discard");
        styleDiscardButton(teacherDiscardButton);
        buttonPanel.add(teacherDiscardButton);
        buttonPanel.add(teacherSaveButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        addTeacherBar.add(buttonPanel, gbc);
    }

    private void createShowAbsentsPanel() {
        showAbsentsBar.setLayout(new GridBagLayout());
        showAbsentsBar.setBackground(Color.WHITE);
        showAbsentsBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("View Attendance Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        showAbsentsBar.add(title, gbc);
        gbc.gridwidth = 1; // Reset

        // Role Selection
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        showAbsentsBar.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        studentRadioButton = new JRadioButton("Student");
        studentRadioButton.setBackground(Color.WHITE);
        studentRadioButton.setSelected(true); // Default
        teacherRadioButton = new JRadioButton("Teacher");
        teacherRadioButton.setBackground(Color.WHITE);
        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(studentRadioButton);
        roleButtonGroup.add(teacherRadioButton);
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(studentRadioButton);
        rolePanel.add(teacherRadioButton);
        showAbsentsBar.add(rolePanel, gbc);

        // Date Selection
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        showAbsentsBar.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dateComboBox = new JComboBox<>();
        dateComboBox.setBackground(Color.WHITE);
        // Dates will be populated by populateDateComboBox()
        // Custom renderer to display LocalDate nicely
        dateComboBox.setRenderer(new DefaultListCellRenderer() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)");
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LocalDate) {
                    setText(((LocalDate) value).format(formatter));
                }
                return this;
            }
        });
        showAbsentsBar.add(dateComboBox, gbc);


        // Grade/Section Selection (for Students)
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        absentGradeSecLabel = new JLabel("Grade Section:");
        showAbsentsBar.add(absentGradeSecLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        absentGradeSecComboBox = new JComboBox<>(GRADESEC.values());
        absentGradeSecComboBox.removeItem(GRADESEC.none); // Typically students are in a section
        absentGradeSecComboBox.setBackground(Color.WHITE);
        showAbsentsBar.add(absentGradeSecComboBox, gbc);

        // Search Button
        gbc.gridy = 2; // Align with Date
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        searchAbsentsButton = new JRButton("Search");
        searchAbsentsButton.setBackground(GOLD_COLOR);
        searchAbsentsButton.setForeground(Color.WHITE);
        searchAbsentsButton.setBorderRadius(10);
        searchAbsentsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchAbsentsButton.setPreferredSize(new Dimension(100, 30));
        showAbsentsBar.add(searchAbsentsButton, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset

        // Results Area
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        absentsDisplayArea = new JTextArea(10, 30);
        absentsDisplayArea.setEditable(false);
        absentsDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        absentsDisplayArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollPane = new JScrollPane(absentsDisplayArea);
        showAbsentsBar.add(scrollPane, gbc);

        // Initial state for student-specific fields
        absentGradeSecLabel.setVisible(studentRadioButton.isSelected());
        absentGradeSecComboBox.setVisible(studentRadioButton.isSelected());
    }


    private void styleTextField(JRTextField textField) {
        textField.setBorderLeftThickness(0);
        textField.setBorderTopThickness(0);
        textField.setBorderRightThickness(0);
        textField.setBorderBottomThickness(2);
        textField.setBorderColor(GOLD_COLOR);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleSaveButton(JRButton button) {
        button.setBackground(GOLD_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderRadius(10);
        button.setBorderThickness(0);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100,35));
    }

    private void styleDiscardButton(JRButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.DARK_GRAY);
        button.setBorderRadius(10);
        button.setBorderThickness(1);
        button.setBorderColor(Color.LIGHT_GRAY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(100,35));
    }


    private void saveStudent() {
        String name = studentNameField.getText().trim();
        String phone = studentPhoneField.getText().trim();
        GRADESEC gradeSec = (GRADESEC) studentGradeSecComboBox.getSelectedItem();

        if (name.isEmpty() || phone.isEmpty() || gradeSec == null || gradeSec == GRADESEC.none) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and select a valid grade section for the student.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // The Student constructor calls IncludeStudent.addStudent()
        new Student(name, gradeSec, phone);
        // Student constructor already prints confirmation or error.
        // We can add a JOptionPane confirmation here as well.
        JOptionPane.showMessageDialog(this,
                "Student " + name + " has been added.",
                "Student Added",
                JOptionPane.INFORMATION_MESSAGE);
        discardStudentChanges();
    }

    private void discardStudentChanges() {
        studentNameField.setText("");
        studentPhoneField.setText("");
        if (studentGradeSecComboBox.getItemCount() > 0) {
            studentGradeSecComboBox.setSelectedIndex(0);
        }
    }

    private void saveTeacher() {
        String name = teacherNameField.getText().trim();
        String phone = teacherPhoneField.getText().trim();
        GRADESEC gradeSec = (GRADESEC) teacherGradeSecComboBox.getSelectedItem(); // This will NOT be GRADESEC.none due to earlier modification

        if (name.isEmpty() || phone.isEmpty() || gradeSec == null) { // gradeSec == GRADESEC.none should not happen if removed from ComboBox
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and select a home room section for the teacher.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if selected section already has a home room teacher
        // This check is still relevant even if .none is removed, 
        // as we're assigning a *specific* section.
        if (gradeSec != GRADESEC.none) { // This condition will always be true if .none is removed from combobox
            for (Teacher existingTeacher : IncludeTeacher.returnHomeRoomTeacher()) {
                if (existingTeacher.getGradeSec() == gradeSec) {
                    int confirmation = JOptionPane.showConfirmDialog(this,
                            "Section " + gradeSec.toString() + " already has a home room teacher (" + existingTeacher.getName() + ").\n" +
                                    "Do you want to replace them and assign this new teacher as home room teacher?\n" +
                                    "(The existing teacher will no longer be a home room teacher for this section).",
                            "Confirm Home Room Teacher Change",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        existingTeacher.changeGradeSec(); // Sets their gradeSec to GRADESEC.none
                    } else {
                        JOptionPane.showMessageDialog(this, "Teacher not added. The existing home room teacher remains.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        return; // User cancelled
                    }
                    break;
                }
            }
        }

        new Teacher(name, gradeSec, phone);
        JOptionPane.showMessageDialog(this,
                "Teacher " + name + " has been added as home room teacher for " + gradeSec.toString() + ".",
                "Teacher Added",
                JOptionPane.INFORMATION_MESSAGE);
        discardTeacherChanges();
    }

    private void discardTeacherChanges() {
        teacherNameField.setText("");
        teacherPhoneField.setText("");
        if (teacherGradeSecComboBox.getItemCount() > 0) {
            // Default to the first available section if .none was removed
            teacherGradeSecComboBox.setSelectedIndex(0);
        }
    }

    private void populateDateComboBox() {
        Set<LocalDate> uniqueDates = new HashSet<>();
        for (DateChecker dc : AttendanceHistory.recordedStudentsDates) {
            uniqueDates.add(dc.returnDate());
        }
        for (DateChecker dc : AttendanceHistory.recordedTeachersDates) {
            uniqueDates.add(dc.returnDate());
        }

        List<LocalDate> sortedDates = new ArrayList<>(uniqueDates);
        Collections.sort(sortedDates, Collections.reverseOrder()); // Most recent first

        dateComboBox.removeAllItems();
        for (LocalDate date : sortedDates) {
            dateComboBox.addItem(date);
        }
        if (dateComboBox.getItemCount() == 0) {
            dateComboBox.setEnabled(false);
            searchAbsentsButton.setEnabled(false);
            absentsDisplayArea.setText("No attendance records found in the system.");
        } else {
            dateComboBox.setEnabled(true);
            searchAbsentsButton.setEnabled(true);
        }
    }

    private void searchAndDisplayAbsents() {
        LocalDate selectedDate = (LocalDate) dateComboBox.getSelectedItem();
        if (selectedDate == null) {
            absentsDisplayArea.setText("Please select a date.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");
        sb.append("Absentee Report for: ").append(selectedDate.format(dateFormatter)).append("\n");
        sb.append("--------------------------------------------------\n");

        boolean found = false;

        if (studentRadioButton.isSelected()) {
            GRADESEC selectedGradeSec = (GRADESEC) absentGradeSecComboBox.getSelectedItem();
            if (selectedGradeSec == null) {
                absentsDisplayArea.setText("Please select a grade section for students.");
                return;
            }
            sb.append("Role: Students\nSection: ").append(selectedGradeSec.toString()).append("\n\n");
            sb.append(String.format("%-8s %-30s %-15s\n", "ID", "Name", "Phone"));
            sb.append("--------------------------------------------------\n");

            for (DateChecker dc : AttendanceHistory.recordedStudentsDates) {
                if (dc.returnDate().equals(selectedDate) && dc.returnGradeSec().equals(selectedGradeSec)) {
                    if (dc.unatendees.isEmpty()) {
                        sb.append("No students were absent in this section on this date.\n");
                    } else {
                        for (Student student : dc.unatendees) {
                            sb.append(String.format("%-8s %-30s %-15s\n", student.getID(), student.getName(), student.getPhone()));
                            found = true;
                        }
                    }
                    found = true; // Found the date checker for this class
                    break;
                }
            }
            if (!found) {
                sb.append("No attendance record found for section ").append(selectedGradeSec.toString()).append(" on this date.\n");
            }

        } else if (teacherRadioButton.isSelected()) {
            sb.append("Role: Teachers\n\n");
            sb.append(String.format("%-8s %-30s %-15s\n", "ID", "Name", "Phone"));
            sb.append("--------------------------------------------------\n");
            for (DateChecker dc : AttendanceHistory.recordedTeachersDates) {
                if (dc.returnDate().equals(selectedDate)) {
                    if (dc.unatendeeTeachers.isEmpty()) {
                        sb.append("No teachers were absent on this date.\n");
                    } else {
                        for (Teacher teacher : dc.unatendeeTeachers) {
                            String phone = "N/A"; // Placeholder for teacher phone if not directly available or needed
                            // If Teacher class had a getPhone() method:
                            // phone = teacher.getPhone(); 
                            sb.append(String.format("%-8s %-30s %-15s\n", teacher.getID(), teacher.getName(), phone));
                            found = true;
                        }
                    }
                    found = true; // Found the date checker for teachers
                    break;
                }
            }
            if (!found) {
                sb.append("No teacher attendance record found for this date.\n");
            }
        }

        absentsDisplayArea.setText(sb.toString());
        absentsDisplayArea.setCaretPosition(0); // Scroll to top
    }


}