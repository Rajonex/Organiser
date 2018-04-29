package sends;

import java.util.ArrayList;
import java.util.List;


public class Lesson {
	private long id;
	private List<StudentPresent> studentPresent;
	private String topic;
	private String description;
	private long date;
	private long groupId;
	private String teacherToken;



	public List<StudentPresent> getStudentPresent() {
		return studentPresent;
	}

	public void setStudentPresent(List<StudentPresent> studentPresent) {
		this.studentPresent = studentPresent;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}


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
	
	public String getTeacherToken() {
		return teacherToken;
	}

	public void setTeacherToken(String teacherToken) {
		this.teacherToken = teacherToken;
	}

	public Lesson(long id, List<StudentPresent> studentPresent, String topic, String description, long date,
			long groupId, String teacherToken) {
		super();
		this.id = id;
		this.studentPresent = studentPresent;
		this.topic = topic;
		this.description = description;
		this.date = date;
		this.groupId = groupId;
		this.teacherToken = teacherToken;
	}

	public Lesson() {
		studentPresent = new ArrayList<>();
	}

}
