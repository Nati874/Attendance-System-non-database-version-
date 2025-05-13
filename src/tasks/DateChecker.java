package tasks;
import java.util.*;
import java.time.*;
import member.*;

public class DateChecker {

    public ArrayList<Student> unatendees = new ArrayList<>();
    public ArrayList<Teacher> unatendeeTeachers = new ArrayList<>();
    ArrayList<String> appeals = new ArrayList<>();
    ArrayList<String> teachersAppeals = new ArrayList<>();
    private  LocalDate date;
    private GRADESEC gradeSec;
    final private String noAppeal = "No appeal yet";
    public DateChecker(LocalDate date, GRADESEC gradeSec ) {
        this.date = date;
        this.gradeSec = gradeSec;
    }

    public DateChecker(LocalDate date) {
        this.date = date;
    }

    public void addAbsentStudent(Student absentStudent) {
        for(Student student : unatendees)
        {
            if(absentStudent.getID().equals(student.getID())) // Compare by ID for uniqueness
            {
                System.out.println("Student " + absentStudent.getName() + " is already in the daily absent list for " + date);
                return;
            }
        }
        unatendees.add(absentStudent);
        appeals.add(noAppeal); // Default to "No appeal yet"
    }

    public void addAbsentStudent(Student absentStudent, String appeal){
        for(Student student : unatendees)
        {
            if(absentStudent.getID().equals(student.getID()))
            {
                System.out.println("Student " + absentStudent.getName() + " is already in the daily absent list for " + date);
                // Optionally update appeal if already present
                int index = unatendees.indexOf(student);
                if (index != -1 && !appeal.equalsIgnoreCase(noAppeal)) {
                    appeals.set(index, appeal);
                    System.out.println("Updated appeal for student " + absentStudent.getName() + " on " + date);
                }
                return;
            }
        }
        unatendees.add(absentStudent);
        appeals.add(appeal);
    }

    public void removeAbsentStudents(Student unAbsentStudent){
        int indexToRemove = -1;
        for(int i = 0; i < unatendees.size(); i++){
            if(unatendees.get(i).getID().equals(unAbsentStudent.getID())){
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            unatendees.remove(indexToRemove);
            appeals.remove(indexToRemove);
            unAbsentStudent.decreaseAlertNumber();
            System.out.println("Student " + unAbsentStudent.getName() + " has been removed from the daily absent list for " + date);
        } else {
            System.out.println("Student " + unAbsentStudent.getName() + " not found in absent list for " + date);
        }
    }

    public void addAbsentTeacher(Teacher absentTeacher) {
        for(Teacher teacher : unatendeeTeachers)
        {
            if(absentTeacher.getID().equals(teacher.getID()))
            {
                System.out.println("Teacher "+ absentTeacher.getName() +" is already in the daily absent list for " + date);
                return;
            }
        }
        unatendeeTeachers.add(absentTeacher);
        teachersAppeals.add(noAppeal);
    }

    public void addAbsentTeacher(Teacher absentTeacher, String appeal){
        for(Teacher teacher : unatendeeTeachers)
        {
            if(absentTeacher.getID().equals(teacher.getID()))
            {
                System.out.println("Teacher "+ absentTeacher.getName() +" is already in the daily absent list for " + date);
                int index = unatendeeTeachers.indexOf(teacher);
                if (index != -1 && !appeal.equalsIgnoreCase(noAppeal)) {
                    teachersAppeals.set(index, appeal);
                    System.out.println("Updated appeal for teacher " + absentTeacher.getName() + " on " + date);
                }
                return;
            }
        }
        unatendeeTeachers.add(absentTeacher);
        teachersAppeals.add(appeal);
    }

    public void removeAbsentTeachers(Teacher unAbsentTeacher){
        int indexToRemove = -1;
        for(int i = 0; i < unatendeeTeachers.size(); i++){
            if(unatendeeTeachers.get(i).getID().equals(unAbsentTeacher.getID())){
                indexToRemove = i;
                break;
            }
        }

        if(indexToRemove != -1){
            unatendeeTeachers.remove(indexToRemove);
            teachersAppeals.remove(indexToRemove);
            unAbsentTeacher.decreaseAlertNumber();
            System.out.println("Teacher"+unAbsentTeacher.getName()+" has been removed from the daily absent list for " + date);
        } else {
            System.out.println("Teacher " + unAbsentTeacher.getName() + " not found in absent list for " + date);
        }
    }

    public void addStudentAppeal(Student appealingStudent, String appeal){
        int index = -1;
        for (int i = 0; i < unatendees.size(); i++) {
            if (unatendees.get(i).getID().equals(appealingStudent.getID())) {
                index = i;
                break;
            }
        }

        if(index != -1){
            appeals.set(index, appeal);
            System.out.println("Student " + appealingStudent.getName() + " appeal for " + date + " updated/added.");
        } else {
            System.out.println("Student " + appealingStudent.getName() + " not found in absent list for " + date + " to add appeal.");
        }
    }

    public void removeAppeal(Student appealedStudent){
        int index = -1;
        for (int i = 0; i < unatendees.size(); i++) {
            if (unatendees.get(i).getID().equals(appealedStudent.getID())) {
                index = i;
                break;
            }
        }
        if(index != -1){
            appeals.set(index, noAppeal);
            System.out.println("Student " + appealedStudent.getName() + " appeal for " + date + " removed (set to 'No appeal yet').");
        } else {
            System.out.println("Student " + appealedStudent.getName() + " not found in absent list for " + date + " to remove appeal.");
        }
    }

    public void addTeacherAppeal(Teacher appealingTeacher, String appeal){
        int index = -1;
        for (int i = 0; i < unatendeeTeachers.size(); i++) {
            if (unatendeeTeachers.get(i).getID().equals(appealingTeacher.getID())) {
                index = i;
                break;
            }
        }
        if(index != -1){
            teachersAppeals.set(index, appeal);
            System.out.println("Teacher " + appealingTeacher.getName() + " appeal for " + date + " updated/added.");
        } else {
            System.out.println("Teacher " + appealingTeacher.getName() + " not found in absent list for " + date + " to add appeal.");
        }
    }

   public void removeTeacherAppeal(Teacher appealedTeacher){ // Sets appeal to "No appeal yet"
        int index = -1;
        for (int i = 0; i < unatendeeTeachers.size(); i++) {
            if (unatendeeTeachers.get(i).getID().equals(appealedTeacher.getID())) {
                index = i;
                break;
            }
        }
        if(index != -1){
            teachersAppeals.set(index, noAppeal);
            System.out.println("Teacher " + appealedTeacher.getName() + " appeal for " + date + " removed.");
        } else {
            System.out.println("Teacher " + appealedTeacher.getName() + " not found in absent list for " + date + " to remove appeal.");
        }
    }

    public void addDateToHistory(){
        System.out.println(gradeSec+" daily attendance is completed successfully for " + date);
        AttendanceHistory.addDateChecker(this);
    }

    public void addDateToTeacherHistory(){
        System.out.println("Teacher daily attendance is completed successfully for " + date);
        AttendanceHistory.addDateCheckerForTeachers(this);
    }

    public LocalDate returnDate(){
        return date;
    }

    void displayAbsentStudents(){
        System.out.println("Absent students for " + gradeSec + " on " + date + ":");
        for(int i = 0; i < unatendees.size(); i++){
            System.out.println("Name: " + unatendees.get(i).getName() + ", Appeal: " + appeals.get(i));
        }
        if (unatendees.isEmpty()) {
            System.out.println("No students were absent.");
        }
    }

    void displayAbsentTeachers(){
        System.out.println("Absent teachers on " + date + ":");
        for(int i = 0; i < unatendeeTeachers.size(); i++){
            System.out.println("Name: " + unatendeeTeachers.get(i).getName() + ", Appeal: " + teachersAppeals.get(i));
        }
        if (unatendeeTeachers.isEmpty()) {
            System.out.println("No teachers were absent.");
        }
    }

    void displayAbsentStudent(String name){ // Should be by ID
        int i=1;
        for(Student student : unatendees){
            if(student.getName().equals(name)){ // Better to match by ID
                System.out.println(i + ". Absent on: " + this.returnDate().toString());
                i++;
            }
        }
    }

    public GRADESEC returnGradeSec(){
        return gradeSec;
    }

    public boolean checkAbsentOnDate(String studentId){
        for(Student unattendee : unatendees){
            if(unattendee.getID().equals(studentId)){
                return true;
            }
        }
        return false;
    }

    public boolean checkTAbsentOnDate(String teacherId){
        for(Teacher unattendee :unatendeeTeachers){
            if(unattendee.getID().equals(teacherId)){
                return true;
            }
        }
        return false;
    }


    public String getStudentAppeal(String studentId) {
        for (int i = 0; i < unatendees.size(); i++) {
            if (unatendees.get(i).getID().equals(studentId)) {
                return appeals.get(i);
            }
        }
        return null; // Student not found in absentees for this date
    }


    public boolean hasStudentMadeAppeal(String studentId) {
        String appeal = getStudentAppeal(studentId);
        return appeal != null && !appeal.equals(noAppeal);
    }

    public final String getNoAppealString() {
        return noAppeal;
    }
}