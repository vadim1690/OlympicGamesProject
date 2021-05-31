package exceptions;

import java.time.LocalDate;

public class DatesMismatchException extends Exception {

	public DatesMismatchException(LocalDate startingDate, LocalDate endingDate) {
		super("The dates are not valid\nStaring date: "+startingDate+"\nEnding date: "+endingDate+"\nTry again");
	}

}
