package application.includes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import application.Main;
import application.classes.*;
import application.Main;
import application.secretary.*;
import application.patient.*;
public class index_viewController {

	ObservableList<String> gender_list = FXCollections.observableArrayList("Male","Female");
	
	@FXML
	private Tab login_tb;
	@FXML
	private Tab sigun_tb;
	@FXML
	private TabPane tabPane;
	@FXML
	private TextField login_email_tf;
	@FXML
	private TextField login_password_tf;
	@FXML
	private TextField name_tf;
	@FXML
	private TextField email_tf;
	@FXML
	private TextField password_tf;
	@FXML
	private TextField age_tf;
	@FXML
	private TextField phone_tf;
	@FXML
	private ChoiceBox gender_cb;
	
	@FXML
	private void initialize() {
		gender_cb.setValue("Male");
		gender_cb.setItems(gender_list);
	}

	@FXML
    private void age_handler(KeyEvent event) {
		age_tf.setText(age_tf.getText().replaceAll("[^\\d]", ""));
		age_tf.positionCaret(age_tf.getText().length());
    }
	
	@FXML
    private void phone_handler(KeyEvent event) {
		phone_tf.setText(phone_tf.getText().replaceAll("[^\\d]", ""));
		phone_tf.positionCaret(phone_tf.getText().length());
    }
	
	@FXML
	private void login() throws SQLException, IOException {
		String email = login_email_tf.getText();
		String password = login_password_tf.getText().trim();
		password = user.hashPassword(password);
		if(email.trim().isEmpty() || password.trim().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Please enter valid information");
	        alert.showAndWait();
		}
		String query = "SELECT id,password,role FROM patient WHERE email = ?"
				+ " UNION "+
				"SELECT id,password,role FROM doctor WHERE email = ?"
				+ " UNION "+
				"SELECT id,password,role FROM secretary WHERE email = ?"
				+ " UNION "+
				"SELECT id,password,role FROM admin WHERE email = ?";
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		PreparedStatement stmt = dbc.prepareStatement(query);
		for (int i = 1; i <= 4; i++) {
		    stmt.setString(i, email);
		}
		ResultSet result = stmt.executeQuery();
		if(!result.next() || !result.getString("password").trim().equals(password)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        System.out.println("Invalid credentiels");
	        alert.showAndWait();
		}
		else {
			String role = result.getString("role");
			role = role.trim();
			int id = result.getInt("id");
			int doctor_id = -1;
			
			//Secretary
			if(role.equals("secretary")) {
				query = "Select doctor_id FROM secretary WHERE id=?";
				stmt = dbc.prepareStatement(query);
				stmt.setInt(1,id);
				result = stmt.executeQuery();
				while(result.next()) {
					doctor_id = result.getInt("doctor_id");
				}
				requests_viewController.doctor_id = doctor_id;
				//Open secretary window
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("secretary/requests_view.fxml"));
				Main.mainLayout = loader.load();
				Scene scene = new Scene(Main.mainLayout);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
			}
			
			//Patient
			if(role.equals("patient")) {
				main_patient_viewController.patient_id = id;
				//Open patient window
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("patient/main_patient_view.fxml"));
				Main.mainLayout = loader.load();
				Scene scene = new Scene(Main.mainLayout);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
			}
		}
	}
	
	@FXML
	private void signup() throws SQLException {
		String name = name_tf.getText().trim();
		String email = email_tf.getText().trim();
		String password = password_tf.getText().trim();
		String phone = phone_tf.getText().trim();
		String gender="M";
		if(gender_cb.getValue()=="Female")
			gender="F";
		int age = 0;
		if(!age_tf.getText().trim().isEmpty())
			age = Integer.parseInt(age_tf.getText().trim());
		if(name.trim().isEmpty() || email.trim().isEmpty() || password_tf.getText().trim().isEmpty()
			|| phone.trim().isEmpty() || gender.trim().isEmpty() || age_tf.getText().trim().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Invalid information");
	        alert.showAndWait();
	        return;
		}
		patient new_patient = new patient(name,email,password,
				phone,age,gender);
		if(new_patient.insert_patient()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Account created successfully");
	        alert.showAndWait();
	        tabPane.getSelectionModel().select(login_tb);
		}
		else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Email already used");
	        alert.showAndWait();
		}
		
	}
}
