package member;
import java.util.*;
import com.sun.tools.javac.Main;
import gradesection.IncludeStudent;
import gradesection.IncludeTeacher;
import tasks.Attendance;
import tasks.AttendanceHistory;

public class Teacher extends People{
    String id;
    boolean isHomeRoomTeacher;
    boolean isAlerted;
    int alertNumber;
    public Teacher(String name, GRADESEC gradeSec, String phone)
    {
        super(name, gradeSec, phone);
        this.isAlerted = false;
        this.role = ROLE.Teacher;
        this.id = "t" + idNum;
        if(gradeSec == GRADESEC.none) {
            isHomeRoomTeacher = false;
        }
        else
        {
            isHomeRoomTeacher = true;
        }
        IncludeTeacher.addTeacher(this);

    }


    public void removeAbsent(){
        Scanner input = new Scanner(System.in);
        AttendanceHistory.showDailyAbsents(this.gradeSec);
        System.out.println("Type the name: ");
        String name = input.nextLine();
        System.out.println("Date of absence: (Format=> yyyy-mm-dd)");
        String date = input.nextLine();
        AttendanceHistory.removeAbsentFromHistory(date, name, this.gradeSec);
    }

    public int getAbsentDay(){
        return AttendanceHistory.showAbsentOfATeacher(getID());

    }

    void makeAppeal(){
        System.out.println("Type the date you have been absent in this format: yyyy-mm-dd");
        String absentDate = new Scanner(System.in).nextLine();
        System.out.println("Type your appeal for this day below: ");
        String appeal = new Scanner(System.in).nextLine();
        AttendanceHistory.addAppeal(this, absentDate, appeal);
    }

    void deleteAppeal(){
        System.out.println("Type the date you wanted to submitted your appeal for: ");
        String absentDate = new Scanner(System.in).nextLine();
        AttendanceHistory.removeAppeal(this, absentDate);
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

    public void changeGradeSec(){
        this.gradeSec=GRADESEC.none;
    }

    public String getName() {
        return this.name;
    }

    public String getID(){
        return this.id;
    }

    public GRADESEC getGradeSec()
    {
        return this.gradeSec;
    }

    @Override
    public String toString() {
        return "ID: "+id+", Name: "+name+", Grade & Sec: "+gradeSec+", Role: "+role.toString()+", Phone: "+phone;
    }
}