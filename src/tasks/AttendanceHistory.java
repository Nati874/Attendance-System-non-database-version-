package tasks;
import java.util.ArrayList;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import gradesection.*;
import member.GRADESEC;
import member.Student;
import member.Teacher;
import gradesection.IncludeStudent; // Added for getStudentById

public class AttendanceHistory {
    // Changed to public static for easier access from dashboards. Consider proper encapsulation later.
    public static ArrayList<DateChecker> recordedStudentsDates = new ArrayList<>();
    public static ArrayList<DateChecker> recordedTeachersDates = new ArrayList<>();

    static LocalDate today = LocalDate.now();

    public static void addDateChecker(DateChecker dateChecker){
        // Prevent adding duplicate DateCheckers for the same date and section
        for (DateChecker existingDc : recordedStudentsDates) {
            if (existingDc.returnDate().equals(dateChecker.returnDate()) &&
                    existingDc.returnGradeSec() != null && // Teacher DateCheckers have null gradeSec
                    existingDc.returnGradeSec().equals(dateChecker.returnGradeSec())) {
                // System.out.println("Attendance for " + dateChecker.returnGradeSec() + " on " + dateChecker.returnDate() + " already recorded. Not adding duplicate.");
                return;
            }
        }
        recordedStudentsDates.add(dateChecker);
    }


    public static void addDateCheckerForTeachers(DateChecker dateChecker){
        for (DateChecker existingDc : recordedTeachersDates) {
            if (existingDc.returnDate().equals(dateChecker.returnDate())) {
                // System.out.println("Teacher attendance for " + dateChecker.returnDate() + " already recorded. Not adding duplicate.");
                return;
            }
        }
        recordedTeachersDates.add(dateChecker);
    }

    public static void showDailyAbsents(GRADESEC gradesec){ // Shows for today
        System.out.println("Class: "+gradesec+"\n\nDate: "+today.toString());
        boolean found = false;
        for(DateChecker dailyAbsentee : recordedStudentsDates){
            if(dailyAbsentee.returnDate().equals(today)&&dailyAbsentee.returnGradeSec().equals(gradesec)) {
                dailyAbsentee.displayAbsentStudents();
                found = true;
                return;
            }
        }
        if (!found) System.out.println("Haven't taken the attendance yet for today or no absentees.");
    }

    public static void removeAbsentFromHistory(String removedDateStr, String studentName, GRADESEC gradeSec){
        LocalDate date = LocalDate.parse(removedDateStr);
        Student studentToRemove = null;
        // Find the student object first
        for(Student s : IncludeStudent.returnStudents()){
            if(s.getName().equals(studentName) && s.getGradeSec().equals(gradeSec)){
                studentToRemove = s;
                break;
            }
        }
        if(studentToRemove == null){
            System.out.println("Student "+studentName+" not found in section "+gradeSec);
            return;
        }

        for(DateChecker dc : recordedStudentsDates){
            if(dc.returnDate().equals(date) && dc.returnGradeSec().equals(gradeSec)){
                dc.removeAbsentStudents(studentToRemove);
                System.out.println("Absence of " + studentName + " on " + removedDateStr + " (Section: " + gradeSec + ") has been removed (marked present).");
                return;
            }
        }
        System.out.println("No record found for student "+studentName+" on "+removedDateStr+" in section "+gradeSec);
    }

    // Overload for using Student object directly
    public static void removeAbsentFromHistory(LocalDate date, Student student) {
        if (student == null) return;
        for (DateChecker dc : recordedStudentsDates) {
            if (dc.returnDate().equals(date) && dc.returnGradeSec().equals(student.getGradeSec())) {
                dc.removeAbsentStudents(student);
                System.out.println("Absence of " + student.getName() + " on " + date + " (Section: " + student.getGradeSec() + ") has been removed (marked present).");
                return;
            }
        }
        System.out.println("No record found for student " + student.getName() + " on " + date + " in section " + student.getGradeSec());
    }


    public static void removeAbsentFromHistory(String removedDateStr, String teacherName){
        LocalDate date = LocalDate.parse(removedDateStr);
        Teacher teacherToRemove = null;
        for(Teacher t : IncludeTeacher.returnTeachers()){
            if(t.getName().equals(teacherName)){
                teacherToRemove = t;
                break;
            }
        }
        if(teacherToRemove == null){
            System.out.println("Teacher "+teacherName+" not found.");
            return;
        }

        for(DateChecker dc : recordedTeachersDates){
            if(dc.returnDate().equals(date)){
                dc.removeAbsentTeachers(teacherToRemove);
                return;
            }
        }
        System.out.println("No record found for teacher "+teacherName+" on "+removedDateStr);
    }

    public static void showDailyAbsents(){ // Shows for teachers for today
        System.out.println("Absent teachers today (" + today.toString() + "):");
        boolean found = false;
        for(DateChecker dailyAbsentee : recordedTeachersDates){
            if(dailyAbsentee.returnDate().toString().equals(today.toString())){
                dailyAbsentee.displayAbsentTeachers();
                found = true;
                break;
            }
        }
        if (!found) System.out.println("No teacher attendance taken today or no teachers absent.");
    }

    public static void addAppeal(Teacher teacher, String dateStr, String appeal){
        LocalDate date = LocalDate.parse(dateStr);
        DateChecker dc = findDateChecker(date); // For teachers, gradeSec is not relevant
        if (dc == null || !dc.checkTAbsentOnDate(teacher.getID())) {
            System.out.println("Teacher " + teacher.getName() + " was not marked absent on " + dateStr + " or record does not exist.");
            return;
        }
        dc.addTeacherAppeal(teacher, appeal);
    }

    public static void removeAppeal(Teacher teacher, String dateStr){
        LocalDate date = LocalDate.parse(dateStr);
        DateChecker dc = findDateChecker(date);
        if(dc == null || !dc.checkTAbsentOnDate(teacher.getID())){
            System.out.println("Teacher " + teacher.getName() + " was not marked absent on " + dateStr + " or record does not exist to remove appeal.");
            return;
        }
        dc.removeTeacherAppeal(teacher);
    }

    public static void addAppeal(Student student, String dateStr, GRADESEC gradesec, String appeal){
        LocalDate date = LocalDate.parse(dateStr);
        DateChecker dc = findDateChecker(date, gradesec);
        if(dc == null || !dc.checkAbsentOnDate(student.getID())){
            System.out.println("Student " + student.getName() + " was not marked absent on " + dateStr + " in " + gradesec + " or record does not exist.");
            return;
        }
        dc.addStudentAppeal(student, appeal);
    }

    // To explicitly set an appeal status, e.g., "Approved by [Teacher]" or "Denied by [Teacher]"
    public static void setAppealStatus(Student student, LocalDate date, GRADESEC gradesec, String statusMessage) {
        DateChecker dc = findDateChecker(date, gradesec);
        if (dc != null && dc.checkAbsentOnDate(student.getID())) {
            dc.addStudentAppeal(student, statusMessage); // addStudentAppeal can also update
            System.out.println("Appeal status for " + student.getName() + " on " + date + " set to: " + statusMessage);
        } else {
            System.out.println("Could not set appeal status: Student " + student.getName() + " not found absent on " + date + " in " + gradesec + " or record missing.");
        }
    }


    public static void removeAppeal(Student student, String dateStr, GRADESEC gradesec){
        LocalDate date = LocalDate.parse(dateStr);
        DateChecker dc = findDateChecker(date, gradesec);
        if(dc == null || !dc.checkAbsentOnDate(student.getID())){
            System.out.println("Student " + student.getName() + " was not marked absent on " + dateStr + " in " + gradesec + " or record does not exist to remove appeal.");
            return;
        }
        dc.removeAppeal(student);
    }

    static boolean validDateChecker(String dateStr, GRADESEC gradesec){
        LocalDate date = LocalDate.parse(dateStr);
        for(DateChecker specificDateAttendance : recordedStudentsDates){
            if(specificDateAttendance.returnDate().equals(date)&&specificDateAttendance.returnGradeSec().equals(gradesec)){
                return true;
            }
        }
        return false;
    }

    static boolean validDateChecker(String dateStr){ // For teachers
        LocalDate date = LocalDate.parse(dateStr);
        for(DateChecker specificDateAttendance : recordedTeachersDates){
            if(specificDateAttendance.returnDate().equals(date)){
                return true;
            }
        }
        return false;
    }

    static DateChecker findDateChecker(LocalDate date, GRADESEC gradesec){
        for(DateChecker specificDateAttendance : recordedStudentsDates){
            if(specificDateAttendance.returnDate().equals(date)&&specificDateAttendance.returnGradeSec().equals(gradesec)){
                return specificDateAttendance;
            }
        }
        return null;
    }

    static DateChecker findDateChecker(LocalDate date){ // For teachers
        for(DateChecker specificDateAttendance : recordedTeachersDates){
            if(specificDateAttendance.returnDate().equals(date)){
                return specificDateAttendance;
            }
        }
        return null;
    }

    public static boolean checkDuplication(LocalDate date){ // For teachers
        for(DateChecker dailyRecord: recordedTeachersDates){
            if(dailyRecord.returnDate().equals(date)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkDuplication(LocalDate date, GRADESEC gradeSec){
        for(DateChecker dailySectionRecord: recordedStudentsDates){
            if(dailySectionRecord.returnDate().equals(date) && dailySectionRecord.returnGradeSec() != null && dailySectionRecord.returnGradeSec().equals(gradeSec)){
                return true;
            }
        }
        return false;
    }

    public static int showAbsentOfAStudent(String studentName, GRADESEC gradesec){ // Better to use student ID
        int totalNo=0;
        Student targetStudent = null;
        for(Student s : IncludeStudent.returnStudents()){
            if(s.getName().equals(studentName) && s.getGradeSec().equals(gradesec)){
                targetStudent = s;
                break;
            }
        }
        if(targetStudent == null) return 0;

        for(DateChecker dc : recordedStudentsDates){
            if(dc.returnGradeSec().equals(gradesec)){
                if(dc.checkAbsentOnDate(targetStudent.getID())){
                    totalNo++;
                }
            }
        }
        //System.out.println("Student " + studentName + " has been absent " + totalNo + " times in " + gradesec);
        return totalNo;
    }

    public static int showAbsentOfATeacher(String teacherId){
        int totalNo=0;
        Teacher targetTeacher = IncludeTeacher.returnTeacher(teacherId);
        if(targetTeacher == null) return 0;

        for(DateChecker dc : recordedTeachersDates){
            if(dc.checkTAbsentOnDate(teacherId)){
                totalNo++;
            }
        }
        //System.out.println("Teacher " + targetTeacher.getName() + " (ID: " + teacherId + ") has been absent " + totalNo + " times.");
        return totalNo;
    }

    /**
     * Retrieves all absence records for a specific student.
     * Each map entry is: LocalDate -> Appeal String.
     */
    public static Map<LocalDate, String> getStudentAbsenceDetails(String studentId, GRADESEC gradeSec) {
        Map<LocalDate, String> absenceDetails = new HashMap<>();
        for (DateChecker dc : recordedStudentsDates) {
            if (dc.returnGradeSec() != null && dc.returnGradeSec().equals(gradeSec)) { // Ensure gradeSec matches
                if (dc.checkAbsentOnDate(studentId)) {
                    String appeal = dc.getStudentAppeal(studentId);
                    absenceDetails.put(dc.returnDate(), appeal != null ? appeal : dc.getNoAppealString());
                }
            }
        }
        return absenceDetails;
    }

    /**
     * Retrieves pending student appeals for a specific grade and section.
     * A pending appeal is one that is not "No appeal yet" and does not start with "Approved by" or "Denied by".
     * The map is StudentID -> (LocalDate -> AppealText).
     */
    public static Map<String, Map<LocalDate, String>> getPendingStudentAppealsForSection(GRADESEC section) {
        Map<String, Map<LocalDate, String>> pendingAppeals = new HashMap<>();
        DateChecker dummyDc = new DateChecker(LocalDate.now()); // To access NO_APPEAL_STRING

        for (DateChecker dc : recordedStudentsDates) {
            if (dc.returnGradeSec() != null && dc.returnGradeSec().equals(section)) {
                for (Student student : dc.unatendees) {
                    String appealText = dc.getStudentAppeal(student.getID());
                    if (appealText != null &&
                            !appealText.equals(dummyDc.getNoAppealString()) &&
                            !appealText.toLowerCase().startsWith("approved by") &&
                            !appealText.toLowerCase().startsWith("denied by")) {

                        pendingAppeals.computeIfAbsent(student.getID(), k -> new HashMap<>())
                                .put(dc.returnDate(), appealText);
                    }
                }
            }
        }
        return pendingAppeals;
    }

    public static Student getStudentById(String studentId) {
        for (Student student : IncludeStudent.returnStudents()) {
            if (student.getID().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

}