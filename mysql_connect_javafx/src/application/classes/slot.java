package application.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class slot {
	private String start_date;
	private String end_date;
	
	public slot(String start_date,String end_date) {
		this.start_date=start_date;
		this.end_date=end_date;
	}
	
	public String get_start_date() {
		return start_date;
	}
	
	public String get_end_date() {
		return end_date;
	}
	
	public static ObservableList<slot> get_available_slots(int doctor_id,String date) {
		ArrayList<slot> busy_slots = new ArrayList<slot>();
		DBConnection db;
		db = DBConnection.getInstance();
		Connection dbc = db.getConnection();
		ObservableList<slot> free_slots = FXCollections.observableArrayList();
		//First get the busy slots
		try {
			String query = "SELECT * FROM busy_schedule_view"
					+ " WHERE doctor_id=? AND DATE(start_date)=?";
			PreparedStatement stmt = dbc.prepareStatement(query);
	        stmt.setInt(1,doctor_id);
	        stmt.setString(2, date);
	        ResultSet result= stmt.executeQuery();
	        while(result.next()) {
	        	slot current = new slot(result.getString("start_date"),result.getString("end_date"));
	        	busy_slots.add(current);
	        }
	        stmt.close();
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., log it, throw a custom exception, etc.)	    
		}
		
		//Extract the free_slots now
		//First let's merge the intervals
		ArrayList<slot> merged_slots = new ArrayList<>();
		for(int i=0;i<busy_slots.size();i++) {
			String start = busy_slots.get(i).get_start_date();
			String end = busy_slots.get(i).get_end_date();
			if(i<busy_slots.size()-1) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime1 = LocalDateTime.parse(end, formatter);
		        LocalDateTime dateTime2 = LocalDateTime.parse(busy_slots.get(i+1).get_start_date(), formatter);
		        while(i<busy_slots.size() && !dateTime1.isBefore(dateTime2)) {
		        	i++;
		        	end = busy_slots.get(i).get_end_date();
		        	if(i==busy_slots.size()-1)
		        		break;
		        	dateTime1 = LocalDateTime.parse(end, formatter);
			        dateTime2 = LocalDateTime.parse(busy_slots.get(i+1).get_start_date(), formatter);
		        }
			}
			slot temp = new slot(start,end);
			if(temp.get_start_date().equals(temp.get_end_date()))
				continue;
			merged_slots.add(temp);
		}
		for(slot s:merged_slots) {
			System.out.println(s.get_start_date()+"->"+s.get_end_date());
		}
		
		//Now construct the available slots
		String start = "08:00";
		String end = "16:00";
		for(slot s:merged_slots) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime start_datetime = LocalDateTime.parse(s.get_start_date(), formatter);
			LocalTime start_time = start_datetime.toLocalTime();
			slot temp = new slot(start,start_time.toString());
			if(!temp.get_start_date().equals(temp.get_end_date()))
				free_slots.add(temp);
			LocalDateTime end_datetime = LocalDateTime.parse(s.get_end_date(), formatter);
			LocalTime end_time = end_datetime.toLocalTime();
			start = end_time.toString();
		}
		slot temp = new slot(start,end);
		if(!temp.get_start_date().equals(temp.get_end_date()))
			free_slots.add(temp);
		return free_slots;
	}
	
	@Override
	public String toString() {
		return start_date+"  till  "+end_date;
	}
}
