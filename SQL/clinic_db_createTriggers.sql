USE clinic_db;

-- Create Trigger: new_document for new_appointment
DELIMITER //
CREATE TRIGGER `new_document` AFTER INSERT ON `appointment` FOR EACH ROW
BEGIN
    INSERT INTO `document` (`appointment_id`, `details`, `prescription`) VALUES (NEW.id, NULL, NULL);
END;
//
DELIMITER ;

-- Create Trigger: Default_week_schedule for a new doctor
DELIMITER //
CREATE TRIGGER `Default_week_schedule` AFTER INSERT ON `doctor` FOR EACH ROW
BEGIN
    INSERT INTO `week_schedule` (`doctor_id`, `day`, `start_hour`, `end_hour`)
    VALUES
        (NEW.id, 'monday', '08:00', '16:00'),
        (NEW.id, 'tuesday', '08:00', '16:00'),
        (NEW.id, 'wednesday', '08:00', '16:00'),
        (NEW.id, 'thursday', '08:00', '16:00'),
        (NEW.id, 'friday', '08:00', '16:00'),
        (NEW.id, 'saturday', '08:00', '08:00'),
        (NEW.id, 'sunday', '08:00', '08:00');
END;
//
DELIMITER ;

-- <-----------------------Appointment intergrity constraints------------------------>
-- Trigger to check foreign key constraints
DELIMITER //
CREATE TRIGGER before_insert_appointment
BEFORE INSERT ON appointment
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM doctor WHERE id = NEW.doctor_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: DoctorID does not exist.';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM patient WHERE id = NEW.patient_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: PatientID does not exist.';
    END IF;
END;
//
DELIMITER //

-- <-----------------------Doctor intergrity constraints------------------------>
-- Trigger to check foreign key constraints
DELIMITER //
CREATE TRIGGER before_insert_doctor
BEFORE INSERT ON Doctor
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM department WHERE id = NEW.department_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: DepartmentID does not exist.';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM specialization WHERE id = NEW.specialization_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: SpecializationID does not exist.';
    END IF;
END;
//
DELIMITER //

-- <-----------------------Unavailable_slots intergrity constraints------------------------>
-- Trigger to check foreign key constraints
DELIMITER //
CREATE TRIGGER before_insert_unavailable_slots
BEFORE INSERT ON unavailable_slots
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM doctor WHERE id = NEW.doctor_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: DoctorID does not exist.';
    END IF;
END;
//
DELIMITER //

-- <-----------------------Document intergrity constraints------------------------>
-- Trigger to check foreign key constraints
DELIMITER //
CREATE TRIGGER before_insert_document
BEFORE INSERT ON document
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM appointment WHERE id = NEW.appointment_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: AppointmentID does not exist.';
    END IF;
END;
//
DELIMITER //

-- <-----------------------Department intergrity constraints------------------------>
-- Trigger to check foreign key constraints
DELIMITER //
CREATE TRIGGER before_insert_department
BEFORE INSERT ON department
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM city WHERE id = NEW.city_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Foreign key violation: CityID does not exist.';
    END IF;
END;
//
DELIMITER //