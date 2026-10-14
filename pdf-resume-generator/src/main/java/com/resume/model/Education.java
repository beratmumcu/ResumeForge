package com.resume.model;

/**
 * Eğitim bilgilerini (okul adı, bölüm/program, tarih aralığı, not ortalaması ve logo yolu)
 * saklayan model sınıfıdır.
 */
public class Education {
    private String school;
    private String department;
    private String dateRange;
    private String grade;
    private String logoPath;

    public Education() {
    }

    public Education(String school, String department, String dateRange, String grade) {
        this(school, department, dateRange, grade, null);
    }

    public Education(String school, String department, String dateRange, String grade, String logoPath) {
        this.school = school;
        this.department = department;
        this.dateRange = dateRange;
        this.grade = grade;
        this.logoPath = logoPath;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public boolean hasLogo() {
        return logoPath != null && !logoPath.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Education{" +
                "school='" + school + '\'' +
                ", department='" + department + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", grade='" + grade + '\'' +
                ", logoPath='" + logoPath + '\'' +
                '}';
    }
}
