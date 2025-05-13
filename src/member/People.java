package member;

public abstract class People {
    static int idCount=0;
    String name;
    int idNum;
    ROLE role;
    GRADESEC gradeSec;
    String phone;
    public People(String name, GRADESEC gradeSec, String phone)
    {
        this.name = name;
        this.gradeSec = gradeSec;
        this.phone=phone;
        this.idNum = generateId();
    }
    int generateId()
    {
        return idCount++;
    }
}
