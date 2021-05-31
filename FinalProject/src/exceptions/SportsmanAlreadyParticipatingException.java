package exceptions;

import java.time.LocalDate;

import model.Sportsman;

public class SportsmanAlreadyParticipatingException extends Exception {
	public SportsmanAlreadyParticipatingException(Sportsman sportsman, LocalDate date) {
		super("The sprotsman "+sportsman.getSportsmanName()+" already participating a competition at "+date);
	}
}
