package exceptions;

import model.Sportsman;

public class CantRemoveSportsmanException extends Exception {

	public CantRemoveSportsmanException(Sportsman sportsman) {
		super("The sportsman "+sportsman.getSportsmanName()+" is participating a Competition\nTo remove the sportsman remove the competition first");
	}

}
