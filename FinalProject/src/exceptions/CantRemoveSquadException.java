package exceptions;

import model.Squad;

public class CantRemoveSquadException extends Exception {

	public CantRemoveSquadException(Squad squad) {
		super(squad.getSquadNationality()+" is participating a Competition\nTo remove the squad remove the competition first");

	}

}
