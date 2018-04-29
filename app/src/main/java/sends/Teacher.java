package sends;

public class Teacher {
	private String login;
	private String password;
	private String token;
	private String name;




	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Teacher(String login, String password, String token, String name) {
		super();
		this.login = login;
		this.password = password;
		this.token = token;
		this.name = name;
	}

	public Teacher(Teacher teacher)
	{
		this.name = teacher.name;
		this.password = teacher.password;
		this.token = teacher.token;
		this.login = teacher.login;
	}

	public Teacher() {
	}

	@Override
	public String toString() {
		return "Teacher [name=" + name + ", password=" + password + ", token=" + token + "]";
	}

	
}
