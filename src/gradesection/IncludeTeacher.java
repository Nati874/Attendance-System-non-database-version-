package gradesection;
import java.util.ArrayList;
import member.*;

public class IncludeTeacher {
    private static ArrayList<Teacher> teachers = new ArrayList<Teacher>();

    public static void addTeacher(Teacher teacher)
    {
        for (Teacher teacher1 : teachers) {
            if (teacher1.getName().equals(teacher.getName())) {
                System.out.println(teacher.getName() + " is already a Teacher");
                return;
            }
        }
        teachers.add(teacher);

    }

    public static void removeTeacher(String idRemove){
       boolean isRemoved = false;
        for(Teacher teacher: teachers){
            if(teacher.getID().equals(idRemove)){
                teachers.remove(teacher);
                System.out.println(teacher.getName()+" was removed from class ");
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            System.out.println("This id is not found in the database!");
        }
    }

    public static void removeTeacherByName(String name){
        boolean isRemoved = false;
        for(Teacher teacher: teachers){
            if(teacher.getName().equals(name)){
                teachers.remove(teacher);
                System.out.println(teacher.getName()+" was removed from class ");
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            System.out.println("This name is not found in the database!");
        }
    }

    public static boolean checkTeacher(String name, String id){
        for(Teacher teacher : teachers){
            if(teacher.getName().equals(name)&&teacher.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public static void listTeachers()
    {
        int teacherCount=1;
        for(Teacher teacher: teachers){
            System.out.println(teacherCount+". "+teacher.getName());
            teacherCount++;
        }

    }

    public static ArrayList<Teacher> returnHomeRoomTeacher(){
        ArrayList<Teacher> homeRoomTeachers = new ArrayList<>();
        for(Teacher teacher : teachers){
            if(teacher.getGradeSec().equals(GRADESEC.none)){
                continue;
            }
            homeRoomTeachers.add(teacher);
        }

        return homeRoomTeachers;
    }

    public static ArrayList<Teacher> returnTeachers() { return teachers; }

    public static Teacher returnTeacher(String id){
        for(Teacher teacher: teachers){
            if(teacher.getID().equals(id))
            {
                return teacher;
            }
        }
        System.out.println("No such ID exits");
        return null;
    }

}
