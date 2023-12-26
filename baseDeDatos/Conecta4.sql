-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.28-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para conecta4
CREATE DATABASE IF NOT EXISTS `conecta4` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `conecta4`;

-- Volcando estructura para tabla conecta4.detallestablero
CREATE TABLE IF NOT EXISTS `detallestablero` (
  `IdTablero` int(11) NOT NULL,
  `OcupadoJugador1` bit(1) DEFAULT NULL,
  `OcupadoJugador2` bit(1) DEFAULT NULL,
  `Columna` int(11) DEFAULT NULL,
  `Fila` int(11) DEFAULT NULL,
  KEY `IdTablero` (`IdTablero`),
  CONSTRAINT `FK_detallestablero_tablero` FOREIGN KEY (`IdTablero`) REFERENCES `tablero` (`IdTablero`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.detallestablero: ~0 rows (aproximadamente)

-- Volcando estructura para tabla conecta4.esperandoconexion
CREATE TABLE IF NOT EXISTS `esperandoconexion` (
  `IdJugador` int(11) NOT NULL,
  `IdPartida` int(11) NOT NULL,
  KEY `IdJugador` (`IdJugador`),
  KEY `IdPartida` (`IdPartida`),
  CONSTRAINT `FK__jugadores` FOREIGN KEY (`IdJugador`) REFERENCES `jugadores` (`IdJugador`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK__partidas` FOREIGN KEY (`IdPartida`) REFERENCES `partidas` (`IdPartida`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.esperandoconexion: ~0 rows (aproximadamente)

-- Volcando estructura para tabla conecta4.estadisticas
CREATE TABLE IF NOT EXISTS `estadisticas` (
  `IdPartida` int(11) NOT NULL,
  `IdJugador` int(11) NOT NULL,
  `PartidasJugadas` int(11) DEFAULT NULL,
  `Victorias` int(11) DEFAULT NULL,
  KEY `IdPartida` (`IdPartida`),
  KEY `IdJugador` (`IdJugador`),
  CONSTRAINT `FK_estadisticas_jugadores` FOREIGN KEY (`IdJugador`) REFERENCES `jugadores` (`IdJugador`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_estadisticas_partidas` FOREIGN KEY (`IdPartida`) REFERENCES `partidas` (`IdPartida`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.estadisticas: ~0 rows (aproximadamente)

-- Volcando estructura para tabla conecta4.jugadores
CREATE TABLE IF NOT EXISTS `jugadores` (
  `IdJugador` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IdJugador`),
  KEY `IdJugador` (`IdJugador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.jugadores: ~0 rows (aproximadamente)

-- Volcando estructura para tabla conecta4.partidas
CREATE TABLE IF NOT EXISTS `partidas` (
  `IdPartida` int(11) NOT NULL AUTO_INCREMENT,
  `Jugador1` int(11) NOT NULL,
  `Jugador2` int(11) NOT NULL,
  `Turno` bit(1) DEFAULT NULL,
  PRIMARY KEY (`IdPartida`),
  KEY `IdPartida` (`IdPartida`),
  KEY `Jugador1` (`Jugador1`),
  KEY `Jugador2` (`Jugador2`),
  CONSTRAINT `FK_partidas_jugadores` FOREIGN KEY (`Jugador1`) REFERENCES `jugadores` (`IdJugador`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_partidas_jugadores_2` FOREIGN KEY (`Jugador2`) REFERENCES `jugadores` (`IdJugador`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.partidas: ~0 rows (aproximadamente)

-- Volcando estructura para tabla conecta4.tablero
CREATE TABLE IF NOT EXISTS `tablero` (
  `IdTablero` int(11) NOT NULL AUTO_INCREMENT,
  `Columnas` int(11) DEFAULT 0,
  `Filas` int(11) DEFAULT 0,
  `IdPartida` int(11) NOT NULL,
  PRIMARY KEY (`IdTablero`),
  KEY `IdPartida` (`IdPartida`),
  CONSTRAINT `FK_tablero_partidas` FOREIGN KEY (`IdPartida`) REFERENCES `partidas` (`IdPartida`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla conecta4.tablero: ~0 rows (aproximadamente)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
