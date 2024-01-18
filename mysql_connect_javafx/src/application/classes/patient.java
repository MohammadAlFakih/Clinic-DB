package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.db.DBConnection;

public class patient extends user{
	
	public patient(String name,String email,String password,
			String phone,int age,String gender){
		super(name, email, password,
				 phone, age, gender, "patient");
	}
	
	public boolean insert_patient() throws SQLException{
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		PreparedStatement preparedStatement = super.insert_user();
		preparedStatement.executeUpdate();
		preparedStatement.close();
		return true;
	}
}
