package model;

import java.io.Serializable;

import model.Competition.CompetitionType;

public class Judge implements Serializable {

	private CompetitionType competitionType;
	private String judgeName;
	private String judgeNation;

	public Judge(String judgeName, String judgeNation, CompetitionType competitionType) {

		this.judgeName = judgeName;
		this.judgeNation = judgeNation;
		this.competitionType = competitionType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Judge other = (Judge) obj;
		if (competitionType != other.competitionType)
			return false;
		if (judgeName == null) {
			if (other.judgeName != null)
				return false;
		} else if (!judgeName.equals(other.judgeName))
			return false;
		if (judgeNation == null) {
			if (other.judgeNation != null)
				return false;
		} else if (!judgeNation.equals(other.judgeNation))
			return false;
		return true;
	}

	public CompetitionType getCompetitionType() {
		return competitionType;
	}

	public String getJudgeName() {
		return judgeName;
	}

	public String getJudgeNation() {
		return judgeNation;
	}

	@Override
	public String toString() {
		return "Judge name: " + judgeName + "\nJudge nationality: " + judgeNation + "\nJurisdiction: " + competitionType.toString()
				+ "\n";

	}

}
