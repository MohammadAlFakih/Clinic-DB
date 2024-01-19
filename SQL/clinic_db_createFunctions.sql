USE clinic_db;

-- Function to check if a new appointment overlaps with existing appointment or unavailabile slots
DELIMITER //
CREATE FUNCTION is_time_slot_available(
    p_start_date DATETIME,
    p_end_date DATETIME,
    p_doctor_id INT
)
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE is_available BOOLEAN;
    -- Check for overlapping appointments without using a procedure
    SET is_available = NOT EXISTS (
        SELECT 1
        FROM busy_schedule_view
        WHERE doctor_id = p_doctor_id
          AND DATE(start_date) = DATE(p_start_date)
          AND (
            (start_date <= p_start_date AND end_date >= p_start_date) OR
            (start_date <= p_end_date AND end_date >= p_end_date) OR
            (start_date >= p_start_date AND end_date <= p_end_date)
          )
    );
    RETURN is_available;
END;
// DELIMITER ;



