package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import exceptions.CantAddTheSameSportsmanException;
import exceptions.CantAddTheSameSquadException;
import exceptions.CantRemoveJudgeException;
import exceptions.CantRemoveSportsmanException;
import exceptions.CantRemoveSquadException;
import exceptions.CantRemoveStadiumException;
import exceptions.DateOfTheCompetitionIsNotValidException;
import exceptions.DatesMismatchException;
import exceptions.NoCountriesParticipatingException;
import exceptions.NoSelectedItemException;
import exceptions.OneOfTheFieldsWasNotFilledException;
import exceptions.SportsmanAlreadyParticipatingException;
import exceptions.SquadAlreadyParticipatingException;
import exceptions.SquadNationExistForCompetitionType;
import exceptions.WrongJudgeForTheCompetitionTypeException;
import listeners.OlympicsManagementSystemEventsListener;
import listeners.OlympicsManagementSystemUIEventsListener;
import model.Competition;
import model.Competition.CompetitionType;
import model.Judge;
import model.OlympicsManagementSystem;
import model.Sportsman;
import model.Squad;
import model.Stadium;
import view.AbstractView;

public class SystemController
		implements OlympicsManagementSystemUIEventsListener, OlympicsManagementSystemEventsListener {

	OlympicsManagementSystem managementSystem;
	AbstractView systemView;

	public SystemController(OlympicsManagementSystem managementSystem, AbstractView systemView) throws ClassNotFoundException, IOException {
		this.managementSystem = managementSystem;
		this.systemView = systemView;
		
		managementSystem.registerListener(this);
		systemView.registerListener(this);
		
		
		try {
			readFiles();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveAndExitUI() throws IOException {
		managementSystem.saveAndExit();
	}

	@Override
	public void addPersonalCompetitionToUI(CompetitionType competitionType, Judge judge, Stadium stadium,
			LocalDate date, ArrayList<Sportsman> selectedSportsmen) throws OneOfTheFieldsWasNotFilledException,  DateOfTheCompetitionIsNotValidException, SportsmanAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException {
		managementSystem.addPersonalCompetition(competitionType, judge, stadium, date, selectedSportsmen);
	}

	@Override
	public void addNationalCompetitionToUI(CompetitionType competitionType, Judge judge, Stadium stadium,
			LocalDate date, ArrayList<Squad> selectedSquads) throws OneOfTheFieldsWasNotFilledException,  DateOfTheCompetitionIsNotValidException, SquadAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException {
		managementSystem.addNationalCompetition(competitionType, judge, stadium, date, selectedSquads);
	}

	@Override
	public void addWinnersToPersonalCompetitionUI(Competition currentCompetition, Sportsman firstPlace,
			Sportsman secondPlace, Sportsman thirdPlace) throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSportsmanException {
		managementSystem.addWinnersToPersonalCompetition(currentCompetition, firstPlace, secondPlace, thirdPlace);

	}

	@Override
	public void addJudgeToUI(String judgeName, String nation, CompetitionType competitionType) throws OneOfTheFieldsWasNotFilledException {

		managementSystem.addJudge(judgeName, nation, competitionType);

	}

	@Override
	public void addStadiumToUI(String stadiumName, String stadiumLocation, Integer numOfSeatsInStadium) throws OneOfTheFieldsWasNotFilledException {
		managementSystem.addStadium(stadiumName, stadiumLocation, numOfSeatsInStadium);
	}

	@Override
	public void addJudgeToModelEvent(Judge judge, Boolean readFromFile) {
		systemView.addJudgeToUI(judge, readFromFile);

	}

	@Override
	public void addCompetitionToModelEvent(Competition competition, Boolean readFromFile) {
		systemView.addCompetitionToUI(competition, readFromFile);

	}

	@Override
	public void addStadiumToModelEvent(Stadium stadium, Boolean readFromFile) {
		systemView.addStadiumToUI(stadium, readFromFile);
	}

	@Override
	public void removeCompetitionFromUI(Competition selectedItem, Boolean pressedExit) throws NoSelectedItemException {
		managementSystem.removeCompetition(selectedItem, pressedExit);
	}

	@Override
	public void removeCompetitionFromModelEvent(Competition selectedItem, Boolean pressedExit) {
		systemView.removeCompetitionFromUI(selectedItem, pressedExit);
	}

	@Override
	public void removeJudgeFromUI(Judge judge) throws NoSelectedItemException, CantRemoveJudgeException {
		managementSystem.removeJudge(judge);
	}

	@Override
	public void removeJudgeFromModelEvent(Judge judge) {
		systemView.removeJudgeFromUI(judge);
	}

	@Override
	public void removeStadiumFromUI(Stadium stadium) throws NoSelectedItemException, CantRemoveStadiumException {
		managementSystem.removeStadium(stadium);
	}

	@Override
	public void removeCompetitionFromModelEvent(Stadium stadium) {
		systemView.removeStadium(stadium);
	}

	@Override
	public void printAllJudgesUI() {
		managementSystem.printAllJudges();
	}

	@Override
	public void printAllJudgesModelEvent(StringBuffer sbPrintAllJudges) {
		systemView.printAllJudges(sbPrintAllJudges);
	}

	@Override
	public void printAllStadiumsUI() {
		managementSystem.printAllStadiums();
	}

	@Override
	public void printAllStadiumsModelEvent(StringBuffer sbPrintAllStadiums) {
		systemView.printAllStadiums(sbPrintAllStadiums);
	}

	@Override
	public void addSportsmanToUI(String sportsmanName, String sportsmanNationality, String sportsmanCompetitionType) throws OneOfTheFieldsWasNotFilledException {
		managementSystem.addSportsman(sportsmanName, sportsmanNationality, sportsmanCompetitionType);

	}

	@Override
	public void addSportsmanModelToEvent(Sportsman sportsman, Boolean readFromFile) {
		systemView.addSportsmanToUI(sportsman, readFromFile);
	}

	@Override
	public void removeSportsmanFromUI(Sportsman sportsman) throws NoSelectedItemException, CantRemoveSportsmanException {
		managementSystem.removeSportsman(sportsman);
	}

	@Override
	public void removeSportsmanFromModelEvent(Sportsman sportsman) {
		systemView.removeSportmanFromUI(sportsman);
	}

	@Override
	public void printAllSportsmenUI() {
		managementSystem.printAllSportmen();

	}

	@Override
	public void printAllSportsmenModelEvent(StringBuffer sbPrintAllSportmen) {
		systemView.printAllSportmenUI(sbPrintAllSportmen);
	}

	@Override
	public void addSquadToUI(String nation, CompetitionType competitionType, ArrayList<Sportsman> selectedSportsmen) throws OneOfTheFieldsWasNotFilledException, SquadNationExistForCompetitionType {
		managementSystem.addSquad(nation, competitionType, selectedSportsmen);

	}

	@Override
	public void AddSquadToModelEvent(Squad squad, Boolean readFromFile) {
		systemView.addSquadToUI(squad, readFromFile);

	}

	@Override
	public void removeSquadFromUI(Squad squad) throws NoSelectedItemException, CantRemoveSquadException {
		managementSystem.removeSquad(squad);
	}

	@Override
	public void removeSquadFromModelEvent(Squad squad) {
		systemView.removeSquadFromUI(squad);
	}

	@Override
	public void printAllSquadsUI() {
		managementSystem.printAllSquads();

	}

	@Override
	public void printAllSquadsModelEvent(StringBuffer sbPrintAllSquads) {
		systemView.printAllSquadsUI(sbPrintAllSquads);
	}

	@Override
	public void addWinnersToCompetitionModelEvent(Competition currentCompetition) {
		systemView.addWinnersToCompetitionUI(currentCompetition);

	}

	@Override
	public void addWinnersToNationalCompetitionUI(Competition currentCompetition, Squad firstPlace, Squad secondPlace,
			Squad thirdPlace) throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSquadException {
		managementSystem.addWinnersToNationalCompetition(currentCompetition, firstPlace, secondPlace, thirdPlace);

	}

	@Override
	public void saveFileModelEvent() {
		systemView.saveAndExitUI();
	}

	@Override
	public void readFiles() throws ClassNotFoundException, IOException {
		managementSystem.readFromFiles();
	}

	@Override
	public void showMedalsForTheCountryUIEvent(String country) throws OneOfTheFieldsWasNotFilledException {
		managementSystem.showMedalsForCountry(country);

	}

	@Override
	public void showMedalsForCountryModelEvent(int numberOfPersonalMedals, int numberOfNationalMedals, String country) {
		systemView.showMedalsForCountryUI(numberOfPersonalMedals, numberOfNationalMedals, country);

	}

	@Override
	public void finishOlympicGamesUIEvent() throws FileNotFoundException, NoCountriesParticipatingException {
		managementSystem.finishOlympicGames();

	}

	@Override
	public void winnersModelEvent(String firstPlace, String secondPlace, String thirdPlace,
			int numberOfFirstPlaceMedals, int numberOfSecondPlaceMedals, int numberOfThirdPlaceMedals) {
		systemView.showWinnersUI(firstPlace, secondPlace, thirdPlace, numberOfFirstPlaceMedals,
				numberOfSecondPlaceMedals, numberOfThirdPlaceMedals);

	}

	@Override
	public void olympicsDatePickedUIEvnet(LocalDate startingDate, LocalDate endingDate) throws OneOfTheFieldsWasNotFilledException, DatesMismatchException {
		managementSystem.setDates(startingDate, endingDate);

	}

	@Override
	public void DatePickedAndSetModelEvent() {
		systemView.DatePickedAndSet();

	}

}
