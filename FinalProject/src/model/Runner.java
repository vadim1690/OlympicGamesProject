package model;

public class Runner extends Sportsman{

	public Runner(String sportsmanName,String sportsmanNationality) {
		super(sportsmanName, sportsmanNationality);
	}

	@Override
	public String toString() {
		return super.toString()+"\nCompetition type: " +getClass().getSimpleName()+"\n";
	}


}
