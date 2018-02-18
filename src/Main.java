import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static ArrayList<Sections> courseList = new ArrayList<>();

    public static void main(String[] args){

        try {
            getSections("https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Sections section: courseList){
            System.out.println(section);
        }
    }

    public static void getSections(String url) throws IOException{
        Document doc = Jsoup.connect(url).timeout(60*1000).get();

        //Element divMain = doc.select("div#maincontent").first();
        Element resultTable = doc.select("div#maincontent > table.results").first();
        Elements rows = resultTable.select("tr.list_row");

        for (Element row: rows){
            Elements td = row.select("td");
            String crn = td.get(0).text();
            String courseURL = td.get(1).text();
            String course = td.get(2).text();
            String sectionNumber = td.get(3).text();
            String type = td.get(4).text();
            String title = td.get(5).text();
            String credits = td.get(6).text();
            String days = td.get(7).text();
            String times = td.get(8).text();
            String room = td.get(9).text();
            String instructor = td.get(10).text();

            Sections section = new Sections(crn, courseURL, course, sectionNumber, type, title, credits, days, times, room, instructor);
            courseList.add(section);
        }
    }

    /*
    The order for td is: CRN, courseURL, course, sectionNumber, type, title, credits, days, times, room, instructor
     */

    /*
    ArrayList<Parks> parks = new ArrayList<>()
    Element table = doc.selectFirst("table")
    Element tbody = table.selectFirst("tbody")
    Elements rows = tbody.select("tr")

    for (Element tr: rows){
    Elements td = tr.select("td")
    if (td.size() == 3){
        state = td.get(0).text()
        state = state.replaceAll("([(]\\d[)])", "")
        parkName = td.get(1).text()
        date = td.get(2).text()
        Parks park = new Parks(state, parkName, date)
        parks.add(park)
    }
    if (td.size() < 3) {
        parkName = td.get(0).text()
        date = td.get(1).text()
        Parks park = new Parks(state, parkName, date)
        parks.add(park)
    }
    }
     */
}
