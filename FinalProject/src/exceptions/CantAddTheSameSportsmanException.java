package exceptions;


public class CantAddTheSameSportsmanException extends Exception {
	public CantAddTheSameSportsmanException() {
	super("You can't set the same Sportsman in different winner places !");
	}
}
