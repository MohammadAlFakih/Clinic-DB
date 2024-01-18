package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.db.DBConnection;

public class secretary extends user{
	private int doctor_id;
	
	public secretary(String name,String email,String password,
			String phone,int age,String gender,int doctor_id){
		super(name, email, password,
				 phone, age, gender, "secretary");
		this.doctor_id = doctor_id;
	}
	
	public void insert_secretary() throws SQLException {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		PreparedStatement preparedStatement = super.insert_user();
		preparedStatement.setInt(7, doctor_id);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
}
