SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================
-- USERS (ROOT TABLE)
-- ========================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255),
  email varchar(255),
  password varchar(255),
  role varchar(50),
  createdAt datetime,
  updatedAt datetime,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO users VALUES
(1,'Yan Lu','student@mail.com','...','Student','2005-02-04 23:31:27','2015-11-24 04:23:47'),
(2,'Lili','evaluator@mail.com','...','Evaluator','2005-02-04 23:31:27','2015-11-24 04:23:47'),
(3,'Adam','coordinator@mail.com','...','Coordinator','2005-02-04 23:31:27','2015-11-24 04:23:47'),
(7,'Admin','admin@mail.com','...','Admin',NULL,NULL),
(8,'Student 3','student3@mail.com','test','Student','2026-01-08 06:18:11','2026-01-08 06:18:11'),
(9,'evaluator','evaluator2@mail.com','...','Evaluator','2026-01-08 06:19:47','2026-01-08 06:19:47'),
(10,'coordinator2','coordinator2@mail.com','...','Coordinator','2026-01-08 06:20:06','2026-01-08 06:20:06'),
(11,'asdasd','asd@AD','asd','Student','2026-01-08 06:25:02','2026-01-08 06:25:02');

-- ========================
-- ADMINS
-- ========================
DROP TABLE IF EXISTS admins;
CREATE TABLE admins (
  id int AUTO_INCREMENT PRIMARY KEY,
  userID int,
  createdAt datetime,
  updatedAt datetime,
  CONSTRAINT fk_admin_user FOREIGN KEY (userID) REFERENCES users(id)
);

INSERT INTO admins VALUES (1,7,NULL,NULL);

-- ========================
-- STUDENTS
-- ========================
DROP TABLE IF EXISTS students;
CREATE TABLE students (
  id int AUTO_INCREMENT PRIMARY KEY,
  userID int NOT NULL,
  program varchar(255),
  year int,
  createdAt datetime,
  updatedAt datetime,
  CONSTRAINT fk_students_users FOREIGN KEY (userID)
    REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO students VALUES
(1,1,'2025',1,'2015-12-31 22:37:50','2007-06-28 15:39:51'),
(3,6,'2026',6,'2026-01-07 22:55:33','2026-01-07 22:55:33'),
(4,8,'ICT',2025,'2026-01-08 06:18:11','2026-01-08 06:18:11'),
(5,11,'asda',2025,'2026-01-08 06:25:02','2026-01-08 06:25:02');

-- ========================
-- SESSIONS
-- ========================
DROP TABLE IF EXISTS sessions;
CREATE TABLE sessions (
  id int AUTO_INCREMENT PRIMARY KEY,
  date date,
  venue varchar(255),
  sessionType varchar(255),
  timeSlot time,
  createdAt datetime,
  updatedAt datetime
);

INSERT INTO sessions VALUES
(1,'2026-01-06','FCI Class Room','Final Presentation','05:31:52',NULL,'2026-01-07 16:53:13'),
(2,'2026-01-07','Test venue 1','session 1','16:45:07','2026-01-07 16:46:57','2026-01-07 17:05:10'),
(3,'2026-01-07','F','adad','16:47:24','2026-01-07 16:47:29','2026-01-07 16:47:29');

-- ========================
-- EVALUATORS
-- ========================
DROP TABLE IF EXISTS evaluators;
CREATE TABLE evaluators (
  id int AUTO_INCREMENT PRIMARY KEY,
  userID int NOT NULL,
  expertise varchar(255),
  sessionID int,
  createdAt datetime,
  updatedAt datetime,
  CONSTRAINT fk_eval_user FOREIGN KEY (userID) REFERENCES users(id),
  CONSTRAINT fk_eval_session FOREIGN KEY (sessionID) REFERENCES sessions(id)
);

INSERT INTO evaluators VALUES
(1,2,'IT',1,'0000-00-00 00:00:00',NULL),
(2,9,'ICT',NULL,'2026-01-08 06:19:47','2026-01-08 06:19:47');

-- ========================
-- ASSIGNED SESSION
-- ========================
DROP TABLE IF EXISTS assigned_session;
CREATE TABLE assigned_session (
  id int AUTO_INCREMENT PRIMARY KEY,
  sessionID int,
  evaluatorID int,
  createdAt datetime,
  updatedAt datetime,
  CONSTRAINT fk_as_session FOREIGN KEY (sessionID) REFERENCES sessions(id),
  CONSTRAINT fk_as_evaluator FOREIGN KEY (evaluatorID) REFERENCES evaluators(id)
);

INSERT INTO assigned_session VALUES
(1,1,1,'2026-01-06 08:04:36','2026-01-06 08:04:38');

SET FOREIGN_KEY_CHECKS = 1;