//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


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
    static ResultSet rs;
    static Statement stmt;

    public static void main(String[] args) throws SQLException, IOException {
        String addr = "https://aps2.missouriwestern.edu/schedule/?tck=201830";

        ArrayList<SubjectObject> subjects = getSubjects(addr);


//        System.out.println("The first :" + subjects.get(0).toString());

//        System.out.println("The first :" + String.valueOf(subjects.get(0)));


        getSubjects("https://aps2.missouriwestern.edu/schedule/?tck=201830");

        InsertApp app = new InsertApp();

        for (int i = 0; i < subjects.size(); i++) {
//            System.out.println("An object :" +subjects.get(i));
            SubjectObject so = subjects.get(i);
            String abbrev = so.subjectAbbrev;
            String full = so.subjFullName;
            app.insert(abbrev,full);
        }
    }
        /**
         *
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
            public void insert(String SubAbbrev, String SubFullName) {
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

//                System.out.println("In getSubjects: "+ subjectAbbrev + " " + subjectFull.text());
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

                System.out.println(deptAbbrev + " " + deptFull.text());
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
    public static void insertDB(ArrayList<SubjectObject> d) throws SQLException {
        String insert = " insert into Subject (SubAbbrev, SubFullName)" + " values (?, ?)";

//        String insert = "insert into Subject values(?,?)";
        PreparedStatement ps = conn.prepareStatement(insert);
        for (SubjectObject object : d) {
            ps.setString(1, object.subjectAbbrev); // resource
            ps.setString(2, object.subjFullName); // activity
            ps.addBatch();
        }
//        ps.executeBatch();
//        ps.execute();
        System.out.println("Helloe");
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