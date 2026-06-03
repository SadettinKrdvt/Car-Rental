# Araç Kiralama Sistemi (Car Rental System) - Spring Boot Backend Projesi

Bu proje, **Orta Düzey Programlama Final Projesi** gereksinimlerine uygun olarak geliştirilmiş; kurumsal mimari standartlarına sahip, güvenli, doğrulanabilir ve ölçeklenebilir bir **Araç Kiralama (Car Rental) API** uygulamasıdır.

Proje, tamamen **Temiz Kod (Clean Code)** standartlarında yazılmış olup, sunum esnasında sözlü olarak açıklanacağı için **kod içerisinde hiçbir açıklama/yorum satırı barındırmamaktadır.**

---

## 🚀 Kullanılan Teknolojiler ve Kütüphaneler

- **Core Framework:** Spring Boot 3.3.0 (Java 17)
- **Security & Auth:** Spring Security (Role-Based Access Control - RBAC) & JWT (JSON Web Token)
- **Database / ORM:** PostgreSQL & Spring Data JPA (Hibernate)
- **Validation:** Jakarta Validation API (`@Valid`, `@NotBlank`, `@Email`, vb.)
- **Boilerplate Reduction:** Lombok (`@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, vb.)
- **API Testing:** Postman

---

## 📂 Mimari Katmanlar ve Paket Yapısı

Proje, hiyerarşik paket yapısı ve **Separation of Concerns (Sorumlulukların Ayrılması)** ilkesine uygun şekilde aşağıdaki gibi yapılandırılmıştır:

```
com.example.carrental
├── CarRentalApplication.java      # Uygulama Başlangıç Sınıfı
├── entity                          # 1) Veritabanı Modelleri (ENTITY)
│   ├── User.java                   # Kullanıcı Tablosu (UserDetails İmplementasyonu)
│   ├── Role.java                   # Rol Yönetimi (ADMIN, CUSTOMER)
│   ├── Car.java                    # Araç Tablosu
│   └── Rental.java                 # Kiralama İşlemleri Tablosu
├── repository                      # 2) Veritabanı Erişim Katmanı (REPOSITORY)
│   ├── UserRepository.java         # Türetilmiş (Derived) Sorgular içerir
│   ├── CarRepository.java          # Türetilmiş (Derived) Sorgular içerir
│   └── RentalRepository.java       # Türetilmiş (Derived) Sorgular içerir
├── service                         # 3) İş Mantığı Katmanı (SERVICE)
│   ├── UserService.java            # Kayıt, Giriş ve Şifreleme Mantığı
│   ├── CarService.java             # Araç CRUD ve Plaka Doğrulama İş Mantığı
│   └── RentalService.java          # Kiralama Tarihleri, Fiyat ve Çakışma Kontrolü İş Mantığı
├── controller                      # 4) Dış İstek Karşılama Katmanı (CONTROLLER)
│   ├── AuthController.java         # Kayıt ve Giriş HTTP uç noktaları
│   ├── CarController.java          # Araç yönetimi HTTP uç noktaları (Rol Kısıtlamalı)
│   └── RentalController.java       # Kiralama HTTP uç noktaları (Rol Kısıtlamalı)
├── dto                             # 5) Doğrulama ve İstek/Yanıt Veri Transfer Modelleri (DTO & VALID)
│   ├── RegisterRequest.java        # Validasyonlu Kayıt Modeli
│   ├── LoginRequest.java           # Validasyonlu Giriş Modeli
│   ├── AuthResponse.java           # JWT içeren Yanıt Modeli
│   ├── CarRequest.java             # Validasyonlu Araç Kayıt/Güncelleme Modeli
│   ├── CarResponse.java            # Araç Bilgisi Döndürme Modeli
│   ├── RentalRequest.java          # Validasyonlu Kiralama İstek Modeli
│   └── RentalResponse.java         # Hesaplanan Toplam Ücretli Kiralama Yanıt Modeli
├── exception                       # 6) Merkezi Hata Yönetimi (EXCEPTION)
│   ├── CustomException.java        # Temel Uygulama Hatası (Abstract)
│   ├── ResourceNotFoundException.java # Bulunamadı Hataları (404)
│   ├── CarNotAvailableException.java  # Müsaitlik Hataları (400)
│   ├── InvalidRentalDateException.java # Geçersiz Tarih Hataları (400)
│   ├── DuplicateResourceException.java # Çakışan Veri Hataları (409)
│   ├── ErrorResponse.java          # Standart Hata JSON Şablonu
│   └── GlobalExceptionHandler.java # @RestControllerAdvice ile Hataların Yakalanması
└── security                        # 8) RBAC ve JWT Güvenlik Yönetimi (SECURITY)
    ├── SecurityConfig.java         # Spring Security ve Yetkilendirme Kuralları
    ├── JwtService.java             # JWT Token Üretim ve Doğrulama
    ├── JwtAuthenticationFilter.java # JWT İstek Filtresi (OncePerRequestFilter)
    └── CustomUserDetailsService.java # E-posta ile Kullanıcı Detayı Yükleme
```

---

## 🛠️ Kurulum ve Çalıştırma Adımları

Uygulamayı yerel bilgisayarınızda başlatmak için aşağıdaki adımları sırasıyla uygulayınız:

### 1. Veritabanı Kurulumu
Bilgisayarınızdaki PostgreSQL istemcisinde (pgAdmin veya psql terminal) aşağıdaki isimde boş bir veritabanı oluşturun:
```sql
CREATE DATABASE carrental;
```

### 2. Veritabanı Bağlantı Ayarları
`src/main/resources/application.properties` dosyasında PostgreSQL kullanıcı adı (`postgres`) ve şifrenizi (`postgres`) kendi yerel PostgreSQL ayarlarınıza göre güncelleyin:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carrental
spring.datasource.username=postgres
spring.datasource.password=şifreniz
```

### 3. Projeyi IntelliJ IDEA'da Açın ve Çalıştırın
- IntelliJ IDEA uygulamasını açın.
- **Open** diyerek projenin ana klasörünü seçin.
- Maven bağımlılıklarının indirilmesini bekleyin.
- `CarRentalApplication.java` sınıfındaki yeşil **Run** butonuna basarak projeyi başlatın.
- Proje varsayılan olarak **`http://localhost:8080`** portunda ayağa kalkacaktır.

---

## 🛡️ Spring Security & Rol Tabanlı Erişim Kısıtlamaları (RBAC)

Uygulamamızdaki roller:
- **`ADMIN`:** Sisteme araç ekleyebilir, güncelleyebilir, silebilir ve tüm kullanıcıların kiralamalarını listeleyebilir.
- **`CUSTOMER`:** Sistemdeki araçları listeleyebilir ve müsait araçlar için kiralama işlemi yapabilir.

| Endpoint | HTTP Metodu | Gerekli Rol / Yetki | Açıklama |
| :--- | :---: | :---: | :--- |
| `/api/auth/**` | POST | Herkese Açık | Kayıt olma (`/register`) ve Giriş yapma (`/login`) işlemleri |
| `/api/cars` | GET | Herkese Açık | Sistemdeki tüm araçları listeleme |
| `/api/cars?available=true` | GET | Herkese Açık | Sadece müsait araçları listeleme |
| `/api/cars?brand=Audi` | GET | Herkese Açık | Markaya göre büyük/küçük harf duyarsız arama (**Derived Query**) |
| `/api/cars/{id}` | GET | Herkese Açık | ID değerine göre araç detayı getirme |
| `/api/cars` | POST | `ADMIN` | Yeni araç ekleme |
| `/api/cars/{id}` | PUT | `ADMIN` | Araç bilgilerini güncelleme |
| `/api/cars/{id}` | DELETE | `ADMIN` | Aracı silme |
| `/api/rentals` | POST | `CUSTOMER` veya `ADMIN` | Seçilen aracı kiralama (**İş Mantığı ve Çakışma Kontrolü**) |
| `/api/rentals/my` | GET | `CUSTOMER` veya `ADMIN` | Giriş yapmış kullanıcının kendi kiralama geçmişi |
| `/api/rentals` | GET | `ADMIN` | Sistemdeki tüm kiralama kayıtlarını listeleme |

---

## 🧠 Gelişmiş İş Mantığı ve Kuralları (Service Katmanı)

1. **Tarih Validasyonu:** Araç kiralarken başlangıç tarihi geçmiş bir gün olamaz. Bitiş tarihi ise başlangıç tarihinden önce seçilemez. Aksi halde `InvalidRentalDateException` fırlatılır.
2. **Müsaitlik ve Çakışma Kontrolü:** Kiralanmak istenen araç, seçilen tarihlerde başka bir kullanıcı tarafından kiralanmışsa, **Derived Query** (`existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual`) ile çakışma saptanır ve `CarNotAvailableException` fırlatılır.
3. **Fiyatlandırma:** Günlük kiralama ücreti ile toplam gün sayısı çarpılarak kiralama kaydının `totalPrice` alanı otomatik olarak hesaplanır ve veritabanına kaydedilir.
4. **Aktif Durum Güncellemesi:** Kiralama başlangıç tarihi "bugün" ise aracın `available` durumu otomatik olarak `false` (müsait değil) konumuna getirilir.

---

## 📇 Derived Query Metotları (Türetilmiş Sorgular)

Spring Data JPA'nın gücünden faydalanarak repository katmanında aşağıdaki özelleştirilmiş sorgular tanımlanmıştır:

- **`CarRepository.findByAvailableTrue()`**: Sadece boşta olan araçları veritabanından çeker.
- **`CarRepository.findByBrandIgnoreCase(String brand)`**: Girilen markaya ait araçları büyük/küçük harfe bakmaksızın listeler.
- **`CarRepository.existsByPlateNumber(String plateNumber)`**: Eklenmek istenen plakanın veritabanında mükerrer olmasını engellemek için plaka varlığını sorgular.
- **`UserRepository.findByEmail(String email)`**: Giriş işlemlerinde e-posta eşleşmesini sağlar.
- **`RentalRepository.findByUserEmail(String email)`**: Kullanıcının geçmiş siparişlerini getirir.
- **`RentalRepository.existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Car car, LocalDate endDate, LocalDate startDate)`**: Çakışan kiralama tarihlerini kontrol eder.

---

## 📬 Postman ile API Testi

Projenin kök dizininde bulunan **`postman_collection.json`** dosyasını Postman uygulamanıza import ederek tüm uç noktaları saniyeler içinde test edebilirsiniz:

1. Postman uygulamasını açın ve **Import** butonuna tıklayın.
2. Proje klasöründeki `postman_collection.json` dosyasını seçerek yükleyin.
3. **Authentication** altındaki `Register Admin` ve `Register Customer` istekleriyle test kullanıcılarınızı oluşturun.
4. `Login Admin` veya `Login Customer` isteğini attığınızda, sistem tarafından dönen JWT Token **otomatik olarak yakalanacak** ve koleksiyon değişkenlerine kaydedilecektir.
5. Diğer korumalı API uç noktalarını (Örn: araç ekleme veya kiralama) ek bir işlem yapmaksızın doğrudan çalıştırarak test edebilirsiniz.
