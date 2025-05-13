package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import member.GRADESEC;
import member.Student;
import tasks.AttendanceHistory;
import tasks.DateChecker; // Required for DateChecker.noAppeal comparison

public class StudentDashboard extends JFrame{

    private Student student;

    private JPanel mainPage;
    private JPanel sideBar;
    private JPanel profileBar;
    private JPanel profileImageBar;
    private JPanel profileLabelBar;
    private JPanel navBar;
    private JPanel bottomPanel; // Spacer or future use
    private JRPanel mainTopPanel;
    private JPanel mainBottomPanel; // Will use CardLayout
    private CardLayout mainBottomCardLayout;


    private JLabel profilePicHolder;
    private JLabel studentNameLabel; // Renamed for clarity
    private JLabel studentSectionLabel; // Renamed
    private JLabel studentIDLabel; // Renamed
    private JLabel absentDaysLabel; // Renamed
    private JLabel redFlagLabel; // Renamed

    private JRButton historyButton;
    private JRButton appealButton;
    private JRButton editAppealButton;
    private JRButton logOutButton; // Placeholder, add action later

    // Components for History Panel
    private JPanel historyPanel;
    private JTextArea historyTextArea;

    // Components for Make Appeal Panel
    private JPanel makeAppealPanel;
    private JComboBox<LocalDate> unappealedDatesComboBox;
    private JTextArea makeAppealTextArea;
    private JRButton submitAppealButton;

    // Components for Edit Appeal Panel
    private JPanel editAppealPanel;
    private JComboBox<LocalDate> appealedDatesComboBox;
    private JTextArea editAppealTextArea;
    private JRButton updateAppealButton;
    private JRButton deleteAppealButton;


    private ImageIcon profilePic;

    private final Color GOLD_COLOR = new Color(218, 165, 32);
    private final Color PURPLE_COLOR = new Color(72, 0, 72);
    private final Color PRESSED_PURPLE_COLOR = new Color(65, 0, 60);
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)");


    // Card names for mainBottomPanel
    private static final String HISTORY_PANEL = "HistoryPanel";
    private static final String MAKE_APPEAL_PANEL = "MakeAppealPanel";
    private static final String EDIT_APPEAL_PANEL = "EditAppealPanel";
    private static final String DEFAULT_STUDENT_PANEL = "DefaultStudentPanel";


    public StudentDashboard(Student student){
        this.student = student;
        setSize(900,600);
        setLayout(new BorderLayout(10,10));
        setResizable(false);
        setTitle("Student Dashboard - " + student.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Or HIDE_ON_CLOSE

        initComponent();
        setUpComponents();
        addActionListeners();

        // Set initial view for student
        mainBottomCardLayout.show(mainBottomPanel, DEFAULT_STUDENT_PANEL);
        updateTopPanelInfo(); // Refresh top panel info on load
    }

    public void initComponent(){
        sideBar = new JPanel();
        mainPage = new JPanel();

        profileBar = new JPanel();
        profileImageBar = new JPanel();
        profileLabelBar = new JPanel();
        navBar = new JPanel();
        bottomPanel = new JPanel();
        mainTopPanel = new JRPanel();

        mainBottomCardLayout = new CardLayout();
        mainBottomPanel = new JPanel(mainBottomCardLayout);

        profilePicHolder = new JLabel();
        studentNameLabel = new JLabel();
        studentSectionLabel = new JLabel();
        studentIDLabel = new JLabel();
        absentDaysLabel = new JLabel();
        redFlagLabel = new JLabel();

        historyButton = sideBarButtons("View Absence History");
        appealButton = sideBarButtons("Make an Appeal");
        editAppealButton = sideBarButtons("Edit/Delete Appeal");
        logOutButton = sideBarButtons("Log Out"); // Styled as sidebar button


        profilePicHolder.setPreferredSize(new Dimension(100,100));
        profilePic = new ImageIcon("././Images/student.png"); // Student profile pic
        if (profilePic.getImage() != null) {
            profilePic.setImage(profilePic.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH));
        }
        profilePicHolder.setIcon(profilePic);
        profilePicHolder.setHorizontalAlignment(SwingConstants.CENTER);


        sideBar.setBackground(PURPLE_COLOR);
        sideBar.setPreferredSize(new Dimension(320, 600));
        sideBar.setLayout(new GridLayout(3,1,0,0));

        mainPage.setBackground(Color.WHITE);
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
        navBar.setPreferredSize(new Dimension(sideBar.getWidth(), 220)); // Adjusted for 4 buttons
        navBar.setLayout(new GridLayout(4,1,0,0)); // Changed to 4 rows

        bottomPanel.setBackground(PURPLE_COLOR);
        bottomPanel.setPreferredSize(new Dimension(sideBar.getWidth(), 200)); // Spacer

        mainTopPanel.setLayout(new GridLayout(2,1,10,5)); // 2 rows for absent days and red flag
        mainTopPanel.setPreferredSize(new Dimension(380,100)); // Adjusted height
        mainTopPanel.setBackground(GOLD_COLOR);
        mainTopPanel.setBorderThickness(0);
        mainTopPanel.setBorderRadius(0);
        mainTopPanel.setBorderBottomRightRadius(20);
        mainTopPanel.setBorderBottomLeftRadius(20);
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        mainBottomPanel.setBackground(Color.white);

        studentNameLabel.setFont(new Font("Segoe UI",Font.BOLD,16));
        studentNameLabel.setText("Name: " + student.getName());
        studentNameLabel.setForeground(Color.WHITE);
        studentSectionLabel.setFont(new Font("Segoe UI",Font.PLAIN,15));
        studentSectionLabel.setText("Section: "+student.getGradeSec().toString());
        studentSectionLabel.setForeground(Color.WHITE);
        studentIDLabel.setFont(new Font("Segoe UI",Font.PLAIN,15));
        studentIDLabel.setText("ID: "+student.getID());
        studentIDLabel.setForeground(Color.WHITE);


        absentDaysLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        absentDaysLabel.setForeground(PURPLE_COLOR); // Dark purple text
        redFlagLabel.setFont(new Font("Segoe UI",Font.ITALIC,16));
        redFlagLabel.setForeground(PURPLE_COLOR);

        // Initialize panels for CardLayout
        createHistoryPanel();
        createMakeAppealPanel();
        createEditAppealPanel();

        JPanel defaultPanel = new JPanel(new BorderLayout());
        defaultPanel.setBackground(Color.WHITE);
        JLabel welcomeMsg = new JLabel("Welcome, " + student.getName() + "! Select an option.", SwingConstants.CENTER);
        welcomeMsg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        defaultPanel.add(welcomeMsg, BorderLayout.CENTER);

        mainBottomPanel.add(defaultPanel, DEFAULT_STUDENT_PANEL);
        mainBottomPanel.add(historyPanel, HISTORY_PANEL);
        mainBottomPanel.add(makeAppealPanel, MAKE_APPEAL_PANEL);
        mainBottomPanel.add(editAppealPanel, EDIT_APPEAL_PANEL);

    }

    private void updateTopPanelInfo() {
        int absentCount = student.getAbsentDay(); // This already queries AttendanceHistory
        absentDaysLabel.setText("Total Absent Days: "+ absentCount);
        if(absentCount >= 3){ // Assuming 3 is the threshold from Student class logic
            redFlagLabel.setText("Status: Red Flagged (3+ absences)");
            redFlagLabel.setForeground(Color.RED.darker());
        } else {
            redFlagLabel.setText("Status: Good Standing");
            redFlagLabel.setForeground(new Color(0, 100, 0)); // Dark Green
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

        profileLabelBar.add(studentNameLabel);
        profileLabelBar.add(studentIDLabel);
        profileLabelBar.add(studentSectionLabel);

        profileBar.add(profileImageBar,BorderLayout.NORTH);
        profileBar.add(profileLabelBar, BorderLayout.CENTER);

        navBar.add(historyButton);
        navBar.add(appealButton);
        navBar.add(editAppealButton);
        navBar.add(logOutButton);


        mainTopPanel.add(absentDaysLabel);
        mainTopPanel.add(redFlagLabel);

        sideBar.add(profileBar);
        sideBar.add(navBar);
        sideBar.add(bottomPanel); // Spacer

        mainPage.add(mainTopPanel, BorderLayout.NORTH);
        mainPage.add(mainBottomPanel, BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
        add(mainPage, BorderLayout.CENTER);
    }

    private void createHistoryPanel() {
        historyPanel = new JPanel(new BorderLayout(10,10));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Absence History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        historyPanel.add(title, BorderLayout.NORTH);

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createMakeAppealPanel() {
        makeAppealPanel = new JPanel(new GridBagLayout());
        makeAppealPanel.setBackground(Color.WHITE);
        makeAppealPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Make an Appeal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        makeAppealPanel.add(title, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;


        gbc.gridx = 0; gbc.gridy = 1;
        makeAppealPanel.add(new JLabel("Select Absent Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        unappealedDatesComboBox = new JComboBox<>();
        unappealedDatesComboBox.setBackground(Color.WHITE);
        styleComboBox(unappealedDatesComboBox);
        makeAppealPanel.add(unappealedDatesComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHEAST; // Align label to top
        makeAppealPanel.add(new JLabel("Appeal Reason:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.weighty=1.0;
        makeAppealTextArea = new JTextArea(5, 20);
        makeAppealTextArea.setLineWrap(true);
        makeAppealTextArea.setWrapStyleWord(true);
        JScrollPane appealScrollPane = new JScrollPane(makeAppealTextArea);
        makeAppealPanel.add(appealScrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx=0; gbc.weighty=0; // Reset

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        submitAppealButton = new JRButton("Submit Appeal");
        styleJRButton(submitAppealButton, GOLD_COLOR, Color.WHITE);
        makeAppealPanel.add(submitAppealButton, gbc);
    }

    private void createEditAppealPanel() {
        editAppealPanel = new JPanel(new GridBagLayout());
        editAppealPanel.setBackground(Color.WHITE);
        editAppealPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Edit or Delete Appeal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PURPLE_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        editAppealPanel.add(title, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;


        gbc.gridx = 0; gbc.gridy = 1;
        editAppealPanel.add(new JLabel("Select Appealed Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.WEST;
        appealedDatesComboBox = new JComboBox<>();
        appealedDatesComboBox.setBackground(Color.WHITE);
        styleComboBox(appealedDatesComboBox);
        editAppealPanel.add(appealedDatesComboBox, gbc);
        gbc.gridwidth=1;


        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHEAST;
        editAppealPanel.add(new JLabel("Your Appeal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth=2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.weighty=1.0;
        editAppealTextArea = new JTextArea(5, 20);
        editAppealTextArea.setLineWrap(true);
        editAppealTextArea.setWrapStyleWord(true);
        JScrollPane appealScrollPane = new JScrollPane(editAppealTextArea);
        editAppealPanel.add(appealScrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx=0; gbc.weighty=0; gbc.gridwidth=1;


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        deleteAppealButton = new JRButton("Delete Appeal");
        styleJRButton(deleteAppealButton, Color.GRAY.brighter(), PURPLE_COLOR);
        updateAppealButton = new JRButton("Update Appeal");
        styleJRButton(updateAppealButton, GOLD_COLOR, Color.WHITE);
        buttonsPanel.add(deleteAppealButton);
        buttonsPanel.add(updateAppealButton);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.EAST;
        editAppealPanel.add(buttonsPanel, gbc);
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
    }

    private void styleJRButton(JRButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorderRadius(10);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35));
    }


    private void addActionListeners() {
        historyButton.addActionListener(e -> {
            loadHistory();
            mainBottomCardLayout.show(mainBottomPanel, HISTORY_PANEL);
        });

        appealButton.addActionListener(e -> {
            loadUnappealedDates();
            mainBottomCardLayout.show(mainBottomPanel, MAKE_APPEAL_PANEL);
        });

        editAppealButton.addActionListener(e -> {
            loadAppealedDates();
            mainBottomCardLayout.show(mainBottomPanel, EDIT_APPEAL_PANEL);
        });

        logOutButton.addActionListener(e -> {
            // Implement logout: dispose this frame, show LoginPanel
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginPanel().setVisible(true));
        });

        submitAppealButton.addActionListener(e -> submitNewAppeal());

        appealedDatesComboBox.addActionListener(e -> { // When selection changes, load existing appeal text
            LocalDate selectedDate = (LocalDate) appealedDatesComboBox.getSelectedItem();
            if (selectedDate != null) {
                Map<LocalDate, String> absenceDetails = AttendanceHistory.getStudentAbsenceDetails(student.getID(), student.getGradeSec());
                editAppealTextArea.setText(absenceDetails.getOrDefault(selectedDate, ""));
            } else {
                editAppealTextArea.setText("");
            }
        });

        updateAppealButton.addActionListener(e -> updateExistingAppeal());
        deleteAppealButton.addActionListener(e -> deleteSelectedAppeal());
    }

    private void loadHistory() {
        Map<LocalDate, String> absenceDetails = AttendanceHistory.getStudentAbsenceDetails(student.getID(), student.getGradeSec());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %s\n", "Date of Absence", "Appeal Status / Reason"));
        sb.append("----------------------------------------------------------------------\n");

        if (absenceDetails.isEmpty()) {
            sb.append("No absences recorded.\n");
        } else {
            List<LocalDate> sortedDates = new ArrayList<>(absenceDetails.keySet());
            Collections.sort(sortedDates, Collections.reverseOrder()); // Show most recent first

            for (LocalDate date : sortedDates) {
                String appeal = absenceDetails.get(date);
                sb.append(String.format("%-20s | %s\n", date.format(DATE_FORMATTER), appeal));
            }
        }
        historyTextArea.setText(sb.toString());
        historyTextArea.setCaretPosition(0);
        updateTopPanelInfo();
    }

    private void loadUnappealedDates() {
        Map<LocalDate, String> absenceDetails = AttendanceHistory.getStudentAbsenceDetails(student.getID(), student.getGradeSec());
        Vector<LocalDate> unappealed = new Vector<>();
        DateChecker dummyDc = new DateChecker(LocalDate.now()); // To access NO_APPEAL_STRING without static context issues in older Java

        for (Map.Entry<LocalDate, String> entry : absenceDetails.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals(dummyDc.getNoAppealString())) {
                unappealed.add(entry.getKey());
            }
        }
        Collections.sort(unappealed, Collections.reverseOrder());
        unappealedDatesComboBox.removeAllItems(); // Clear previous items
        for(LocalDate date : unappealed) {
            unappealedDatesComboBox.addItem(date);
        }

        if (unappealed.isEmpty()) {
            unappealedDatesComboBox.setEnabled(false);
            makeAppealTextArea.setEnabled(false);
            submitAppealButton.setEnabled(false);
            makeAppealTextArea.setText("No unappealed absences found.");
        } else {
            unappealedDatesComboBox.setEnabled(true);
            makeAppealTextArea.setEnabled(true);
            submitAppealButton.setEnabled(true);
            makeAppealTextArea.setText("");
        }
        updateTopPanelInfo();
    }

    private void loadAppealedDates() {
        Map<LocalDate, String> absenceDetails = AttendanceHistory.getStudentAbsenceDetails(student.getID(), student.getGradeSec());
        Vector<LocalDate> appealed = new Vector<>();
        DateChecker dummyDc = new DateChecker(LocalDate.now());


        for (Map.Entry<LocalDate, String> entry : absenceDetails.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().equals(dummyDc.getNoAppealString())) {
                appealed.add(entry.getKey());
            }
        }
        Collections.sort(appealed, Collections.reverseOrder());
        appealedDatesComboBox.removeAllItems(); // Clear previous items
        for(LocalDate date : appealed) {
            appealedDatesComboBox.addItem(date);
        }


        if (appealed.isEmpty()) {
            appealedDatesComboBox.setEnabled(false);
            editAppealTextArea.setEnabled(false);
            updateAppealButton.setEnabled(false);
            deleteAppealButton.setEnabled(false);
            editAppealTextArea.setText("No existing appeals found to edit.");
        } else {
            appealedDatesComboBox.setEnabled(true);
            editAppealTextArea.setEnabled(true);
            updateAppealButton.setEnabled(true);
            deleteAppealButton.setEnabled(true);
            // Manually trigger the action listener for the first item if it exists
            if (appealedDatesComboBox.getItemCount() > 0) {
                appealedDatesComboBox.setSelectedIndex(0); // This will trigger its action listener
                LocalDate selectedDate = (LocalDate) appealedDatesComboBox.getSelectedItem();
                editAppealTextArea.setText(absenceDetails.getOrDefault(selectedDate, ""));
            } else {
                editAppealTextArea.setText("");
            }
        }
        updateTopPanelInfo();
    }

    private void submitNewAppeal() {
        LocalDate selectedDate = (LocalDate) unappealedDatesComboBox.getSelectedItem();
        String appealText = makeAppealTextArea.getText().trim();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date for the appeal.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (appealText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your appeal reason.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AttendanceHistory.addAppeal(student, selectedDate.toString(), student.getGradeSec(), appealText);
        JOptionPane.showMessageDialog(this, "Appeal for " + selectedDate.format(DATE_FORMATTER) + " submitted successfully.", "Appeal Submitted", JOptionPane.INFORMATION_MESSAGE);
        makeAppealTextArea.setText("");
        loadUnappealedDates(); // Refresh the list
    }

    private void updateExistingAppeal() {
        LocalDate selectedDate = (LocalDate) appealedDatesComboBox.getSelectedItem();
        String appealText = editAppealTextArea.getText().trim();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select an appealed date to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (appealText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appeal reason cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AttendanceHistory.addAppeal(student, selectedDate.toString(), student.getGradeSec(), appealText); // addAppeal also updates
        JOptionPane.showMessageDialog(this, "Appeal for " + selectedDate.format(DATE_FORMATTER) + " updated successfully.", "Appeal Updated", JOptionPane.INFORMATION_MESSAGE);
        loadAppealedDates(); // Refresh the list and text area
    }

    private void deleteSelectedAppeal() {
        LocalDate selectedDate = (LocalDate) appealedDatesComboBox.getSelectedItem();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select an appealed date to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the appeal for " + selectedDate.format(DATE_FORMATTER) + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            AttendanceHistory.removeAppeal(student, selectedDate.toString(), student.getGradeSec());
            JOptionPane.showMessageDialog(this, "Appeal for " + selectedDate.format(DATE_FORMATTER) + " deleted.", "Appeal Deleted", JOptionPane.INFORMATION_MESSAGE);
            loadAppealedDates(); // Refresh list
            editAppealTextArea.setText("");
        }
    }

/*
    public static void main(String args[]){
        // Sample Data for testing StudentDashboard
        Student testStudent = new Student("Test Stu FullName", GRADESEC.ten_a, "0912345678");

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);

        // Absence 1: Yesterday, no appeal yet
        DateChecker dcYesterday = new DateChecker(yesterday, GRADESEC.ten_a);
        dcYesterday.addAbsentStudent(testStudent); // Defaults to "No appeal yet"
        AttendanceHistory.addDateChecker(dcYesterday);


        // Absence 2: Two days ago, with an appeal
        DateChecker dcTwoDaysAgo = new DateChecker(twoDaysAgo, GRADESEC.ten_a);
        dcTwoDaysAgo.addAbsentStudent(testStudent, "Had a doctor's appointment.");
        AttendanceHistory.addDateChecker(dcTwoDaysAgo);

        // Absence 3: Today, to test alert
        DateChecker dcToday = new DateChecker(today, GRADESEC.ten_a);
        dcToday.addAbsentStudent(testStudent);
        AttendanceHistory.addDateChecker(dcToday);
        testStudent.increaseAlertNumber(); // Simulate multiple absences
        testStudent.increaseAlertNumber();
        testStudent.increaseAlertNumber();


        SwingUtilities.invokeLater(() -> new StudentDashboard(testStudent).setVisible(true));
    }*/
}