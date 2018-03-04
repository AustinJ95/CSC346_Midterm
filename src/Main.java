import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

//landing page url="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910"
//TODO: retrieve information from the detail_row class of <tr> in the schedule table
//TODO: sort sections based on department
//COMPLETED: remove duplicate courses from the courseList ArrayList
//TODO: add courses to the previously created database
//TODO: create method to update courseList based on department

public class Main {
    static ArrayList<Sections> courseList = new ArrayList<>();
    static ArrayList<String> departments = new ArrayList<>();
    static final String BASEURL = "https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910";
    static String DBFileName = "midtermdb.db";
    static Connection conn;

    public static void main(String[] args) {
        getCourses("ART"); //tests individual departments
        //System.out.println(courseList.get(0));//prints out specified number of entries in courseList
        //getAllCourses();//scrapes all departments NOTE: Takes a long time to run
        insertCourses(courseList);
        //printOUT();//prints first 100 entries ArrayList courseList out
    }

    public static void connectToDB() {
        String DBPath = "jdbc:sqlite:" + DBFileName;
        conn = null;
        try {
            conn = DriverManager.getConnection(DBPath);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void insertCourses(ArrayList<Sections> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                int CRN = list.get(i).getCRN();
                String URL = list.get(i).getURL();
                String course = list.get(i).getCourse();
                String department = list.get(i).getDepartment();
                int sectionNumber = list.get(i).getSectionNumber();
                String type = list.get(i).getType();
                String title = courseList.get(i).getTitle();
                int credits = courseList.get(i).getCredits();
                String days = courseList.get(i).getDays();
                String times = courseList.get(i).getTimes();
                String room = courseList.get(i).getRoom();
                String instructor = courseList.get(i).getInstructor();
                int maxEnrollment = courseList.get(i).getMaxEnrollment();
                int availableSeats = courseList.get(i).getAvailableSeats();
                String courseNote = courseList.get(i).getCourseNote();
                String courseFees = courseList.get(i).getCourseFees();
                String feeTitles = courseList.get(i).getFeeTitles();
                String perCourse = courseList.get(i).getPerCourse();
                String perCredit = courseList.get(i).getPerCredit();
                String courseTerm = courseList.get(i).getCourseTerm();
                String startDate = courseList.get(i).getStartDate();
                String endDate = courseList.get(i).getEndDate();

                String sql = "INSERT INTO COURSES(" +
                        "CRN," +
                        "COURSE," +
                        "COURSE_NAME," +
                        "DEPARTMENT," +
                        "SECTION_NUMBER," +
                        "CLASS_TYPE," +
                        "CREDITS," +
                        "DAYS," +
                        "TIMES," +
                        "LOCATION," +
                        "INSTRUCTOR," +
                        "MAX_INROLLMENT," +
                        "SEATS_AVAILABLE," +
                        "COURSE_NOTE," +
                        "COURSE_FEES," +
                        "FEE_TITLES," +
                        "PER_COURSE," +
                        "PER_CREDIT," +
                        "COURSE_TERM," +
                        "START_DATE," +
                        "END_DATE," +
                        "COURSE_URL) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement prpStmt = conn.prepareStatement(sql);
                prpStmt.setInt(1, CRN);
                prpStmt.setString(2, course);
                prpStmt.setString(3, title);
                prpStmt.setString(4, department);
                prpStmt.setInt(5, sectionNumber);
                prpStmt.setString(6, type);
                prpStmt.setInt(6, credits);
                prpStmt.setString(6, days);
                prpStmt.setString(6, times);
                prpStmt.setString(6, room);
                prpStmt.setString(6, instructor);
                prpStmt.setInt(6, maxEnrollment);
                prpStmt.setInt(6, availableSeats);
                prpStmt.setString(6, courseNote);
                prpStmt.setString(6, courseFees);
                prpStmt.setString(6, feeTitles);
                prpStmt.setString(6, perCourse);
                prpStmt.setString(6, perCredit);
                prpStmt.setString(6, courseTerm);
                prpStmt.setString(6, startDate);
                prpStmt.setString(6, endDate);
                prpStmt.setString(6, URL);
                prpStmt.executeUpdate();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllCourses() {
        addDepartments();
        for (String dept : departments) {
            getCourses(dept);
        }
        Sections.removeDuplicateCourses(courseList);
    }

    public static void printOUT() {
        for (int i = 0; i < 100; i++) {
            System.out.println(courseList.get(i));
        }
    }

    public static Document getPost(String department) {
        Document doc = null;
        try {
            Response response = Jsoup.connect(BASEURL)
                    .userAgent("Mozilla")
                    .timeout(2 * 60 * 1000)
                    .method(Method.POST)
                    .data("course_number", "")
                    .data("subject", "ALL")
                    .data("department", department)
                    .data("display_closed", "YES")
                    .data("course_type", "ALL")
                    .followRedirects(true)
                    .execute();
            doc = response.parse();
            System.out.println("Connected to: https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910.");
            System.out.printf("Please do not close. Program is parsing using %s.\n", department);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            return doc;
        }
    }

    public static void getCourses(String department) {
        int CRN;
        String URL;
        String course;
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

        Document doc = getPost(department);
        Elements resultTable = doc.select("div#maincontent > table.results");
        Elements courseGeneral = resultTable.select("tr.list_row");
        Elements courseSpecific = resultTable.select("tr.detail_row");

        for (Element row : courseGeneral) {
            Elements td = row.select("td");
            if (td.size() == 10) {
                CRN = Integer.parseInt(td.get(0).text());
                URL = "https://aps2.missouriwestern.edu/schedule/" + td.select("a").first().attr("href");
                course = td.get(1).text();
                sectionNumber = Integer.parseInt(td.get(2).text());
                type = td.get(3).text();
                title = td.get(4).text();
                credits = Integer.parseInt(td.get(5).text());
                days = td.get(6).text();
                times = td.get(7).text();
                room = td.get(8).text();
                instructor = td.get(9).text();

                Sections section = new Sections(department, CRN, URL, course, sectionNumber, type, title, credits,
                        days, times, room, instructor, 0, 0, null, null,
                        null, null, null, null, null);
                courseList.add(section);
            }
        }
        for (Element row : courseSpecific) {
            Elements span = row.select("span");

        }
    }

    public static void updateDepartment(ArrayList<Sections> courseList) {

        Sections.removeDuplicateCourses(courseList);
    }

    public static void addDepartments() {
        try {

        } catch (Exception e) {

        }
        departments.add("AF");
        departments.add("ART");
        departments.add("BIO");
        departments.add("BUS");
        departments.add("CHE");
        departments.add("CST");
        departments.add("CSMP");
        departments.add("CJLS");
        departments.add("EPSS");
        departments.add("EDU");
        departments.add("ET");
        departments.add("EFLJ");
        departments.add("GS");
        departments.add("HPER");
        departments.add("HPG");
        departments.add("HON");
        departments.add("MIL");
        departments.add("MUS");
        departments.add("NUR");
        departments.add("PSY");
        departments.add("FINE");
        departments.add("CON");
    }
}
