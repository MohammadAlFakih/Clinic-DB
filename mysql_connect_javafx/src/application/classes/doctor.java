package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

import application.db.DBConnection;
import application.patient.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class doctor extends user{
	private Map.Entry<Integer, String> specialization;
	private Map.Entry<Integer, String> department;
	private String city;
	
	public doctor(String name,String email,String password,
			String phone,int age,String gender,int department_id,String address,int specialization_id,
			String specialization,String city){
		super(name, email, password,
				 phone, age, gender, "doctor");
		this.specialization = new AbstractMap.SimpleEntry<>(specialization_id,specialization);
		this.city = city;
		this.department = new AbstractMap.SimpleEntry<>(department_id,address);
	}
	
	public doctor(int id,String name,int dep_id,String address,String phone) {
		this.id = id;
		this.name = name;
		this.department = new AbstractMap.SimpleEntry<>(dep_id, address);	
		this.phone = phone;
		}
	
	public void insert_doctor() throws SQLException {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		PreparedStatement preparedStatement = super.insert_user();
		preparedStatement.setInt(7, this.department.getKey());
		preparedStatement.setInt(8, this.specialization.getKey());
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	public static ObservableList<doctor> get_available_doctors(String specialization,String city){
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		ObservableList<doctor> available_doctors =  FXCollections.observableArrayList();;
		try {
			String query = "SELECT * FROM available_doctors_view"
					+ " WHERE specialization=? AND city_name=?";
			PreparedStatement stmt = dbc.prepareStatement(query);
	        stmt.setString(1,specialization);
	        stmt.setString(2, city);
	        ResultSet result= stmt.executeQuery();
	        while(result.next()) {
	        	doctor doc = new doctor(result.getInt("doctor_id"),result.getString("name"),result.getInt("department_id")
	        			,result.getString("address"),result.getString("phone"));
	        	available_doctors.add(doc);
	        }
	        stmt.close();
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., log it, throw a custom exception, etc.)	    
		}
		return available_doctors;
	}
	
	@Override
	public String toString() {
		return "Dc. " + name+" / Phone: "+phone+" / Address: "+department.getValue();
	}
}
