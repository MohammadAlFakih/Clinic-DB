package application.patient;

import java.io.IOException;

import application.Main;
import application.classes.appointment;
import application.classes.doctor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import application.patient.*;

public class available_doctorsController {
	@FXML
	private ListView available_doctors_lv;
	
	public static String search_date;
	public static String search_specialization;
	public static String search_city;
	
	
	@FXML
	private void initialize() {
		ObservableList<doctor> doctors = doctor.get_available_doctors(search_specialization,search_city);
        
		// Set the items to the ListView
		available_doctors_lv.setItems(doctors);
		
		// Create ListView with a custom cell factory
		available_doctors_lv.setCellFactory(param -> new doctorListCell());
	}
	
	private class doctorListCell extends ListCell<doctor> {
        @Override
        protected void updateItem(doctor doc, boolean empty) {
            super.updateItem(doc, empty);
            if (empty || doc == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox();
                
                setGraphic(hbox);
                setText(doc.toString());
                
                //If pending allow accept
                Button check_doctor = new Button("Check");
                check_doctor.setOnAction(event -> {
					try {
						handleRemoveButton(doc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
                hbox.getChildren().addAll(check_doctor);
            }
        }

		private void handleRemoveButton(doctor doc) throws IOException {
			book_appointment_viewController.date = search_date;
			book_appointment_viewController.doctor_id = doc.getID();
			book_appointment_viewController.doctor_name  = doc.getName();
			book_appointment_viewController.patient_id = main_patient_viewController.patient_id;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("patient/book_appointment_view.fxml"));
			Main.mainLayout = loader.load();
			Scene scene = new Scene(Main.mainLayout);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
		}
	}
	
	@FXML
	private void back() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("patient/main_patient_view.fxml"));
		Main.mainLayout = loader.load();
		Scene scene = new Scene(Main.mainLayout);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
}
