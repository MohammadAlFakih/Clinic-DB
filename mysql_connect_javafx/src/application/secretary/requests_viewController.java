package application.secretary;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import application.Main;
import application.classes.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class requests_viewController {
	
	public static int doctor_id;
	
	@FXML
	private ListView appointments_lv;
	
	
	
	@FXML
	private void initialize() {
		ObservableList<appointment> appointments = appointment.getappointments(doctor_id,"secretary");
        
		// Set the items to the ListView
		appointments_lv.setItems(appointments);
		
		// Create ListView with a custom cell factory
		appointments_lv.setCellFactory(param -> new appListCell());
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
                setText(app.toString());
                
                //If pending allow accept
                Button removeButton = new Button("Remove");
                removeButton.setOnAction(event -> handleRemoveButton(app));
                if(app.get_status().equals("pending")) {
                	Button acceptButton = new Button("Accept");
                	acceptButton.setOnAction(event -> handleAcceptButton(app));
                	hbox.getChildren().addAll(acceptButton,removeButton);
                }
                else {
                	hbox.getChildren().addAll(removeButton);
                }
            }
        }
	}
	
	public void handleAcceptButton(appointment app) {
        // Handle accept button action (update the database or perform any other action)
        app.accept_appointment();
        initialize();
    }

    private void handleRemoveButton(appointment app) {
        // Handle remove button action (update the database or perform any other action)
        app.remove_appointment();
        initialize();
    }
}
