SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================
-- users
-- =====================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255),
  role VARCHAR(50),
  createdAt DATETIME,
  updatedAt DATETIME
) ENGINE=InnoDB;

INSERT INTO users VALUES
(1,'Yan Lu - Edit','student@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Student','2005-02-04 23:31:27','2026-01-11 04:35:52'),
(2,'Adam','evaluator@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Evaluator','2005-02-04 23:31:27','2026-01-11 04:36:59'),
(3,'Lulu','coordinator@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Coordinator','2005-02-04 23:31:27','2026-01-11 04:36:45'),
(4,'Tan','tan@mail','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Student','2026-01-07 22:52:22','2026-01-08 05:07:42'),
(5,'Tab 123','asdjk@asdasd','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Student','2026-01-07 22:54:21','2026-01-08 05:20:46'),
(6,'Tan','asd','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Evaluator','2026-01-07 22:55:33','2026-01-08 05:26:34'),
(7,'Admin','admin@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Admin',NULL,NULL),
(8,'Student 3','student3@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Student','2026-01-08 06:18:11','2026-01-08 06:18:11'),
(9,'evaluator','evaluator2@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Evaluator','2026-01-08 06:19:47','2026-01-08 06:19:47'),
(10,'coordinator2','coordinator2@mail.com','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Coordinator','2026-01-08 06:20:06','2026-01-08 06:20:06'),
(11,'asdasd','asd@AD','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','Student','2026-01-08 06:25:02','2026-01-08 06:25:02');

-- =====================
-- sessions
-- =====================
DROP TABLE IF EXISTS sessions;
CREATE TABLE sessions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  date DATE,
  venue VARCHAR(255),
  sessionType VARCHAR(255),
  timeSlot TIME,
  createdAt DATETIME,
  updatedAt DATETIME
) ENGINE=InnoDB;

INSERT INTO sessions VALUES
(1,'2026-01-06','FCI Class Room','Final Presentation','05:31:52',NULL,'2026-01-07 16:53:13');

-- =====================
-- admins
-- =====================
DROP TABLE IF EXISTS admins;
CREATE TABLE admins (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userID INT,
  createdAt DATETIME,
  updatedAt DATETIME,
  FOREIGN KEY (userID) REFERENCES users(id)
) ENGINE=InnoDB;

INSERT INTO admins VALUES (1,7,NULL,NULL);

-- =====================
-- coordinators
-- =====================
DROP TABLE IF EXISTS coordinators;
CREATE TABLE coordinators (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userID INT NOT NULL,
  department VARCHAR(255),
  createdAt DATETIME,
  updatedAt DATETIME,
  FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO coordinators VALUES
(1,3,'wQwtbvXFEh','2012-06-15 05:05:38',NULL),
(2,10,'Dean','2026-01-08 06:20:06','2026-01-08 06:20:06');

-- =====================
-- students
-- =====================
DROP TABLE IF EXISTS students;
CREATE TABLE students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userID INT NOT NULL,
  program VARCHAR(255),
  year INT,
  createdAt DATETIME,
  updatedAt DATETIME,
  FOREIGN KEY (userID) REFERENCES users(id)
) ENGINE=InnoDB;

INSERT INTO students VALUES
(1,1,'FCI',2025,'2015-12-31 22:37:50','2026-01-11 04:35:52'),
(3,6,'2026',6,'2026-01-07 22:55:33','2026-01-07 22:55:33'),
(4,8,'ICT',2025,'2026-01-08 06:18:11','2026-01-08 06:18:11'),
(5,11,'asda',2025,'2026-01-08 06:25:02','2026-01-08 06:25:02');

-- =====================
-- evaluators
-- =====================
DROP TABLE IF EXISTS evaluators;
CREATE TABLE evaluators (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userID INT NOT NULL,
  expertise VARCHAR(255),
  sessionID INT,
  createdAt DATETIME,
  updatedAt DATETIME,
  FOREIGN KEY (userID) REFERENCES users(id),
  FOREIGN KEY (sessionID) REFERENCES sessions(id)
) ENGINE=InnoDB;

INSERT INTO evaluators VALUES
(1,2,'IT',1,NULL,NULL),
(2,9,'ICT',NULL,'2026-01-08 06:19:47','2026-01-08 06:19:47');

-- =====================
-- assigned_session
-- =====================
DROP TABLE IF EXISTS assigned_session;
CREATE TABLE assigned_session (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sessionID INT,
  evaluatorID INT,
  createdAt DATETIME,
  updatedAt DATETIME,
  FOREIGN KEY (sessionID) REFERENCES sessions(id),
  FOREIGN KEY (evaluatorID) REFERENCES evaluators(id)
) ENGINE=InnoDB;

INSERT INTO assigned_session VALUES
(6,1,1,NULL,NULL),
(11,1,2,NULL,NULL);

-- =====================
-- submissions (FIXED ONLY HERE)
-- =====================
DROP TABLE IF EXISTS submissions;
CREATE TABLE submissions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  studentID INT,
  researchTitle VARCHAR(255),
  abstracts VARCHAR(255),
  supervisorName VARCHAR(255),
  presentationType VARCHAR(50),
  filePath VARCHAR(255),
  status VARCHAR(50),
  sessionID INT,
  createdDate DATETIME,
  updatedDate DATETIME,
  FOREIGN KEY (studentID) REFERENCES students(id),
  FOREIGN KEY (sessionID) REFERENCES sessions(id)
) ENGINE=InnoDB;

INSERT INTO submissions VALUES
(1,1,'Test','testabstract','test1','Oral','C:\\Users\\chaiy\\Downloads\\MC.pdf','submitted',1,'2026-01-06 05:27:10','2026-01-06 05:27:12'),
(2,1,'Test2','haha','sohai','Poster','C:\\Users\\chaiy\\Downloads\\MC.pdf','submitted',NULL,'2026-01-06 05:42:19','2026-01-06 05:42:20'),
(3,1,'Test3','nn','nn','Oral','C:\\Users\\chaiy\\Downloads\\MC.pdf','submitted',NULL,'2026-01-06 17:58:37','2026-01-06 17:58:38');

-- =====================
-- evaluation
-- =====================
DROP TABLE IF EXISTS evaluation;
CREATE TABLE evaluation (
  id INT AUTO_INCREMENT PRIMARY KEY,
  evaluatorID INT,
  submissionID INT,
  problemClarityScore INT,
  methodologyScore INT,
  resultScore INT,
  presentationScore INT,
  comments VARCHAR(255),
  totalScore DOUBLE,
  status VARCHAR(50),
  FOREIGN KEY (evaluatorID) REFERENCES evaluators(id),
  FOREIGN KEY (submissionID) REFERENCES submissions(id)
) ENGINE=InnoDB;

-- =====================
-- peoples_choice_votes
-- =====================
DROP TABLE IF EXISTS peoples_choice_votes;
CREATE TABLE peoples_choice_votes (
    voteID INT AUTO_INCREMENT PRIMARY KEY,
    evaluatorID INT NOT NULL,
    submissionID INT NOT NULL,
    voteDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (evaluatorID),  -- only 1 vote per evaluator globally
    FOREIGN KEY (evaluatorID) REFERENCES evaluators(id) ON DELETE CASCADE,
    FOREIGN KEY (submissionID) REFERENCES submissions(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create awards table
DROP TABLE IF EXISTS awards;
CREATE TABLE awards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    awardName VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    criteria VARCHAR(255)
) ENGINE=InnoDB;

-- Insert the 3 awards
INSERT INTO awards (awardName, category, criteria) VALUES
('Best Oral', 'Presentation', 'Highest total score from evaluator evaluations'),
('Best Poster', 'Poster', 'Highest total score from evaluator evaluations'),
('People''s Choice', 'Voting', 'Most votes from evaluators');

SET FOREIGN_KEY_CHECKS = 1;
