import java.util.ArrayList;

public class Sections{
    int CRN;
    String URL;
    String course;
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
    String courseFees;
    String feeTitles;
    String perCourse;
    String perCredit;
    String courseTerm;
    String startDate;
    String endDate;

    public Sections(String department, int CRN, String courseURL, String course, int secNumber, String type,
                    String title, int credits, String days, String hours, String room, String instructor,
                    int maxEnrollment, int availableSeats, String courseNote, String courseFees, String feeTitles,
                    String perCourse, String perCredit, String startDate, String endDate){

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
        setMaxEnrollment(maxEnrollment);
        setAvailableSeats(availableSeats);
        setCourseNote(courseNote);
        setCourseFees(courseFees);
        setFeeTitles(feeTitles);
        setPerCourse(perCourse);
        setPerCredit(perCredit);
        setStartDate(startDate);
        setEndDate(endDate);
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

    public String getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(String courseFees) {
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

    public static void removeDuplicateCourses(ArrayList<Sections> courseList){
        Sections tempCourse;
        for (int i=0; i<courseList.size(); i++){
            tempCourse = courseList.get(i);
            for (int j=0; j<courseList.size()-1; j++){
                if (tempCourse.getCRN() - (courseList.get(j).getCRN()) == 0){
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
