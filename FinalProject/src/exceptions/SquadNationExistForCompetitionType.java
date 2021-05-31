package exceptions;

import model.Competition.CompetitionType;

public class SquadNationExistForCompetitionType extends Exception {

	public SquadNationExistForCompetitionType(String nation, CompetitionType competitionType) {
		super("There is already a " + nation + " squad that competes " + competitionType + " in the system");
	}

}
