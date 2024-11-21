-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: university
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `asignaciones`
--

DROP TABLE IF EXISTS `asignaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(80) DEFAULT NULL,
  `id_profesor` int NOT NULL,
  `id_curso` int NOT NULL,
  `valor_porcentual` int DEFAULT NULL,
  `fecha_limite` datetime DEFAULT NULL,
  `descripcion` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id_asignacion`),
  KEY `id_profesor` (`id_profesor`),
  KEY `id_curso` (`id_curso`),
  CONSTRAINT `fk_curso_id` FOREIGN KEY (`id_curso`) REFERENCES `cursos` (`curso_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_profesor_id` FOREIGN KEY (`id_profesor`) REFERENCES `usuario` (`identificacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaciones`
--

LOCK TABLES `asignaciones` WRITE;
/*!40000 ALTER TABLE `asignaciones` DISABLE KEYS */;
INSERT INTO `asignaciones` VALUES (2,'Jhashua',1,2,10,'2024-12-28 06:00:00','Nueva'),(3,'dsds',1,2,5,'2024-11-15 06:00:00','sada'),(4,'princesita',1,2,5,'2024-11-01 06:00:00','ffd'),(5,'fdf',1,2,5,'2024-11-01 06:00:00','dsada');
/*!40000 ALTER TABLE `asignaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carreras`
--

DROP TABLE IF EXISTS `carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carreras` (
  `carrera_id` int NOT NULL AUTO_INCREMENT,
  `departamento_id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`carrera_id`),
  KEY `fk_departamento_id_idx` (`departamento_id`),
  CONSTRAINT `fk_departamento_id` FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`departamento_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carreras`
--

LOCK TABLES `carreras` WRITE;
/*!40000 ALTER TABLE `carreras` DISABLE KEYS */;
INSERT INTO `carreras` VALUES (1,14,'Sistemas'),(4,16,'Topografia');
/*!40000 ALTER TABLE `carreras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `curso_id` int NOT NULL AUTO_INCREMENT,
  `carrera_id` int NOT NULL,
  `profesor_id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `informacion` varchar(400) DEFAULT NULL,
  `campos_totales` int(10) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`curso_id`),
  KEY `carrera_id` (`carrera_id`),
  KEY `fk_usuario_profesor_id` (`profesor_id`),
  CONSTRAINT `fk_carreras_id` FOREIGN KEY (`carrera_id`) REFERENCES `carreras` (`carrera_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_profesor_id` FOREIGN KEY (`profesor_id`) REFERENCES `usuario` (`identificacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,4,1,'j','sdads',0000000422),(2,4,1,'sdsddfd','sdad',0000123222),(3,4,1,'ss','dsd',0000000004),(7,4,1,'ss','dsd',0000000004),(8,4,1213123,'ss','dsd',0000000004);
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cursos_usuarios`
--

DROP TABLE IF EXISTS `cursos_usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos_usuarios` (
  `curso_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  PRIMARY KEY (`curso_id`,`usuario_id`),
  KEY `fk_usuario_id_idx` (`usuario_id`),
  CONSTRAINT `fk_cursos_id` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`curso_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`identificacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos_usuarios`
--

LOCK TABLES `cursos_usuarios` WRITE;
/*!40000 ALTER TABLE `cursos_usuarios` DISABLE KEYS */;
INSERT INTO `cursos_usuarios` VALUES (1,2),(2,2),(3,2),(8,2);
/*!40000 ALTER TABLE `cursos_usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamentos`
--

DROP TABLE IF EXISTS `departamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamentos` (
  `departamento_id` int NOT NULL AUTO_INCREMENT,
  `facultad_id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`departamento_id`),
  KEY `facultad_id` (`facultad_id`),
  CONSTRAINT `fk_facultades_id` FOREIGN KEY (`facultad_id`) REFERENCES `facultades` (`facultad_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamentos`
--

LOCK TABLES `departamentos` WRITE;
/*!40000 ALTER TABLE `departamentos` DISABLE KEYS */;
INSERT INTO `departamentos` VALUES (14,9,'Ingenieria'),(15,10,'Jhashua'),(16,12,'Topografia'),(17,10,'Jhashua'),(18,9,'Nuevo');
/*!40000 ALTER TABLE `departamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documentos`
--

DROP TABLE IF EXISTS `documentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos` (
  `id_documento` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `url` varchar(60) NOT NULL,
  `hash_` varchar(32) NOT NULL,
  PRIMARY KEY (`id_documento`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `fk_estudiante_id` FOREIGN KEY (`id_estudiante`) REFERENCES `usuario` (`identificacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documentos`
--

LOCK TABLES `documentos` WRITE;
/*!40000 ALTER TABLE `documentos` DISABLE KEYS */;
INSERT INTO `documentos` VALUES (1,2,'netflix.com','54'),(16,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(17,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(18,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(19,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(20,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(21,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(22,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(23,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(24,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(25,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(26,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(27,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(28,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(29,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(30,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760'),(31,2,'C:\\Users\\Usuario\\Desktop\\sdsd.txt','239397621760');
/*!40000 ALTER TABLE `documentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facultades`
--

DROP TABLE IF EXISTS `facultades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facultades` (
  `facultad_id` int NOT NULL AUTO_INCREMENT,
  `universidad_id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`facultad_id`),
  KEY `fk_universidad_id_idx` (`universidad_id`),
  CONSTRAINT `fk_universidad_id` FOREIGN KEY (`universidad_id`) REFERENCES `universidades` (`universidad_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facultades`
--

LOCK TABLES `facultades` WRITE;
/*!40000 ALTER TABLE `facultades` DISABLE KEYS */;
INSERT INTO `facultades` VALUES (9,11,'2131'),(10,11,'Jhashua'),(12,11,'Topografia');
/*!40000 ALTER TABLE `facultades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ia`
--

DROP TABLE IF EXISTS `ia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ia` (
  `id_ia` int NOT NULL AUTO_INCREMENT,
  `valor` int(10) unsigned zerofill NOT NULL,
  `respuesta` varchar(200) NOT NULL,
  PRIMARY KEY (`id_ia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ia`
--

LOCK TABLES `ia` WRITE;
/*!40000 ALTER TABLE `ia` DISABLE KEYS */;
/*!40000 ALTER TABLE `ia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ia_documento`
--

DROP TABLE IF EXISTS `ia_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ia_documento` (
  `id_ia` int NOT NULL,
  `id_documento` int NOT NULL,
  PRIMARY KEY (`id_ia`,`id_documento`),
  KEY `id_ia` (`id_ia`),
  KEY `id_documento` (`id_documento`),
  CONSTRAINT `fk_documento_id` FOREIGN KEY (`id_documento`) REFERENCES `documentos` (`id_documento`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ia_id` FOREIGN KEY (`id_ia`) REFERENCES `ia` (`id_ia`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ia_documento`
--

LOCK TABLES `ia_documento` WRITE;
/*!40000 ALTER TABLE `ia_documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ia_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lista_asignaciones`
--

DROP TABLE IF EXISTS `lista_asignaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lista_asignaciones` (
  `id_asignacion` int NOT NULL,
  `id_estudiante` int NOT NULL,
  `id_documento` int NOT NULL,
  `calificado` tinyint(1) unsigned zerofill DEFAULT NULL,
  `valor_obtenido` int(10) unsigned zerofill DEFAULT NULL,
  `fecha_entregado` datetime DEFAULT NULL,
  `comentario` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id_asignacion`,`id_estudiante`,`id_documento`),
  KEY `id_asignacion` (`id_asignacion`),
  KEY `id_estudiante` (`id_estudiante`),
  KEY `fk_documento_id_idx` (`id_documento`),
  CONSTRAINT `fk_asignaciones_id` FOREIGN KEY (`id_asignacion`) REFERENCES `asignaciones` (`id_asignacion`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_documento_extra_id` FOREIGN KEY (`id_documento`) REFERENCES `documentos` (`id_documento`),
  CONSTRAINT `fk_propietario_id` FOREIGN KEY (`id_estudiante`) REFERENCES `usuario` (`identificacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lista_asignaciones`
--

LOCK TABLES `lista_asignaciones` WRITE;
/*!40000 ALTER TABLE `lista_asignaciones` DISABLE KEYS */;
INSERT INTO `lista_asignaciones` VALUES (2,2,24,1,0000000000,'2024-11-21 03:14:51','sd'),(3,2,23,1,0000000000,'2024-11-21 03:12:27','sds'),(4,2,31,0,0000000000,'2024-11-21 03:59:00','Comentarios');
/*!40000 ALTER TABLE `lista_asignaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `universidades`
--

DROP TABLE IF EXISTS `universidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `universidades` (
  `universidad_id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`universidad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `universidades`
--

LOCK TABLES `universidades` WRITE;
/*!40000 ALTER TABLE `universidades` DISABLE KEYS */;
INSERT INTO `universidades` VALUES (0,'Administrador'),(1,'Jhashua'),(11,'12133');
/*!40000 ALTER TABLE `universidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `identificacion` int NOT NULL,
  `pass` varchar(32) NOT NULL,
  `token` varchar(32) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `rol` varchar(45) NOT NULL,
  `universidad_id` int DEFAULT NULL,
  PRIMARY KEY (`identificacion`),
  KEY `fk_usuario_universidad` (`universidad_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'1','daeb3e67bed6bc354dab1f7a7cb50ca8','Josuar22','Sapo','j','Profesor',11),(2,'2','39b4cd772d966aa7ed573810c1ee2e83','222','sda','2','Estudiante',11),(3,'1','123','Jhashua22',' Canton','jhashua10@gmail.com','Admin',11),(12,'2','8ec9c8f5f232ec79b503a26e88b5e18e','sdasd','sdasd','jhas','Estudiante',11),(232,'asd','0583f4207d6b6712b2edfa2cf376518c','sds','asd','ad','Estudiante',11),(1213123,'sds','6a23f6d0c5d956fc7556064fef572e6f','sdsdsds','sdsd','sdsds','Profesor',11),(1703190175,'sadasa','b7e0c4cdd3aa183022411e59a6f72cd4','Jhashua','cac','sdad','Estudiante',11);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-20 22:04:28
