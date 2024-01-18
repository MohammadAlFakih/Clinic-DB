CREATE DATABASE IF NOT EXISTS clinic_db;
-- Use Database
USE `clinic_db`;


-- Create Table: city
CREATE TABLE IF NOT EXISTS `city` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `alias` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Table: department
CREATE TABLE IF NOT EXISTS `department` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `address` VARCHAR(100),
    `room` INT NULL,
    `city_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`city_id`) REFERENCES `city` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Create Table: specialization
CREATE TABLE IF NOT EXISTS `specialization` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `alias` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Table: admin
CREATE TABLE IF NOT EXISTS `admin` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(30) NOT NULL,
    `password` TEXT NOT NULL,
    `name` VARCHAR(30) NOT NULL,
    `age` INT NOT NULL,
    `gender` CHAR(1) NOT NULL,
    `phone` VARCHAR(30) NOT NULL,
    `role` VARCHAR(30) NOT NULL DEFAULT 'admin',
    PRIMARY KEY (`id`),
    UNIQUE (`email`)
);

-- Create Table: doctor
CREATE TABLE IF NOT EXISTS `doctor` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `department_id` INT NOT NULL,
    `email` VARCHAR(30) NOT NULL,
    `password` TEXT NOT NULL,
    `name` VARCHAR(30) NOT NULL,
    `age` INT NOT NULL,
    `gender` CHAR(1) NOT NULL,
    `phone` VARCHAR(30) NOT NULL,
    `specialization_id` INT NOT NULL,
    `role` VARCHAR(30) NOT NULL DEFAULT 'doctor',
    `pp` VARCHAR(255) NOT NULL DEFAULT 'default.png',
    PRIMARY KEY (`id`),
    UNIQUE (`email`),
    FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (`specialization_id`) REFERENCES `specialization`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Table: secretary
CREATE TABLE IF NOT EXISTS `secretary` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `doctor_id` INT NULL,
    `email` VARCHAR(30) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(30) NOT NULL,
    `age` INT NOT NULL,
    `gender` CHAR(1) NOT NULL,
    `phone` VARCHAR(30) NOT NULL,
    `role` VARCHAR(30) NOT NULL DEFAULT 'secretary',
    `pp` VARCHAR(255) NOT NULL DEFAULT 'default.png',
    PRIMARY KEY (`id`),
    UNIQUE (`email`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Create Table: patient
CREATE TABLE IF NOT EXISTS `patient` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(30) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(30) NOT NULL,
    `age` INT NOT NULL,
    `gender` CHAR(1) NOT NULL,
    `phone` VARCHAR(30) NULL,
    `role` VARCHAR(30) NOT NULL DEFAULT 'patient',
    `pp` VARCHAR(255) NOT NULL DEFAULT 'default.png',
    PRIMARY KEY (`id`),
    UNIQUE (`email`)
);

-- Create Table: payment
CREATE TABLE IF NOT EXISTS `payment` (
    `patient_id` INT NOT NULL,
    `doctor_id` INT NOT NULL,
    `balance` FLOAT NOT NULL CHECK(`balance` >= 0),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (`patient_id`) REFERENCES `patient`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Create Table: appointment
CREATE TABLE IF NOT EXISTS `appointment` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `doctor_id` INT NOT NULL,
    `patient_id` INT NOT NULL,
    `department_id` INT NOT NULL,
    `start_date` DATETIME NOT NULL,
    `end_date` DATETIME NOT NULL,
    `bill` FLOAT NOT NULL DEFAULT 0 CHECK(`bill` > 0),
    `status` VARCHAR(30) NOT NULL DEFAULT 'pending',
    `book_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (`patient_id`) REFERENCES `patient`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE RESTRICT ON UPDATE NO ACTION
);

-- Create Table: document
CREATE TABLE IF NOT EXISTS `document` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `appointment_id` INT NOT NULL,
    `details` TEXT NULL,
    `prescription` TEXT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`appointment_id`) REFERENCES `appointment`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- Create Table: week_schedule
CREATE TABLE IF NOT EXISTS `week_schedule` (
    `day` VARCHAR(12) NOT NULL,
    `doctor_id` INT NOT NULL,
    `start_hour` TIME NOT NULL,
    `end_hour` TIME NOT NULL,
    PRIMARY KEY (`doctor_id`, `day`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Table: unavailable_slots
CREATE TABLE IF NOT EXISTS `unavailable_slots` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `doctor_id` INT NOT NULL,
    `start_date` DATETIME NOT NULL,
    `end_date` DATETIME NOT NULL,
    `department_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


