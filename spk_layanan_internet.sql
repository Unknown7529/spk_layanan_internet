-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Feb 2026 pada 08.33
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spk_layanan_internet`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` char(3) NOT NULL,
  `namalengkap` varchar(20) DEFAULT NULL,
  `user` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `namalengkap`, `user`, `password`) VALUES
('001', 'abeil', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kriteria`
--

CREATE TABLE `kriteria` (
  `kd_kriteria` char(3) NOT NULL,
  `nama_kriteria` varchar(30) NOT NULL,
  `prioritas_kepentingan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kriteria`
--

INSERT INTO `kriteria` (`kd_kriteria`, `nama_kriteria`, `prioritas_kepentingan`) VALUES
('K1', 'Fiber Optic', 'Sangat Penting ke-1'),
('K2', 'Harga', 'Penting ke-2'),
('K3', 'FUP ( Fair Play Usage)', 'Cukup Penting ke-3'),
('K4', 'Bandwidth', 'Kurang Penting ke-4'),
('K5', 'Layanan Tambahan', 'Biasa ke-5');

-- --------------------------------------------------------

--
-- Struktur dari tabel `layanan_internet`
--

CREATE TABLE `layanan_internet` (
  `no_id` int(10) NOT NULL,
  `nama_layanan` varchar(100) DEFAULT NULL,
  `keterangan` text DEFAULT NULL,
  `layanan_tambahan` varchar(100) DEFAULT NULL,
  `harga` varchar(50) DEFAULT NULL,
  `bandwidth` varchar(50) DEFAULT NULL,
  `fup` varchar(20) DEFAULT NULL,
  `fiber` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `layanan_internet`
--

INSERT INTO `layanan_internet` (`no_id`, `nama_layanan`, `keterangan`, `layanan_tambahan`, `harga`, `bandwidth`, `fup`, `fiber`) VALUES
(1, 'BENEFIT Desa', 'Paket internet dasar untuk wilayah pedesaan', 'Internet saja', '< Rp.250.000', '30 Mbps', 'Ada', 'Belum Fiber'),
(2, 'BENEFIT ECO', 'Paket hemat untuk penggunaan rumah tangga', 'Internet + TV BOX', 'Rp 250.000 -  Rp.500.000', '30 Mbps', 'Ada', 'Sudah Fiber'),
(3, 'BENEFIT SMART', 'Paket stabil untuk belajar dan kerja daring', 'Internet + TV BOX', 'Rp 250.000 -  Rp.500.000', '< 100 Mbps', 'Tidak Ada', 'Sudah Fiber'),
(4, 'BENEFIT PRO', 'Paket profesional untuk kerja jarak jauh', 'Internet + TV BOX + Cloud Storage', 'Rp 250.000 -  Rp.500.000', '< 100 Mbps', 'Tidak Ada', 'Sudah Fiber'),
(5, 'BENEFIT MAX', 'Paket performa tinggi untuk streaming dan gaming', 'Internet + TV BOX + Cloud Storage', '> Rp.500.000', '> 100 Mbps', 'Tidak Ada', 'Sudah Fiber'),
(6, 'BENEFIT Ultra', 'Paket premium dengan perangkat jaringan tambahan', 'Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)', '> Rp.500.000', '> 100 Mbps', 'Tidak Ada', 'Sudah Fiber'),
(7, 'BENEFIT Rural Plus', 'Paket alternatif untuk daerah belum terjangkau fiber', 'Internet saja', '< Rp.250.000', '30 Mbps', 'Ada', 'Belum Fiber');

-- --------------------------------------------------------

--
-- Struktur dari tabel `register`
--

CREATE TABLE `register` (
  `id` int(3) NOT NULL,
  `email` varchar(50) NOT NULL,
  `user` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `register`
--

INSERT INTO `register` (`id`, `email`, `user`, `password`) VALUES
(1, 'admin', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `seleksi`
--

CREATE TABLE `seleksi` (
  `no_id` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `hasil_penilaian` decimal(4,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `seleksi`
--

INSERT INTO `seleksi` (`no_id`, `nama`, `hasil_penilaian`) VALUES
('1', 'BENEFIT Desa', 0.39),
('2', 'BENEFIT ECO', 0.67),
('3', 'BENEFIT SMART', 0.78),
('4', 'BENEFIT PRO', 0.79),
('5', 'BENEFIT MAX', 0.77),
('6', 'BENEFIT Ultra', 0.78),
('7', 'BENEFIT Rural Plus', 0.39);

-- --------------------------------------------------------

--
-- Struktur dari tabel `sub_kriteria`
--

CREATE TABLE `sub_kriteria` (
  `no_sub` int(3) NOT NULL,
  `kd_kriteria` char(10) NOT NULL,
  `nama_kriteria` varchar(255) NOT NULL,
  `nama_sub_kriteria` varchar(255) NOT NULL,
  `prioritas_kepentingan` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `sub_kriteria`
--

INSERT INTO `sub_kriteria` (`no_sub`, `kd_kriteria`, `nama_kriteria`, `nama_sub_kriteria`, `prioritas_kepentingan`) VALUES
(1, 'K5', 'Layanan Tambahan', 'Internet saja', 'Sangat Penting ke-1'),
(2, 'K5', 'Layanan Tambahan', 'Internet + TV BOX', 'Penting ke-2'),
(3, 'K5', 'Layanan Tambahan', 'Internet + TV BOX + Cloud Storage', 'Cukup Penting ke-3'),
(4, 'K5', 'Layanan Tambahan', 'Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)', 'Biasa ke-4'),
(5, 'K2', 'Harga', '< Rp.250.000', 'Sangat Penting ke-1'),
(6, 'K2', 'Harga', 'Rp 250.000 -  Rp.500.000', 'Cukup Penting ke-2'),
(7, 'K2', 'Harga', '> Rp.500.000', 'Biasa ke-3'),
(8, 'K4', 'Bandwidth', '> 100 Mbps', 'Sangat Penting ke-1'),
(9, 'K4', 'Bandwidth', '< 100 Mbps', 'Cukup Penting ke-2'),
(10, 'K4', 'Bandwidth', '30 Mbps', 'Biasa ke-3'),
(11, 'K3', 'FUP ( Fair Play Usage)', 'Tidak Ada', 'Sangat Penting ke-1'),
(12, 'K3', 'FUP ( Fair Play Usage)', 'Ada', 'Penting ke-2'),
(13, 'K1', 'Fiber Optic', 'Sudah Fiber', 'Sangat Penting ke-1'),
(14, 'K1', 'Fiber Optic', 'Belum Fiber', 'Penting ke-2');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kd_kriteria`);

--
-- Indeks untuk tabel `layanan_internet`
--
ALTER TABLE `layanan_internet`
  ADD PRIMARY KEY (`no_id`);

--
-- Indeks untuk tabel `register`
--
ALTER TABLE `register`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `seleksi`
--
ALTER TABLE `seleksi`
  ADD UNIQUE KEY `no_id` (`no_id`);

--
-- Indeks untuk tabel `sub_kriteria`
--
ALTER TABLE `sub_kriteria`
  ADD PRIMARY KEY (`no_sub`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `layanan_internet`
--
ALTER TABLE `layanan_internet`
  MODIFY `no_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `register`
--
ALTER TABLE `register`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
