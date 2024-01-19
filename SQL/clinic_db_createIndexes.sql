USE clinic_db;

-- Indexes on Primary Keys
CREATE UNIQUE INDEX idx_admin_id ON admin (id);
CREATE UNIQUE INDEX idx_department_id ON department (id);
CREATE UNIQUE INDEX idx_city_id ON city (id);
CREATE UNIQUE INDEX idx_specialization_id ON specialization (id);
CREATE UNIQUE INDEX idx_doctor_id ON doctor (id);
CREATE UNIQUE INDEX idx_secretary_id ON secretary (id);
CREATE UNIQUE INDEX idx_patient_id ON patient (id);
CREATE UNIQUE INDEX idx_appointment_id ON appointment (id);
CREATE UNIQUE INDEX idx_document_id ON document (id);

-- Indexes on Foreign Keys
CREATE INDEX idx_doctor_department_id ON doctor (department_id);
CREATE INDEX idx_doctor_specialization_id ON doctor (specialization_id);
CREATE INDEX idx_secretary_doctor_id ON secretary (doctor_id);
CREATE INDEX idx_payment_doctor_id_fk ON payment (doctor_id);
CREATE INDEX idx_payment_patient_id_fk ON payment (patient_id);
CREATE INDEX idx_appointment_doctor_id_fk ON appointment (doctor_id);
CREATE INDEX idx_appointment_patient_id_fk ON appointment (patient_id);
CREATE INDEX idx_document_appointment_id_fk ON document (appointment_id);
CREATE INDEX idx_week_schedule_doctor_id_fk ON week_schedule (doctor_id);
CREATE INDEX idx_unavailable_slots_doctor_id_fk ON unavailable_slots (doctor_id);
CREATE INDEX idx_unavailable_slots_department_id_fk ON unavailable_slots (department_id);
CREATE INDEX idx_department_city_id ON department (city_id);

-- Indexes on email address for login
CREATE INDEX idx_doctor_login_email ON doctor (email);
CREATE INDEX idx_patient_login_email ON patient (email);
CREATE INDEX idx_secretary_login_email ON secretary (email);

-- Indexes on start_date used in WHERE clause
CREATE INDEX idx_appointmnet_start_date ON appointment (start_date);
CREATE INDEX idx_unavailable_start_date ON unavailable_slots (start_date);