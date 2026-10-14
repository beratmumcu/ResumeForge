package com.resume.model;

/**
 * Kişisel bilgileri (ad, unvan, e-posta, telefon ve fotoğraf dosya yolu)
 * saklayan model sınıfıdır.
 */
public class PersonalInfo {
    private String fullName;
    private String title;
    private String email;
    private String phone;
    private String photoPath;

    public PersonalInfo() {
    }

    public PersonalInfo(String fullName, String title, String email, String phone, String photoPath) {
        this.fullName = fullName;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.photoPath = photoPath;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean hasPhoto() {
        return photoPath != null && !photoPath.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "PersonalInfo{" +
                "fullName='" + fullName + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }
}
