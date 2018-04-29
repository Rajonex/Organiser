package sends;



import utils.Day;


public class GroupCalendar {
	private long id;
	private String teacherToken;
	private long groupId;
	private Day day;
	private long time;
	
	
	public String getTeacherToken() {
		return teacherToken;
	}
	public void setTeacherToken(String teacherToken) {
		this.teacherToken = teacherToken;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public long getTime() {
		return time;
//		return Time.valueOf(LocalTime.now());
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public GroupCalendar(long id, String teacherToken, long groupId, Day day, long time) {
		super();
		this.id = id;
		this.teacherToken = teacherToken;
		this.groupId = groupId;
		this.day = day;
		this.time = time;
	}
	public GroupCalendar() {}
	
	
}
