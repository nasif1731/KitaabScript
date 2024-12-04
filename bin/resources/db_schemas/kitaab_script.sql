-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 03, 2024 at 10:00 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kitaab_script`
--

-- --------------------------------------------------------

--
-- Table structure for table `pagination`
--

CREATE TABLE `pagination` (
  `id` int(11) NOT NULL,
  `text_file_id` int(11) NOT NULL,
  `page_number` int(11) NOT NULL,
  `page_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pagination`
--

INSERT INTO `pagination` (`id`, `text_file_id`, `page_number`, `page_content`) VALUES
(1, 2, 1, 'بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيمِ. إنَّ فِي خَلْقِ السَّمَاوَاتِ وَالأَرْضِ وَاخْتِلَافِ اللَّيْلِ وَالنَّهَارِ لَآيَاتٍ لِّأُوْلِي الألْبَابِ. الَّذِينَ يَذْكُرُونَ اللهَ قِيَامًا وَقُعُودًا وَعَلَى جُنُوبِهِمْ وَيَتَفَكَّرُونَ فِي خَلْقِ السَّمَاوَاتِ وَالأَرْضِ.'),
(2, 2, 2, 'رَبَّنَا مَا خَلَقْتَ هَذَا بَاطِلًا سُبْحَانَكَ فَقِنَا عَذَابَ النَّارِ. رَبَّنَا إِنَّكَ مَن تُدْخِلِ النَّارَ فَقَدْ أَخْزَيْتَهُ وَمَا لِلظَّالِمِينَ مِنْ أَنصَارٍ.'),
(3, 2, 3, 'رَبَّنَا وَأَدْخِلْنَا جَنَّاتٍ تَجْرِي مِن تَحْتِهَا الأَنْهَارُ خَالِدِينَ فِيهَا وَعَدَ اللهِ حَقًّا وَمَنْ أَصْدَقُ مِنَ اللهِ قِيلًا. رَبَّنَا وَتَقَبَّلْ مِنَّا صَالِحَ الأَعْمَالِ وَاغْفِرْ لَنَا ذُنُوبَنَا وَتَوَفَّنَا مُسْلِمِينَ.'),
(4, 2, 4, 'اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا وَعَمَلًا مُتَقَبَّلًا. اللَّهُمَّ اجْعَلْنَا مِنَ الصَّالِحِينَ وَاغْفِرْ لَنَا وَارْحَمْنَا يَا أَرْحَمَ الرَّاحِمِينَ.'),
(5, 2, 5, 'سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا أَنْتَ، أَسْتَغْفِرُكَ وَأَتُوبُ إِلَيْكَ. وَاجْعَلْ لَنَا فِي هَذَا الْيَوْمِ بَرَكَةً وَنُورًا وَتَسْهِيلًا.'),
(6, 2, 6, 'اللَّهُمَّ اجْعَلْنَا مِنَ الْمُتَذَكِّرِينَ وَاجْعَلْ فِي قُلُوبِنَا نُورًا، وَفِي أَعْمَالِنَا نَجَاحًا، وَفِي أَرْزَاقِنَا بَرَكَةً، وَاجْعَلْنَا مِنَ الْمُؤْمِنِينَ الْمُتَقِينَ.'),
(7, 2, 7, 'اللَّهُمَّ إِنَّا نَسْتَغْفِرُكَ لِكُلِّ ذَنْبٍ، وَنَسْأَلُكَ رَحْمَتَكَ وَمَغْفِرَتَكَ. اجْعَلْنَا مِمَّنْ يُقِيمُونَ الصَّلَاةَ وَيُقَدِّمُونَ الصَّدَقَاتِ، وَرَحْمَتَكَ عَلَى عِبَادِكَ الصَّالِحِينَ.'),
(8, 35, 1, 'hhjkk'),
(10, 36, 1, 'hjjkk');

-- --------------------------------------------------------

--
-- Table structure for table `recently_opened_files`
--

CREATE TABLE `recently_opened_files` (
  `id` int(11) NOT NULL,
  `text_file_id` int(11) NOT NULL,
  `opened_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `text_files`
--

CREATE TABLE `text_files` (
  `id` int(11) NOT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `hash` varchar(64) DEFAULT NULL,
  `word_count` int(11) NOT NULL DEFAULT 0,
  `language` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Urdu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `text_files`
--

INSERT INTO `text_files` (`id`, `filename`, `created_at`, `updated_at`, `hash`, `word_count`, `language`) VALUES
(2, 'example_arabic.txt', '2024-10-08 19:55:40', '2024-11-03 07:16:31', 'e0d0c5f98b00b204e99111098df12345', 27, 'Arabic'),
(24, 'some points to ponder.txt', '2024-10-25 09:44:43', '2024-10-25 09:44:43', '2bcd801c11ad6d45303006c84a7d4e64732b5de6', 53, 'English'),
(26, 'فائل.txt', '2024-10-25 09:54:51', '2024-10-25 09:54:51', 'ccecc27963d22e4db7ed23d85e39026a71ca443f', 32, 'Urdu'),
(35, 'nehal.txt', '2024-11-02 22:47:09', '2024-11-02 22:47:13', 'e7f3a96cd58c855a30fa7cf03459728e479fead3', 1, 'Urdu'),
(36, 'hi.txt', '2024-11-03 07:51:22', '2024-11-03 07:51:44', '277ec925331003aeb2dfcc3c54c72cf486ccebb5', 1, 'Urdu');

-- --------------------------------------------------------

--
-- Table structure for table `tfidf_analysis`
--

CREATE TABLE `tfidf_analysis` (
  `id` int(11) NOT NULL,
  `text_file_id` int(11) NOT NULL,
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `term_frequency` float NOT NULL,
  `inverse_document_frequency` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transliterations`
--

CREATE TABLE `transliterations` (
  `id` int(11) NOT NULL,
  `pagination_id` int(11) NOT NULL,
  `original_text` text NOT NULL,
  `transliterated_text` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transliterations`
--

INSERT INTO `transliterations` (`id`, `pagination_id`, `original_text`, `transliterated_text`) VALUES
(1, 1, ' وَعَلَى جُنُوبِهِمْ وَيَتَفَكَّرُونَ فِي خَلْقِ السَّمَاوَاتِ وَالأَرْض', ' wa`alaى junuwbihim wayatafakkaruwna fiy khalqi aalssamaaawaaati waaalaarḍ');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pagination`
--
ALTER TABLE `pagination`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `text_file_id` (`text_file_id`,`page_number`);

--
-- Indexes for table `recently_opened_files`
--
ALTER TABLE `recently_opened_files`
  ADD PRIMARY KEY (`id`),
  ADD KEY `text_file_id` (`text_file_id`);

--
-- Indexes for table `text_files`
--
ALTER TABLE `text_files`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `filename` (`filename`),
  ADD UNIQUE KEY `hash` (`hash`);

--
-- Indexes for table `tfidf_analysis`
--
ALTER TABLE `tfidf_analysis`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `text_file_id` (`text_file_id`,`term`);

--
-- Indexes for table `transliterations`
--
ALTER TABLE `transliterations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pagination_transliteration` (`pagination_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pagination`
--
ALTER TABLE `pagination`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `recently_opened_files`
--
ALTER TABLE `recently_opened_files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `text_files`
--
ALTER TABLE `text_files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `tfidf_analysis`
--
ALTER TABLE `tfidf_analysis`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transliterations`
--
ALTER TABLE `transliterations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pagination`
--
ALTER TABLE `pagination`
  ADD CONSTRAINT `pagination_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `recently_opened_files`
--
ALTER TABLE `recently_opened_files`
  ADD CONSTRAINT `recently_opened_files_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tfidf_analysis`
--
ALTER TABLE `tfidf_analysis`
  ADD CONSTRAINT `tfidf_analysis_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `transliterations`
--
ALTER TABLE `transliterations`
  ADD CONSTRAINT `fk_pagination_transliteration` FOREIGN KEY (`pagination_id`) REFERENCES `pagination` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
