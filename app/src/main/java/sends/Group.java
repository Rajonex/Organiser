package sends;

import java.util.ArrayList;
import java.util.List;



public class Group {
	private long id;
	private String name;
	private List<Student> students;
	private double rate;
	private String teacherToken;
	private boolean activity;
	private List<GroupCalendar> groupCalendar;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	public List<GroupCalendar> getGroupCalendar() {
		return groupCalendar;
	}

	public void setGroupCalendar(List<GroupCalendar> groupCalendar) {
		this.groupCalendar = groupCalendar;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTeacherToken() {
		return teacherToken;
	}

	public void setTeacherToken(String teacherToken) {
		this.teacherToken = teacherToken;
	}

	public Group(long id, String name, List<Student> students, double rate, String teacherToken, boolean activity,
			List<GroupCalendar> groupCalendar) {
		super();
		this.id = id;
		this.name = name;
		this.students = students;
		this.rate = rate;
		this.teacherToken = teacherToken;
		this.activity = activity;
		this.groupCalendar = groupCalendar;
	}

	public Group() {
		this.students = new ArrayList<>();
		this.groupCalendar = new ArrayList<>();
	}

}
