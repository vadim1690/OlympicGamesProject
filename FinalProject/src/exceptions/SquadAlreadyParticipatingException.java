package exceptions;

import java.time.LocalDate;

import model.Squad;

public class SquadAlreadyParticipatingException extends Exception {
	public SquadAlreadyParticipatingException(Squad squad, LocalDate date) {
		super(squad.getSquadNationality()+" already participating a competition at "+date);
	}
}
