package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gradesection.*;
import member.*;

public class LoginPanel extends JFrame {

    private JPanel imagePanel;
    private JPanel roleSelectionPanel;
    private JPanel loginFormPanel;
    private JPanel bottomPanel;

    private JRButton studentButton;
    private JRButton teacherButton;
    private JRButton officialButton;

    private JLabel nameLabel;
    private JRTextField nameField;
    private JLabel idLabel;
    private JRPasswordField idField;
    private JRButton loginButton;

    private JPanel headerPanel;
    private JLabel headerLabel;
    private JRButton closeButton;

    private final Color GOLD_COLOR = new Color(218, 165, 32);
    private final Color PURPLE_COLOR = new Color(72, 0, 72);
    private final Color WHITE_COLOR = Color.WHITE;

    private String currentRole = "";
    
    public LoginPanel() {
        ImageIcon logo = new ImageIcon("././Images/logo.png");
        logo.setImage(logo.getImage().getScaledInstance(30,30,4));
        setIconImage(logo.getImage());
        setBackground(Color.white);
        setSize(400,650);
        setResizable(false);
        setTitle("Present | Log In Page");

        setLayout(new BorderLayout(0,1));
        initComponents();
        setupLayout();
        addListeners();

    }
    
    private void initComponents() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(GOLD_COLOR);
        headerPanel.setPreferredSize(new Dimension(400, 70));
        
        headerLabel = new JLabel("Log In Menu");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        closeButton = new JRButton("Back");
        closeButton.setBorderRadius(55);
        closeButton.setPreferredSize(new Dimension(70, 25));
        closeButton.setBackground(GOLD_COLOR);
        closeButton.setBorderColor(Color.white);
        closeButton.setForeground(Color.white);
        closeButton.setFont(new Font("Arial", Font.BOLD, 15));
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(closeButton,BorderLayout.EAST);


        ImageIcon mainImage = new ImageIcon("././Images/login.png");
        mainImage.setImage(mainImage.getImage().getScaledInstance(200,205,4));
        imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(WHITE_COLOR);
        imagePanel.setPreferredSize(new Dimension(200, 200));

        JLabel imageHolder = new JLabel();
        //imageHolder.setPreferredSize(new Dimension(100,100));
        imageHolder.setIcon(mainImage);

        imagePanel.add(imageHolder);
        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(400, 300));
        bottomPanel.setBackground(Color.white);
        
        // Initialize role selection panel
        roleSelectionPanel = new JPanel();
        roleSelectionPanel.setLayout(new GridLayout(3, 1, 0, 20));
        roleSelectionPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        roleSelectionPanel.setBackground(WHITE_COLOR);
        roleSelectionPanel.setPreferredSize(new Dimension(400, 300));

        ImageIcon studentIcon = new ImageIcon("././Images/student.png");
        studentIcon.setImage(studentIcon.getImage().getScaledInstance(40,40, 4));
        ImageIcon teacherIcon = new ImageIcon("././Images/teacher.png");
        teacherIcon.setImage(teacherIcon.getImage().getScaledInstance(40,40, 4));
        ImageIcon officialIcon = new ImageIcon("././Images/official.png");
        officialIcon.setImage(officialIcon.getImage().getScaledInstance(40,40,4));

        studentButton = createRoleButton("Student", studentIcon);
        teacherButton = createRoleButton("Teacher", teacherIcon);
        officialButton = createRoleButton("Official", officialIcon);
        
        roleSelectionPanel.add(studentButton);
        roleSelectionPanel.add(teacherButton);
        roleSelectionPanel.add(officialButton);

        loginFormPanel = new JPanel();
        loginFormPanel.setLayout(new BoxLayout(loginFormPanel, BoxLayout.Y_AXIS));
        loginFormPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginFormPanel.setBackground(WHITE_COLOR);
        loginFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        loginFormPanel.setVisible(false);
        loginFormPanel.setPreferredSize(new Dimension(400, 300));

        nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(PURPLE_COLOR);
        
        nameField = new JRTextField(20);
        nameField.setPlaceholder("name");
        nameField.setBorderThickness(0);
        nameField.setBorderThickness(2);
        nameField.setBorderColor(GOLD_COLOR);
        nameField.setPreferredSize(new Dimension(300, 50));
        
        idLabel = new JLabel("id:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20));
        idLabel.setForeground(PURPLE_COLOR);
        
        idField = new JRPasswordField(20);
        idField.setPreferredSize(new Dimension(300, 50));;
        idField.setPlaceholder("Type ID here");
        idField.setBorderRadius(0);
        idField.setBorderThickness(2);
        
        loginButton = new JRButton("Log In");
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setBorderRadius(35);
        loginButton.setBorderColor(Color.white);
        loginButton.setBorderThickness(2);
        loginButton.setBackground(GOLD_COLOR);
        loginButton.setForeground(WHITE_COLOR);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(WHITE_COLOR);
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePanel.add(nameLabel);
        namePanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        namePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        namePanel.add(nameField);

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS));
        idPanel.setBackground(WHITE_COLOR);
        idPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);
        idPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        idPanel.add(idLabel);
        idPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        idPanel.add(idField);

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginFormPanel.add(namePanel);
        loginFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginFormPanel.add(idPanel);
        loginFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginFormPanel.add(loginButton);
    }
    
    private JRButton createRoleButton(String text, ImageIcon icon) {
        JRButton button = new JRButton(text);
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(20);
        button.setBorderRadius(40);
        button.setPreferredSize(new Dimension(300, 30));
        button.setBackground(WHITE_COLOR);
        button.setBorderColor(Color.LIGHT_GRAY);
        button.setForeground(GOLD_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        return button;
    }
    
    private void setupLayout() {
        bottomPanel.add(roleSelectionPanel);
        bottomPanel.add(loginFormPanel);
        add(headerPanel, BorderLayout.NORTH);
        add(imagePanel);

        add(bottomPanel, BorderLayout.SOUTH);

    }
    
    private void addListeners() {
        // Add action listeners to role buttons
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentRole = "Student";
                showLoginForm();
            }
        });
        
        teacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentRole = "Teacher";
                showLoginForm();
            }
        });
        
        officialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentRole = "Official";
                showLoginForm();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = new String(idField.getPassword());
                
                if (name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                            "Please enter both name and id",
                            "Login Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(IncludeStudent.studentExist(id, name)&&currentRole.equals("Student")){
                    setVisible(false);
                    StudentDashboard studentDashboard = new StudentDashboard(IncludeStudent.returnStudent(id, name));
                    studentDashboard.setVisible(true);
                }

                else if(IncludeTeacher.checkTeacher(name, id)&&currentRole.equals("Teacher")){
                    System.out.println("hihi");
                    setVisible(false);
                    TeacherDashboard teacherDashboard = new TeacherDashboard(IncludeTeacher.returnTeacher(id));
                    teacherDashboard.setVisible(true);
                }

                else if(id.equals("1234")&&name.equals("Alemu"))
                {
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                }
                JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Logging in as " + currentRole + "\nName: " + name, 
                        "Login Attempt", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add action listener to close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the top-level window containing this panel
                /*Window window = SwingUtilities.getWindowAncestor(LoginPanel.this);
                if (window != null) {
                    window.dispose();
                }*/
                reset();
            }
        });
    }
    
    private void showLoginForm() {

        roleSelectionPanel.setVisible(false);
        loginFormPanel.setVisible(true);
        // Update the header label
        headerLabel.setText("Log In as " + currentRole);
    }

    public void reset() {
        // Clear fields
        nameField.setText("");
        idField.setText("");
        
        // Reset header
        headerLabel.setText("Log In Menu");
        
        roleSelectionPanel.setVisible(true);
        loginFormPanel.setVisible(false);
    }
    

}
