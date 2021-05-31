package model;

public class HighJumper extends Sportsman{

	public HighJumper(String sportsmanName,String sportsmanNationality) {
		super(sportsmanName, sportsmanNationality);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return super.toString()+"\nCompetition type: " +"high jumper"+"\n";
	}

}
