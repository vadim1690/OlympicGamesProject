package model;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Competition.CompetitionType;

public class NationalCompetition extends Competition {
	
	private ArrayList<Squad> squadParticipants;
	private Squad firstPlace, secondPlace, thirdPlace;

	public NationalCompetition(Judge judge, Stadium stadium, LocalDate dateOfCompetition,
			CompetitionType competitionType, ArrayList<Squad> selectedSquad) {
		super(judge, stadium, dateOfCompetition, competitionType);
		setSquadParticipants(selectedSquad);
	}

	public ArrayList<Squad> getSquadParticipants() {
		return squadParticipants;
	}

	private void setSquadParticipants(ArrayList<Squad> squadParticipants) {
		this.squadParticipants = squadParticipants;
	}

	public void setFirstPlace(Squad squadFirst) {
		this.firstPlace = squadFirst;

		

	}

	public void setSecondPlace(Squad squadSecond) {
		this.secondPlace = squadSecond;
		

	}

	public void setThirdPlace(Squad squadThird) {
		this.thirdPlace = squadThird;
		

	}

	public void setWinners(Squad firstPlace, Squad secondPlace, Squad thirdPlace) {
		firstPlace.addMedals(FIRST_PLACE_NUMBER_OF_MEDALS);
		secondPlace.addMedals(SECOND_PLACE_NUMBER_OF_MEDALS);
		thirdPlace.addMedals(THIRD_PLACE_NUMBER_OF_MEDALS);

		setFirstPlace(firstPlace);
		setSecondPlace(secondPlace);
		setThirdPlace(thirdPlace);
	}

	@Override
	public String toString() {
		StringBuffer sbAllSquads = new StringBuffer("\n");
		for (Squad squad : squadParticipants) {
			sbAllSquads.append(squad.toString());
			sbAllSquads.append("\n");
		}
		sbAllSquads.append("First Place: " + firstPlace.getSquadNationality());
		sbAllSquads.append("\nSecond Place: " + secondPlace.getSquadNationality());
		sbAllSquads.append("\nThird Place: " + thirdPlace.getSquadNationality());
		return super.toString() + sbAllSquads.toString();
	}

	public Squad getFirstPlace() {
		return firstPlace;
	}

	public Squad getSecondPlace() {
		return secondPlace;
	}

	public Squad getThirdPlace() {
		return thirdPlace;
	}

	@Override
	public void removeMedalsFromWinners() {
		firstPlace.addMedals(-(FIRST_PLACE_NUMBER_OF_MEDALS));
		secondPlace.addMedals(-(SECOND_PLACE_NUMBER_OF_MEDALS));
		thirdPlace.addMedals(-(THIRD_PLACE_NUMBER_OF_MEDALS));
		
	}

	

}
