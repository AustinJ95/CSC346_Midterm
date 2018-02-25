import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;
import java.io.IOException;
import java.util.ArrayList;

//landing page url="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910"

public class Main {
    static ArrayList<Sections> courseList = new ArrayList<>();
    static ArrayList<String> departments = new ArrayList<>();

    public static void main(String[] args){
        addDepartments();
        for (String dept: departments) {
            getSections(dept);
        }
        for (Sections section: courseList){
            System.out.println(section);
        }
    }

    public static Document getPost(String department){
        Document doc = null;
        try{
            Response response = Jsoup.connect("https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910")
                    .userAgent("Mozilla")
                    .timeout(60*1000)
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
            e.printStackTrace();
        }
        finally {
            return doc;
        }
    }

    public static void getSections(String department) {
        Document doc = getPost(department);
        Elements resultTable = doc.select("div#maincontent > table.results");
        Elements courseGeneral = resultTable.select("tr.list_row");
        Elements courseSpecific = resultTable.select("tr.detail_row");

        for (Element row : courseGeneral) {
            Elements td = row.select("td");
            if (td.size()==10) {
                String crn = td.get(0).text();
                //String courseURL = td.get(1).text();
                String course = td.get(1).text();
                String sectionNumber = td.get(2).text();
                String type = td.get(3).text();
                String title = td.get(4).text();
                String credits = td.get(5).text();
                String days = td.get(6).text();
                String times = td.get(7).text();
                String room = td.get(8).text();
                String instructor = td.get(9).text();

                Sections section = new Sections(department, crn, "Place Holder URL", course, sectionNumber, type, title, credits, days, times, room, instructor);
                courseList.add(section);
            }
        }
    }

    public static void addDepartments(){
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
