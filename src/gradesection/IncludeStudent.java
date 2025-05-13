package gradesection;
import java.util.ArrayList;
import member.*;

public class IncludeStudent {
    private static ArrayList<Student> students = new ArrayList<Student>();

    public static void addStudent(Student student)
    {
        for (Student student1 : students) {
            if (student1.getName().equals(student.getName())) {
                System.out.println(student1.getName() + " is already a student");
                return;
            }
        }
        students.add(student);
        System.out.println(student.getName() + " has been enrolled to " + student.getGradeSec().toString());

    }

    public static void removeStudent(String idRemove, int num){
        boolean isRemoved = false;
        for(Student student: students){
            if(student.getID().equals(idRemove)){
                students.remove(student);
                System.out.println(student.getName()+" was removed from class "+student.getGradeSec());
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            System.out.println("This id is not found in the database!");
        }
    }

    public static void removeStudentByName(String name){
         boolean isRemoved = false;
        for(Student student: students){
            if(student.getName().equals(name)){
                students.remove(student);
                System.out.println(student.getName()+" was removed from class "+student.getGradeSec());
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            System.out.println("This id is not found in the database!");
        }
    }

    public static void listStudents()
    {
        for(int i=0; i<12; i++)
        {
            System.out.println("Class "+GRADESEC.values()[i]+": ");
            for(Student student: students){
                if(student.getGradeSec().equals(GRADESEC.values()[i])){
                    System.out.println("\t"+student.getName());
                }
            }
        }
    }


    public static ArrayList<Student> returnStudents() { return students; }

    public static boolean studentExist(String id, String name){
        for(Student student: students){
            if(student.getID().equals(id))
            {
                if(student.getName().equals(name)){

                    return true;
                }
                else{
                    System.out.println("Wrong Id, or name");
                    return false;
                }
            }
        }
        System.out.println("No such ID exits");
        return false;
    }

    public static Student returnStudent(String id, String name){
        for(Student student: students){
            if(student.getID().equals(id))
            {
                if(student.getName().equals(name)){

                    return student;
                }
                else{
                    System.out.println("Wrong Id, or name");
                    return null;
                }
            }
        }
        System.out.println("No such ID exits");
        return null;
    }

    void displayStudentInfo(Student student){
        System.out.println("Student Name: "+student.getName());
        System.out.println("Student Grade: "+student.getGradeSec());
        System.out.println("Student ID: "+student.getID());
        System.out.println("Student Phone: "+student.getPhone());
        if(student.getStat()){
        System.out.println("Student Status: More than three absents");}
    }

    void displayStudentInfo(String name){
        for(Student student : students){
            if(student.getName().equals(name)){
                displayStudentInfo(student);
                break;
            }
        }
    }


}
