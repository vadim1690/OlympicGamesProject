package exceptions;

public class CantAddTheSameSquadException extends Exception {
	public CantAddTheSameSquadException() {
		super("You can't set the same Squad in different winner places !");
	}
}
