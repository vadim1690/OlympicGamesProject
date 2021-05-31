package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class PersonalCompetition extends Competition {

	private ArrayList<Sportsman> participants;
	private Sportsman firstPlace, secondPlace, thirdPlace;

	public PersonalCompetition(Judge judge, Stadium stadium, LocalDate dateOfCompetition,
			CompetitionType competitionType, ArrayList<Sportsman> selectedSportsmen) {
		super(judge, stadium, dateOfCompetition, competitionType);
		setParticipants(selectedSportsmen);
	}

	public ArrayList<Sportsman> getParticipants() {
		return participants;
	}

	private void setParticipants(ArrayList<Sportsman> participants) {
		this.participants = participants;
	}

	public void addSportsman(Sportsman sportsman) {
		if (this.competitionType == CompetitionType.RunningCompetition && sportsman instanceof HighJumper) {
			// throw exception
		}
		if (this.competitionType == CompetitionType.HighJumpCompetition && sportsman instanceof Runner) {
			// throw exception
		}
		participants.add(sportsman);
	}

	public void setFirstPlace(Sportsman sportsman) {
		this.firstPlace = sportsman;
		

	}

	public void setSecondPlace(Sportsman sportsman) {
		this.secondPlace = sportsman;
		

	}

	public void setThirdPlace(Sportsman sportsman) {
		this.thirdPlace = sportsman;
		
	}

	public void setWinners(Sportsman firstPlace, Sportsman secondPlace, Sportsman thirdPlace) {
		firstPlace.setNumOfPersonalMedals(FIRST_PLACE_NUMBER_OF_MEDALS);
		secondPlace.setNumOfPersonalMedals(SECOND_PLACE_NUMBER_OF_MEDALS);
		thirdPlace.setNumOfPersonalMedals(THIRD_PLACE_NUMBER_OF_MEDALS);

		setFirstPlace(firstPlace);
		setSecondPlace(secondPlace);
		setThirdPlace(thirdPlace);
	}

	@Override
	public String toString() {
		StringBuffer sbAllSportsmen = new StringBuffer("\n");
		for (Sportsman sportsman : participants) {
			sbAllSportsmen.append(sportsman.toString());
			sbAllSportsmen.append("\n");
		}
		sbAllSportsmen
				.append("First Place: " + firstPlace.getSportsmanName() + "," + firstPlace.getSportsmanNationality());
		sbAllSportsmen.append(
				"\nSecond Place: " + secondPlace.getSportsmanName() + "," + secondPlace.getSportsmanNationality());
		sbAllSportsmen
				.append("\nThird Place: " + thirdPlace.getSportsmanName() + "," + thirdPlace.getSportsmanNationality());
		return super.toString() + sbAllSportsmen.toString();
	}

	public Sportsman getFirstPlace() {
		return firstPlace;
	}

	public Sportsman getSecondPlace() {
		return secondPlace;
	}

	public Sportsman getThirdPlace() {
		return thirdPlace;
	}

	@Override
	public void removeMedalsFromWinners() {
		firstPlace.setNumOfPersonalMedals(-(FIRST_PLACE_NUMBER_OF_MEDALS));
		secondPlace.setNumOfPersonalMedals(-(SECOND_PLACE_NUMBER_OF_MEDALS));
		thirdPlace.setNumOfPersonalMedals(-(THIRD_PLACE_NUMBER_OF_MEDALS));

	}

}
