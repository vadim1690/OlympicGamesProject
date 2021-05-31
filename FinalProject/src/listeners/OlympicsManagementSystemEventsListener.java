package listeners;

import model.Competition;
import model.Judge;
import model.Sportsman;
import model.Squad;
import model.Stadium;

public interface OlympicsManagementSystemEventsListener {
	
	void addCompetitionToModelEvent(Competition competition,Boolean readFromFile);

	void addJudgeToModelEvent(Judge judge,Boolean readFromFile);

	void addStadiumToModelEvent(Stadium stadium, Boolean readFromFile);

	void removeCompetitionFromModelEvent(Competition selectedItem, Boolean pressedExit);

	void removeJudgeFromModelEvent(Judge judge);

	void removeCompetitionFromModelEvent(Stadium stadium);

	void printAllJudgesModelEvent(StringBuffer sbPrintAllJudges);

	void printAllStadiumsModelEvent(StringBuffer sbPrintAllStadiums);

	void addSportsmanModelToEvent(Sportsman sportsman,Boolean readFromFile);

	void removeSportsmanFromModelEvent(Sportsman sportsman);

	void printAllSportsmenModelEvent(StringBuffer sbPrintAllSportmen);

	void AddSquadToModelEvent(Squad squad,Boolean readFromFile);

	void removeSquadFromModelEvent(Squad squad);

	void printAllSquadsModelEvent(StringBuffer sbPrintAllSquads);

	void addWinnersToCompetitionModelEvent(Competition currentCompetition);

	void saveFileModelEvent();

	void showMedalsForCountryModelEvent(int numberOfPersonalMedals, int numberOfNationalMedals, String country);

	void winnersModelEvent(String firstPlace, String secondPlace, String thirdPlace, int numberOfFirstPlaceMedals,
			int numberOfSecondPlaceMedals, int numberOfThirdPlaceMedals);

	void DatePickedAndSetModelEvent();


	
}
