package listeners;

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
import model.Competition;
import model.Competition.CompetitionType;
import model.Judge;
import model.Sportsman;
import model.Squad;
import model.Stadium;

public interface OlympicsManagementSystemUIEventsListener {
	
	void saveAndExitUI() throws IOException;
	
	void addPersonalCompetitionToUI(CompetitionType competitionType, Judge judge, Stadium stadium, LocalDate date,
			ArrayList<Sportsman> selectedSportsmen) throws OneOfTheFieldsWasNotFilledException, DateOfTheCompetitionIsNotValidException, SportsmanAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException;

	void addNationalCompetitionToUI(CompetitionType competitionType, Judge judge, Stadium stadium, LocalDate date,
			ArrayList<Squad> selectedSquads) throws OneOfTheFieldsWasNotFilledException, DateOfTheCompetitionIsNotValidException, SquadAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException;

	void addJudgeToUI(String judgeName,String nation,CompetitionType competitionType) throws OneOfTheFieldsWasNotFilledException;

	void addStadiumToUI(String stadiumName, String stadiumLocation , Integer numOfSeats) throws OneOfTheFieldsWasNotFilledException;
	
	void addSportsmanToUI(String sportsmanName, String sportsmanNationality, String sportsmanCompetitionType) throws OneOfTheFieldsWasNotFilledException;
	
	void addSquadToUI(String nation, CompetitionType competitionType, ArrayList<Sportsman> selectedSportsmen) throws OneOfTheFieldsWasNotFilledException, SquadNationExistForCompetitionType;

	void removeCompetitionFromUI(Competition selectedItem, Boolean pressedExit) throws NoSelectedItemException;

	void removeJudgeFromUI(Judge judge) throws NoSelectedItemException, CantRemoveJudgeException;

	void removeStadiumFromUI(Stadium value) throws NoSelectedItemException, CantRemoveStadiumException;
	
	void removeSportsmanFromUI(Sportsman sportsman) throws NoSelectedItemException, CantRemoveSportsmanException;
	
	void removeSquadFromUI(Squad squad) throws NoSelectedItemException, CantRemoveSquadException;

	void printAllJudgesUI();

	void printAllStadiumsUI();

	void printAllSportsmenUI();

	void printAllSquadsUI();

	void addWinnersToPersonalCompetitionUI(Competition currentCompetition, Sportsman firstPlace, Sportsman secondPlace,
			Sportsman thirdPlace) throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSportsmanException;

	void addWinnersToNationalCompetitionUI(Competition currentCompetition, Squad firstPlace, Squad secondPlace, Squad thirdPlace) throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSquadException;

	void readFiles() throws ClassNotFoundException, IOException;

	void showMedalsForTheCountryUIEvent(String Country) throws OneOfTheFieldsWasNotFilledException;

	void finishOlympicGamesUIEvent() throws FileNotFoundException, NoCountriesParticipatingException;

	void olympicsDatePickedUIEvnet(LocalDate startingDate, LocalDate endingDate) throws OneOfTheFieldsWasNotFilledException, DatesMismatchException;

	





}
