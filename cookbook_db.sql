-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 08 Wrz 2018, 21:23
-- Wersja serwera: 10.1.34-MariaDB
-- Wersja PHP: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `cookbook`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `id_recipe` int(11) NOT NULL,
  `username` varchar(24) NOT NULL,
  `content` text,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `comments`
--

INSERT INTO `comments` (`id`, `id_recipe`, `username`, `content`, `date`) VALUES
(7, 1, 'jan', 'Super danie. Polecam ;)', '2018-09-01'),
(8, 6, 'karol', 'Bardzo smaczne', '2018-09-01'),
(9, 6, 'karol', ';)', '2018-09-01'),
(10, 1, 'jan', 'ekstra', '2018-09-03'),
(11, 2, 'jan', '9/10', '2018-09-05'),
(12, 8, 'jan', 'Nie smakuje mi', '2018-09-05'),
(13, 1, 'jan', 'test', '2018-09-05'),
(14, 2, 'jan', 'adsdasd', '2018-09-05');

--
-- Wyzwalacze `comments`
--
DELIMITER $$
CREATE TRIGGER `set_date_comment` BEFORE INSERT ON `comments` FOR EACH ROW BEGIN
    SET NEW.date = CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `meals`
--

CREATE TABLE `meals` (
  `id` int(3) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_recipe` int(11) DEFAULT NULL,
  `id_product` int(11) DEFAULT NULL,
  `recipe_name` varchar(24) DEFAULT NULL,
  `product_name` varchar(16) DEFAULT NULL,
  `kcal` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `meals`
--

INSERT INTO `meals` (`id`, `id_user`, `id_recipe`, `id_product`, `recipe_name`, `product_name`, `kcal`) VALUES
(1, 12, 4, NULL, 'test4', NULL, 400),
(2, 12, 6, NULL, 'zxc', NULL, 540),
(3, 12, NULL, 24, NULL, 'smietanka', 200),
(4, 12, NULL, 27, NULL, 'kajzerka', 40),
(8, 1, 5, NULL, 'kolejny test', NULL, 235),
(9, 1, NULL, 25, NULL, 'ziemniaki', 35),
(10, 1, NULL, 31, NULL, 'mas?o', 300),
(11, 12, NULL, 24, NULL, 'smietanka', 200),
(12, 12, 5, NULL, 'kolejny test', NULL, 235),
(13, 12, NULL, 30, NULL, 'piers z kurczaka', 60),
(14, 12, 4, NULL, 'test4', NULL, 400),
(15, 12, NULL, 24, NULL, 'smietanka', 80);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `products`
--

CREATE TABLE `products` (
  `id_prod` int(11) NOT NULL,
  `name` varchar(24) NOT NULL,
  `id_user` int(11) NOT NULL,
  `kcal` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `username` varchar(24) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `products`
--

INSERT INTO `products` (`id_prod`, `name`, `id_user`, `kcal`, `amount`, `username`) VALUES
(17, 'jogurt', 12, 200, 200, 'jan'),
(24, 'smietanka', 12, 200, 100, 'jan'),
(25, 'ziemniaki', 1, 35, 100, 'karol'),
(26, 'chleb', 1, 250, 100, 'karol'),
(27, 'kajzerka', 1, 170, 50, 'karol'),
(30, 'piers z kurczaka', 15, 120, 100, 'slawek'),
(31, 'mas?o', 1, 300, 100, 'karol'),
(32, 'olej', 12, 800, 100, 'jan'),
(35, 'ser', 12, 330, 100, 'jan'),
(36, 'woda', 12, 0, 100, 'jan'),
(37, 'asd', 12, 200, 100, 'jan'),
(38, 'test', 12, 100, 20, 'jan'),
(39, 'test2', 12, 200, 190, 'jan'),
(40, 'test123', 12, -100, 100, 'jan'),
(41, 'test1234', 12, 122, 100, 'jan');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipes`
--

CREATE TABLE `recipes` (
  `id_recipe` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `username` varchar(24) NOT NULL,
  `name` varchar(24) NOT NULL,
  `all_kcal` int(11) DEFAULT NULL,
  `description` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `recipes`
--

INSERT INTO `recipes` (`id_recipe`, `id_user`, `username`, `name`, `all_kcal`, `description`) VALUES
(1, 1, 'karol', 'test', 685, 'test'),
(2, 1, 'karol', 'test2', 590, 'test2'),
(3, 12, 'jan', 'test3', 450, 'test'),
(4, 12, 'jan', 'test4', 400, 'test'),
(5, 12, 'jan', 'kolejny test', 235, 'test'),
(6, 1, 'karol', 'zxc', 540, 'zxc'),
(7, 1, 'karol', 'chleb ze smietana', 130, 'posmaruj chleb smietana'),
(8, 1, 'karol', 'kajzerka piers ziemniaki', 325, 'Usmaz kurczaka, ugotuj ziemniaki'),
(9, 12, 'jan', 'qwe', 120, 'qwe'),
(12, 12, 'jan', 'dddd', 200, 'ffff'),
(13, 12, 'jan', 'yyyy', 35, 'yyyyy'),
(14, 12, 'jan', 'uuu', 440, 'uuu'),
(15, 12, 'jan', 'test123', 85, 'test123'),
(16, 12, 'jan', 'testttttt', 50, 'testtt');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipes_products`
--

CREATE TABLE `recipes_products` (
  `id` int(11) NOT NULL,
  `id_recipe` int(11) NOT NULL,
  `id_prod` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `recipes_products`
--

INSERT INTO `recipes_products` (`id`, `id_recipe`, `id_prod`) VALUES
(1, 1, 17),
(2, 1, 24),
(3, 1, 25),
(4, 1, 26),
(5, 2, 31),
(6, 2, 30),
(7, 2, 27),
(8, 3, 17),
(9, 3, 26),
(10, 4, 17),
(11, 4, 24),
(12, 5, 25),
(13, 5, 17),
(14, 6, 27),
(15, 6, 26),
(16, 6, 30),
(17, 7, 26),
(18, 7, 24),
(19, 8, 27),
(20, 8, 30),
(21, 8, 25),
(22, 9, 26),
(27, 12, 17),
(28, 13, 25),
(29, 14, 24),
(30, 14, 25),
(31, 15, 17),
(32, 15, 25),
(33, 16, 17);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id_user` int(3) NOT NULL,
  `username` varchar(12) NOT NULL,
  `email` varchar(12) NOT NULL,
  `password` varchar(16) NOT NULL,
  `sex` varchar(10) DEFAULT 'Male',
  `age` int(3) DEFAULT NULL,
  `weight` int(3) DEFAULT NULL,
  `height` int(3) DEFAULT NULL,
  `kcal_demand` int(5) DEFAULT NULL,
  `eaten_kcal` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`id_user`, `username`, `email`, `password`, `sex`, `age`, `weight`, `height`, `kcal_demand`, `eaten_kcal`) VALUES
(1, 'karol', 'karol@gmail.', 'qwerty', 'Male', 18, 70, 180, 1890, 570),
(12, 'jan', 'jan@wp.pl', 'lol', 'Male', 22, 90, 197, 2136, 2155),
(15, 'slawek', 'slakop@gmail', 'qwer', 'Male', 21, 85, 185, 2057, NULL),
(16, 'test', 'test@test.pl', 'test', 'Male', NULL, NULL, NULL, NULL, NULL),
(17, 'test2', 'test2', 'test2', 'Male', NULL, NULL, NULL, NULL, NULL);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_recipe` (`id_recipe`) USING BTREE;

--
-- Indeksy dla tabeli `meals`
--
ALTER TABLE `meals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`) USING BTREE,
  ADD KEY `id_recipe` (`id_recipe`),
  ADD KEY `id_product` (`id_product`);

--
-- Indeksy dla tabeli `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id_prod`),
  ADD KEY `id_user` (`id_user`) USING BTREE;

--
-- Indeksy dla tabeli `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`id_recipe`),
  ADD KEY `id_user` (`id_user`) USING BTREE;

--
-- Indeksy dla tabeli `recipes_products`
--
ALTER TABLE `recipes_products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_prod` (`id_prod`) USING BTREE,
  ADD KEY `id_recipe` (`id_recipe`) USING BTREE;

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT dla tabeli `meals`
--
ALTER TABLE `meals`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT dla tabeli `products`
--
ALTER TABLE `products`
  MODIFY `id_prod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT dla tabeli `recipes`
--
ALTER TABLE `recipes`
  MODIFY `id_recipe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT dla tabeli `recipes_products`
--
ALTER TABLE `recipes_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`id_recipe`) REFERENCES `recipes` (`id_recipe`);

--
-- Ograniczenia dla tabeli `meals`
--
ALTER TABLE `meals`
  ADD CONSTRAINT `meals_ibfk_5` FOREIGN KEY (`id_recipe`) REFERENCES `recipes` (`id_recipe`),
  ADD CONSTRAINT `meals_ibfk_6` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_prod`),
  ADD CONSTRAINT `meals_ibfk_7` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Ograniczenia dla tabeli `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Ograniczenia dla tabeli `recipes`
--
ALTER TABLE `recipes`
  ADD CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Ograniczenia dla tabeli `recipes_products`
--
ALTER TABLE `recipes_products`
  ADD CONSTRAINT `recipes_products_ibfk_1` FOREIGN KEY (`id_recipe`) REFERENCES `recipes` (`id_recipe`),
  ADD CONSTRAINT `recipes_products_ibfk_2` FOREIGN KEY (`id_prod`) REFERENCES `products` (`id_prod`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
