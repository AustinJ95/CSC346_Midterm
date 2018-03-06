import java.util.ArrayList;

public class Sections{
    String department;
    String CRN;
    String courseURL;
    String course;
    String secNumber;
    String type;
    String title;
    String credits;
    String days;
    String hours;
    String room;
    String instuctor;

    public Sections(String department, String CRN, String courseURL, String course, String secNumber, String type, String title, String credits, String days, String hours, String room, String instructor){
        setDepartment(department);
        setCRN(CRN);
        setCourseURL(courseURL);
        setCourse(course);
        setSecNumber(secNumber);
        setType(type);
        setTitle(title);
        setCredits(credits);
        setDays(days);
        setHours(hours);
        setRoom(room);
        setInstructor(instructor);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCRN() {
        return CRN;
    }

    public void setCRN(String CRN) {
        this.CRN = CRN;
    }

    public String getCourseURL() {
        return courseURL;
    }

    public void setCourseURL(String courseURL) {
        this.courseURL = courseURL;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSecNumber() {
        return secNumber;
    }

    public void setSecNumber(String secNumber) {
        this.secNumber = secNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getInstructor() {
        return instuctor;
    }

    public void setInstructor(String instructor) {
        this.instuctor = instructor;
    }

    @Override
    public String toString() {
        return String.format("%s    %s     %s    %s      %s      %s", department, CRN, course, secNumber, title, instuctor);
    }

    public static void removeDuplicateCourses(ArrayList<Sections> courseList){
        Sections tempCourse;
        for (int i=0; i<courseList.size(); i++){
            tempCourse = courseList.get(i);
            for (int j=0; j<courseList.size()-1; j++){
                if (tempCourse.getCRN().compareTo(courseList.get(j).getCRN()) == 0){
                    courseList.remove(j);
                }
            }
        }
    }

    public int compareTo(Sections other){
        int result;
        if (getCourse().compareTo(other.getCourse()) > 0){
            result = -1;
        } else if (getCourse().compareTo(other.getCourse()) < 0){
            result = 1;
        } else
            result = 0;
        return result;
    }
}
