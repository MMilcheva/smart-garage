-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.5.18-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table smart-garage.brands: ~14 rows (approximately)
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` (`brand_id`, `brand_name`, `isArchived`) VALUES
	(1, 'BMW', 0),
	(2, 'HONDA', 0),
	(3, 'LEXUS', 0),
	(4, 'new brand via mvc to be arch', 1),
	(5, 'brand2 via mvc', 0),
	(6, 'brand3 edited', 0),
	(7, 'brand via postmanNotArc', 0),
	(8, 'brand name from mvc', 0),
	(9, 'new brand via mvc333', 0),
	(10, 'new brand via mvc 21.04', 0),
	(11, 'brand new brand 15h15', 0),
	(12, 'HON', 0),
	(13, 'NewBrand26.04', 0),
	(14, 'new brand 15496', 0);
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;

-- Dumping data for table smart-garage.car_maintenances: ~18 rows (approximately)
/*!40000 ALTER TABLE `car_maintenances` DISABLE KEYS */;
INSERT INTO `car_maintenances` (`car_maintenance_id`, `car_maintenance_name`, `isArchived`) VALUES
	(1, 'Oil change', 0),
	(2, 'Brake fluid change uuu', 1),
	(3, 'Full tech diagnostic', 0),
	(4, 'Oil change222', 0),
	(7, 'OIL AND COOLANT LEVELS CHECK-UP', 0),
	(8, 'AIR FILTER CHECK-UP', 0),
	(9, 'TIRE PRESSURE AND TREAD DEPTH CHECK-UP', 0),
	(10, 'HEADLIGHTS, TURN SIGNALS, BRAKE, AND PARKING LIGHTS CHECK-UP', 0),
	(11, 'OIL & FILTER CHECK-UP', 0),
	(12, 'WAX VEHICLE', 0),
	(13, 'TRANSMISSION FLUID CHECK-UP', 0),
	(14, 'TRANSFER CASE FLUID CHECK-UP', 0),
	(15, 'INSPECT SHOCKS AND STRUTS ', 0),
	(16, 'ROTATE TIRES', 0),
	(17, 'COOLANT FLUID EXCHANGE', 0),
	(18, 'SPARK PLUGS CHECK-UP', 0),
	(19, 'SERPENTINE BELT CHECK-UP', 0),
	(20, 'FRONT AND/OR REAR DIFFERENTIAL CHECK-UP', 0);
/*!40000 ALTER TABLE `car_maintenances` ENABLE KEYS */;

-- Dumping data for table smart-garage.car_models: ~37 rows (approximately)
/*!40000 ALTER TABLE `car_models` DISABLE KEYS */;
INSERT INTO `car_models` (`car_model_id`, `car_model_name`, `brand_id`, `isArchived`) VALUES
	(1, 'M5', 1, 0),
	(2, 'Accord', 2, 0),
	(3, 'LS350', 3, 0),
	(4, 'hsdghdflgjdf ahfadsf', 9, 0),
	(5, 'dgfgdfg_up', 9, 0),
	(6, 'Car model from mvc via brandId', 10, 0),
	(7, 'Car model from mvc via brandId', 10, 0),
	(8, 'Car model 21.04', 10, 0),
	(9, 'Car model 21.04_2', 10, 0),
	(10, 'Car model 21.04_3', 10, 0),
	(11, 'Car model 21.04_5', 10, 0),
	(12, 'Car model 21.04_333', 10, 0),
	(13, 'Car model 21.04ssss', 10, 0),
	(14, 'Car model 216666', 10, 0),
	(15, 'bbb', 11, 0),
	(16, '6666666', 11, 0),
	(17, '6666666', 11, 0),
	(18, '7777', 11, 0),
	(19, '888', 11, 0),
	(20, '999', 11, 0),
	(21, '999555666', 11, 0),
	(22, 'rrrrrroooooooo', 11, 0),
	(23, 'ssssssss', 11, 0),
	(24, 'uuuuuuuuu', 11, 0),
	(25, 'ppppppppppp', 11, 0),
	(26, 'aaaaaaaaa', 11, 0),
	(27, 'zzzzzzzz', 11, 0),
	(28, 'zzzzzzzz', 11, 0),
	(29, 'zzzzzzzz', 11, 0),
	(30, 'llllllllllll', 11, 0),
	(31, 'LS', 12, 0),
	(32, 'new model 26.04', 5, 0),
	(33, 'lsj+-+-+', 12, 0),
	(34, 'Car model 1557-1608', 14, 0),
	(35, 'dgfgdfg_up2233', 2, 0),
	(36, 'dgfgdfg_up1652', 3, 0),
	(37, 'new model 1549-1653', 14, 0);
/*!40000 ALTER TABLE `car_models` ENABLE KEYS */;

-- Dumping data for table smart-garage.discounts: ~9 rows (approximately)
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` (`discount_id`, `user_id`, `discount_amount`, `valid_from`, `valid_to`, `discount_name`, `isArchived`) VALUES
	(2, 2, 10, '2023-04-01', '2023-04-20', 'Discount for loyalty', 0),
	(3, 3, 66, '2023-04-20', '2023-04-24', 'update666 from Postman', 0),
	(4, 11, 30, '2023-04-01', '2099-12-31', 'Discount for loyalty', 0),
	(5, 3, 60, '2023-04-20', '2023-04-25', 'from Postman', 0),
	(6, 3, 22, '2023-04-13', '2099-12-31', 'disc from mvc updated2', 0),
	(7, 2, 45, '2023-04-21', '2023-04-29', 'disc222 from mvc upddd', 0),
	(8, 2, 50, '2023-04-21', '2023-03-30', 'disc from mvc 555upd', 0),
	(9, 3, 33, '2023-04-25', '2023-04-25', 'discount for Gosho99', 0),
	(11, 12, 77, '2023-04-22', '2023-04-26', 'discount test', 1);
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;

-- Dumping data for table smart-garage.orders: ~24 rows (approximately)
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` (`order_id`, `car_maintenance_id`, `date_of_creation`, `visit_id`, `price_id`, `order_name`) VALUES
	(3, 1, '2023-04-09', 2, 1, 'Order'),
	(4, 2, '2023-04-09', 2, 2, 'Order'),
	(5, 3, '2023-04-10', 2, 2, 'Order'),
	(6, 1, '2023-04-10', 5, 1, 'Order'),
	(7, 2, '2023-04-10', 5, 2, 'Order'),
	(8, 3, '2023-04-10', 5, 2, 'Order'),
	(9, 1, '2023-04-11', 7, 1, 'Order'),
	(10, 2, '2023-04-11', 7, 2, 'Order'),
	(11, 3, '2023-04-11', 7, 2, 'Order'),
	(13, 1, '2023-04-14', 7, 1, 'Order'),
	(72, 1, '2023-05-04', 49, 43, 'Order'),
	(73, 1, '2023-05-04', 49, 43, 'Order'),
	(74, 2, '2023-05-04', 50, 37, 'Order'),
	(75, 2, '2023-05-04', 50, 37, 'Order'),
	(76, 4, '2023-05-04', 2, 32, 'Order'),
	(77, 3, '2023-05-04', 51, 31, 'Order'),
	(78, 1, '2023-05-04', 52, 43, 'Order'),
	(79, 1, '2023-05-04', 53, 43, 'Order'),
	(80, 1, '2023-05-04', 54, 43, 'Order'),
	(81, 2, '2023-05-04', 54, 37, 'Order'),
	(82, 3, '2023-05-04', 55, 31, 'Order'),
	(83, 1, '2023-05-04', 56, 43, 'Order'),
	(84, 2, '2023-05-04', 56, 37, 'Order'),
	(85, 1, '2023-05-09', 57, 43, 'Order');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;

-- Dumping data for table smart-garage.password_reset_tokens: ~0 rows (approximately)
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;

-- Dumping data for table smart-garage.payments: ~13 rows (approximately)
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` (`payment_id`, `date_of_payment`, `total_price_BGN`, `visit_id`, `isArchived`, `payment_status`, `list_price`, `discount`, `VAT`, `original_currency`, `exchange_rate`) VALUES
	(4, '2023-05-04 00:00:00', 264, 2, 1, 'PAID', 240, 20, 44, 'BGN', 1),
	(6, '2023-05-01 01:06:37', 264, 5, 0, 'UNPAID', 240, 20, 44, 'BGN', 1),
	(10, '2023-04-11 20:34:25', 264, 7, 1, 'PAID', 240, 20, 44, 'BGN', 1),
	(12, '2023-04-22 00:00:00', 300, 3, 1, 'PAID', 270, 20, 50, 'BGN', 1),
	(39, '2023-04-19 00:00:00', 1028.4, 6, 1, 'PAID', 877, 20, 171.4, 'EUR', 0.5123456),
	(43, '2023-04-22 00:00:00', 288, 4, 1, 'PAID', 270, 30, 48, 'USD', 0.545079),
	(65, '2023-05-04 00:00:00', 451.2, 49, 1, 'PAID', 376, 0, 75.2, 'EUR', 0.5555555),
	(66, '2023-05-04 00:00:00', 264, 50, 1, 'PAID', 220, 0, 44, 'USD', 0.5555555),
	(67, '2023-05-04 00:00:00', 432, 2, 1, 'PAID', 360, 0, 72, 'EUR', 0.5555555),
	(68, '2023-05-04 00:00:00', 225.6, 53, 1, 'PAID', 188, 0, 37.6, 'EUR', 0.511289),
	(69, NULL, 357.6, 54, 0, 'UNPAID', 298, 0, 59.6, 'USD', 0.566001),
	(70, '2023-05-04 00:00:00', 357.6, 56, 1, 'PAID', 298, 0, 59.6, 'EUR', 0.511359),
	(71, '2023-05-09 00:00:00', 225.6, 57, 1, 'PAID', 188, 0, 37.6, 'EUR', 0.511759);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;

-- Dumping data for table smart-garage.prices: ~20 rows (approximately)
/*!40000 ALTER TABLE `prices` DISABLE KEYS */;
INSERT INTO `prices` (`price_id`, `car_maintenance_id`, `amount`, `valid_from`, `valid_to`) VALUES
	(1, 1, 60, '2023-04-01', '2023-04-14'),
	(2, 2, 90, '2023-04-01', '2023-04-14'),
	(3, 3, 100, '2023-04-01', '2023-04-16'),
	(27, 1, 97, '2023-04-15', '2023-04-21'),
	(29, 2, 100, '2023-04-15', '2023-04-16'),
	(30, 2, 110, '2023-04-17', '2023-04-19'),
	(31, 3, 115, '2023-04-17', '2099-12-31'),
	(32, 4, 120, '2023-04-17', '2099-12-31'),
	(33, 7, 125, '2023-04-17', '2099-12-31'),
	(34, 8, 130, '2023-04-17', '2099-12-31'),
	(35, 9, 135, '2023-04-17', '2099-12-31'),
	(36, 10, 140, '2023-04-17', '2099-12-31'),
	(37, 2, 110, '2023-04-20', '2099-12-31'),
	(38, 1, 15, '2023-04-22', '2023-04-24'),
	(41, 1, 177, '2023-04-25', '2023-04-25'),
	(43, 1, 188, '2023-04-26', '2099-12-31'),
	(44, 20, 150, '2023-05-01', '2099-12-31'),
	(45, 19, 120, '2023-05-01', '2099-12-31'),
	(46, 18, 65, '2023-05-01', '2099-12-31'),
	(48, 15, 100, '2023-05-01', '2099-12-31');
/*!40000 ALTER TABLE `prices` ENABLE KEYS */;

-- Dumping data for table smart-garage.roles: ~3 rows (approximately)
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`role_id`, `role_name`) VALUES
	(1, 'customer'),
	(2, 'admin'),
	(3, 'moderator');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

-- Dumping data for table smart-garage.users: ~4 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone_number`, `role_id`, `isActivated`, `isArchived`) VALUES
	(2, 'Petar', 'Ivanov', 'pesho99', 'pass', 'pesho@company.com', '5551234567', 2, 0, 0),
	(3, 'Gosho', 'Goshov', 'gosho99', 'pass1', 'gosho99@company.com', '1234567890', 1, 0, 0),
	(11, 'Georgi ', 'Petkov', 'joro95', 'Pass$pass8', 'jorkata@company.com', '9876543210', 1, 0, 0),
	(12, 'Mariana', 'Milcheva', 'mariana', 'pass2', 'mariana@abv.bg', '+359898958191', 1, 0, 0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping data for table smart-garage.vehicles: ~15 rows (approximately)
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` (`vehicle_id`, `plate`, `vin`, `year_of_creation`, `model_id`, `user_id`, `isArchived`) VALUES
	(4, 'CA3344PB', '4Y1SL65848Z411439', 2023, 1, 2, 0),
	(5, 'CA2244PB', '7T89665848Z411439', 2019, 2, 3, 0),
	(6, 'CA3345PB', '4Y1SL65848B511439', 2018, 3, 11, 0),
	(7, 'CA1088MM', '11111111111111111', 2000, 31, 12, 0),
	(10, 'CA1088AA', '11111111111111222', 2023, 2, 12, 1),
	(11, 'CA1234GG', '33333333331111111', 2003, 2, 3, 0),
	(12, 'CA7777MM', '11111111111111226', 1999, 2, 3, 0),
	(13, 'CA5555MM', '111111111MM111222', 1885, 2, 12, 0),
	(14, 'CA8888KK', '1111111111KKK1222', 1887, 2, 12, 0),
	(15, 'CA4444MM', '11111111111551222', 2000, 2, 11, 0),
	(27, 'CA1774MM', '66333333331111111', 1555, 2, 12, 0),
	(28, 'CA2211AA', '55333333331111111', 1855, 2, 12, 0),
	(29, 'CA1366MM', '66111111111111111', 1888, 2, 12, 0),
	(30, 'CA1515MM', '11111113311111111', 1999, 2, 12, 0),
	(31, 'CA1099MM', '11111112211111111', 2002, 2, 12, 0);
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;

-- Dumping data for table smart-garage.visits: ~15 rows (approximately)
/*!40000 ALTER TABLE `visits` DISABLE KEYS */;
INSERT INTO `visits` (`visit_id`, `start_date_of_visit`, `vehicle_id`, `end_date_of_visit`, `isArchived`, `payment_id`, `notes`) VALUES
	(2, '2023-02-08', 4, '2023-05-04', 1, 4, 'note'),
	(3, '2023-04-03', 5, '2023-04-22', 1, 12, 'notes123'),
	(4, '2023-03-01', 6, '2023-04-22', 1, 43, 'notes1234'),
	(5, '2023-04-09', 4, '2023-05-01', 1, 6, 'note12345'),
	(6, '2023-04-09', 5, '2023-04-19', 1, 39, 'note654'),
	(7, '2023-04-11', 5, '2023-04-11', 1, 10, 'note582'),
	(49, '2023-05-04', 30, '2023-05-04', 1, 65, 'test1'),
	(50, '2023-05-04', 30, '2023-05-04', 1, 66, 'test2'),
	(51, '2023-05-04', 30, NULL, 0, NULL, 'test3 '),
	(52, '2023-05-04', 30, NULL, 0, NULL, '4'),
	(53, '2023-05-04', 30, '2023-05-04', 1, 68, 'test5'),
	(54, '2023-05-04', 30, NULL, 0, 69, 'test10'),
	(55, '2023-05-04', 29, NULL, 0, NULL, 'test11'),
	(56, '2023-05-04', 31, '2023-05-04', 1, 70, 'test note'),
	(57, '2023-05-09', 31, '2023-05-09', 1, 71, 'test 09.05');
/*!40000 ALTER TABLE `visits` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
