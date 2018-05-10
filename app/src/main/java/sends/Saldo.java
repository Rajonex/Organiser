package sends;

import java.sql.Date;

public class Saldo {
	private long id;
	private long groupId;
	private Student student;
	private int lessonsNumber;
	private double toPay;
	private double paid;
	private long date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getLessonsNumber() {
		return lessonsNumber;
	}

	public void setLessonsNumber(int lessonsNumber) {
		this.lessonsNumber = lessonsNumber;
	}

	public double getToPay() {
		return toPay;
	}

	public void setToPay(double toPay) {
		this.toPay = toPay;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Saldo(long id, long groupId, Student student, int lessonsNumber, double toPay, double paid, long date) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.student = student;
		this.lessonsNumber = lessonsNumber;
		this.toPay = toPay;
		this.paid = paid;
		this.date = date;
	}

	public Saldo() {
	}
}
