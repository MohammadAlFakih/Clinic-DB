package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

import application.db.DBConnection;

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
}
