package view;

import controller.SystemController;
import model.Competition;
import model.Judge;
import model.Sportsman;
import model.Squad;
import model.Stadium;

public interface AbstractView  {

	void registerListener(SystemController systemController);

	void addJudgeToUI(Judge judge, Boolean readFromFile);

	void addCompetitionToUI(Competition competition, Boolean readFromFile);

	void addStadiumToUI(Stadium stadium, Boolean readFromFile);

	void removeCompetitionFromUI(Competition selectedItem, Boolean pressedExit);

	void removeJudgeFromUI(Judge judge);

	void removeStadium(Stadium stadium);

	void printAllJudges(StringBuffer sbPrintAllJudges);

	void printAllStadiums(StringBuffer sbPrintAllStadiums);

	void addSportsmanToUI(Sportsman sportsman,Boolean readFromFile);

	void removeSportmanFromUI(Sportsman sportsman);

	void printAllSportmenUI(StringBuffer sbPrintAllSportmen);

	void addSquadToUI(Squad squad,Boolean readFromFile);

	void removeSquadFromUI(Squad squad);

	void printAllSquadsUI(StringBuffer sbPrintAllSquads);

	void addWinnersToCompetitionUI(Competition currentCompetition);

	void saveAndExitUI();

	void showMedalsForCountryUI(int numberOfPersonalMedals, int numberOfNationalMedals, String country);

	void showWinnersUI(String firstPlace, String secondPlace, String thirdPlace, int numberOfFirstPlaceMedals,
			int numberOfSecondPlaceMedals, int numberOfThirdPlaceMedals);

	void DatePickedAndSet();
	
}
