CREATE DATABASE  IF NOT EXISTS `carrello` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `carrello`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: carrello
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buyer`
--

DROP TABLE IF EXISTS `buyer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyer` (
  `id_buyer` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  PRIMARY KEY (`id_buyer`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyer`
--

LOCK TABLES `buyer` WRITE;
/*!40000 ALTER TABLE `buyer` DISABLE KEYS */;
INSERT INTO `buyer` VALUES (1,'Leo','Rossi','leo.rossi@gmail.com','rossi','Via Giuseppe Verdi 21'),(2,'Aldo','Ferrari','aldo.ferr@gmail.com','aldo','Via Legnano 11'),(3,'Marina','Mastroleo','marina.mas@gmail.com','tavolo','Via San Siro 23'),(4,'Sonia','Bianchi','sonia.b02@gmail.com','sedia','Via Lambrate 7'),(5,'Giovanni','Lombardo','giovy@gmail.com','computer','Via Duomo 5'),(6,'Beatrice','Ricci','bearicci88@gmail.com','acqua','Via Pacini 33'),(7,'Lucio','Gallo','luciog02@gmail.com','divano','Via Pascal 19'),(8,'Carlo','Esposito','carloesp@gmail.com','carletto23','Via Emilia 20');
/*!40000 ALTER TABLE `buyer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `photo` varchar(45) NOT NULL,
  `id_order` int NOT NULL,
  PRIMARY KEY (`id_item`),
  KEY `id_orrder_idx` (`id_order`),
  CONSTRAINT `id_order` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id_order`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id_order` int NOT NULL AUTO_INCREMENT,
  `name_supplier` varchar(45) NOT NULL,
  `price_tot_products` float NOT NULL,
  `delivery_costs` float NOT NULL,
  `delivery_date` date NOT NULL,
  `address_buyer` varchar(45) NOT NULL,
  `num_ordered_articles` int DEFAULT NULL,
  `id_buyer` int NOT NULL,
  `time` timestamp NOT NULL,
  PRIMARY KEY (`id_order`),
  KEY `cod_acquirente` (`id_buyer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id_product` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  `merchandise_category` varchar(45) NOT NULL,
  `photo` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_product`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'scarpa lacci','sportivo','calzature','scarpa_lacci.png'),(2,'stivali','casual','calzature','stivali.png'),(3,'maglietta maniche corte','elegante','abbigliamento','tshirt.png'),(4,'maglietta maniche lunghe','sportivo','abbigliamento','maglia_lunghe.png'),(5,'giacca','sportivo','abbigliamento','giacca.png'),(6,'cintura','casual','abbigliamento','cintura.png'),(7,'pantaloni','elegante','abbigliamento','pantaloni.png'),(8,'sandali','elegante','calzature','sandali.png'),(9,'vestito','elegante','abbigliamento','vestito.png'),(10,'gonna','casual','abbigliamento','gonna.png'),(11,'pantaloncini','casual','abbigliamento','pantaloncini.png'),(12,'gilet','elegante','abbigliamento','gilet.png'),(13,'sciarpa','sportivo','abbigliamento','sciarpa.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranges`
--

DROP TABLE IF EXISTS `ranges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ranges` (
  `id_range` int NOT NULL AUTO_INCREMENT,
  `num_min_articles` int NOT NULL,
  `num_max_articles` int DEFAULT NULL,
  `shipping_costs` float NOT NULL,
  `id_supplier` int NOT NULL,
  PRIMARY KEY (`id_range`),
  KEY `id_supplier` (`id_supplier`),
  CONSTRAINT `id_supplier` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id_supplier`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranges`
--

LOCK TABLES `ranges` WRITE;
/*!40000 ALTER TABLE `ranges` DISABLE KEYS */;
INSERT INTO `ranges` VALUES (1,1,3,15,1),(2,4,10,20,1),(3,10,NULL,40,1),(4,1,2,10,2),(5,3,7,15,2),(6,8,NULL,16,2),(7,1,2,20,3),(8,3,5,30,3),(9,6,NULL,40,3),(10,1,4,15,4),(11,5,8,20,4),(12,9,15,25,4),(13,16,NULL,30,4),(14,1,2,10,5),(15,3,4,20,5),(16,5,NULL,15,5);
/*!40000 ALTER TABLE `ranges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_conditions`
--

DROP TABLE IF EXISTS `sales_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_conditions` (
  `id_sale` int NOT NULL AUTO_INCREMENT,
  `id_product` int NOT NULL,
  `id_supplier` int NOT NULL,
  `unit_price` float NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id_sale`),
  KEY `id_product` (`id_product`),
  KEY `id_supplier` (`id_supplier`),
  CONSTRAINT `id_product` FOREIGN KEY (`id_product`) REFERENCES `product` (`id_product`) ON UPDATE CASCADE,
  CONSTRAINT `id_supplier2` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id_supplier`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_conditions`
--

LOCK TABLES `sales_conditions` WRITE;
/*!40000 ALTER TABLE `sales_conditions` DISABLE KEYS */;
INSERT INTO `sales_conditions` VALUES (1,1,1,100,0),(2,3,1,20,20),(3,4,1,30,40),(4,7,1,50,50),(5,6,1,15,40),(6,1,2,110,20),(7,3,2,25,50),(8,4,2,35,40),(9,7,2,55,60),(10,2,3,110,20),(11,3,3,80,10),(12,5,3,150,50),(13,6,3,50,40),(14,1,4,45,50),(15,5,4,38,60),(16,3,5,15,20),(17,4,5,20,10),(18,7,5,30,30),(19,8,2,40,10),(20,9,2,50,20),(21,10,2,15,15),(22,11,2,10,40),(23,12,2,20,20),(24,13,2,7,15);
/*!40000 ALTER TABLE `sales_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `selected_product`
--

DROP TABLE IF EXISTS `selected_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `selected_product` (
  `id_sel_prod` int NOT NULL AUTO_INCREMENT,
  `views_time` timestamp NOT NULL,
  `quantity_selected` int DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `id_buyer` int NOT NULL,
  `id_product` int NOT NULL,
  `id_range` int DEFAULT NULL,
  PRIMARY KEY (`id_sel_prod`),
  KEY `id_buyer_idx` (`id_buyer`),
  KEY `id_product_idx` (`id_product`),
  KEY `id_range_idx` (`id_range`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `selected_product`
--

LOCK TABLES `selected_product` WRITE;
/*!40000 ALTER TABLE `selected_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `selected_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `sub_vista`
--

DROP TABLE IF EXISTS `sub_vista`;
/*!50001 DROP VIEW IF EXISTS `sub_vista`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `sub_vista` AS SELECT 
 1 AS `a`,
 1 AS `b`,
 1 AS `c`,
 1 AS `d`,
 1 AS `e`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id_supplier` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `num_stars` int NOT NULL,
  `amount` float NOT NULL,
  `removed` tinyint NOT NULL,
  PRIMARY KEY (`id_supplier`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'Adidas',3,45,0),(2,'Nike',4,100,0),(3,'Gucci',5,700,0),(4,'Puma',2,30,0),(5,'Zara',1,20,1);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'carrello'
--

--
-- Final view structure for view `sub_vista`
--

/*!50001 DROP VIEW IF EXISTS `sub_vista`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sub_vista` (`a`,`b`,`c`,`d`,`e`) AS select `p2`.`id_product` AS `id_product`,`p2`.`name` AS `name`,`p2`.`description` AS `description`,`p2`.`merchandise_category` AS `merchandise_category`,`p2`.`photo` AS `photo` from ((`product` `p2` join `buyer` `b2`) join `selected_product` `s2`) where ((`b2`.`id_buyer` = 2) and (`b2`.`id_buyer` = `s2`.`id_buyer`) and (`s2`.`id_product` = `p2`.`id_product`)) order by `s2`.`views_time` desc limit 5 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-13 14:12:48
