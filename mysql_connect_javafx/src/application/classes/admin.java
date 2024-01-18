package application.classes;

public class admin extends user{
	public admin(String name,String email,String password,
			String phone,int age,String gender) {
		super(name, email, password,
				 phone, age, gender, "admin");
	}
}
