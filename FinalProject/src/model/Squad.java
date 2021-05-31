package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.Competition.CompetitionType;

public class Squad implements Serializable {

	private ArrayList<Sportsman> squadSportsmen;
	private CompetitionType competitionType;
	private String squadNationality;
	private int numOfSquadMedals;

	public Squad(ArrayList<Sportsman> squadSportsmen, String squadNationality, CompetitionType competitionType) {
		this.competitionType = competitionType;
		this.squadSportsmen = squadSportsmen;
		this.squadNationality = squadNationality;
		this.numOfSquadMedals = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squad other = (Squad) obj;
		if (competitionType != other.competitionType)
			return false;
		if (numOfSquadMedals != other.numOfSquadMedals)
			return false;
		if (squadNationality == null) {
			if (other.squadNationality != null)
				return false;
		} else if (!squadNationality.equals(other.squadNationality))
			return false;
		if (squadSportsmen == null) {
			if (other.squadSportsmen != null)
				return false;
		} else if (!squadSportsmen.equals(other.squadSportsmen))
			return false;
		return true;
	}

	public CompetitionType getCompetitionType() {
		return competitionType;
	}

	public void addSportsmenToSquad(Sportsman sportsman) {
		squadSportsmen.add(sportsman);

	}

	public void addMedals(int numOfSquadMedals) {
		this.numOfSquadMedals += numOfSquadMedals;
	}

	public ArrayList<Sportsman> getSquadSportsmen() {
		return squadSportsmen;
	}

	public void setSquadSportsmen(ArrayList<Sportsman> squadSportsmen) {
		this.squadSportsmen = squadSportsmen;
	}

	public String getSquadNationality() {
		return squadNationality;
	}

	public int getNumOfSquadMedals() {
		return numOfSquadMedals;
	}

	public boolean checkIfSportsmanExist(Sportsman sportsman) {
		if (squadSportsmen.contains(sportsman)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbSportsmen = new StringBuffer("\n");
		for (Sportsman sportsman : squadSportsmen) {
			sbSportsmen.append(sportsman.toString() + "\n");
		}

		return "Squad Nationality: " + squadNationality + "\nCompetition type: " + competitionType.toString()+"\n"
				+ sbSportsmen + "\n";
	}

}
