package com.resume;

import com.resume.model.Certificate;
import com.resume.model.Education;
import com.resume.model.Experience;
import com.resume.model.PersonalInfo;
import com.resume.model.Resume;
import com.resume.service.PdfResumeGenerator;

import java.io.File;

/**
 * Başlatıcı (Driver) sınıfı.
 * Kişisel bilgileri, üniversiteyi (Kırklareli Üniversitesi),
 * iş deneyimlerini (Palantir Technologies ve Lockheed Martin) doldurur
 * ve logolarıyla birlikte PDF belgesini üretir.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("          ResumeForge (OpenPDF) Başlatılıyor...       ");
        System.out.println("==================================================");

        try {
            String projectDir = System.getProperty("user.dir");

            // ==============================================================
            // 1. KİŞİSEL BİLGİLER & FOTOĞRAF
            // ==============================================================
            String photoPath = projectDir + File.separator + "WhatsApp Image 2026-10-13 at 21.27.05.jpeg";

            PersonalInfo personalInfo = new PersonalInfo(
                    "Berat Mumcu",
                    "Yazılım Mühendisi",
                    "berat.yilmaz@example.com",
                    "+90 (555) 123 45 67",
                    photoPath
            );

            // ==============================================================
            // 2. EĞİTİM BİLGİLERİ (Kırklareli Üniversitesi Logosu ile)
            // ==============================================================
            Education edu1 = new Education(
                    "Kırklareli Üniversitesi",
                    "Yazılım Mühendisliği (Lisans)",
                    "2020 - 2024",
                    "Mezuniyet Not Ortalaması: 3.48 / 4.00",
                    "logos/klu.png" // Kırklareli Üniversitesi LinkedIn Logosu
            );

            Education edu2 = new Education(
                    "Halil Bekmezci Anadolu Lisesi",
                    "Sayısal Alan",
                    "2016 - 2020",
                    "Okul Başarı Puanı: 94.5 / 100",
                    null
            );

            // ==============================================================
            // 3. SERTİFİKALAR VE YETKİNLİKLER
            // ==============================================================
            Certificate cert1 = new Certificate(
                    "Oracle Certified Associate: Java SE Programmer",
                    "Oracle University",
                    "2023",
                    "Doğrulama ID: OC-982341"
            );

            Certificate cert2 = new Certificate(
                    "Spring Boot & Cloud Microservices Architecture",
                    "Udemy / Coursera",
                    "2024",
                    "Sertifika No: UC-441290"
            );

            Certificate cert3 = new Certificate(
                    "AWS Certified Solutions Architect – Associate",
                    "Amazon Web Services (AWS)",
                    "2024",
                    "Doğrulama ID: AWS-897120"
            );

            // ==============================================================
            // 4. İŞ DENEYİMLERİ (Palantir Technologies & Lockheed Martin)
            // ==============================================================
            // Deneyim 1: Palantir Technologies (Logolu)
            Experience exp1 = new Experience(
                    "Palantir Technologies",
                    "Forward Deployed Software Engineer",
                    "2023 - Günümüz",
                    "Palantir Foundry ve Gotham platformları üzerinde büyük ölçekli kurumsal veri boru hatları (pipeline) tasarladı. " +
                    "Yüksek performanslı mikroservisler, veri entegrasyonları ve yapay zeka destekli karar destek mekanizmaları geliştirdi.",
                    "logos/palantir.png" // Palantir Logosu
            );

            // Deneyim 2: Lockheed Martin (Logolu)
            Experience exp2 = new Experience(
                    "Lockheed Martin",
                    "Defense Systems & Software Engineer",
                    "2022 - 2023",
                    "Havacılık ve savunma teknolojileri için görev kritik (mission-critical) gömülü yazılımlar ve güvenli haberleşme " +
                    "protokolleri geliştirdi. Dağıtık sistemlerde yüksek güvenilirlik ve düşük gecikmeli veri akışlarını optimize etti.",
                    "logos/lockheed.png" // Lockheed Martin Logosu
            );

            // Deneyim 3: InnoSoft Ar-Ge ve İnovasyon
            Experience exp3 = new Experience(
                    "InnoSoft Ar-Ge ve İnovasyon",
                    "Junior Yazılım Geliştirici",
                    "2021 - 2022",
                    "Nesne yönelimli programlama (OOP) ve katmanlı mimari standartlarına uygun masaüstü ve web modülleri kodladı. " +
                    "Birim testleri yazarak yazılım kalite ve sürekli entegrasyon süreçlerine katkı sağladı.",
                    null
            );

            // ==============================================================
            // 5. ANA RESUME MODELİNE BAĞLAMA (OOP)
            // ==============================================================
            Resume resume = new Resume(personalInfo, exp1, exp2, exp3);
            resume.addEducation(edu1);
            resume.addEducation(edu2);
            resume.addCertificate(cert1);
            resume.addCertificate(cert2);
            resume.addCertificate(cert3);

            // ==============================================================
            // 6. PDF ÇIKTISINI OLUŞTURMA
            // ==============================================================
            PdfResumeGenerator pdfGenerator = new PdfResumeGenerator();
            String outputPath = projectDir + File.separator + "output" + File.separator + "ozgecmis.pdf";

            System.out.println("Fotoğraf yükleniyor: " + photoPath);
            System.out.println("Kırklareli Üniversitesi, Palantir Technologies ve Lockheed Martin logoları işleniyor...");
            System.out.println("PDF belgesi oluşturuluyor...");

            pdfGenerator.generatePdf(resume, outputPath);

            System.out.println("\n[BAŞARILI] Güncel PDF Özgeçmiş başarıyla üretildi!");
            System.out.println("Dosya Yolu: " + outputPath);
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("\n[HATA] PDF oluşturulurken bir sorun meydana geldi:");
            e.printStackTrace();
        }
    }
}
