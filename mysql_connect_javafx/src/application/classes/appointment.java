package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class appointment {
	private int id;
	private int doctor_id;
	private int patient_id;
	private String doctor_name;
	private String patient_name;
	private String start_date;
	private String end_date;
	private String status;
	private float bill;
	
	public appointment(int id,int doctor_id,int patient_id,String start_date,
	 String end_date,String status,float bill,String doctor_name,String patient_name) {
		this.id=id;
		this.doctor_id=doctor_id;
		this.patient_id=patient_id;
		this.start_date=start_date;
		this.end_date=end_date;
		this.status=status;
		this.bill=bill;
		this.doctor_name=doctor_name;
		this.patient_name=patient_name;
	}
	
	public appointment(int doctor_id,int patient_id,String start_date,
	 String end_date) {
		this.doctor_id=doctor_id;
		this.patient_id=patient_id;
		this.start_date=start_date;
		this.end_date=end_date;
	}
	
	 public String get_status() {
		 return status;
	 }
	 @Override
     public String toString() {
         return patient_name + " - " + start_date + " till " + end_date
        		 + " (Status: " + status + ")";
     }
	 
	 public static ObservableList<appointment> getappointments(int id,String request) {
		 DBConnection db;
		 ObservableList<appointment> appointments = FXCollections.observableArrayList();
			db = DBConnection.getInstance();
			Connection dbc = db.getConnection();
			try {
				String query="";
				if(request.equals("secretary")) {
				query = "SELECT app.*,doc.name as doctor_name,p.name as patient_name"
						+ " FROM appointment app"
						+ " JOIN doctor doc ON doc.id=app.doctor_id"
						+ " JOIN patient p ON p.id = app.patient_id"
						+" WHERE doc.id=?";
				}
				else {
					query = "SELECT * FROM appointments_view WHERE patient_id = ?";
				}
				PreparedStatement stmt = dbc.prepareStatement(query);
	            stmt.setInt(1, id);
	            ResultSet result = stmt.executeQuery();
	            while(result.next()) {
	            	appointment app = new appointment(result.getInt("id"),result.getInt("doctor_id")
	            			,result.getInt("patient_id"),result.getString("start_date"),result.getString("end_date"),
	            			result.getString("status"),result.getFloat("bill"),result.getString("doctor_name"),
	            			result.getString("patient_name"));
	            	appointments.add(app);
	            }
	            stmt.close();
			}
			catch (SQLException e) {
		        e.printStackTrace();
		        // Handle the exception (e.g., log it, throw a custom exception, etc.)
		    }
			return appointments;
	 }

	 public void accept_appointment() {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		try {
			String query = "CALL accept_appointment_procedure(?,?,?,?)";
			PreparedStatement stmt = dbc.prepareStatement(query);
	        stmt.setInt(1, id);
	        stmt.setString(2,start_date);
	        stmt.setString(3, end_date);
	        stmt.setInt(4, doctor_id);
	        stmt.executeUpdate();
	        stmt.close();
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., log it, throw a custom exception, etc.)	    
		}
	 }
	 
	 public void remove_appointment() {
			DBConnection db;
			db = DBConnection.getInstance();
			Connection dbc = db.getConnection();
			try {
				if(status.equals("pending")) {
				String query = "DELETE FROM appointment WHERE id=?";
				PreparedStatement stmt = dbc.prepareStatement(query);
		        stmt.setInt(1, id);
		        stmt.executeUpdate();
		        stmt.close();
				}
				else {
					String query = "CALL delete_upcoming_appointment_procedure(?,?,?,?)";
					PreparedStatement stmt = dbc.prepareStatement(query);
			        stmt.setInt(1, id);
			        stmt.setString(2,start_date);
			        stmt.setString(3, end_date);
			        stmt.setInt(4, doctor_id);
			        stmt.executeUpdate();
			        stmt.close();
				}
			}
			catch (SQLException e) {
		        e.printStackTrace();
		        // Handle the exception (e.g., log it, throw a custom exception, etc.)	    
			}
		 }
	 
	 public String display_for_patient() {
		 String text = "With Dc. " + doctor_name+ " / "+start_date+" till "+end_date +" / Bill: "+bill;
		 if(status.equals("upcoming"))
			 text += " / Status : Accepted";
		 else
			 text+=" / Status : Pending";
		 return text;
	 }
	 
	 public void insert_appointment() {
		 DBConnection db;
			db = DBConnection.getInstance();
			Connection dbc = db.getConnection();
			try {
				String query = "INSERT INTO appointment (patient_id,doctor_id,start_date,end_date)"
						+ " VALUES (?,?,?,?)";
				PreparedStatement stmt = dbc.prepareStatement(query);
		        stmt.setInt(1, patient_id);
		        stmt.setInt(2, doctor_id);
		        stmt.setString(3, start_date);
		        stmt.setString(4, end_date);
		        stmt.executeUpdate();
		        stmt.close();
			}
			catch (SQLException e) {
		        e.printStackTrace();
		        // Handle the exception (e.g., log it, throw a custom exception, etc.)	    
			}
	 }
}
