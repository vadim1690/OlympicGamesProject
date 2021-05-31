package model;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Competition implements Serializable {

	public static final int FIRST_PLACE_NUMBER_OF_MEDALS = 5;
	public static final int SECOND_PLACE_NUMBER_OF_MEDALS = 3;
	public static final int THIRD_PLACE_NUMBER_OF_MEDALS = 1;

	public static enum CompetitionType {

		RunningCompetition {
			public String toString() {
				return "Running Competition";
			}
		},

		HighJumpCompetition {
			public String toString() {
				return "High Jump Competition";
			}
		}

	};

	protected CompetitionType competitionType;
	protected Judge judge;
	protected Stadium stadium;
	protected LocalDate dateOfCompetition;

	public Competition(Judge judge, Stadium stadium, LocalDate dateOfCompetition, CompetitionType competitionType) {
		this.judge = judge;
		this.stadium = stadium;
		this.competitionType = competitionType;
		// set competitionType
		this.dateOfCompetition = dateOfCompetition;
	}

	public abstract void removeMedalsFromWinners();

	public Judge getJudge() {
		return judge;

	}

	public Stadium getStadium() {
		return stadium;
	}

	public CompetitionType getCompetitionType() {
		return competitionType;
	}

	public LocalDate getDateOfCompetition() {
		return dateOfCompetition;
	}

	

	@Override
	public String toString() {
		return "date of Competition: " + getDateOfCompetition() + "\ncompetition Type: " + competitionType + "\njudge: "
				+ judge.getJudgeName() + "\nstadium: " + stadium.getStadiumName() + "\n";
	}

	public void replaceJudge(Judge judge) {
		this.judge = judge;

	}

	public void replaceStadium(Stadium stadium) {
		this.stadium = stadium;

	}

}
