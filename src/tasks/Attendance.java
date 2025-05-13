package tasks;
import member.*;
import gradesection.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class Attendance {

    private ArrayList<Student> sectionStudents = new ArrayList<Student>();
    GRADESEC gradesec;
    LocalDate currentDate;//yyyy-mm-dd;
    public Attendance(GRADESEC gradesec){
        currentDate = LocalDate.now();
        this.gradesec=gradesec;
        System.out.println("Checking attendance for section "+gradesec.toString());
        System.out.print("Date: "+currentDate.toString());
    }

    public Attendance(){
        currentDate = LocalDate.now();
        System.out.println("Checking attendance for teachers");
        System.out.println("Date: "+currentDate.toString());
    }

    public void checkAttendance(){
        if(AttendanceHistory.checkDuplication(this.currentDate, this.gradesec)){
            System.out.println("The daily attendance for this class has already been completed");
        }
        else{
            DateChecker todaysAttendance = new DateChecker(this.currentDate, this.gradesec);
            Scanner input = new Scanner(System.in);
            String choice;
            sectionStudents=studentsInAGrade(this.gradesec);
            System.out.println("* Press 'A' for Absent");
            System.out.println("* Press any key for Present");
            for(Student student : sectionStudents)
            {
                System.out.print(student.getName()+": ");
                choice = input.nextLine();
                if(choice.toLowerCase().equals("a")){
                    System.out.println("Appeal: (type none if there is no appeal yet");
                    String appeal = input.nextLine();
                    student.increaseAlertNumber();
                    if(appeal.toLowerCase().equals("none")) {
                        todaysAttendance.addAbsentStudent(student);
                    }
                    else {
                        todaysAttendance.addAbsentStudent(student, appeal);
                    }
                }
            }
            todaysAttendance.addDateToHistory();
        }
    }

    public void checkTeacherAttendance(){
        if(AttendanceHistory.checkDuplication(this.currentDate)){
            System.out.println("The daily attendance for this class has already been completed");
        }
        else {
            DateChecker todaysAttendance = new DateChecker(this.currentDate);
            Scanner input = new Scanner(System.in);
            String choice;
            System.out.println("* Press 'A' for Absent");
            System.out.println("* Press 'P' for Present");
            for(Teacher teacher : IncludeTeacher.returnTeachers())
            {
                System.out.print(teacher.getName());
                choice = input.nextLine();
                if(choice.toLowerCase().equals("a")){
                    System.out.println("Appeal: (type none if there is no appeal yet");
                    String appeal = input.nextLine();
                    teacher.increaseAlertNumber();
                    if(appeal.toLowerCase().equals("none")) {
                        todaysAttendance.addAbsentTeacher(teacher);
                    }
                    else {
                        todaysAttendance.addAbsentTeacher(teacher, appeal);
                    }
                }
            }
            todaysAttendance.addDateToTeacherHistory();

        }
    }

    public static ArrayList<Student> studentsInAGrade(GRADESEC section){
        ArrayList<Student> sectionStudents = new ArrayList<Student>();
        for(Student student : IncludeStudent.returnStudents()){
            if(section == student.getGradeSec())
            {
                sectionStudents.add(student);
            }
        }
        return sectionStudents;
    }
}
