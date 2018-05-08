package sends;

public class Student {
	private long id;
	private String firstname;
	private String lastname;
	private String phone;
	private String email;
	private String teacherToken;
	private boolean activity;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	public String getTeacherToken() {
		return teacherToken;
	}

	public void setTeacherToken(String teacherToken) {
		this.teacherToken = teacherToken;
	}
	
	

	public Student(long id, String firstname, String lastname, String phone, String email, String teacherToken,
			boolean activity) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
		this.teacherToken = teacherToken;
		this.activity = activity;
	}

	public Student() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;

		Student student = (Student) o;

		if (getId() != student.getId()) return false;
		if (isActivity() != student.isActivity()) return false;
		if (!getFirstname().equals(student.getFirstname())) return false;
		if (!getLastname().equals(student.getLastname())) return false;
		if (getPhone() != null ? !getPhone().equals(student.getPhone()) : student.getPhone() != null)
			return false;
		if (getEmail() != null ? !getEmail().equals(student.getEmail()) : student.getEmail() != null)
			return false;
		return getTeacherToken().equals(student.getTeacherToken());
	}

	@Override
	public int hashCode() {
		int result = (int) (getId() ^ (getId() >>> 32));
		result = 31 * result + getFirstname().hashCode();
		result = 31 * result + getLastname().hashCode();
		result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
		result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
		result = 31 * result + getTeacherToken().hashCode();
		result = 31 * result + (isActivity() ? 1 : 0);
		return result;
	}
}
