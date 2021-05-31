package exceptions;

import java.time.LocalDate;

public class DateOfTheCompetitionIsNotValidException extends Exception {

	public DateOfTheCompetitionIsNotValidException(LocalDate startDate,LocalDate endDate) {
		super("The date you chose for the competition is not between "+startDate+" to "+endDate+" !");
	}
}
