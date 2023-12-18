USE latte-art-election;

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 01 déc. 2023 à 10:48
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
-- Structure de la table `candidats`
--

DROP TABLE IF EXISTS `candidats`;
CREATE TABLE IF NOT EXISTS `candidats` (
  `Id_candidat` int NOT NULL,
  `Nom` text NOT NULL,
  `Prenom` text NOT NULL,
  `Presentation` text NOT NULL,
  `Chemin_photo_profil` text NOT NULL,
  `Chemin_photo_latte` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `candidats`
--

INSERT INTO `candidats` (`Id_candidat`, `Nom`, `Prenom`, `Presentation`, `Chemin_photo_profil`, `Chemin_photo_latte`) VALUES
(1, 'Martin', 'Emile', 'Emile, passionné de café depuis son premier expresso, jongle avec les grains comme un artiste avec ses couleurs. Son engagement envers le latte art est un hommage à la beauté éphémère du café, une invitation à plonger dans une expérience sensorielle unique.', 'images/Barista/Emile.jpeg', 'images/latte1.jpg'),
(2, 'Bernard', 'Jules', 'Jules, amoureux de la tradition et de l\'innovation, crée des motifs latte uniques inspirés par l\'héritage du café. Son objectif est de capturer l\'essence du café dans des dessins qui transcendent les frontières culturelles.', 'images/Barista/Jules.jpeg', 'images/latte2.jpg'),
(3, 'Petit', 'Louis', 'Louis, barista autodidacte, crée des chefs-d\'œuvre avec de la mousse et du lait. Sa motivation réside dans la quête de l\'innovation : explorer de nouvelles techniques et repousser les limites de l\'art du latte pour étonner et inspirer.', 'images/Barista/Louis.jpeg', 'images/latte3.jpeg'),
(4, 'Thomas', 'Manon', 'Manon, artisanne du café, trouve la poésie dans chaque motif dessiné à la surface d\'une tasse. Son engagement envers le latte art est une manifestation de son désir d\'apporter de la joie, de l\'émerveillement et de la chaleur dans la vie des autres.', 'images/Barista/Manon.jpeg', 'images/latte4.jpg'),
(5, 'Moreau', 'Paul', 'Paul, le maître du mélange et de la précision, trouve son inspiration dans chaque goutte de café. Son talent en latte art est une extension de sa philosophie : chaque tasse raconte une histoire, et chaque dessin évoque une émotion différente.', 'images/Barista/Paul.jpeg', 'images/latte5.jpeg'),
(6, 'Dubois', 'Victoire', 'Victoire, passionnée par l\'union entre l\'art et le café, transforme chaque tasse en toile. Son latte art est une célébration de la diversité culturelle, un moyen de connecter les gens autour d\'une tasse de café à la fois.', 'images/Barista/Victoire.webp', 'images/latte6.jpg'),
(7, 'Richard', 'Zoé', 'Zoé, exploratrice des arômes et des textures, considère le latte art comme un langage visuel. Chaque dessin représente une histoire unique, une fusion entre sa passion pour l\'art et son amour pour le café.', 'images/Barista/Zoe.jpeg', 'images/latte7.jpg');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
