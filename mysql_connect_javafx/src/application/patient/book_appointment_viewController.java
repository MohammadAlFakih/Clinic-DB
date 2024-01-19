package application.patient;

import application.classes.slot;
import application.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.classes.appointment;
import application.classes.doctor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class book_appointment_viewController {
	@FXML
	private ListView available_slots_lv;
	@FXML
	private TextField start_hour;
	@FXML
	private TextField start_min;
	@FXML
	private TextField end_hour;
	@FXML
	private TextField end_min;
	@FXML
	private Label info_lb;
	
	public static int patient_id;
	public static int doctor_id;
	public static String date;
	public static String doctor_name;
	
	@FXML
	private void initialize() {
		info_lb.setText("With Dc. "+doctor_name+" on "+date);
		ObservableList<slot> free_slots = slot.get_available_slots(doctor_id,date);
		available_slots_lv.setItems(free_slots);
		available_slots_lv.setCellFactory(param -> new slotListCell());
	}
	private class slotListCell extends ListCell<slot> {
        @Override
        protected void updateItem(slot st, boolean empty) {
            super.updateItem(st, empty);
            if (empty || st == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox();
                
                setGraphic(hbox);
                setText(st.toString());
               
            }
        }
	}
	
	@FXML
	private void home() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("patient/main_patient_view.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
	
	@FXML void back() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("patient/available_doctors.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
	
	@FXML void book() throws SQLException, IOException {
		String start_date=date+" "+start_hour.getText()+":"+start_min.getText();
		String end_date= date+" "+end_hour.getText()+":"+end_min.getText();
		//Check if the slot is available
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		String query = "SELECT is_time_slot_available(?,?,?)";
		PreparedStatement stmt = dbc.prepareStatement(query);
		stmt.setString(1,start_date);
        stmt.setString(2, end_date);
        stmt.setInt(3,doctor_id);
        int is_available=0;
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
        	is_available=result.getInt(1);
        }
        stmt.close();
        if(is_available==0) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Invalid appointment time!!");
	        alert.showAndWait();
        	return;
        }
        appointment app = new appointment(doctor_id,patient_id,start_date,end_date);
        app.insert_appointment();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Your appointment is pending!!");
        alert.showAndWait();
        FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("patient/main_patient_view.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
}
