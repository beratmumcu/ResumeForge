package com.resume.model;

/**
 * İş deneyimi bilgilerini (şirket adı, pozisyon, tarih aralığı, açıklama ve logo yolu)
 * saklayan model sınıfıdır.
 */
public class Experience {
    private String company;
    private String position;
    private String dateRange;
    private String description;
    private String logoPath;

    public Experience() {
    }

    public Experience(String company, String position, String dateRange, String description) {
        this(company, position, dateRange, description, null);
    }

    public Experience(String company, String position, String dateRange, String description, String logoPath) {
        this.company = company;
        this.position = position;
        this.dateRange = dateRange;
        this.description = description;
        this.logoPath = logoPath;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Experience{" +
                "company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", description='" + description + '\'' +
                ", logoPath='" + logoPath + '\'' +
                '}';
    }
}
