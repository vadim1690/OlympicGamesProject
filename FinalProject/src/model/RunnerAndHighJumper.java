package model;

public class RunnerAndHighJumper extends Sportsman {

	public RunnerAndHighJumper(String sportsmanName,String sportsmanNationality) {
		super(sportsmanName, sportsmanNationality);
	}
	@Override
	public String toString() {
		return super.toString()+"\nCompetition type: " + "runner and high jumper"+"\n";
	}

}
