package model;

import java.io.Serializable;

public class Sportsman implements Serializable {
	private String sportsmanName;
	private String sportsmanNationality;
	private int numOfPersonalMedals;

	

	public Sportsman(String sportsmanName, String sportsmanNationality) {
		setSportsmanName(sportsmanName);
		setSportsmanNationality(sportsmanNationality);

	}
	
	@Override
	public boolean equals(Object sportsman) {
		if (sportsman instanceof Sportsman) {
			Sportsman newSportsman = (Sportsman) sportsman;
			return newSportsman.sportsmanName.equals(this.sportsmanName)
					&& newSportsman.sportsmanNationality.equals(this.sportsmanNationality)
					&& newSportsman.numOfPersonalMedals == this.numOfPersonalMedals;
		} else {
			return false;
		}
	}

	public String getSportsmanNationality() {
		return sportsmanNationality;
	}

	private void setSportsmanNationality(String sportsmanNationality) {
		this.sportsmanNationality = sportsmanNationality;
	}

	public String getSportsmanName() {
		return sportsmanName;
	}

	private void setSportsmanName(String sportsmanName) {
		this.sportsmanName = sportsmanName;
	}

	public int getNumOfPersonalMedals() {
		return numOfPersonalMedals;
	}
												
	public void setNumOfPersonalMedals(int numOfPersonalMedals) {
		this.numOfPersonalMedals += numOfPersonalMedals;
	}

	@Override
	public String toString() {
		return "Sportsman name: " + sportsmanName + "\nSportsman`s nationality:" + sportsmanNationality
				+ "\nNumber of personal medals: " + numOfPersonalMedals;
	}

}
