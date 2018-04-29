package sends;

import sends.Teacher;

public class ResultTeacher {

	private int result;
	private Teacher teacher;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public ResultTeacher(int result, Teacher teacher) {
		super();
		this.result = result;
		this.teacher = teacher;
	}
	

	
}
