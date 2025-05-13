package GUI;
import member.*;

public class Main {
    public static void main(String[] args) {

        /*
        * Section 2: Attendance System
        * Group Members:
        * Nathnael Ayizohibel
        * Elias Chanie
        * Nahusenay Getnet
        * Lidiya Baynesagn
        *
        * */

        /*
        * Their ID of each student and teahcer created is shown in the comment
        * for example: s0 for Alice WonderLand
        *
        * */

        Student student1 = new Student("Alice Wonderland", GRADESEC.nine_b, "0911223344");//s0
        Student student2 = new Student("Bob The Builder", GRADESEC.nine_b, "0955667788");//s1
        Student student3 = new Student("Lidya", GRADESEC.nine_a, "1234567890");//s2
        Student student4 = new Student("Nahusenay", GRADESEC.nine_c, "0934567871");//s3
        Student student5 = new Student("Elias", GRADESEC.nine_c, "0953256171");//s4
        Student student6 = new Student("Ermi", GRADESEC.nine_c, "0953256171");//s5
        Student student7 = new Student("Aman", GRADESEC.nine_c, "0953256171");//s6
        Student student8 = new Student("Eyob", GRADESEC.nine_c, "0953256171");//s7
        Student student9 = new Student("Abriham", GRADESEC.nine_c, "0953256171");//s8
        Student student10 = new Student("Kalkidan", GRADESEC.nine_c, "0953256171");//s9
        Student student11 = new Student("Mahider", GRADESEC.nine_c, "0953256171");//s10
        Teacher teacher2 = new Teacher("Getch", GRADESEC.nine_a,"0900321022");//t11
        Teacher teacher3 = new Teacher("Nardos", GRADESEC.nine_c,"0900321022");//t12
        Teacher teacher4 = new Teacher("Haile", GRADESEC.ten_c,"0900321022");//t13

        LoginPanel loginPanel = new LoginPanel();
        loginPanel.setVisible(true);

    }

}
