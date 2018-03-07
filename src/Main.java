import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;
import org.jsoup.Connection.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Department> departments = new ArrayList<>();
    static ArrayList<Discipline> disciplines = new ArrayList<>();
    static ArrayList<Sections> tempArrayList = new ArrayList<>();
    static final String BASEURL = "https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910";
    static SQLITE sqlite = new SQLITE();

    public static void main(String[] args) throws SQLException, IOException {
        sqlite.connectToDB();
        showMenu();
        addDisciplines();
        addDepartments();
        removeCoursesBasedOnDepartmentDB("ALL");//clears table
        updateDepartment("ART");//scrapes all departments or individual departments NOTE: Takes a long time to run if using "ALL"
        sqlite.closeDB();
    }

    private static void showMenu() throws IOException {
        /*
        * Keep in mind that you are working with a database.  Presumably, someone has already populated the departments and disciplines.
        * If not, it will have to be done.  Items A and B are not about loading the tables into memory.
        * They are about rebuilding the tables in the database.

            You should not be writing the full logic to do the table building in the case statement.
            The case statements for A and B should call methods that rebuild the database tables.
            This doesn't just give you cleaner code, it is actually important for program operation.
            For example, if someone selects option E and the department and discipline tables are empty,
            then a user selecting E will effectively also be calling A and B.

            I don't mean doing a create statement.  It is fine to do that in the GUI.
            I meant clearing the records and then adding them back in.
            Building the tables should only need to happen if there is a change in departments or disciplines.
            Items A and B would only need to be done maybe once a semester, or if you suspected that a department or discipline had changed.
            For example, if two departments had been combined or a department changed its name, then the department table would need to updated.


        * */
//        Please remember to implement this in a method.
// the case 'E': code should be little more than a call to the method followed by a break.
// Also, if the method used for E finds another table empty it would need to call the methods to build the tables before it can produce the report.
        char ch;
        do {
            System.out.println("-- Actions --");
            System.out.println(
                    "Select an option: \n" +
                            "  A) Erase and Build Subjects table\n" +
                            "  B) Erase and build the Department table\n" +
                            "  C) Print Subjects table\n" +
                            "  D) Print Departments table\n" +
                            "  E) Print the report of disciplines by Department\n" +
                            "  G) Erase and build sections data (Will be prompted for the department)\n" +
                            "  H) Print a simple listing of all sections by department or by discipline (Will be prompted)\n" +
                            "  I) Print faculty and faculty schedules  by department\n" +
                            "  J) Print control-break section report for a department  (Will be prompted for the department)\n" +
                            "  K) Produce the control-break output\n" +
                            "  L) A statement about how much Person B hates the web developer that designed the sections layout\n" +
                            "  Q) Quit\n "
            );

            Scanner input = new Scanner(System.in);

            String s = input.next().toUpperCase().trim();
            ch = (s.length() > 0) ? s.charAt(0) : 'x';
            input.nextLine();

            switch (ch) {

                case 'A':
                    //Erase and Build the Subjects table
                    deleteSubjectFields();
                    printSubjectTable();
                    break;
                case 'B':
                    //Erase and Build the Department table
                    deleteDeptFields();
                    printDeptTable();
                    break;
                case 'C':
                    //Create report for Subjects
                    printSubjectTable();
                    break;
                case 'D':
                    //Create report for Departments
                    printDeptTable();
                    break;
                case 'H':
                    //addDepartments();
                    //addDisciplines();
                    //updateDepartment("ART");break;
                case 'Q':
                    //Create report for Departments
                    //this.exit();
                    break;

                default:
                    System.out.println("Type a letter from the menu!");
                    break;
            }

        } while (ch != 'Q');

    }

    public static void insertSubjectFields(String SubAbbrev, String SubFullName) {
        String sql = "INSERT INTO subject(SubAbbrev,SubFullName) VALUES(?,?)";

        try {
            Connection conn = sqlite.conn;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, SubAbbrev);
            pstmt.setString(2, SubFullName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //landing page url="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910"
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
                double courseFees = list.get(i).getCourseFees();
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
                add.setDouble(16, courseFees);
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

    public static void removeCoursesBasedOnDepartmentDB(String department) {
        try {
            if (department.equalsIgnoreCase("ALL")) {
                String deleteSQL = "DELETE FROM COURSES";
                PreparedStatement delete = sqlite.getConn().prepareStatement(deleteSQL);
                delete.executeUpdate();
            } else {
                String deleteSQL = "DELETE FROM COURSES WHERE DEPARTMENT = ?";
                PreparedStatement delete = sqlite.getConn().prepareStatement(deleteSQL);
                delete.setString(1, department.toUpperCase());
                delete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                    .data("department", department.toUpperCase())
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

    public static void insertDeptFields(String deptAbbrev, String deptFullName) {
        String sql = "INSERT INTO department(DepAbbrev,DepFullName) VALUES(?,?)";

        try {
            Connection conn = sqlite.conn;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deptAbbrev);
            pstmt.setString(2, deptFullName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteSubjectFields() {
        String sql = "DELETE from subject";

        try {
            Connection conn = sqlite.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Subject fields deleted.");
    }

    public static void deleteDeptFields() {
        String sql = "DELETE from department";

        try (Connection conn = sqlite.conn;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Department fields deleted.");
    }

    //Print function for Departments
    public static void printSubjectTable() throws IOException {
        String adr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
        ArrayList<SubjectObject> subjects = getSubjects(adr);
        System.out.println("Here is the Subject Table:");
        for (int i = 1; i < subjects.size(); i++) {
            SubjectObject so = subjects.get(i);
            String abbrev = so.subjectAbbrev;
            String full = so.subjFullName;
            insertSubjectFields(abbrev, full);
            System.out.println(abbrev + " " + full);
        }
    }

    public static void printDeptTable() throws IOException {
        String adr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
        ArrayList<DeptObject> departments = getDepartments(adr);
        System.out.println("Here is the Dept Table:");
        for (int i = 1; i < departments.size(); i++) {
            DeptObject deptO = departments.get(i);
            String abbrev = deptO.deptAbbrev;
            String full = deptO.deptFullName;
            insertDeptFields(abbrev, full);
            System.out.println(abbrev + " " + full);
        }
    }

    public static ArrayList<SubjectObject> getSubjects(String addr) throws IOException {
        ArrayList<SubjectObject> subjects = new ArrayList<>();
        try {
            Document document = Jsoup.connect(addr).get();
            int i = 1;
            while (true) {

                Element subjectFull = document.select("#subject > option:nth-child(" + i + ")").first();
                if (subjectFull == null) break;
                String subjectAbbrev = subjectFull.attr("value");

                SubjectObject newSubjectObject = new SubjectObject(subjectAbbrev, subjectFull.text());
                subjects.add(newSubjectObject);
                //Add size.text() to your list
//                subjects.Add(subjectFull);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static ArrayList<DeptObject> getDepartments(String addr) throws IOException {
        ArrayList<DeptObject> departments = new ArrayList<>();
        try {
            Document document = Jsoup.connect(addr).get();
            int i = 2;
            while (true) {

                Element deptFull = document.select("#department > option:nth-child(" + i + ")").first();
                if (deptFull == null) break;
                String deptAbbrev = deptFull.attr("value");

//                System.out.println(deptAbbrev + " " + deptFull.text());
                DeptObject newSubjectObject = new DeptObject(deptAbbrev, deptFull.text());
                departments.add(newSubjectObject);
                //Add size.text() to your list

                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departments;
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
        double courseFees = 0;
        String feeTitles = "";
        String perCourse = "";
        String perCredit = "";
        String courseTerm = "";
        String startDate = "";
        String endDate = "";

        Document doc = getPost(department);
        Elements resultTable = doc.select("div#maincontent > table.results");
        Elements courseGeneral = resultTable.select("tr");

        System.out.printf("Scraping based on Department:%s\n", department);
        for (Element rowGeneral : courseGeneral) {
            String className = rowGeneral.attr("class");
            Elements tdGeneral = rowGeneral.select("td");
            if (className.equals("list_row")) {
                if (tdGeneral.size() == 10) {
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
                } else {
                    days = days + "\n" + tdGeneral.get(1).text().trim();
                    times = times + "\n" + tdGeneral.get(2).text().trim();
                    room = room + "\n" + tdGeneral.get(3).text();
                }
            }
            if (className.equals("detail_row")) {
                int startIndexOfMaxSeats = tdGeneral.select("span.course_seats").text().indexOf(":") + 2;
                int endIndexOfMaxSeats = tdGeneral.select("span.course_seats").text().indexOf("Section Seats Available:") - 1;
                int startIndexOfAvailableSeats = tdGeneral.select("span.course_seats").text().lastIndexOf(":") + 2;
                maxEnrollment = Integer.parseInt(tdGeneral.select("span.course_seats").text().substring(startIndexOfMaxSeats, endIndexOfMaxSeats));
                availableSeats = Integer.parseInt(tdGeneral.select("span.course_seats").text().substring(startIndexOfAvailableSeats));

                Elements fees = tdGeneral.select("span.course_fees");
                for (int i = 0; i < fees.size(); i++) {
                    int indexOfColon = fees.get(i).text().indexOf(':');
                    int indexOfFee = fees.get(i).text().indexOf("Fee") + 4;
                    int indexOfFlat = fees.get(i).text().indexOf("FLAT");
                    int indexOfCred = fees.get(i).text().indexOf("CRED");
                    if (fees.get(i).text().contains("Flat Fee")) {
                        perCourse = "Yes";
                        if (feeTitles.length() == 0) {
                            feeTitles = fees.get(i).text().substring(indexOfColon + 2, indexOfFlat + 4);
                        } else {
                            feeTitles = feeTitles + "\n" + fees.get(i).text().substring(indexOfColon + 2, indexOfFlat + 4);//.substring(fee.text().indexOf(": ") + 1, fee.text().indexOf("&nbsp"));
                        }
                        courseFees = courseFees + Double.parseDouble(fees.get(i).text().substring(indexOfFee, indexOfFlat - 1));
                    }
                    if (fees.get(i).text().contains("per Credit Hour fee")) {
                        perCredit = "Yes";
                        if (feeTitles.length() == 0) {
                            feeTitles = fees.get(i).text().substring(indexOfColon + 2, indexOfCred + 4);
                        } else {
                            feeTitles = feeTitles + "\n" + fees.get(i).text().substring(indexOfColon + 2, indexOfCred + 4);//.substring(fee.text().indexOf(": "), fee.text().indexOf("&nbsp"));
                        }
                        courseFees = courseFees + (credits * (Double.parseDouble(fees.get(i).text().substring(indexOfFee, indexOfCred - 1))));
                    }
                    if (!(fees.get(i).text().contains("Flat Fee")) && !(fees.get(i).text().contains("per Credit Hour fee"))) {
                        perCourse = "No";
                        perCredit = "No";
                        feeTitles = "None";
                    }
                }

                courseTerm = tdGeneral.select("span.course_term").text();
                startDate = tdGeneral.select("span.course_begins").text().trim();
                endDate = tdGeneral.select("span.course_ends").text().trim();
            }
            if (!(CRN == 0)) {
                Sections section = new Sections(department, CRN, URL, course, sectionNumber, type, title, credits,
                        days, times, room, instructor, maxEnrollment, availableSeats, courseNote, courseFees,
                        feeTitles, perCourse, perCredit, courseTerm, startDate, endDate);
                list.add(section);
                feeTitles = "";
                courseFees = 0.0;
            }
        }
    }

    public static void updateDepartment(String department) {
        if (department.equalsIgnoreCase("ALL")) {
            Sections.removeALL();
            removeCoursesBasedOnDepartmentDB(department);
            for (Department dpt : departments) {
                getCourses(dpt.getDepartmentAbbrev(), tempArrayList);
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
        String getDepartments = "SELECT DepAbbrev, DepFullName FROM department";
        try {
            Connection conn = sqlite.conn;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(getDepartments);
            while (rs.next()){
                Department department = new Department(rs.getString("DepAbbrev"), rs.getString("DepFullName"));
                departments.add(department);
            }
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
        /*departments.add("AF");
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
        departments.add("CON");*/
    }

    public static void addDisciplines() {
        String getDisciplines = "SELECT SubAbbrev, SubFullName FROM subject";
        try {
            Connection conn = sqlite.conn;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(getDisciplines);
            while (rs.next()) {
                Discipline discipline = new Discipline(rs.getString("SubAbbrev"), rs.getString("SubFullName"));
                disciplines.add(discipline);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

class SubjectObject {
    String subjectAbbrev;
    String subjFullName;

    public SubjectObject(String subjectAbbrevFromHTML, String subjFullFromHTML) {
        this.subjectAbbrev = subjectAbbrevFromHTML;
        this.subjFullName = subjFullFromHTML;

    }


    @Override
    public String toString() {
        return "SubjectObject{" +
                "subjectAbbrev='" + subjectAbbrev + '\'' +
                ", subjFullName='" + subjFullName + '\'' +
                '}';
    }
}

class DeptObject {
    String deptAbbrev;
    String deptFullName;

    public DeptObject(String subjectAbbrevFromHTML, String subjFullFromHTML) {
        this.deptAbbrev = subjectAbbrevFromHTML;
        this.deptFullName = subjFullFromHTML;

    }

}