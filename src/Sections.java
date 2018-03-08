import java.sql.Connection;
import java.sql.ResultSet;

public class Sections{
    int CRN;
    String URL;
    String course;
    String discipline;
    String department;
    int sectionNumber;
    String type;
    String title;
    int credits;
    String days;
    String times;
    String room;
    String instructor;
    int maxEnrollment;
    int availableSeats;
    String courseNote;
    double courseFees;
    String feeTitles;
    String perCourse;
    String perCredit;
    String courseTerm;
    String startDate;
    String endDate;

    public Sections(String department, int CRN, String courseURL, String course,int secNumber, String type,
                    String title, int credits, String days, String hours, String room, String instructor,
                    int maxEnrollment, int availableSeats, String courseNote, double courseFees, String feeTitles,
                    String perCourse, String perCredit, String courseTerm, String startDate, String endDate){

        setDepartment(department);
        setCRN(CRN);
        setCourseURL(courseURL);
        setCourse(course);
        setDiscipline();
        setSecNumber(secNumber);
        setType(type);
        setTitle(title);
        setCredits(credits);
        setDays(days);
        setHours(hours);
        setRoom(room);
        setInstructor(instructor);
        setMaxEnrollment(maxEnrollment);
        setAvailableSeats(availableSeats);
        setCourseNote(courseNote);
        setCourseFees(courseFees);
        setFeeTitles(feeTitles);
        setPerCourse(perCourse);
        setPerCredit(perCredit);
        setCourseTerm(courseTerm);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public void setDiscipline(){
        if (getCourse().length()<2){
            discipline = "";
        }else {
            discipline = getCourse().substring(0, 3);
        }
    }

    public String getDiscipline(){
        return discipline;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    public double getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(double courseFees) {
        this.courseFees = courseFees;
    }

    public String getFeeTitles() {
        return feeTitles;
    }

    public void setFeeTitles(String feeTitles) {
        this.feeTitles = feeTitles;
    }

    public String getPerCourse() {
        return perCourse;
    }

    public void setPerCourse(String perCourse) {
        this.perCourse = perCourse;
    }

    public String getPerCredit() {
        return perCredit;
    }

    public void setPerCredit(String perCredit) {
        this.perCredit = perCredit;
    }

    public String getCourseTerm() {
        return courseTerm;
    }

    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCRN() {
        return CRN;
    }

    public void setCRN(int CRN) {
        this.CRN = CRN;
    }

    public String getCourseURL() {
        return URL;
    }

    public void setCourseURL(String courseURL) {
        this.URL = courseURL;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSecNumber() {
        return sectionNumber;
    }

    public void setSecNumber(int secNumber) {
        this.sectionNumber = secNumber;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return times;
    }

    public void setHours(String hours) {
        this.times = hours;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return String.format("%s    %d     %s    %s      %s      %s", department, CRN, course, URL, title, instructor);
    }

    public static void removeBasedOnDepartmentList(String department){
        for (int i=0; i<Main.tempArrayList.size(); i++){
            if (Main.tempArrayList.get(i).getDepartment().equals(department)){
                Main.tempArrayList.remove(i);
            }
        }
    }

    public static void removeALL(){
        for (int i=0; i<Main.tempArrayList.size(); i++){
            Main.tempArrayList.remove(i);
        }
    }

    public static void printBreak() {
        for (int i=0; i<Main.tempArrayList.size(); i++) {
            String nextDiscipline = "";
            if (i<Main.tempArrayList.size()-1) {
                nextDiscipline = Main.tempArrayList.get(i+1).getDiscipline();
            }
            if (Main.tempArrayList.get(i).getDiscipline().equalsIgnoreCase(nextDiscipline)) {
                String output = String.format("CRN:(%d)   COURSE:(%s)   TITLE:(%s)   DISCIPLINE:(%s)   DEPARTMENT:(%s)   SECTION:(%d)   TYPE:(%s)   " +
                                "CREDITS:(%d)   DAYS:(%s)   TIMES:(%s)   LOCATION:(%s)   INSTRUCTOR:(%s)   MAX SEATS:(%d)   " +
                                "AVAILABLE SEATS:(%d)   COURSE NOTES:(%s)   COURSE FEES:(%1.2f)   FEE TITLES:(%s)   PER COURSE:(%S)   " +
                                "PER CREDIT:(%s)   TERM:(%s)   START DATE:(%s)   END DATE:(%s)   URL:(%s)",
                        Main.tempArrayList.get(i).CRN, Main.tempArrayList.get(i).course, Main.tempArrayList.get(i).title, Main.tempArrayList.get(i).discipline,
                        Main.tempArrayList.get(i).department, Main.tempArrayList.get(i).sectionNumber, Main.tempArrayList.get(i).type, Main.tempArrayList.get(i).credits,
                        Main.tempArrayList.get(i).days, Main.tempArrayList.get(i).times, Main.tempArrayList.get(i).room, Main.tempArrayList.get(i).instructor,
                        Main.tempArrayList.get(i).maxEnrollment, Main.tempArrayList.get(i).availableSeats, Main.tempArrayList.get(i).courseNote, Main.tempArrayList.get(i).courseFees,
                        Main.tempArrayList.get(i).feeTitles, Main.tempArrayList.get(i).perCourse, Main.tempArrayList.get(i).perCredit, Main.tempArrayList.get(i).courseTerm,
                        Main.tempArrayList.get(i).startDate, Main.tempArrayList.get(i).endDate, Main.tempArrayList.get(i).URL);
                System.out.println(output);
            } else {
                String output = String.format("CRN:(%d)   COURSE:(%s)   TITLE:(%s)   DISCIPLINE:(%s)   DEPARTMENT:(%s)   SECTION:(%d)   TYPE:(%s)   " +
                                "CREDITS:(%d)   DAYS:(%s)   TIMES:(%s)   LOCATION:(%s)   INSTRUCTOR:(%s)   MAX SEATS:(%d)   " +
                                "AVAILABLE SEATS:(%d)   COURSE NOTES:(%s)   COURSE FEES:(%1.2f)   FEE TITLES:(%s)   PER COURSE:(%S)   " +
                                "PER CREDIT:(%s)   TERM:(%s)   START DATE:(%s)   END DATE:(%s)   URL:(%s)\n",
                        Main.tempArrayList.get(i).CRN, Main.tempArrayList.get(i).course, Main.tempArrayList.get(i).title, Main.tempArrayList.get(i).discipline,
                        Main.tempArrayList.get(i).department, Main.tempArrayList.get(i).sectionNumber, Main.tempArrayList.get(i).type, Main.tempArrayList.get(i).credits,
                        Main.tempArrayList.get(i).days, Main.tempArrayList.get(i).times, Main.tempArrayList.get(i).room, Main.tempArrayList.get(i).instructor,
                        Main.tempArrayList.get(i).maxEnrollment, Main.tempArrayList.get(i).availableSeats, Main.tempArrayList.get(i).courseNote, Main.tempArrayList.get(i).courseFees,
                        Main.tempArrayList.get(i).feeTitles, Main.tempArrayList.get(i).perCourse, Main.tempArrayList.get(i).perCredit, Main.tempArrayList.get(i).courseTerm,
                        Main.tempArrayList.get(i).startDate, Main.tempArrayList.get(i).endDate, Main.tempArrayList.get(i).URL);
                System.out.println(output);
            }
        }
    }
}
