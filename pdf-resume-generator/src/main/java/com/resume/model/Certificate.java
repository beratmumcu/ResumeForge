package com.resume.model;

/**
 * Sertifika ve başarı belgelerini (sertifika adı, veren kurum, tarih ve doğrulama kodu)
 * saklayan model sınıfıdır.
 */
public class Certificate {
    private String title;
    private String issuer;
    private String date;
    private String credentialId;

    public Certificate() {
    }

    public Certificate(String title, String issuer, String date) {
        this(title, issuer, date, null);
    }

    public Certificate(String title, String issuer, String date, String credentialId) {
        this.title = title;
        this.issuer = issuer;
        this.date = date;
        this.credentialId = credentialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "title='" + title + '\'' +
                ", issuer='" + issuer + '\'' +
                ", date='" + date + '\'' +
                ", credentialId='" + credentialId + '\'' +
                '}';
    }
}
