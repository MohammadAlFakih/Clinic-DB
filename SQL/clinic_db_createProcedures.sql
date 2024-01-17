USE clinic_db;

-- Get patient information
DELIMITER //
CREATE PROCEDURE get_patient_info(
    IN patient_id INT,
    IN include_sensitive_info BOOLEAN
)
BEGIN
    DECLARE email_field VARCHAR(50);
    DECLARE password_field VARCHAR(255);

    IF include_sensitive_info THEN
        SET email_field = 'patient.email';
        SET password_field = 'patient.password';
    ELSE
        SET email_field = NULL;
        SET password_field = NULL;
    END IF;

    SELECT
        patient.id,
        patient.name,
        patient.age,
        patient.gender,
        patient.phone,
        patient.role,
        -- Include sensitive fields conditionally
        IFNULL(email_field, NULL) AS email,
        IFNULL(password_field, NULL) AS password
    FROM
        patient
    WHERE
        patient.id = patient_id;
END;
//
DELIMITER ;


-- Get doctor information
DELIMITER //
CREATE PROCEDURE get_doctor_info(
    IN doctor_id INT,
    IN include_sensitive_info BOOLEAN
)

BEGIN
    DECLARE email_field VARCHAR(50);
    DECLARE password_field VARCHAR(255);

    IF include_sensitive_info THEN
        SET email_field = 'doctor.email';
        SET password_field = 'doctor.password';
    ELSE
        SET email_field = NULL;
        SET password_field = NULL;
    END IF;

    SELECT
        doctor.id,
        doctor.name,
        doctor.gender,
        doctor.age,
        doctor.phone,
        doctor.role,
        specialization.specialization_name,
        department.department_name,
        city.city_name,
        -- Include sensitive fields conditionally
        IFNULL(email_field, NULL) AS email,
        IFNULL(password_field, NULL) AS password
    FROM
        doctor
    LEFT JOIN specialization ON doctor.specialization_id = specialization.id
    LEFT JOIN department ON doctor.department_id = department.id
    LEFT JOIN city ON department.city_id = city.id
    WHERE
        doctor.id = doctor_id;
END;
//
DELIMITER ;

-- Get secretary information
DELIMITER //
CREATE PROCEDURE get_secretary_info(
    IN secretary_id INT,
    IN include_sensitive_info BOOLEAN
)
BEGIN
    DECLARE email_field VARCHAR(50);
    DECLARE password_field VARCHAR(255);

    IF include_sensitive_info THEN
        SET email_field = 'secretary.email';
        SET password_field = 'secretary.password';
    ELSE
        SET email_field = NULL;
        SET password_field = NULL;
    END IF;

    SELECT
        secretary.id,
        secretary.name,
        secretary.age,
        secretary.gender,
        secretary.phone,
        secretary.role,
        secretary.doctor_id,
        -- Include sensitive fields conditionally
        IFNULL(email_field, NULL) AS email,
        IFNULL(password_field, NULL) AS password
    FROM
        secretary
    WHERE
        secretary.id = secretary_id;
END;
//
DELIMITER ;

-- Get all busy slots on a specific date
DELIMITER //
CREATE PROCEDURE get_unavailable_slots_on_date(
    IN specific_date DATE,
    IN doctor_id INT
)
BEGIN
    SELECT
        id AS slot_id,
        doctor_id,
        start_date,
        end_date
    FROM
        busy_schedule_view
    WHERE
        DATE(start_date) = specific_date;
END;
// DELIMITER ;
