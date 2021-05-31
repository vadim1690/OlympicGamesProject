package exceptions;

import model.Competition.CompetitionType;
import model.Judge;

public class WrongJudgeForTheCompetitionTypeException extends Exception {

	public WrongJudgeForTheCompetitionTypeException(Judge judge, CompetitionType competitionType) {
		super("The judge "+judge.getJudgeName()+" Can`t judge "+competitionType);
	}

}
