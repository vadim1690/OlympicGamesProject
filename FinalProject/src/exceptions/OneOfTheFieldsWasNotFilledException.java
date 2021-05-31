package exceptions;

public class OneOfTheFieldsWasNotFilledException extends Exception {
	
	public OneOfTheFieldsWasNotFilledException() {
		super("All the fields should be filled !");
	}
}
