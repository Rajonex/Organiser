package utils;

public enum Day {
	MONDAY("Poniedziałek"), TUESDAY("Wtorek"), WEDNESDAY("Środa"), THURSDAY("Czwartek"), FRIDAY("Piątek"), SATURDAY("Sobota"), SUNDAY("Niedziela");

	private String description;
	Day(String desc)
	{
		description = desc;
	}

	public String getDescription() {
		return description;
	}
}
