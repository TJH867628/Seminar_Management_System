/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : sms_db

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 06/01/2026 04:04:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `createdAt`, `updatedAt`) VALUES (1, 'Yan Lu', 'student@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Student', '2005-02-04 23:31:27', '2015-11-24 04:23:47');
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `createdAt`, `updatedAt`) VALUES (2, 'Lili', 'evaluator@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Evaluator', '2005-02-04 23:31:27', '2015-11-24 04:23:47');
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `createdAt`, `updatedAt`) VALUES (3, 'Adam', 'coordinator@mail.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 'Coordinator', '2005-02-04 23:31:27', '2015-11-24 04:23:47');
COMMIT;

-- ----------------------------
-- Table structure for coordinators
-- ----------------------------
DROP TABLE IF EXISTS `coordinators`;
CREATE TABLE `coordinators` (
  `id` int NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `createdAt` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `userID` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-coordinators` (`userID`),
  CONSTRAINT `userID-coordinators` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of coordinators
-- ----------------------------
BEGIN;
INSERT INTO `coordinators` (`id`, `department`, `createdAt`, `updatedAt`, `userID`) VALUES (1, 'FCI', 'wQwtbvXFEh', '2012-06-15 05:05:38', 3);
COMMIT;

-- ----------------------------
-- Table structure for evaluators
-- ----------------------------
DROP TABLE IF EXISTS `evaluators`;
CREATE TABLE `evaluators` (
  `id` int NOT NULL,
  `expertise` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `userID` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-evaluator` (`userID`),
  CONSTRAINT `userID-evaluator` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of evaluators
-- ----------------------------
BEGIN;
INSERT INTO `evaluators` (`id`, `expertise`, `createdAt`, `updatedAt`, `userID`) VALUES (1, 'Math', '2015-08-29 00:12:24', '2015-06-15 02:08:32', 2);
COMMIT;

-- ----------------------------
-- Table structure for sessions
-- ----------------------------
DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `id` int NOT NULL,
  `date` datetime DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `sessionType` varchar(255) DEFAULT NULL,
  `timeSlot` datetime DEFAULT NULL,
  `createdAt` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sessions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` int NOT NULL,
  `program` varchar(255) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `userID` int DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userID-students` (`userID`),
  CONSTRAINT `userID-students` FOREIGN KEY (`userID`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of students
-- ----------------------------
BEGIN;
INSERT INTO `students` (`id`, `program`, `year`, `userID`, `createdAt`, `updatedAt`) VALUES (1, 'FCI', 2025, 1, '2015-12-31 22:37:50', '2007-06-28 15:39:51');
COMMIT;

-- ----------------------------
-- Table structure for submissions
-- ----------------------------
DROP TABLE IF EXISTS `submissions`;
CREATE TABLE `submissions` (
  `submissionID` int NOT NULL,
  `userID` int DEFAULT NULL,
  `researchTitle` varchar(255) DEFAULT NULL,
  `abstract` varchar(255) DEFAULT NULL,
  `supervisorName` varchar(255) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `updatedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`submissionID`),
  KEY `userID-submission` (`userID`),
  CONSTRAINT `userID-submission` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of submissions
-- ----------------------------
BEGIN;
COMMIT;



SET FOREIGN_KEY_CHECKS = 1;
