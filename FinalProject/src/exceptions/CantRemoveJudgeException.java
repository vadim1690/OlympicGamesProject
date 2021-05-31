package exceptions;

import model.Judge;

public class CantRemoveJudgeException extends Exception {

	public CantRemoveJudgeException(Judge judge) {
		super("the judge "+judge.getJudgeName()+" is judging a competition and can`t be removed\nTo remove the judge remove the competition first");
	}

}
