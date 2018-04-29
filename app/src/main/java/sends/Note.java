package sends;

public class Note {
	private long id;
	private String teacherToken;
	private String title;
	private String text;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public Note(long id, String teacherToken, String title, String text) {
		super();
		this.id = id;
		this.teacherToken = teacherToken;
		this.title = title;
		this.text = text;
	}

	public Note(Note note)
	{
		this.id = note.id;
		this.teacherToken = note.teacherToken;
		this.title = note.title;
		this.text = note.text;
	}

	public Note() {

	}

}
