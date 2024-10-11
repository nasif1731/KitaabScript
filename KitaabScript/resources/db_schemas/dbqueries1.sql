-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: kitaab_script
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pagination`
--

DROP TABLE IF EXISTS `pagination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagination` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text_file_id` int(11) NOT NULL,
  `page_number` int(11) NOT NULL,
  `page_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `text_file_id` (`text_file_id`,`page_number`),
  CONSTRAINT `pagination_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagination`
--

LOCK TABLES `pagination` WRITE;
/*!40000 ALTER TABLE `pagination` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recently_opened_files`
--

DROP TABLE IF EXISTS `recently_opened_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recently_opened_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text_file_id` int(11) NOT NULL,
  `opened_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `text_file_id` (`text_file_id`),
  CONSTRAINT `recently_opened_files_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recently_opened_files`
--

LOCK TABLES `recently_opened_files` WRITE;
/*!40000 ALTER TABLE `recently_opened_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `recently_opened_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_files`
--

DROP TABLE IF EXISTS `text_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `hash` char(32) NOT NULL,
  `word_count` int(11) NOT NULL DEFAULT 0,
  `language` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Urdu',
  PRIMARY KEY (`id`),
  UNIQUE KEY `filename` (`filename`),
  UNIQUE KEY `hash` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_files`
--

LOCK TABLES `text_files` WRITE;
/*!40000 ALTER TABLE `text_files` DISABLE KEYS */;
INSERT INTO `text_files` VALUES (1,'example_urdu.txt','یہ اردو مواد ہے','2024-10-08 19:55:09','2024-10-08 19:55:09','5a105e8b9d40e1329780f018d1c4e9b6',50,'Urdu'),(2,'example_arabic.txt','هذا محتوى باللغة العربية','2024-10-08 19:55:40','2024-10-08 19:55:40','e0d0c5f98b00b204e99111098df12345',60,'Arabic');
/*!40000 ALTER TABLE `text_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tfidf_analysis`
--

DROP TABLE IF EXISTS `tfidf_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tfidf_analysis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text_file_id` int(11) NOT NULL,
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `term_frequency` float NOT NULL,
  `inverse_document_frequency` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `text_file_id` (`text_file_id`,`term`),
  CONSTRAINT `tfidf_analysis_ibfk_1` FOREIGN KEY (`text_file_id`) REFERENCES `text_files` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tfidf_analysis`
--

LOCK TABLES `tfidf_analysis` WRITE;
/*!40000 ALTER TABLE `tfidf_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `tfidf_analysis` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-09  1:03:15
