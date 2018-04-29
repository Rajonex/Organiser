package sends;

import sends.Student;

public class StudentPresent {
	private long id;
	private Student student;
	private boolean presence;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isPresence() {
		return presence;
	}

	public void setPresence(boolean presence) {
		this.presence = presence;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StudentPresent(long id, Student student, boolean presence) {
		super();
		this.id = id;
		this.student = student;
		this.presence = presence;
	}

	public StudentPresent() {

	}

}
