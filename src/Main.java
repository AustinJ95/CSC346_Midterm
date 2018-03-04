import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

//landing page url="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910"

public class Main {
    static ArrayList<Sections> courseList = new ArrayList<>();
    static ArrayList<String> departments = new ArrayList<>();
    static ArrayList<Discipline> disciplines = new ArrayList<>();
    static ArrayList<Sections> tempArrayList = new ArrayList<>();
    static final String BASEURL = "https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910";
    static SQLITE sqlite = new SQLITE();

    public static void main(String[] args) {
        sqlite.connectToDB();
        addDisciplines();
        addDepartments();
        removeCoursesBasedOnDepartmentDB("ALL");//clears table
        updateDepartment("ALL");//scrapes all departments or individual departments NOTE: Takes a long time to run
        //getCourses("ART", courseList); //tests individual departments
        //System.out.println(courseList.get(0));//prints out specified entry in courseList
        //printOUT();//prints first 100 entries ArrayList courseList out
        sqlite.closeDB();
    }

    public static void insertCourses(ArrayList<Sections> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                int CRN = list.get(i).getCRN();
                String URL = list.get(i).getURL();
                String course = list.get(i).getCourse();
                String discipline = list.get(i).getDisciplineFull();
                String department = list.get(i).getDepartment();
                int sectionNumber = list.get(i).getSectionNumber();
                String type = list.get(i).getType();
                String title = list.get(i).getTitle();
                int credits = list.get(i).getCredits();
                String days = list.get(i).getDays();
                String times = list.get(i).getTimes();
                String room = list.get(i).getRoom();
                String instructor = list.get(i).getInstructor();
                int maxEnrollment = list.get(i).getMaxEnrollment();
                int availableSeats = list.get(i).getAvailableSeats();
                String courseNote = list.get(i).getCourseNote();
                String courseFees = list.get(i).getCourseFees();
                String feeTitles = list.get(i).getFeeTitles();
                String perCourse = list.get(i).getPerCourse();
                String perCredit = list.get(i).getPerCredit();
                String courseTerm = list.get(i).getCourseTerm();
                String startDate = list.get(i).getStartDate();
                String endDate = list.get(i).getEndDate();

                String sql1 = "INSERT or REPLACE INTO COURSES(" +
                        "CRN," +
                        "COURSE," +
                        "COURSE_NAME," +
                        "DISCIPLINE," +
                        "DEPARTMENT," +
                        "SECTION_NUMBER," +
                        "CLASS_TYPE," +
                        "CREDITS," +
                        "DAYS," +
                        "TIMES," +
                        "LOCATION," +
                        "INSTRUCTOR," +
                        "MAX_INROLLMENT," +
                        "AVAILABLE_SEATS," +
                        "COURSE_NOTE," +
                        "COURSE_FEES," +
                        "FEE_TITLES," +
                        "PER_COURSE," +
                        "PER_CREDIT," +
                        "COURSE_TERM," +
                        "START_DATE," +
                        "END_DATE," +
                        "COURSE_URL) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement add = sqlite.getConn().prepareStatement(sql1);
                add.setInt(1, CRN);
                add.setString(2, course);
                add.setString(3, title);
                add.setString(4, discipline);
                add.setString(5, department);
                add.setInt(6, sectionNumber);
                add.setString(7, type);
                add.setInt(8, credits);
                add.setString(9, days);
                add.setString(10, times);
                add.setString(11, room);
                add.setString(12, instructor);
                add.setInt(13, maxEnrollment);
                add.setInt(14, availableSeats);
                add.setString(15, courseNote);
                add.setString(16, courseFees);
                add.setString(17, feeTitles);
                add.setString(18, perCourse);
                add.setString(19, perCredit);
                add.setString(20, courseTerm);
                add.setString(21, startDate);
                add.setString(22, endDate);
                add.setString(23, URL);
                add.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCoursesBasedOnDepartmentDB(String department){
        try {
            if (department.equals("ALL")){
                String deleteSQL = "DELETE FROM COURSES";
                PreparedStatement delete = sqlite.getConn().prepareStatement(deleteSQL);
                delete.executeUpdate();
            }else {
                String deleteSQL = "DELETE FROM COURSES WHERE DEPARTMENT = ?";
                PreparedStatement delete = sqlite.getConn().prepareStatement(deleteSQL);
                delete.setString(1, department);
                delete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            return doc;
        }
    }

    public static void getCourses(String department, ArrayList<Sections> list) {
        int CRN = 0;
        String URL = "";
        String course = "";
        int sectionNumber = 0;
        String type = "";
        String title = "";
        int credits = 0;
        String days = "";
        String times = "";
        String room = "";
        String instructor = "";
        int maxEnrollment = 0;
        int availableSeats = 0;
        String courseNote = "";
        String courseFees = "";
        String feeTitles = "";
        String perCourse = "";
        String perCredit = "";
        String courseTerm = "";
        String startDate = "";
        String endDate = "";

        Document doc = getPost(department);
        Elements resultTable = doc.select("div#maincontent > table.results");
        Elements courseGeneral = resultTable.select("tr");
        Elements courseSpecific = resultTable.select("tr.detail_row");
        Elements courseGeneralException = resultTable.select("tr.list_row + tr.list_row");

        System.out.printf("Scraping based on Department:%s\n", department);
        for (Element rowGeneral : courseGeneral) {
            String className = rowGeneral.attr("class");
            Elements tdGeneral = rowGeneral.select("td");
            if (className.equals("list_row")) {
                if (tdGeneral.size()==10) {
                    CRN = Integer.parseInt(tdGeneral.get(0).text().trim());
                    URL = "https://aps2.missouriwestern.edu/schedule/" + tdGeneral.select("a").first().attr("href");
                    course = tdGeneral.get(1).text().trim();
                    sectionNumber = Integer.parseInt(tdGeneral.get(2).text().trim());
                    type = tdGeneral.get(3).text().trim();
                    title = tdGeneral.get(4).text();
                    credits = Integer.parseInt(tdGeneral.get(5).text().substring(0, 1).trim());
                    days = tdGeneral.get(6).text().trim();
                    times = tdGeneral.get(7).text().trim();
                    room = tdGeneral.get(8).text();
                    instructor = tdGeneral.get(9).text();
                }
                else{
                    days = days + "\n" + tdGeneral.get(1).text().trim();
                    times = times + "\n" + tdGeneral.get(2).text().trim();
                    room = room + "\n" + tdGeneral.get(3).text();
                }
            }if (!(CRN==0)) {
                Sections section = new Sections(department, CRN, URL, course, sectionNumber, type, title, credits,
                        days, times, room, instructor, maxEnrollment, availableSeats, courseNote, courseFees,
                        feeTitles, perCourse, perCredit, startDate, endDate);
                list.add(section);
            }
        }
    }

    public static void updateDepartment(String department) {
        if (department.equals("ALL")){
            Sections.removeALL();
            removeCoursesBasedOnDepartmentDB(department);
            for (String dpt: departments){
                getCourses(dpt, tempArrayList);
            }
        } else {
            Sections.removeBasedOnDepartmentList(department);
            removeCoursesBasedOnDepartmentDB(department);
            getCourses(department, tempArrayList);
        }
        insertCourses(tempArrayList);
        Sections.removeALL();
    }

    public static void addDepartments() {
        /*String getDepartments = "";
        try {
            Connection conn = sqlite.getConn();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(getDepartments);
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }*/
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

    public static void addDisciplines(){
        String getDepartments = "SELECT SubAbbrev, SubFullName FROM subject";
        try {
            Connection conn = sqlite.getConn();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(getDepartments);
            while (rs.next()){
                Discipline discipline = new Discipline(rs.getString("SubAbbrev"), rs.getString("SubFullName"));
                disciplines.add(discipline);
            }
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
