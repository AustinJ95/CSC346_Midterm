//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


        /*
        * The URL for the landing page is https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910
        This should be a simple get page with no post data.
        */

            /*
            *
                I think I have deduced the required POST fields.  Here is what I have figured out.

                course_number: This is only used when searching for a specific course.  Typical data would be something like "csc184"
                but the field can be empty with just "" as the value.  In fact, it should be an empty string if you are searching by subject or department.

                subject: If searching by department, use "ALL" in this field.  If searching for subject, use the list found on the landing page

                dept: This is the department.  "CSMP" would be an example.  The list of departments can be discovered by looking at the html on the landing page.

                display_closed: The value should be "YES"   Otherwise closed sections are not displayed

                course_type:  Should normally be "ALL"  You could probably figure out other values by looking at the landing page.

            * */

public class Main {


    static Connection conn;

    public static void main(String[] args) throws SQLException, IOException {
        InsertApp app = new InsertApp();
        showMenu();

    }

    private static void showMenu() throws IOException {
        String addr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
        ArrayList<DeptObject> departments = getDepartments(addr);
//        InsertApp app = new InsertApp();



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
//
//            String addr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
//            ArrayList<SubjectObject> subjects = new ArrayList<>();
//
////            ArrayList<DeptObject> departments = new ArrayList<>();
////            departments = getDepartments(addr);
//


            switch (ch) {

                case 'A':
//Erase and Build the Subjects table
                    deleteSubjectFields();
                    break;
                case 'B':
//Erase and Build the Department table
//
                    deleteDeptFields();
                    break;
                case 'C':
//                    Create report for Subjects
                    printSubjectTaable();
//                this.dodge();
                    break;
                case 'D':
//                    Create report for Departments
                    printDeptTable();

//                this.exit();
                    break;
                case 'Q':
//                    Create report for Departments
//                this.exit();
                    break;

                default:
                    System.out.println("Type a letter from the menu!");
                    break;
            }

        } while (ch != 'Q');

    }


    /**
     * @author sqlitetutorial.net
     */
    public static class InsertApp {
        /**
         * Connect to the test.db database
         *
         * @return the Connection object
         */
        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:midtermdb.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        /**
         * Insert a new row into the subject table
         *
         * @param SubAbbrev
         * @param SubFullName
         */
        public void insertSubjectFields(String SubAbbrev, String SubFullName) {
            String sql = "INSERT INTO subject(SubAbbrev,SubFullName) VALUES(?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, SubAbbrev);
                pstmt.setString(2, SubFullName);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        /**
         * Insert a new row into the department table
         *
         * @param deptAbbrev
         * @param deptFullName
         */
        public void insertDeptFields(String deptAbbrev, String deptFullName) {
            String sql = "INSERT INTO department(DepAbbrev,DepFullName) VALUES(?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, deptAbbrev);
                pstmt.setString(2, deptFullName);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }




    }
    public static void deleteSubjectFields() {
        InsertApp app = new InsertApp();
        String sql = "DELETE from subject";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Subject fields deleted.");
    }

    public static void deleteDeptFields() {
        InsertApp app = new InsertApp();
        String sql = "DELETE from department";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Department fields deleted.");
    }

    //                        Print function for Departments
    public static void printSubjectTaable() throws IOException {
        InsertApp app = new InsertApp();
//        app.connect();
        String adr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
        ArrayList<SubjectObject> subjects = getSubjects(adr);
        System.out.println("Here is the Subject Table:");
        for (int i = 1; i < subjects.size(); i++) {
            SubjectObject so = subjects.get(i);
            String abbrev = so.subjectAbbrev;
            String full = so.subjFullName;
            app.insertSubjectFields(abbrev, full);
            System.out.println(abbrev + " " + full);
        }
    }

    public static void printDeptTable() throws IOException {
        InsertApp app = new InsertApp();
//        app.connect();
        String adr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";
        ArrayList<DeptObject> departments = getDepartments(adr);
        System.out.println("Here is the Dept Table:");
        for (int i = 1; i < departments.size(); i++) {
            DeptObject deptO = departments.get(i);
            String abbrev = deptO.deptAbbrev;
            String full = deptO.deptFullName;
            app.insertSubjectFields(abbrev, full);
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