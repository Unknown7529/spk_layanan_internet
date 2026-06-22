# 🌐 SPK Pemilihan Paket Layanan Internet Menggunakan Metode AHP

> Sistem Pendukung Keputusan (SPK) berbasis **Java Desktop** untuk membantu pengguna memilih paket layanan internet terbaik menggunakan metode **Analytical Hierarchy Process (AHP)**. Dibangun dengan **Java Swing** dan database **MySQL**.

---

## 📋 Daftar Isi

- [Tentang Aplikasi](#tentang-aplikasi)
- [Metode AHP](#metode-ahp)
- [Fitur](#fitur)
- [Teknologi yang Digunakan](#teknologi-yang-digunakan)
- [Struktur Database](#struktur-database)
- [Kriteria dan Sub-Kriteria](#kriteria-dan-sub-kriteria)
- [Prasyarat](#prasyarat)
- [Instalasi & Konfigurasi](#instalasi--konfigurasi)
- [Cara Menjalankan](#cara-menjalankan)
- [Struktur Proyek](#struktur-proyek)
- [Lisensi](#lisensi)

---

## 📖 Tentang Aplikasi

Aplikasi ini merupakan Sistem Pendukung Keputusan yang dirancang untuk membantu pengguna membandingkan dan memilih paket layanan internet secara sistematis dan objektif. Data paket layanan dikelola melalui antarmuka admin, kemudian sistem menghitung peringkat terbaik menggunakan metode AHP berdasarkan kriteria dan sub-kriteria yang telah ditentukan.

Aplikasi dibangun sebagai **desktop application** menggunakan **Java Swing** dengan IDE **NetBeans**, terkoneksi ke database **MySQL**, dan mendukung ekspor laporan dalam format **PDF** menggunakan pustaka iTextPDF.

---

## 🧮 Metode AHP

**Analytical Hierarchy Process (AHP)** yang diimplementasikan dalam aplikasi ini bekerja melalui tahapan berikut:

### 1. Matriks Perbandingan Berpasangan
Sistem membuat matriks perbandingan berpasangan antar kriteria (5×5) dan antar sub-kriteria (2×2, 3×3, 4×4) menggunakan skala Saaty.

### 2. Normalisasi Matriks
Setiap elemen dibagi dengan jumlah kolomnya untuk menghasilkan matriks ternormalisasi.

### 3. Perhitungan Bobot Prioritas
Rata-rata baris dari matriks ternormalisasi menjadi vektor prioritas (bobot) setiap kriteria/sub-kriteria.

### 4. Uji Konsistensi
Sistem menghitung **Consistency Ratio (CR)** secara otomatis. Keputusan dianggap konsisten jika **CR ≤ 0,10**.

```
λ_maks → CI = (λ_maks - n) / (n - 1) → CR = CI / RI
```

**Nilai Random Index (RI) yang digunakan:**

| n  | 1    | 2    | 3    | 4    | 5    | 6    | 7    | 8    | 9    | 10   |
|----|------|------|------|------|------|------|------|------|------|------|
| RI | 0.00 | 0.00 | 0.58 | 0.90 | 1.12 | 1.24 | 1.32 | 1.41 | 1.45 | 1.49 |

### 5. Perangkingan Alternatif
Setiap paket layanan diberi skor berdasarkan bobot kriteria × nilai sub-kriteria, kemudian diurutkan dari skor tertinggi ke terendah.

---

## ✨ Fitur

- 🔐 **Login Admin** — autentikasi pengguna dengan username dan password
- 📋 **Manajemen Data Layanan Internet** — tambah, edit, hapus data paket internet
- ⚙️ **Pengaturan Kriteria** — kelola nama kriteria dan prioritas kepentingan
- ⚙️ **Pengaturan Sub-Kriteria** — kelola nilai dan bobot setiap sub-kriteria
- 🧮 **Perhitungan AHP Otomatis** — komputasi matriks berpasangan, normalisasi, bobot prioritas, dan uji konsistensi
- 🏆 **Hasil Seleksi & Perangkingan** — tampilan ranking paket internet berdasarkan hasil penilaian AHP
- 📄 **Ekspor Laporan PDF** — cetak hasil penilaian dan ranking ke dalam file PDF

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| Java | 17 | Bahasa pemrograman utama (source & target: 17) |
| Java Swing | — | Framework GUI desktop |
| NetBeans IDE | — | IDE yang digunakan untuk pengembangan |
| MySQL | — | Database penyimpanan data |
| MySQL Connector/J | 5.1.49 | Driver koneksi Java ke MySQL |
| iTextPDF | 5.3.5 | Pustaka pembuatan laporan PDF |
| AbsoluteLayout | — | Layout manager untuk komponen Swing |

---

## 🗄️ Struktur Database

**Nama Database:** `spk_layanan_internet`

**Koneksi default:**
```
Host     : localhost
Database : spk_layanan_internet
Username : root
Password : (kosong)
```

**Tabel-tabel yang digunakan:**

| Tabel | Keterangan |
|-------|------------|
| `register` | Data akun admin (user, password) |
| `layanan_internet` | Data paket internet (no_id, nama_layanan, fiber, harga, fup, bandwidth, layanan_tambahan) |
| `kriteria` | Data kriteria AHP (kd_kriteria, nama_kriteria, prioritas_kepentingan) |
| `sub_kriteria` | Data sub-kriteria setiap kriteria |
| `seleksi` | Hasil penilaian AHP per paket (no_id, hasil_penilaian) |

---

## 📊 Kriteria dan Sub-Kriteria

Sistem menggunakan **5 kriteria** dengan matriks perbandingan berpasangan 5×5:

| Kode | Kriteria | Keterangan |
|------|----------|------------|
| K1 | **Status Fiber** | Apakah jaringan berbasis fiber optik |
| K2 | **Harga** | Biaya berlangganan per bulan |
| K3 | **FUP** (Fair Usage Policy) | Ada/tidaknya batas kecepatan setelah kuota habis |
| K4 | **Bandwidth** | Kecepatan koneksi internet (Mbps) |
| K5 | **Layanan Tambahan** | Paket bundling yang disertakan |

**Sub-kriteria per kriteria:**

| Kriteria | Ukuran Matriks | Sub-Kriteria |
|----------|---------------|--------------|
| Status Fiber | 2×2 | Belum Fiber, Sudah Fiber |
| Harga | 3×3 | < Rp250rb, Rp250rb–500rb, > Rp500rb |
| FUP | 2×2 | Tidak Ada FUP, Ada FUP |
| Bandwidth | 3×3 | 30 Mbps, < 100 Mbps, ≥ 100 Mbps |
| Layanan Tambahan | 4×4 | Internet saja / + TV BOX / + TV BOX + Cloud Storage / + TV BOX + Cloud Storage + Router Mesh |

---

## ⚙️ Prasyarat

Pastikan perangkat Anda sudah memiliki:

- **JDK 17** atau lebih baru → [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **MySQL Server** (versi 5.x atau 8.x) → [Download MySQL](https://dev.mysql.com/downloads/)
- **NetBeans IDE** (opsional, untuk pengembangan) → [Download NetBeans](https://netbeans.apache.org/)

Verifikasi instalasi Java:
```bash
java -version
```

---

## 🚀 Instalasi & Konfigurasi

### 1. Clone atau Ekstrak Proyek

```bash
# Clone dari repositori
git clone https://github.com/username/spk_pemilihan_layanan_internet.git

# Atau ekstrak dari file ZIP
unzip spk_pemilihan_layanan_internet.zip
```

### 2. Import Database MySQL

Buat database dan import struktur tabel:

```sql
CREATE DATABASE spk_layanan_internet;
USE spk_layanan_internet;

-- Import file SQL (jika tersedia)
SOURCE /path/to/spk_layanan_internet.sql;
```

Atau buat tabel secara manual sesuai struktur pada bagian [Struktur Database](#struktur-database).

### 3. Konfigurasi Koneksi Database

Koneksi database dikonfigurasi di file:
```
src/koneksi/Koneksi.java
```

```java
String url = "jdbc:mysql://localhost/spk_layanan_internet";
koneksi = DriverManager.getConnection(url, "root", "");
```

Sesuaikan `url`, `username`, dan `password` jika konfigurasi MySQL Anda berbeda.

---

## ▶️ Cara Menjalankan

### Menggunakan NetBeans IDE

1. Buka **NetBeans IDE**
2. Pilih **File → Open Project** → arahkan ke folder proyek
3. Klik kanan proyek → **Run** atau tekan `F6`

### Menggunakan File JAR (Langsung)

```bash
cd dist/
java -jar spk_pemilihan_layanan_internet.jar
```

Pastikan folder `lib/` berada di lokasi yang sama dengan file `.jar`:
```
dist/
├── spk_pemilihan_layanan_internet.jar
└── lib/
    ├── mysql-connector-java-5.1.49.jar
    ├── itextpdf-5.3.5.jar
    └── AbsoluteLayout.jar
```

---

## 📁 Struktur Proyek

```
spk_pemilihan_layanan_internet/
├── src/
│   ├── koneksi/
│   │   └── Koneksi.java               # Kelas koneksi ke database MySQL
│   ├── LoginAdmin/
│   │   └── Login.java                 # Form login admin
│   └── menuutama/
│       ├── MenuUtama.java             # Frame utama & navigasi sidebar
│       ├── DataLayananInternet.java   # Manajemen data paket internet (CRUD)
│       ├── DialogTambahData.java      # Dialog tambah/edit data layanan
│       ├── PengaturanKriteria.java    # Manajemen kriteria AHP
│       ├── PengaturanSubKriteria.java # Manajemen sub-kriteria AHP
│       ├── DialogPerhitunganAHP.java  # Proses & tampilan perhitungan AHP
│       ├── Seleksi.java               # Tampilan hasil ranking & seleksi
│       ├── KriteriaAhp.java           # Logika AHP untuk kriteria (5×5)
│       └── SubKriteriaAhp.java        # Logika AHP untuk sub-kriteria
├── build/
│   └── classes/                       # File .class hasil kompilasi
├── dist/
│   ├── spk_pemilihan_layanan_internet.jar  # File JAR siap jalan
│   └── lib/                           # Library dependensi
│       ├── mysql-connector-java-5.1.49.jar
│       ├── itextpdf-5.3.5.jar
│       └── AbsoluteLayout.jar
├── nbproject/                         # Konfigurasi NetBeans
├── build.xml                          # Ant build script
└── manifest.mf                        # Manifest file JAR
```

---

## 👤 Pengembang

**Naufal Sholahuddin**
- IDE: NetBeans
- Bahasa: Java 17
- GUI Framework: Java Swing

---

## 📄 Lisensi

Proyek ini dikembangkan untuk keperluan akademis. Silakan gunakan dan modifikasi sesuai kebutuhan dengan tetap mencantumkan sumber.

---

> ⭐ Jika proyek ini bermanfaat, jangan lupa berikan bintang pada repositori ini!
