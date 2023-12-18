USE latte-art-election;

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 01 déc. 2023 à 10:49
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `latteelection`
--

-- --------------------------------------------------------

--
-- Structure de la table `electeurs`
--

DROP TABLE IF EXISTS `electeurs`;
CREATE TABLE IF NOT EXISTS `electeurs` (
  `Id_electeur` int NOT NULL,
  `Electeur_nom` text NOT NULL,
  `Electeur_prenom` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `electeurs`
--

INSERT INTO `electeurs` (`Id_electeur`, `Electeur_nom`, `Electeur_prenom`) VALUES
(1, 'Dubois', 'Lucas'),
(2, 'Fontaine', 'Sophie'),
(3, 'Girard', 'Emma'),
(4, 'Lefevre', 'Antoine'),
(5, 'Leroy', 'Chloé'),
(6, 'Meyer', 'Hugo'),
(7, 'Moreau', 'Manon'),
(8, 'Dupont', 'Claire'),
(9, 'Rousseau', 'Louis'),
(10, 'Dubois', 'Jean'),
(11, 'Lefevre', 'Lucas'),
(12, 'Meyer', 'Emma'),
(13, 'Rousseau', 'Manon'),
(14, 'Fontaine', 'Antoine'),
(15, 'Girard', 'Hugo'),
(16, 'Leroy', 'Jean'),
(17, 'Dupont', 'Chloé'),
(18, 'Moreau', 'Sophie'),
(19, 'Girard', 'Hugo'),
(20, 'Fontaine', 'Manon'),
(21, 'Lefevre', 'Lucas'),
(22, 'Meyer', 'Louis'),
(23, 'Rousseau', 'Chloé'),
(24, 'Moreau', 'Jean'),
(25, 'Dupont', 'Hugo'),
(26, 'Leroy', 'Lucas'),
(27, 'Fontaine', 'Sophie'),
(28, 'Girard', 'Emma'),
(29, 'Meyer', 'Antoine'),
(30, 'Lefevre', 'Manon'),
(31, 'Rousseau', 'Louis'),
(32, 'Moreau', 'Chloé'),
(33, 'Dupont', 'Jean'),
(34, 'Leroy', 'Hugo'),
(35, 'Fontaine', 'Lucas'),
(36, 'Girard', 'Chloé'),
(37, 'Meyer', 'Sophie'),
(38, 'Lefevre', 'Emma'),
(39, 'Rousseau', 'Manon'),
(40, 'Moreau', 'Antoine'),
(41, 'Rousseau', 'Léonie');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
