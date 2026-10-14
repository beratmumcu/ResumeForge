package com.resume.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.resume.model.Certificate;
import com.resume.model.Education;
import com.resume.model.Experience;
import com.resume.model.PersonalInfo;
import com.resume.model.Resume;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * OpenPDF kütüphanesi kullanarak Resume modelindeki verileri
 * Türkçe karakterleri tam destekleyen, fotoğrafları ve LinkedIn tarzı 1:1 logoları
 * şablona doğrudan ve kusursuz oturtan profesyonel CV servis sınıfıdır.
 */
public class PdfResumeGenerator {

    private static final Locale TR_LOCALE = Locale.forLanguageTag("tr-TR");

    // Renk paleti
    private static final Color PRIMARY_COLOR = new Color(30, 58, 138);       // Koyu lacivert (#1E3A8A)
    private static final Color ACCENT_COLOR = new Color(37, 99, 235);        // Parlak mavi (#2563EB)
    private static final Color TEXT_DARK = new Color(30, 41, 59);            // Koyu gri metin (#1E293B)
    private static final Color TEXT_MUTED = new Color(100, 116, 139);        // Açık gri detay (#64748B)
    private static final Color BORDER_COLOR = new Color(226, 232, 240);      // İnce kenarlık (#E2E8F0)
    private static final Color BADGE_BG = new Color(239, 246, 255);          // Bölüm başlığı zemin (#EFF6FF)
    private static final Color CARD_BG = new Color(248, 250, 252);           // Kart arka planı (#F8FAFC)

    // BaseFont nesneleri
    private BaseFont baseRegular;
    private BaseFont baseBold;
    private BaseFont baseItalic;

    // Fontlar
    private Font fontName;
    private Font fontTitle;
    private Font fontContact;
    private Font fontSection;
    private Font fontExpRole;
    private Font fontExpCompany;
    private Font fontExpDate;
    private Font fontExpDesc;
    private Font fontFooter;

    public PdfResumeGenerator() {
        initFonts();
    }

    private void initFonts() {
        try {
            baseRegular = loadBaseFont("fonts/Arial.ttf", "C:/Windows/Fonts/arial.ttf");
            baseBold = loadBaseFont("fonts/Arial-Bold.ttf", "C:/Windows/Fonts/arialbd.ttf");
            baseItalic = loadBaseFont("fonts/Arial-Italic.ttf", "C:/Windows/Fonts/ariali.ttf");

            if (baseBold == null) baseBold = baseRegular;
            if (baseItalic == null) baseItalic = baseRegular;

            if (baseRegular != null) {
                fontName = new Font(baseBold, 19, Font.NORMAL, PRIMARY_COLOR);
                fontTitle = new Font(baseBold, 10.5f, Font.NORMAL, ACCENT_COLOR);
                fontContact = new Font(baseRegular, 9, Font.NORMAL, TEXT_DARK);
                fontSection = new Font(baseBold, 11, Font.NORMAL, PRIMARY_COLOR);
                fontExpRole = new Font(baseBold, 10.5f, Font.NORMAL, PRIMARY_COLOR);
                fontExpCompany = new Font(baseBold, 9.5f, Font.NORMAL, ACCENT_COLOR);
                fontExpDate = new Font(baseItalic, 8.5f, Font.NORMAL, TEXT_MUTED);
                fontExpDesc = new Font(baseRegular, 9, Font.NORMAL, TEXT_DARK);
                fontFooter = new Font(baseRegular, 8, Font.NORMAL, TEXT_MUTED);
                return;
            }
        } catch (Exception e) {
            System.err.println("[Uyarı] Özel TrueType font yüklenirken hata oluştu: " + e.getMessage());
        }

        // Yedek varsayılan fontlar
        fontName = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 19, PRIMARY_COLOR);
        fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10.5f, ACCENT_COLOR);
        fontContact = FontFactory.getFont(FontFactory.HELVETICA, 9, TEXT_DARK);
        fontSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, PRIMARY_COLOR);
        fontExpRole = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10.5f, PRIMARY_COLOR);
        fontExpCompany = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, ACCENT_COLOR);
        fontExpDate = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8.5f, TEXT_MUTED);
        fontExpDesc = FontFactory.getFont(FontFactory.HELVETICA, 9, TEXT_DARK);
        fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, TEXT_MUTED);
    }

    private BaseFont loadBaseFont(String resourcePath, String systemFallbackPath) {
        String resName = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        try (InputStream is = getClass().getResourceAsStream(resName)) {
            if (is != null) {
                byte[] fontBytes = is.readAllBytes();
                return BaseFont.createFont(resourcePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
            }
        } catch (Exception ignored) {
        }

        File localFile = new File("src/main/resources/" + resourcePath);
        if (localFile.exists()) {
            try {
                return BaseFont.createFont(localFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception ignored) {
            }
        }

        if (systemFallbackPath != null) {
            File sysFile = new File(systemFallbackPath);
            if (sysFile.exists()) {
                try {
                    return BaseFont.createFont(sysFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                } catch (Exception ignored) {
                }
            }
        }

        return null;
    }

    public void generatePdf(Resume resume, String outputPath) throws DocumentException, IOException {
        if (resume == null) {
            throw new IllegalArgumentException("Resume nesnesi null olamaz!");
        }

        // Sayfa boyutu A4, dengeli kenar boşlukları (24pt yanlar, 18pt dikey)
        Document document = new Document(PageSize.A4, 24, 24, 18, 18);

        File outputFile = new File(outputPath);
        if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();

        try {
            // 1. Üst Bilgi Kartı (Şablona tam oturan profil başlığı & fotoğraf)
            addHeaderSection(document, resume.getPersonalInfo());

            // 2. EĞİTİM BÖLÜMÜ (LinkedIn tarzı okul logosu ve detaylar)
            if (resume.getEducations() != null && !resume.getEducations().isEmpty()) {
                addSectionHeader(document, "EĞİTİM BİLGİLERİ / EDUCATION");
                addEducationsSection(document, resume.getEducations());
            }

            // 3. SERTİFİKALAR BÖLÜMÜ
            if (resume.getCertificates() != null && !resume.getCertificates().isEmpty()) {
                addSectionHeader(document, "SERTİFİKALAR VE YETKİNLİKLER / CERTIFICATIONS");
                addCertificatesSection(document, resume.getCertificates());
            }

            // 4. İŞ DENEYİMİ BÖLÜMÜ (Palantir & Lockheed Martin LinkedIn logolu kartlar)
            if (resume.getExperiences() != null && !resume.getExperiences().isEmpty()) {
                addSectionHeader(document, "İŞ DENEYİMİ / WORK EXPERIENCE");
                addExperiencesSection(document, resume);
            }

            // 5. Alt Bilgi / Footer
            addFooter(document);

        } finally {
            document.close();
        }
    }

    /**
     * LinkedIn profil başlığı gibi şablona doğrudan oturan Header kartı.
     */
    private void addHeaderSection(Document document, PersonalInfo info) throws DocumentException {
        if (info == null) return;

        PdfPTable headerCard = new PdfPTable(1);
        headerCard.setWidthPercentage(100);
        headerCard.setSpacingAfter(5);

        PdfPCell cardCell = new PdfPCell();
        cardCell.setBackgroundColor(CARD_BG);
        cardCell.setBorder(Rectangle.BOX);
        cardCell.setBorderColor(BORDER_COLOR);
        cardCell.setBorderWidth(0.75f);
        cardCell.setPadding(7);
        cardCell.setPaddingLeft(10);
        cardCell.setPaddingRight(10);

        PdfPTable innerTable = new PdfPTable(new float[]{78f, 22f});
        innerTable.setWidthPercentage(100);

        // Sol Sütun: Kişisel Bilgiler
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.setPadding(0);

        Paragraph namePara = new Paragraph(safeString(info.getFullName(), "İsimsiz Aday"), fontName);
        namePara.setSpacingAfter(2);
        infoCell.addElement(namePara);

        String rawTitle = safeString(info.getTitle(), "Yazılım Mühendisi");
        String upperTitle = rawTitle.toUpperCase(TR_LOCALE);
        Paragraph titlePara = new Paragraph(upperTitle, fontTitle);
        titlePara.setSpacingAfter(4);
        infoCell.addElement(titlePara);

        if (info.getEmail() != null && !info.getEmail().isBlank()) {
            Paragraph emailPara = new Paragraph("E-posta : " + info.getEmail(), fontContact);
            emailPara.setSpacingAfter(1.5f);
            infoCell.addElement(emailPara);
        }

        if (info.getPhone() != null && !info.getPhone().isBlank()) {
            Paragraph phonePara = new Paragraph("Telefon : " + info.getPhone(), fontContact);
            phonePara.setSpacingAfter(1);
            infoCell.addElement(phonePara);
        }

        innerTable.addCell(infoCell);

        // Sağ Sütun: Profil Fotoğrafı (Şablona tam oturan rozet)
        PdfPCell photoCell = new PdfPCell();
        photoCell.setBorder(Rectangle.NO_BORDER);
        photoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        photoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        photoCell.setPadding(0);

        Image photoImage = loadPhoto(info.getPhotoPath());
        if (photoImage != null) {
            photoImage.scaleToFit(74, 88);
            photoImage.setAlignment(Element.ALIGN_RIGHT);

            // İnce zarif çerçeveli kutucuk
            PdfPTable imgBox = new PdfPTable(1);
            imgBox.setWidthPercentage(100);
            PdfPCell imgCell = new PdfPCell(photoImage, true);
            imgCell.setBorder(Rectangle.BOX);
            imgCell.setBorderColor(ACCENT_COLOR);
            imgCell.setBorderWidth(1.5f);
            imgCell.setPadding(2);
            imgCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            imgCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            imgBox.addCell(imgCell);

            photoCell.addElement(imgBox);
        } else {
            PdfPCell emptyCell = new PdfPCell(new Phrase("Fotoğraf Yok", fontExpDate));
            emptyCell.setBorder(Rectangle.BOX);
            emptyCell.setBorderColor(BORDER_COLOR);
            emptyCell.setFixedHeight(75);
            emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            emptyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            photoCell.addElement(emptyCell);
        }

        innerTable.addCell(photoCell);
        cardCell.addElement(innerTable);
        headerCard.addCell(cardCell);
        document.add(headerCard);
    }

    private void addSectionHeader(Document document, String title) throws DocumentException {
        PdfPTable sectionTable = new PdfPTable(1);
        sectionTable.setWidthPercentage(100);
        sectionTable.setSpacingBefore(2);
        sectionTable.setSpacingAfter(4);

        PdfPCell cell = new PdfPCell(new Phrase(title, fontSection));
        cell.setBackgroundColor(BADGE_BG);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderColor(ACCENT_COLOR);
        cell.setBorderWidth(3f);
        cell.setPadding(4);
        cell.setPaddingLeft(8);

        sectionTable.addCell(cell);
        document.add(sectionTable);
    }

    /**
     * Eğitim Bilgileri Bölümü (LinkedIn tarzı rozet ile şablona tam oturan düzen)
     */
    private void addEducationsSection(Document document, List<Education> educations) throws DocumentException {
        for (Education edu : educations) {
            if (edu == null) continue;

            PdfPTable cardTable = new PdfPTable(1);
            cardTable.setWidthPercentage(100);
            cardTable.setSpacingAfter(4);

            PdfPCell cardCell = new PdfPCell();
            cardCell.setBackgroundColor(CARD_BG);
            cardCell.setBorder(Rectangle.BOX);
            cardCell.setBorderColor(BORDER_COLOR);
            cardCell.setBorderWidth(0.75f);
            cardCell.setPadding(5);
            cardCell.setPaddingLeft(8);

            Image logoImg = resolveEducationLogo(edu);

            if (logoImg != null) {
                // LinkedIn tarzı: Sol sütunda 30x30 1:1 kare logo rozeti, sağda metin
                PdfPTable layoutTable = new PdfPTable(new float[]{7.5f, 92.5f});
                layoutTable.setWidthPercentage(100);

                PdfPCell logoCell = new PdfPCell();
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setPaddingTop(1);
                logoCell.setPaddingRight(5);
                logoCell.setPaddingLeft(0);

                logoImg.scaleToFit(30, 30);
                logoImg.setAlignment(Element.ALIGN_LEFT);
                logoCell.addElement(logoImg);
                layoutTable.addCell(logoCell);

                PdfPCell textCell = new PdfPCell();
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setPadding(0);

                addEducationDetails(textCell, edu);
                layoutTable.addCell(textCell);

                cardCell.addElement(layoutTable);
            } else {
                addEducationDetails(cardCell, edu);
            }

            cardTable.addCell(cardCell);
            document.add(cardTable);
        }
    }

    private void addEducationDetails(PdfPCell containerCell, Education edu) {
        PdfPTable headerRow = new PdfPTable(new float[]{72f, 28f});
        headerRow.setWidthPercentage(100);

        PdfPCell schoolCell = new PdfPCell(new Phrase(safeString(edu.getSchool(), "Okul"), fontExpRole));
        schoolCell.setBorder(Rectangle.NO_BORDER);
        schoolCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        schoolCell.setPadding(0);
        headerRow.addCell(schoolCell);

        PdfPCell dateCell = new PdfPCell(new Phrase(safeString(edu.getDateRange(), "-"), fontExpDate));
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dateCell.setPadding(0);
        headerRow.addCell(dateCell);

        containerCell.addElement(headerRow);

        if (edu.getDepartment() != null && !edu.getDepartment().isBlank()) {
            Paragraph deptPara = new Paragraph(edu.getDepartment(), fontExpCompany);
            deptPara.setSpacingBefore(1.5f);
            deptPara.setSpacingAfter(edu.getGrade() != null && !edu.getGrade().isBlank() ? 1.5f : 0);
            containerCell.addElement(deptPara);
        }

        if (edu.getGrade() != null && !edu.getGrade().isBlank()) {
            Paragraph gradePara = new Paragraph(edu.getGrade(), fontExpDesc);
            gradePara.setLeading(11);
            containerCell.addElement(gradePara);
        }
    }

    /**
     * Sertifikalar Bölümü
     */
    private void addCertificatesSection(Document document, List<Certificate> certificates) throws DocumentException {
        PdfPTable certTable = new PdfPTable(1);
        certTable.setWidthPercentage(100);
        certTable.setSpacingAfter(4);

        PdfPCell certCell = new PdfPCell();
        certCell.setBackgroundColor(CARD_BG);
        certCell.setBorder(Rectangle.BOX);
        certCell.setBorderColor(BORDER_COLOR);
        certCell.setBorderWidth(0.75f);
        certCell.setPadding(5);
        certCell.setPaddingLeft(8);

        for (int i = 0; i < certificates.size(); i++) {
            Certificate cert = certificates.get(i);
            if (cert == null) continue;

            PdfPTable row = new PdfPTable(new float[]{75f, 25f});
            row.setWidthPercentage(100);

            PdfPCell left = new PdfPCell();
            left.setBorder(Rectangle.NO_BORDER);
            left.setPadding(0);

            Paragraph titlePara = new Paragraph("•  " + safeString(cert.getTitle(), "Sertifika"), fontExpRole);
            left.addElement(titlePara);

            String subInfo = safeString(cert.getIssuer(), "");
            if (cert.getCredentialId() != null && !cert.getCredentialId().isBlank()) {
                subInfo += (subInfo.isEmpty() ? "" : " | ") + cert.getCredentialId();
            }
            if (!subInfo.isEmpty()) {
                Paragraph subPara = new Paragraph("    " + subInfo, fontExpDesc);
                left.addElement(subPara);
            }

            PdfPCell right = new PdfPCell(new Phrase(safeString(cert.getDate(), "-"), fontExpDate));
            right.setBorder(Rectangle.NO_BORDER);
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);
            right.setVerticalAlignment(Element.ALIGN_MIDDLE);
            right.setPadding(0);

            row.addCell(left);
            row.addCell(right);
            certCell.addElement(row);

            if (i < certificates.size() - 1) {
                LineSeparator sep = new LineSeparator(0.5f, 100, BORDER_COLOR, Element.ALIGN_CENTER, 1);
                Paragraph p = new Paragraph();
                p.add(sep);
                p.setSpacingBefore(2);
                p.setSpacingAfter(2);
                certCell.addElement(p);
            }
        }

        certTable.addCell(certCell);
        document.add(certTable);
    }

    /**
     * İş Deneyimleri Bölümü (LinkedIn tarzı 1:1 şirket rozeti ile şablona tam oturan kartlar)
     */
    private void addExperiencesSection(Document document, Resume resume) throws DocumentException {
        for (int i = 0; i < resume.getExperiences().size(); i++) {
            Experience exp = resume.getExperiences().get(i);
            if (exp == null) continue;

            PdfPTable cardTable = new PdfPTable(1);
            cardTable.setWidthPercentage(100);
            cardTable.setSpacingAfter(4);

            PdfPCell cardCell = new PdfPCell();
            cardCell.setBackgroundColor(CARD_BG);
            cardCell.setBorder(Rectangle.BOX);
            cardCell.setBorderColor(BORDER_COLOR);
            cardCell.setBorderWidth(0.75f);
            cardCell.setPadding(5);
            cardCell.setPaddingLeft(8);

            Image logoImg = resolveExperienceLogo(exp);

            if (logoImg != null) {
                // LinkedIn tarzı: Sol sütunda 30x30 1:1 kare şirket rozeti, sağda unvan ve açıklama
                PdfPTable layoutTable = new PdfPTable(new float[]{7.5f, 92.5f});
                layoutTable.setWidthPercentage(100);

                PdfPCell logoCell = new PdfPCell();
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setPaddingTop(1);
                logoCell.setPaddingRight(5);
                logoCell.setPaddingLeft(0);

                logoImg.scaleToFit(30, 30);
                logoImg.setAlignment(Element.ALIGN_LEFT);
                logoCell.addElement(logoImg);
                layoutTable.addCell(logoCell);

                PdfPCell textCell = new PdfPCell();
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setPadding(0);

                addExperienceDetails(textCell, exp);
                layoutTable.addCell(textCell);

                cardCell.addElement(layoutTable);
            } else {
                addExperienceDetails(cardCell, exp);
            }

            cardTable.addCell(cardCell);
            document.add(cardTable);
        }
    }

    private void addExperienceDetails(PdfPCell containerCell, Experience exp) {
        PdfPTable headerRow = new PdfPTable(new float[]{72f, 28f});
        headerRow.setWidthPercentage(100);

        PdfPCell roleCell = new PdfPCell(new Phrase(safeString(exp.getPosition(), "Pozisyon"), fontExpRole));
        roleCell.setBorder(Rectangle.NO_BORDER);
        roleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        roleCell.setPadding(0);
        headerRow.addCell(roleCell);

        PdfPCell dateCell = new PdfPCell(new Phrase(safeString(exp.getDateRange(), "-"), fontExpDate));
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dateCell.setPadding(0);
        headerRow.addCell(dateCell);

        containerCell.addElement(headerRow);

        Paragraph companyPara = new Paragraph(safeString(exp.getCompany(), "Şirket"), fontExpCompany);
        companyPara.setSpacingBefore(1.5f);
        companyPara.setSpacingAfter(2.5f);
        containerCell.addElement(companyPara);

        LineSeparator thinLine = new LineSeparator(0.5f, 100, BORDER_COLOR, Element.ALIGN_CENTER, -1);
        containerCell.addElement(thinLine);

        Paragraph descPara = new Paragraph(safeString(exp.getDescription(), ""), fontExpDesc);
        descPara.setLeading(11);
        descPara.setSpacingBefore(2);
        containerCell.addElement(descPara);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("Bu özgeçmiş Antigravity & OpenPDF kullanılarak otomatik üretilmiştir.", fontFooter);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(4);
        document.add(footer);
    }

    private Image resolveEducationLogo(Education edu) {
        if (edu == null) return null;

        if (edu.getLogoPath() != null && !edu.getLogoPath().isBlank()) {
            Image img = loadImage(edu.getLogoPath());
            if (img != null) return img;
        }

        String schoolName = safeString(edu.getSchool(), "").toLowerCase(TR_LOCALE);
        if (schoolName.contains("kırklareli") || schoolName.contains("kirklareli") || schoolName.contains("klü")) {
            return loadImage("klu.png");
        }

        return null;
    }

    private Image resolveExperienceLogo(Experience exp) {
        if (exp == null) return null;

        if (exp.getLogoPath() != null && !exp.getLogoPath().isBlank()) {
            Image img = loadImage(exp.getLogoPath());
            if (img != null) return img;
        }

        String company = safeString(exp.getCompany(), "").toLowerCase(Locale.ENGLISH);
        if (company.contains("palantir")) {
            return loadImage("palantir.png");
        } else if (company.contains("lockheed")) {
            return loadImage("lockheed.png");
        }

        return null;
    }

    private Image loadImage(String path) {
        if (path == null || path.trim().isEmpty()) return null;

        String[] prefixes = new String[]{
                "",
                "logos/",
                "src/main/resources/logos/",
                "src/main/resources/"
        };

        for (String prefix : prefixes) {
            String fullPath = prefix + path;
            try {
                File file = new File(fullPath);
                if (file.exists() && file.isFile()) {
                    return Image.getInstance(file.getAbsolutePath());
                }

                URL res = getClass().getClassLoader().getResource(fullPath);
                if (res != null) {
                    return Image.getInstance(res);
                }

                String clean = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
                res = getClass().getClassLoader().getResource(clean);
                if (res != null) {
                    return Image.getInstance(res);
                }
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private Image loadPhoto(String photoPath) {
        String[] candidates = new String[]{
                photoPath,
                "WhatsApp Image 2026-10-13 at 21.27.05.jpeg",
                "src/main/resources/profile.jpeg",
                "profile.jpeg",
                "src/main/resources/profile.png",
                "profile.png"
        };

        for (String candidate : candidates) {
            Image img = loadImage(candidate);
            if (img != null) return img;
        }

        return null;
    }

    private String safeString(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value : fallback;
    }
}
