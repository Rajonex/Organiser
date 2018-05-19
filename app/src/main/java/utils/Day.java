package utils;

import java.util.Calendar;

public enum Day {
	MONDAY("Poniedziałek", Calendar.MONDAY), TUESDAY("Wtorek", Calendar.TUESDAY), WEDNESDAY("Środa", Calendar.WEDNESDAY), THURSDAY("Czwartek", Calendar.THURSDAY), FRIDAY("Piątek", Calendar.FRIDAY), SATURDAY("Sobota", Calendar.SATURDAY), SUNDAY("Niedziela", Calendar.SUNDAY);

	private String description;
	private int calendarDay;
	Day(String desc, int day)
	{
		description = desc;
		calendarDay = day;
	}

	public String getDescription() {
		return description;
	}

	public int getCalendarDay() {
		return calendarDay;
	}
}
