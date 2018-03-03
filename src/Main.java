import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;
import java.io.IOException;
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

    public static void main(String[] args){
        //getCourses("ART"); //tests individual departments
        //System.out.println(courseList.get(0));//prints out specified number of entries in courseList
        getAllCourses();//scrapes all departments NOTE: Takes a long time to run
        printOUT();//prints first 100 entries ArrayList courseList out
    }

    public static void getAllCourses(){
        addDepartments();
        for (String dept: departments) {
            getSections(dept);
        }
        Sections.removeDuplicateCourses(courseList);
    }

    public static void printOUT(){
        for (int i=0; i<100; i++){
            System.out.println(courseList.get(i));
        }
    }

    public static Document getPost(String department){
        Document doc = null;
        try{
            Response response = Jsoup.connect(BASEURL)
                    .userAgent("Mozilla")
                    .timeout(2*60*1000)
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
        }
        finally {
            return doc;
        }
    }

    public static void getSections(String department) {
        String CRN;
        String URL;
        String course;
        String sectionNumber;
        String type;
        String title;
        String credits;
        String days;
        String times;
        String room;
        String instructor;

        Document doc = getPost(department);
        Elements resultTable = doc.select("div#maincontent > table.results");
        Elements courseGeneral = resultTable.select("tr.list_row");
        Elements courseSpecific = resultTable.select("tr.detail_row");

        for (Element row : courseGeneral) {
            Elements td = row.select("td");
            if (td.size()==10) {
                CRN = td.get(0).text();
                URL = "https://aps2.missouriwestern.edu/schedule/" + td.select("a").first().attr("href");
                course = td.get(1).text();
                sectionNumber = td.get(2).text();
                type = td.get(3).text();
                title = td.get(4).text();
                credits = td.get(5).text();
                days = td.get(6).text();
                times = td.get(7).text();
                room = td.get(8).text();
                instructor = td.get(9).text();

                Sections section = new Sections(department, CRN, URL, course, sectionNumber, type, title, credits, days, times, room, instructor);
                courseList.add(section);
            }
        }
    }

/*    <tr class="detail_row">
			<td colspan="10" class="detail_cell">
				<span class="course_enrollment">

					<span class="course_seats">
						<!--Seats Available:  of 22-->
    Maximum Enrollment: 22<br />
    Section Seats Available: 22<br />

					</span>

				</span>

				<span class="course_messages">
					 <span class="course_fees">ADDITIONAL FEE: Developmental Reading Fee    90.00 FLAT&nbsp;&nbsp;(Flat Fee)</span>
				</span>
				<span class="course_term">
    Full Term
				</span>
				<span class="course_dates">
					<span class="course_begins">Course Begins: 8/27/2018</span>
					<span class="course_ends">Course Ends: 12/14/2018</span>
				</span>
			</td>
		</tr>*/

    public static void updateDepartment(ArrayList<Sections> courseList){

        Sections.removeDuplicateCourses(courseList);
    }

    public static void addDepartments(){
        try {

        } catch (Exception e){

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
