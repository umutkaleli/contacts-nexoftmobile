# 📱 Telefon Rehberi - Jetpack Compose & Clean Architecture

## 🏗 Proje Mimari Yapısı (Project Tree)

```text
com.example.contacts
├── data                 # Veri Katmanı (API & DB Yönetimi)
│   ├── dto              # API Veri Transfer Modelleri
│   ├── local            # Room DB (DAO, Entity, Database)
│   ├── manager          # Image & Device Contact Senkronizasyon
│   ├── mapper           # DTO-Entity-Domain Dönüştürücüler
│   ├── remote           # Retrofit (UserApi) Tanımı
│   └── repository       # Repository Implementasyonları
├── domain               # İş Mantığı Katmanı
│   ├── model            # Uygulama Modelleri
│   ├── repository       # Repository Arayüzleri (Abstractions)
│   └── usecase          # Bağımsız İş Senaryoları (Get, Add, Update...)
├── ui                   # Presentation Katmanı 
│   ├── components       # Ortak Bileşenler
│   ├── screens          # Ekranlar (Contacts, Detail, Edit, Add)
│   ├── theme            
│   └── util             # NetworkResult, State Tanımları
└── di                   # Dependency Injection Katmanı
```

### Katman Sorumlulukları
* **Domain:** Uygulamanın en içteki "beyin" katmanıdır. Dış katmanlardan (API, DB) tamamen bağımsızdır. `UseCase`'ler aracılığıyla iş mantığını kapsüller ve veriyi UI'a sunar.
* **Data:** Verinin nereden geleceğini (Remote API vs Local DB) yöneten katmandır. `Offline-First` (caching) senkronizasyon mantığı, `Room` veritabanı işlemleri ve görsel sıkıştırma işlemleri konumlandırılmıştır.
* **Presentation (UI):** Jetpack Compose ile deklaratif olarak yazılmıştır. `ViewModel`'ler aracılığıyla **MVI (Event-State)** mimarisinde çalışır.

---

### ✅ Temel Özellikler
* **Kişi Yönetimi:** Ad, soyad, telefon numarası ve fotoğraf bilgilerini kaydetme, düzenleme ve silme fonksiyonları.
* **Lottie Animasyonu:** Başarılı kayıt süreçlerinde kullanıcıya geri bildirim sağlayan interaktif Lottie animasyonu.
* **Alfabetik Gruplandırma:** Kişilerin isimlerinin ilk harfine göre gruplandırılması ve alfabetik sıralanması.
* **Swipe Actions:** Liste elemanlarında sola kaydırma hareketi ile "Sil" ve "Düzenle" butonlarına hızlı erişim.
* **Cihaz Rehberi Entegrasyonu:** Kişinin fiziksel rehberde kayıtlı olup olmadığını belirten icon gösterimi ve "Rehbere Kaydet" özelliği.
* **Dinamik Gölge (Palette API):** Profil ekranındaki görsellerin gölgesi, görseldeki en baskın renge göre otomatik olarak değişir.
* **Gelişmiş Arama:** Boşluk içeren isim-soyisim aramaları ve arama geçmişinin (Search History) saklanarak listelenmesi.
* **Local Cache:** Çekilen tüm veriler yerel veritabanına kaydedilir; uygulama internet olmasa dahi son verilerle kusursuz çalışır.
* **Optimistic Updates:** Veri ekleme/silme işlemleri önce yerelde yansıtılır (UX), ardından arka planda API ile senkronize edilir.
* **Görsel Boyutu Düşürme:** Sunucuya yüklenen görseller, kalite-boyut dengesi gözetilerek (Quality: %60) sıkıştırılır; sunucu yükü minimize edilir.
* **Smart Image Caching:** Coil Image Loader ile RAM ve Disk tabanlı akıllı önbellekleme yapılandırılmıştır.
* **Responsive Tasarım:** Esnek Compose yapıları kullanılmaya çalışılmıştır.

---

## 🛠 Kullanılan Teknolojiler

* **UI:** Jetpack Compose (Material 3)
* **DI:** Hilt (Dagger)
* **Database:** Room Persistence
* **Network:** Retrofit, OkHttp 
* **Async:** Kotlin Coroutines & Flow
* **Media:** Coil, Palette API, Lottie

---

## 🚀 Kurulum ve Derleme
1. Projeyi Android Studio ile açın.
2. `Constants.kt` dosyasına size iletilen `ApiKey` değerini ekleyin.
3. Projeyi derleyin ve bir emülatör/cihaz üzerinde çalıştırın.

---
