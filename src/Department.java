public class Department {
    String departmentAbbrev;
    String deparmentFull;

    public Department(String departmentAbrev, String deparmentFull){
        setDepartmentAbrev(departmentAbrev);
        setDeparmentFull(deparmentFull);
    }

    public String getDepartmentAbbrev() {
        return departmentAbbrev;
    }

    public void setDepartmentAbrev(String departmentAbrev) {
        this.departmentAbbrev = departmentAbrev;
    }

    public String getDeparmentFull() {
        return deparmentFull;
    }

    public void setDeparmentFull(String deparmentFull) {
        this.deparmentFull = deparmentFull;
    }
}
