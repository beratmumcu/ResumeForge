package com.resume.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PersonalInfo, Education (Eğitim), Certificate (Sertifikalar) ve
 * Experience (İş Deneyimleri) nesnelerini bir araya getiren ana özgeçmiş (Resume) model sınıfıdır.
 */
public class Resume {
    private PersonalInfo personalInfo;
    private List<Education> educations = new ArrayList<>();
    private List<Certificate> certificates = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();

    public Resume() {
    }

    /**
     * PersonalInfo ve 3 adet Experience nesnesi alan ana kurucu metot.
     *
     * @param personalInfo Kişisel bilgiler nesnesi
     * @param exp1 1. iş deneyimi
     * @param exp2 2. iş deneyimi
     * @param exp3 3. iş deneyimi
     */
    public Resume(PersonalInfo personalInfo, Experience exp1, Experience exp2, Experience exp3) {
        this.personalInfo = personalInfo;
        this.experiences = new ArrayList<>();
        if (exp1 != null) this.experiences.add(exp1);
        if (exp2 != null) this.experiences.add(exp2);
        if (exp3 != null) this.experiences.add(exp3);
    }

    /**
     * Liste tabanlı kurucu metot.
     */
    public Resume(PersonalInfo personalInfo, List<Experience> experiences) {
        this.personalInfo = personalInfo;
        this.experiences = (experiences != null) ? new ArrayList<>(experiences) : new ArrayList<>();
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = (educations != null) ? new ArrayList<>(educations) : new ArrayList<>();
    }

    public void addEducation(Education education) {
        if (education != null) {
            this.educations.add(education);
        }
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = (certificates != null) ? new ArrayList<>(certificates) : new ArrayList<>();
    }

    public void addCertificate(Certificate certificate) {
        if (certificate != null) {
            this.certificates.add(certificate);
        }
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = (experiences != null) ? new ArrayList<>(experiences) : new ArrayList<>();
    }

    public void addExperience(Experience experience) {
        if (experience != null) {
            this.experiences.add(experience);
        }
    }

    @Override
    public String toString() {
        return "Resume{" +
                "personalInfo=" + personalInfo +
                ", educationsCount=" + (educations != null ? educations.size() : 0) +
                ", certificatesCount=" + (certificates != null ? certificates.size() : 0) +
                ", experiencesCount=" + (experiences != null ? experiences.size() : 0) +
                '}';
    }
}
