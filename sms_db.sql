-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for sms_db
CREATE DATABASE IF NOT EXISTS `sms_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sms_db`;

-- Dumping structure for table sms_db.assigned_session
CREATE TABLE IF NOT EXISTS `assigned_session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `SessionID` int DEFAULT NULL,
  `EvaluatorID` int DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `evaluatorId-assigned_session` (`EvaluatorID`),
  KEY `sessionId-assigned_session` (`SessionID`),
  CONSTRAINT `evaluatorId-assigned_session` FOREIGN KEY (`EvaluatorID`) REFERENCES `evaluators` (`id`),
  CONSTRAINT `sessionId-assigned_session` FOREIGN KEY (`SessionID`) REFERENCES `sessions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.assigned_session: ~0 rows (approximately)
INSERT INTO `assigned_session` (`id`, `SessionID`, `EvaluatorID`, `createdAt`, `updatedAt`) VALUES
	(1, 1, 1, '2026-01-06 08:04:36', '2026-01-06 08:04:38');

-- Dumping structure for table sms_db.coordinators
CREATE TABLE IF NOT EXISTS `coordinators` (
  `id` int NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `userID` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-coordinators` (`userID`),
  CONSTRAINT `userID-coordinators` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.coordinators: ~0 rows (approximately)
INSERT INTO `coordinators` (`id`, `department`, `createdAt`, `updatedAt`, `userID`) VALUES
	(1, 'FCI', 'wQwtbvXFEh', '2012-06-15 05:05:38', 3);

-- Dumping structure for table sms_db.evaluation
CREATE TABLE IF NOT EXISTS `evaluation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evaluatorID` int DEFAULT NULL,
  `submissionID` int DEFAULT NULL,
  `problemClarityScore` int DEFAULT NULL,
  `methodologyScore` int DEFAULT NULL,
  `resultScore` int DEFAULT NULL,
  `presentationScore` int DEFAULT NULL,
  `comments` varchar(50) DEFAULT NULL,
  `totalScore` double DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `evaluator-evaluation` (`evaluatorID`),
  KEY `evaluation-submmissionsID` (`submissionID`),
  CONSTRAINT `evaluation-submmissionsID` FOREIGN KEY (`submissionID`) REFERENCES `submissions` (`id`),
  CONSTRAINT `evaluator-evaluation` FOREIGN KEY (`evaluatorID`) REFERENCES `evaluators` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.evaluation: ~0 rows (approximately)

-- Dumping structure for table sms_db.evaluators
CREATE TABLE IF NOT EXISTS `evaluators` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expertise` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `userID` int DEFAULT NULL,
  `sessionID` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-evaluator` (`userID`),
  KEY `session-evaluator` (`sessionID`),
  CONSTRAINT `session-evaluator` FOREIGN KEY (`sessionID`) REFERENCES `sessions` (`id`),
  CONSTRAINT `userID-evaluator` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.evaluators: ~0 rows (approximately)
INSERT INTO `evaluators` (`id`, `expertise`, `createdAt`, `updatedAt`, `userID`, `sessionID`) VALUES
	(1, 'Math', '2015-08-29 00:12:24', '2015-06-15 02:08:32', 2, NULL);

-- Dumping structure for table sms_db.sessions
CREATE TABLE IF NOT EXISTS `sessions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `sessionType` varchar(255) DEFAULT NULL,
  `timeSlot` time DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.sessions: ~0 rows (approximately)
INSERT INTO `sessions` (`id`, `date`, `venue`, `sessionType`, `timeSlot`, `createdAt`, `updatedAt`) VALUES
	(1, '2026-01-06 05:28:24', 'LAB FCI', 'Final Presentation', '2026-01-06 05:31:52', NULL, NULL);

-- Dumping structure for table sms_db.students
CREATE TABLE IF NOT EXISTS `students` (
  `id` int NOT NULL AUTO_INCREMENT,
  `program` varchar(255) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `userID` int DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `sessionID` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-students` (`userID`),
  KEY `session-student` (`sessionID`),
  CONSTRAINT `session-student` FOREIGN KEY (`sessionID`) REFERENCES `sessions` (`id`),
  CONSTRAINT `userID-students` FOREIGN KEY (`userID`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.students: ~0 rows (approximately)
INSERT INTO `students` (`id`, `program`, `year`, `userID`, `createdAt`, `updatedAt`, `sessionID`) VALUES
	(1, 'FCI', 2025, 1, '2015-12-31 22:37:50', '2007-06-28 15:39:51', 1);

-- Dumping structure for table sms_db.submissions
CREATE TABLE IF NOT EXISTS `submissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `studentID` int DEFAULT NULL,
  `researchTitle` varchar(255) DEFAULT NULL,
  `abstract` varchar(255) DEFAULT NULL,
  `supervisorName` varchar(255) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `updatedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-submission` (`studentID`),
  CONSTRAINT `studentid-submission` FOREIGN KEY (`studentID`) REFERENCES `students` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.submissions: ~2 rows (approximately)
INSERT INTO `submissions` (`id`, `studentID`, `researchTitle`, `abstract`, `supervisorName`, `filePath`, `status`, `createdDate`, `updatedDate`) VALUES
	(1, 1, 'Test', 'testabstract', 'test1', '"C:\\Users\\chaiy\\Downloads\\MC.pdf"', 'submitted', '2026-01-06 05:27:10', '2026-01-06 05:27:12'),
	(2, 1, 'Test2', 'haha', 'sohai', '"C:\\Users\\chaiy\\Downloads\\MC.pdf"', 'submitted', '2026-01-06 05:42:19', '2026-01-06 05:42:20'),
	(3, 1, 'Test3', 'nn', 'nn', '"C:\\Users\\chaiy\\Downloads\\MC.pdf"', 'submitted', '2026-01-06 17:58:37', '2026-01-06 17:58:38');

-- Dumping structure for table sms_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sms_db.users: ~2 rows (approximately)
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `createdAt`, `updatedAt`) VALUES
	(1, 'Yan Lu', 'student@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Student', '2005-02-04 23:31:27', '2015-11-24 04:23:47'),
	(2, 'Lili', 'evaluator@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Evaluator', '2005-02-04 23:31:27', '2015-11-24 04:23:47'),
	(3, 'Adam', 'coordinator@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Coordinator', '2005-02-04 23:31:27', '2015-11-24 04:23:47');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
