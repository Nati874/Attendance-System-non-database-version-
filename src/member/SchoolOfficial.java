package member;
import java.util.Scanner;
import com.sun.tools.javac.Main;
import gradesection.IncludeTeacher;
import tasks.Attendance;
import tasks.AttendanceHistory;

public class SchoolOfficial extends People{
    int password = 1234;
    String phone ="0953256171";
    String name = "Alemu Tesfaye";
    boolean isCorrect=true;

    public SchoolOfficial(String name, GRADESEC gradeSec, String phone, int password) {
        super(name, gradeSec, phone);
        this.phone=phone;
        this.role = ROLE.Principal;
        this.gradeSec=GRADESEC.none;
        if(this.password==password && this.name.toLowerCase().equals(name.toLowerCase())) {
            isCorrect = true;
        }
        else{
            isCorrect=false;
        }

    }


    public void displayOptions(){
        Scanner input = new Scanner(System.in);
        System.out.println("1. Add student");
        System.out.println("2. Add teacher");
        System.out.println("3. Check teacher attendance");
        System.out.println("4. List teachers");
        System.out.println("5. Show today's absent teacher");
        System.out.println("6. Remove absents");
        System.out.println("7. List home room teachers");
        System.out.println("Press any other key to go back to menu");
        int choice = input.nextInt();
        String anyKey;
        switch (choice){
            case 1:
                registeraion();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 2:
                teacherRegisteraion();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 3:
                Attendance attendance = new Attendance();
                attendance.checkTeacherAttendance();
                displayOptions();
                break;
            case 4:
                IncludeTeacher.listTeachers();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 5:
                AttendanceHistory.showDailyAbsents();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 6:
                removeAbsent();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 7:
                listHomRoomTeacher();
                System.out.println("Press any key to go the menu...");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            default:
                break;
        }
    }

    void listHomRoomTeacher(){
        int count=1;
        for(Teacher teacher : IncludeTeacher.returnHomeRoomTeacher()){
            System.out.println(count+" :"+teacher.getName());
            count++;
        }
    }

    public boolean getValidation(){
        return isCorrect;
    }

    void registeraion(){
        GRADESEC gradesec=GRADESEC.none;
        Scanner input = new Scanner(System.in);
        System.out.print("Student Name: ");
        String name = input.nextLine();
        System.out.print("Student Phone: ");
        String phone = input.nextLine();
        System.out.println("Class: ");
        for(int i = 0; i< GRADESEC.values().length;i++){
            int j=i+1;
            System.out.println(j+". "+GRADESEC.values()[i]);
        }
        p1 :
        {
            int choice = input.nextInt();
            if (choice < 13 && choice > 0) {
                gradesec = GRADESEC.values()[--choice];
            }
            else {
                System.out.println("Invalid choice. Try again");
                break p1;
            }
        }
        Student student = new Student(name, gradesec, phone);
    }

    void teacherRegisteraion(){
        Scanner input = new Scanner(System.in);
        System.out.print("Teacher Name: ");
        String name = input.nextLine();
        System.out.print("Teacher Phone: ");
        String phone = input.nextLine();
        GRADESEC gradeSec=GRADESEC.none;
        System.out.println("Class: (choose none if the teacher is not a home room teacher)");
        for(int i = 0; i< GRADESEC.values().length;i++){
            int j=i+1;
            System.out.println(j+". "+GRADESEC.values()[i]);
        }
        p1 :
        {
            int choice = input.nextInt();
            if (choice < 14 && choice > 0) {
                for(Teacher teacher : IncludeTeacher.returnHomeRoomTeacher()){
                    if(teacher.getGradeSec().equals(gradeSec))
                    {
                        System.out.println("Already have a Home room teacher for this class. Would you like to change it?");
                        System.out.println("Press 1 if 'Yes'");
                        System.out.println("Any other key if 'No' ");
                        int yesNo = input.nextInt();
                        switch (yesNo){
                            case 1:
                                teacher.changeGradeSec();
                                gradeSec = GRADESEC.values()[--choice];
                                break;
                            default:
                                System.out.println("No change has been made. This teacher will not be a home room teacher to any class");
                                break;
                        }
                    }
                }
            } else {
                System.out.println("Invalid choice. Try again");
                break p1;
            }
        }
        Teacher teacher = new Teacher(name, gradeSec, phone);
    }

    void removeAbsent(){
        System.out.println("Type the name of the teacher: ");
        String teacherName = new Scanner(System.in).nextLine();
        System.out.println("Type the date he/she was absent in this format: yyyy-mm-dd: ");
        String dateOfAbsence = new Scanner(System.in).nextLine();
        AttendanceHistory.removeAbsentFromHistory(dateOfAbsence, teacherName);
    }
}
