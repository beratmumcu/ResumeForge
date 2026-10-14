# ⚡ ResumeForge: OOP Tabanlı PDF Özgeçmiş Üretim Motoru
> **Java 17 • Maven • OpenPDF • Nesne Yönelimli Mimari (OOP)**

**ResumeForge**, Nesne Yönelimli Programlama (Object-Oriented Programming - OOP) ve Temiz Kod (Clean Code) prensiplerine uygun olarak tasarlanmış, **OpenPDF** (`com.github.librepdf:openpdf`) motoru ile modern ve kurumsal standartlarda bir CV (Özgeçmiş) PDF belgesi oluşturan bir Java uygulamasıdır.

---

## 🏛️ Proje Mimarisi ve Tasarım Prensipleri

Projenin temel amacı spagetti koddan kaçınmak ve **Tek Sorumluluk Prensibi (Single Responsibility Principle - SRP)** doğrultusunda PDF üretimini `Main` sınıfından tamamen izole etmektir. Proje 3 ana katmandan oluşur:

```
ResumeForge/
├── pom.xml                                   # Maven yapılandırması ve OpenPDF bağımlılığı
├── run.bat                                   # Tek tıkla temiz derleme ve çalıştırma betiği
├── A4_EL_YAZISI_RAPORU.md                    # A4 el yazısı teslimi için 4 sayfalık rapor
├── README.md                                 # Detaylı proje ve mimari dokümantasyonu
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── resume/
│   │   │           ├── model/                # Veri modelleri (Encapsulation)
│   │   │           │   ├── PersonalInfo.java # Kişisel iletişim & vesikalık fotoğraf yolu
│   │   │           │   ├── Experience.java   # İş tecrübesi modeli (Şirket, unvan, logo)
│   │   │           │   ├── Education.java    # Eğitim bilgisi modeli
│   │   │           │   ├── Certificate.java  # Sertifikasyon modeli
│   │   │           │   └── Resume.java       # Aggregate Root (Tüm modelleri toplayan nesne)
│   │   │           ├── service/
│   │   │           │   └── PdfResumeGenerator.java # PDF çizim motoru, renk & sayfa düzeni
│   │   │           └── Main.java             # Başlatıcı (Driver) sınıfı
│   │   └── resources/
│   │       ├── profile.png                   # Profil fotoğrafı kaynağı
│   │       └── logos/                        # Üniversite ve kurumsal logolar
└── output/
    └── ozgecmis.pdf                          # Üretilen nihai PDF belgesi
```

---

## 🧩 Kullanılan Nesneler ve Sorumlulukları

| Sınıf / Nesne | Katman | Sorumluluk ve Neden Kullanıldı? |
| :--- | :--- | :--- |
| **`PersonalInfo`** | Model | Adayın ad-soyad, unvan, e-posta, telefon ve vesikalık fotoğraf yolunu `private` alanlarda kapsüller (encapsulation). |
| **`Experience`** | Model | 3 hayali iş deneyimini (Palantir Technologies, Lockheed Martin, InnoSoft) şirket logosu ve teknik açıklamalarıyla modeller. |
| **`Education`** | Model | Üniversite (Kırklareli Üniversitesi) ve lise eğitim geçmişini şablonlamak için kullanılır. |
| **`Certificate`** | Model | Oracle, AWS ve Spring Boot sertifikasyon verilerini tutar. |
| **`Resume`** | Model / Aggregate | Tüm alt modelleri bir araya getiren ana veri transfer nesnesidir (Data Transfer / Composition). |
| **`PdfResumeGenerator`** | Service | PDF belgesinin çizimi, A4 sayfa grid düzeni, renk paletleri ve font yönetimiyle ilgilenir. İş mantığını `Main`'den tamamen soyutlar. |
| **`Main`** | Driver | Verileri örnekleyen (`new`) ve `PdfResumeGenerator` servisini çalıştıran giriş noktasıdır. |

---

## 🛠️ OpenPDF Kütüphanesinden Kullanılan Bileşenler

- **`Document`**: Belgenin A4 boyutunu ve kenar boşluklarını yöneten ana kapsayıcı.
- **`PdfWriter`**: Fiziksel dosya yazımı için I/O köprüsünü kuran motor.
- **`PdfPTable` & `PdfPCell`**: Sayfayı iki sütunlu (Sidebar ve Ana Gövde) modern grid düzenine bölen ve hizalamaları sağlayan tablo nesneleri.
- **`Image`**: Vesikalık fotoğrafı ve kurum logolarını piksel bazlı ölçekleyen (`scaleToFit`) görsel nesnesi.
- **`BaseFont` & `Font`**: Türkçe karakterleri (ş, ğ, ı, ö, ç, İ) desteklemek için `IDENTITY_H` (Unicode) kodlamasıyla yüklenen font nesneleri.

---

## 📦 Maven Bağımlılığı (`pom.xml`)

```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>2.0.3</version>
</dependency>
```

---

## 🚀 Projeyi Derleme ve Çalıştırma

### Yöntem 1: Tek Tıkla Betik (Önerilen - Windows)
Proje kök dizinindeki `run.bat` dosyasına çift tıklayarak veya PowerShell/CMD üzerinden çalıştırabilirsiniz:
```cmd
run.bat
```

### Yöntem 2: Maven ile
```bash
mvn compile exec:java
```

### Yöntem 3: Standart Java Komut Satırı ile
```bash
# Derleme
javac -encoding UTF-8 -cp "lib/openpdf-2.0.3.jar" -d target/classes src/main/java/com/resume/model/*.java src/main/java/com/resume/service/*.java src/main/java/com/resume/Main.java

# Çalıştırma
java -cp "target/classes;lib/openpdf-2.0.3.jar;src/main/resources" com.resume.Main
```

Üretilen çıktı belgesi: **`output/ozgecmis.pdf`** konumunda hazır olacaktır.
