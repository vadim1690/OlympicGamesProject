package exceptions;

import model.Stadium;

public class CantRemoveStadiumException extends Exception {

	public CantRemoveStadiumException(Stadium stadium) {
		super("the stadium " + stadium.getStadiumName()
				+ " is holding a competition and can`t be removed\nTo remove the stadium remove the competition first");
	}

}
