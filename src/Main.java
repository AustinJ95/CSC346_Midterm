import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
//Hello
    public static void main(String[] args)  {
        try {

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
            getSubjects("https://aps2.missouriwestern.edu/schedule/?tck=201830");
            getDepartments("https://aps2.missouriwestern.edu/schedule/?tck=201830");
//            usingGet("https://aps2.missouriwestern.edu/schedule/?tck=201830");
//            usingPost("https://aps2.missouriwestern.edu/schedule/?tck=201830");
//            usingGet("https://webservices.missouriwestern.edu/users/noynaert/csc346/target.php?firstName=tommy&car=truck");
            usingPost("https://webservices.missouriwestern.edu/users/noynaert/csc346/target.php?firstName=tommy&car=truck");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
    public static void usingGet(String website) throws IOException {
        Document doc = Jsoup.connect(website).get();
        System.out.println(doc);
    }
    public static void usingPost(String website) throws IOException{
        Connection.Response response = Jsoup.connect(website)
                .method(Connection.Method.POST)
                .timeout(10 * 1000)
                .data("firstName","Sally")
                .data("favoriteMovie","Titanic")
                .data("course_number","")
                .followRedirects(true)
                .execute();
        System.out.println("Connected");
        System.out.println(response.body());

    }

    public static ArrayList<Element> getSubjects(String addr) {
        ArrayList<Element> subjects = null;
        try {
            Document document = Jsoup.connect(addr).get();
            int i = 2;
            while (true) {

                Element subjectFull = document.select("#subject > option:nth-child(" + i + ")").first();
                String subjectAbbrev = subjectFull.attr("value");

                if (subjectFull == null) break;
                System.out.println(subjectAbbrev + " " + subjectFull.text());
                //Add size.text() to your list
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static ArrayList<Element> getDepartments(String addr) {
        ArrayList<Element> departments = null;
        try {
            Document document = Jsoup.connect(addr).get();
            int i = 2;
            while (true) {

                Element deptFull = document.select("#department > option:nth-child(" + i + ")").first();
                String deptAbbrev = deptFull.attr("value");

                if (deptFull == null) break;
                System.out.println(deptAbbrev + " " + deptFull.text());
                //Add size.text() to your list
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departments;
    }

}