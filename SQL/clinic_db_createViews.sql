USE clinic_db;

-- View for doctor with specialization and adress
CREATE VIEW available_doctors_view AS
SELECT
    d.id AS `doctor_id`,
    d.name,
    d.age,
    d.gender,
    d.phone,
    s.alias AS specialization,
    dep.address,dep.room,
    c.alias AS city_name
FROM
    doctor d
JOIN
    specialization s ON d.specialization_id = s.id
JOIN
    department dep ON d.department_id = dep.id
JOIN 
    city c ON c.id = department.city_id;

-- View for appointments
CREATE VIEW appointments_view AS
SELECT
    a.id AS appointment_id,
    d.id AS doctor_id,
    d.name AS doctor_name,
    p.id AS patient_id,
    p.name AS patient_name,
    a.start_date,
    a.end_date,
    a.status,
    dep.address,
    city.alias,
    doc.details,
    doc.prescription
FROM
    appointment a
JOIN
    doctor d ON a.doctor_id = d.id
JOIN
    patient p ON a.patient_id = p.id
JOIN 
    department dep ON a.department_id = dep.id
JOIN 
    city ON dep.city_id = city.id
JOIN document ON a.id = document.appointment_id



-- View for unavailable slots for booking an appointment
CREATE VIEW busy_schedule_view AS
SELECT
    'unavailable_hours' AS schedule_type,
    u.id AS schedule_id,
    d.id AS doctor_id,
    u.start_date,
    u.end_date
FROM
    unavailable_slots u
JOIN
    doctor d ON u.doctor_id = d.id

UNION

SELECT
    'appointments' AS schedule_type,
    a.id AS schedule_id,
    d.id AS doctor_id,
    a.start_date,
    a.end_date
FROM
    appointment a
JOIN
    doctor d ON a.doctor_id = d.id
WHERE
    a.status != 'pending';

