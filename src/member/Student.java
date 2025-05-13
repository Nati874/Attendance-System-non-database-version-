package member;
import gradesection.IncludeStudent;
import java.util.Scanner;
import tasks.AttendanceHistory;
public class Student extends People{
    String id;
    boolean isAlerted;
    int alertNumber;
    public Student(String name, GRADESEC gradeSec, String phone)
    {
        super(name, gradeSec, phone);

        this.role = ROLE.Student;
        this.id = "s" + idNum;
        isAlerted = false;
        IncludeStudent.addStudent(this);

    }

    public void increaseAlertNumber(){
        alertNumber++;
        if(alertNumber>2){
            isAlerted = true;
        }
    }

    public void decreaseAlertNumber(){
        alertNumber--;
        if(alertNumber<3){
            isAlerted = false;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getID(){
        return this.id;
    }

    public int getAbsentDay(){
        return AttendanceHistory.showAbsentOfAStudent(getName(), getGradeSec());

    }

    public GRADESEC getGradeSec()
    {
        return this.gradeSec;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public boolean getStat()
    {
        return this.isAlerted;
    }
    
    public void displayOptions(){
        System.out.println("1. Display info");
        System.out.println("2. Make an appeal");
        System.out.println("3. Delete an appeal");
        System.out.println("4. Show absent days");
        System.out.println("Press any other key to go back");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        String anyKey;
        switch(choice){
            case 1:
                displayInfo();
                System.out.println("Press any key to go the menu");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 2:
                makeAppeal();
                System.out.println("Press any key to go the menu");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 3:
                deleteAppeal();
                System.out.println("Press any key to go the menu");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            case 4:
                int totalAbsent = AttendanceHistory.showAbsentOfAStudent(getName(), getGradeSec());
                System.out.println("Press any key to go the menu");
                anyKey = new Scanner(System.in).nextLine();
                displayOptions();
                break;
            default:
                break;
        }
    }

    public void displayInfo(){
        System.out.println("Information about your account\n");
        System.out.println("Name: "+getName());
        System.out.println("Phone: "+getPhone());
        System.out.println("Status: "+(isAlerted?"Alerted":"Not alerted"));
        System.out.println("Number of days been absent: "+alertNumber);
    }

    private void makeAppeal(){
        System.out.println("First type the day you were absent in this format: yyyy-mm-dd");
        String absentDate = new Scanner(System.in).nextLine();
        System.out.println("If there is already an appeal, it will be overwritten. Type the appeal below");
        String appeal = new Scanner(System.in).nextLine();
        AttendanceHistory.addAppeal(this, absentDate, this.getGradeSec(), appeal);
    }

    private void deleteAppeal(){
        System.out.println("Date of absence you made an appeal for: yyyy-mm-dd");
        String absentDate =  new Scanner(System.in).nextLine();
        AttendanceHistory.removeAppeal(this, absentDate, this.getGradeSec());
    }

}