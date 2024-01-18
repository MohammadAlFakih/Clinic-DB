package application.classes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.db.DBConnection;

public class user {
	protected int id;
	protected String name;
	protected String email;
	protected String password;
	protected String phone;
	protected int age;
	protected String gender;
	protected String role;
	
	public user() {}
	
	public user(String name,String email,String password,
				String phone,int age,String gender,String role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone=phone;
		this.age=age;
		this.gender =gender;
		this.role = role;
	}
	
	public void set_ID(int id) {
		this.id = id;
	}
	
	public PreparedStatement insert_user() {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		try {
			String query;
			if(role=="patient") {
			query = "INSERT INTO patient (name,email,password,age,gender,phone)"+
                     "VALUES(?,?,?,?,?,?);";
			}
			else if(role == "secretary") {
				query = "INSERT INTO secretary (name,email,password,age,gender,phone,doctor_id)"+
	                     "VALUES(?,?,?,?,?,?,?);";
			}
			else {
				query = "INSERT INTO doctor (name,email,password,age,gender,phone,specialization_id,department_id)"+
	                     "VALUES(?,?,?,?,?,?,?,?);";
			}
			PreparedStatement preparedStatement = dbc.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, phone);
            return preparedStatement;
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., log it, throw a custom exception, etc.)
	    }
		return null;
	}
	
	 public static String hashPassword(String password) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

	            // Convert byte array to hexadecimal string
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                String hex = Integer.toHexString(0xff & b);
	                if (hex.length() == 1) {
	                    hexString.append('0');
	                }
	                hexString.append(hex);
	            }

	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            // Handle the exception according to your application's requirements
	            return null;
	        }
	}
	
}
