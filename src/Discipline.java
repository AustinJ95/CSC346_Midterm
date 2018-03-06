public class Discipline {
    String disciplineAbbrev;
    String disciplineFull;

    public Discipline(String departmentAbrev, String deparmentFull){
        setDisciplineAbbrev(departmentAbrev);
        setDisciplineFull(deparmentFull);
    }

    public String getDisciplineAbbrev() {
        return disciplineAbbrev;
    }

    public void setDisciplineAbbrev(String departmentAbrev) {
        this.disciplineAbbrev = departmentAbrev;
    }

    public String getDisciplineFull() {
        return disciplineFull;
    }

    public void setDisciplineFull(String deparmentFull) {
        this.disciplineFull = deparmentFull;
    }
}
