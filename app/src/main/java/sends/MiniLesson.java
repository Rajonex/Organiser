package sends;

public class MiniLesson {
	private long id;
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MiniLesson(long id, String topic) {
		super();
		this.id = id;
		this.topic = topic;
	}

	public MiniLesson() {

	}
}
