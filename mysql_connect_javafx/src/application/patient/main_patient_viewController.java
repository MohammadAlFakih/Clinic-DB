package application.patient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import application.Main;
import application.classes.appointment;
import application.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import application.patient.*;

public class main_patient_viewController {
	public static int patient_id;
	private int appointment_counter = 1;
	@FXML
	private ListView appointments_lv;
	@FXML
	private ChoiceBox specialization_cb;
	@FXML
	private DatePicker date_p;
	@FXML
	private ChoiceBox city_cb;
	
	ObservableList<String> specialization_list = FXCollections.observableArrayList();
	ObservableList<String> city_list = FXCollections.observableArrayList();
	
	
	
	
	@FXML
	private void initialize() throws SQLException {
		
		ObservableList<appointment> appointments = appointment.getappointments(patient_id,"patient");
		// Set the items to the ListView
		appointments_lv.setItems(appointments);
		// Create ListView with a custom cell factory
		appointments_lv.setCellFactory(param -> new appListCell());
		
		fill_specialization_list();
		fill_city_list();
		specialization_cb.setItems(specialization_list);
		city_cb.setItems(city_list);
		specialization_cb.setValue(specialization_list.get(0));
		city_cb.setValue(city_list.get(0));
	}
	
	private class appListCell extends ListCell<appointment> {
		
        @Override
        protected void updateItem(appointment app, boolean empty) {
            super.updateItem(app, empty);
            if (empty || app == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox();
                
                setGraphic(hbox);
                
                setText("# "+(appointment_counter)+" "+app.display_for_patient());
                
                //If pending allow remove
                if(app.get_status().equals("pending")) {
                	Button removeButton = new Button("Cancel");
                	removeButton.setOnAction(event -> {
						try {
							handleAcceptButton(app);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
                	hbox.getChildren().addAll(removeButton);
                }
            }
        }

		private void handleAcceptButton(appointment app) throws SQLException {
			app.remove_appointment();
			initialize();
		}
	}
	
	//New Appointment Tab
	private void fill_specialization_list() throws SQLException {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		String query = "SELECT alias FROM specialization";
		PreparedStatement stmt = dbc.prepareStatement(query);
		ResultSet result = stmt.executeQuery();
		while(result.next()) {
			specialization_list.add(result.getString("alias"));
		}
		stmt.close();
	}
	
	private void fill_city_list() throws SQLException {
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		String query = "SELECT alias FROM city";
		PreparedStatement stmt = dbc.prepareStatement(query);
		ResultSet result = stmt.executeQuery();
		while(result.next()) {
			city_list.add(result.getString("alias"));
		}
		stmt.close();
	}
	
	@FXML
	private void log_out() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("includes/index_view.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
	
	@FXML
	private void search_appointments() throws IOException {
		LocalDate selectedDate = date_p.getValue();
		LocalDate today = LocalDate.now();
		if(date_p.getValue() == null || today.isAfter(selectedDate)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText(null);
	        alert.setContentText("Invalid date");
	        alert.showAndWait();
	        return;
		}
		available_doctorsController.search_city = (String) city_cb.getValue();
		available_doctorsController.search_specialization = (String) specialization_cb.getValue();
		available_doctorsController.search_date = date_p.getValue().toString();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("patient/available_doctors.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
}
