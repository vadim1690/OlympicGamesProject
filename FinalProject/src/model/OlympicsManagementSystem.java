package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.SystemController;
import exceptions.CantRemoveSportsmanException;
import exceptions.CantRemoveSquadException;
import exceptions.CantAddTheSameSportsmanException;
import exceptions.CantAddTheSameSquadException;
import exceptions.CantRemoveJudgeException;
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
import model.Competition.CompetitionType;

public class OlympicsManagementSystem {

	private LocalDate startDate;
	private LocalDate endDate;

	private final String FILE_DATE = "file_date.txt";
	private final String FILE_COMPETITIONS = "file_competitions.txt";
	private final String FILE_JUDGES = "file_judges.txt";
	private final String FILE_STADIUMS = "file_stadiums.txt";
	private final String FILE_SPORTSMEN = "file_sportsmen.txt";
	private final String FILE_SQUADS = "file_squads.txt";

	private File fileCompetitions;
	private File fileJudges;
	private File fileStadiums;
	private File fileSportsmen;
	private File fileSquads;
	private File fileDates;

	private final String runnerSportsman = "Runner";
	private final String highJumperSportsman = "High jumper";
	private final String runnerAndHighJumper = "Runner and high jumper";

	private ArrayList<Competition> allCompetitions;
	private ArrayList<Sportsman> allSportsmen;
	private ArrayList<Squad> allSquads;
	private ArrayList<Judge> allJudges;
	private ArrayList<Stadium> allStadiums;
	private ArrayList<OlympicsManagementSystemEventsListener> systemEventsListeners;

	public OlympicsManagementSystem() throws ClassNotFoundException, IOException {

		fileCompetitions = new File(FILE_COMPETITIONS);
		fileJudges = new File(FILE_JUDGES);
		fileStadiums = new File(FILE_STADIUMS);
		fileSportsmen = new File(FILE_SPORTSMEN);
		fileSquads = new File(FILE_SQUADS);
		fileDates = new File(FILE_DATE);

		this.allCompetitions = new ArrayList<Competition>();
		this.allSportsmen = new ArrayList<Sportsman>();
		this.allSquads = new ArrayList<Squad>();
		this.allJudges = new ArrayList<Judge>();
		this.allStadiums = new ArrayList<Stadium>();
		this.systemEventsListeners = new ArrayList<OlympicsManagementSystemEventsListener>();

	}

	public void readFromFiles() throws ClassNotFoundException, IOException {
		readFileOlympicDate();
		readFileStadiums();
		readFileJudges();
		readFileSportsmen();
		readFileSquads();
		readFileCompetitions();
	}

	public void readFileOlympicDate() throws IOException, ClassNotFoundException {

		FileInputStream fileInput = new FileInputStream(fileDates);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);

		Boolean isDatePicked = false;

		isDatePicked = objectInput.readBoolean();

		if (isDatePicked) {
			startDate = (LocalDate) objectInput.readObject();
			endDate = (LocalDate) objectInput.readObject();
			fileInput.close();
			objectInput.close();
			fireDatePickedAndSetModelEvent();
		}

	}

	private void readFileCompetitions() throws IOException, ClassNotFoundException {
		FileInputStream fileInput = new FileInputStream(fileCompetitions);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);
		int size = objectInput.readInt();

		for (int i = 0; i < size; i++) {

			Competition competition = (Competition) objectInput.readObject();
			if (competition instanceof PersonalCompetition) {
				PersonalCompetition pCompetition = (PersonalCompetition) competition;
				for (int j = 0; j < pCompetition.getParticipants().size(); j++) {
					int index = allSportsmen.indexOf(pCompetition.getParticipants().get(j));
					if (index != -1) {
						pCompetition.getParticipants().set(j, allSportsmen.get(index));
					}
				}
				int indexFirstSprotsman = allSportsmen.indexOf(pCompetition.getFirstPlace());
				int indexSecondSprotsman = allSportsmen.indexOf(pCompetition.getSecondPlace());
				int indexThirdSprotsman = allSportsmen.indexOf(pCompetition.getThirdPlace());

				if (indexFirstSprotsman != -1) {
					pCompetition.setFirstPlace(allSportsmen.get(indexFirstSprotsman));
				}

				if (indexSecondSprotsman != -1) {
					pCompetition.setSecondPlace(allSportsmen.get(indexSecondSprotsman));
				}

				if (indexThirdSprotsman != -1) {
					pCompetition.setThirdPlace(allSportsmen.get(indexThirdSprotsman));
				}

			}

			if (competition instanceof NationalCompetition) {
				NationalCompetition nCompetition = (NationalCompetition) competition;
				for (int j = 0; j < nCompetition.getSquadParticipants().size(); j++) {
					int index = allSquads.indexOf(nCompetition.getSquadParticipants().get(j));
					if (index != -1) {
						nCompetition.getSquadParticipants().set(j, allSquads.get(index));
					}
				}
				int indexFirstSquad = allSquads.indexOf(nCompetition.getFirstPlace());
				int indexSecondSquad = allSquads.indexOf(nCompetition.getSecondPlace());
				int indexThirdSquad = allSquads.indexOf(nCompetition.getThirdPlace());

				if (indexFirstSquad != -1) {
					nCompetition.setFirstPlace(allSquads.get(indexFirstSquad));
				}

				if (indexSecondSquad != -1) {
					nCompetition.setSecondPlace(allSquads.get(indexSecondSquad));
				}

				if (indexThirdSquad != -1) {
					nCompetition.setThirdPlace(allSquads.get(indexThirdSquad));
				}

			}
			int indexJudge = allJudges.indexOf(competition.getJudge());
			int indexSatium = allStadiums.indexOf(competition.getStadium());

			if (indexJudge != -1) {
				competition.replaceJudge(allJudges.get(indexJudge));
			}

			if (indexSatium != -1) {
				competition.replaceStadium(allStadiums.get(indexSatium));
			}

			allCompetitions.add(competition);
			fireAddCompetitionToModelEvent(competition, true);
		}
		fileInput.close();

		objectInput.close();
	}

	private void readFileSportsmen() throws IOException, ClassNotFoundException {
		FileInputStream fileInput = new FileInputStream(fileSportsmen);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);
		int size = objectInput.readInt();

		for (int i = 0; i < size; i++) {

			Sportsman sportsman = (Sportsman) objectInput.readObject();
			allSportsmen.add(sportsman);
			fireAddSportsmanModelEvent(sportsman, true);
		}
		fileInput.close();

		objectInput.close();
	}

	private void readFileSquads() throws IOException, ClassNotFoundException {
		FileInputStream fileInput = new FileInputStream(fileSquads);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);
		int size = objectInput.readInt();
		for (int i = 0; i < size; i++) {
			Squad squad = (Squad) objectInput.readObject();
			for (int j = 0; j < squad.getSquadSportsmen().size(); j++) {
				int index = allSportsmen.indexOf(squad.getSquadSportsmen().get(j));
				if (index != -1) {
					squad.getSquadSportsmen().set(j, allSportsmen.get(index));
				}
			}
			allSquads.add(squad);
			fireAddSquadToModelEvent(squad, true);
		}
		fileInput.close();

		objectInput.close();

	}

	private void readFileJudges() throws ClassNotFoundException, IOException {
		FileInputStream fileInput = new FileInputStream(fileJudges);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);
		int size = objectInput.readInt();

		for (int i = 0; i < size; i++) {

			Judge judge = (Judge) objectInput.readObject();
			allJudges.add(judge);
			fireAddJudgeModelEvent(judge, true);
		}
		fileInput.close();
		objectInput.close();
	}

	private void readFileStadiums() throws IOException, ClassNotFoundException {
		FileInputStream fileInput = new FileInputStream(fileStadiums);
		if (fileInput.available() == 0) {
			fileInput.close();

			return;
		}
		ObjectInputStream objectInput = new ObjectInputStream(fileInput);
		int size = objectInput.readInt();

		for (int i = 0; i < size; i++) {

			Stadium stadium = (Stadium) objectInput.readObject();
			allStadiums.add(stadium);
			fireAddStadiumModelEvent(stadium, true);
		}
		fileInput.close();

		objectInput.close();
	}

	// When the button "Save and Exit is pressed"
	public void saveAndExit() throws IOException {
		saveDates();
		saveCompetitions();
		saveSportsmen();
		saveSquads();
		saveJudges();
		saveStadiums();

		fireSaveFilesModelEvent();
	}

	private void saveDates() throws IOException {
		FileOutputStream output = new FileOutputStream(FILE_DATE);
		ObjectOutputStream o = new ObjectOutputStream(output);
		if (startDate == null || endDate == null) {
			o.writeBoolean(false);
			o.close();
			output.close();
			return;
		}
		o.writeBoolean(true);
		o.writeObject(startDate);
		o.writeObject(endDate);
		o.close();
		output.close();
	}

	public void saveSportsmen() throws IOException {
		FileOutputStream output = new FileOutputStream(fileSportsmen);
		ObjectOutputStream o = new ObjectOutputStream(output);
		o.writeInt(allSportsmen.size());
		for (Sportsman sportsman : allSportsmen) {
			o.writeObject(sportsman);
		}
		o.close();
		output.close();

	}

	public void saveSquads() throws IOException {
		FileOutputStream output = new FileOutputStream(fileSquads);
		ObjectOutputStream o = new ObjectOutputStream(output);
		o.writeInt(allSquads.size());
		for (Squad squad : allSquads) {
			o.writeObject(squad);
		}
		o.close();
		output.close();

	}

	public void saveCompetitions() throws IOException {
		FileOutputStream output = new FileOutputStream(fileCompetitions);
		ObjectOutputStream o = new ObjectOutputStream(output);
		o.writeInt(allCompetitions.size());
		for (Competition competition : allCompetitions) {
			o.writeObject(competition);
		}
		o.close();
		output.close();

	}

	public void saveStadiums() throws IOException {
		FileOutputStream output = new FileOutputStream(fileStadiums);
		ObjectOutputStream o = new ObjectOutputStream(output);
		o.writeInt(allStadiums.size());
		for (Stadium stadium : allStadiums) {
			o.writeObject(stadium);
		}
		o.close();
		output.close();

	}

	public void saveJudges() throws IOException {
		FileOutputStream output = new FileOutputStream(fileJudges);
		ObjectOutputStream o = new ObjectOutputStream(output);
		o.writeInt(allJudges.size());
		for (Judge judge : allJudges) {
			o.writeObject(judge);
		}
		o.close();
		output.close();
	}

	public void registerListener(SystemController systemController) {
		systemEventsListeners.add(systemController);

	}

	
	public void addPersonalCompetition(CompetitionType competitionType, Judge judge, Stadium stadium, LocalDate date,
			ArrayList<Sportsman> selectedSportsmen)
			throws OneOfTheFieldsWasNotFilledException, DateOfTheCompetitionIsNotValidException,
			SportsmanAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException {
		if (competitionType == null || judge == null || stadium == null || date == null || selectedSportsmen == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}

		if (date.isAfter(endDate) || date.isBefore(startDate)) {
			throw new DateOfTheCompetitionIsNotValidException(startDate, endDate);
		}

		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i) instanceof PersonalCompetition
					&& date.equals(allCompetitions.get(i).getDateOfCompetition())) {

				PersonalCompetition p = (PersonalCompetition) allCompetitions.get(i);
				for (int j = 0; j < p.getParticipants().size(); j++) {
					if (selectedSportsmen.contains(p.getParticipants().get(j))) {
						throw new SportsmanAlreadyParticipatingException(p.getParticipants().get(j), date);
					}
				}
			}

		}

		if (judge.getCompetitionType() != competitionType) {
			throw new WrongJudgeForTheCompetitionTypeException(judge, competitionType);
		}

		PersonalCompetition personalCompetition = new PersonalCompetition(judge, stadium, date, competitionType,
				selectedSportsmen);
		allCompetitions.add(personalCompetition);
		fireAddCompetitionToModelEvent(personalCompetition, false);
	}

	// Add National Competition to Model
	public void addNationalCompetition(CompetitionType competitionType, Judge judge, Stadium stadium, LocalDate date,
			ArrayList<Squad> selectedSquads)
			throws OneOfTheFieldsWasNotFilledException, DateOfTheCompetitionIsNotValidException,
			SquadAlreadyParticipatingException, WrongJudgeForTheCompetitionTypeException {
		if (competitionType == null || judge == null || stadium == null || date == null || selectedSquads == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}

		if (date.isAfter(endDate) || date.isBefore(startDate)) {
			throw new DateOfTheCompetitionIsNotValidException(startDate, endDate);
		}
		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i) instanceof NationalCompetition) {
				NationalCompetition n = (NationalCompetition) allCompetitions.get(i);
				for (int j = 0; j < n.getSquadParticipants().size(); j++) {
					if (selectedSquads.contains(n.getSquadParticipants().get(j))
							&& date.equals(allCompetitions.get(i).getDateOfCompetition())) {
						throw new SquadAlreadyParticipatingException(n.getSquadParticipants().get(j), date);
					}
				}
			}
		}
		if (judge.getCompetitionType() != competitionType) {
			throw new WrongJudgeForTheCompetitionTypeException(judge, competitionType);
		}

		NationalCompetition nationalCompetition = new NationalCompetition(judge, stadium, date, competitionType,
				selectedSquads);
		allCompetitions.add(nationalCompetition);
		fireAddCompetitionToModelEvent(nationalCompetition, false);
	}

	public void addWinnersToPersonalCompetition(Competition currentCompetition, Sportsman firstPlace,
			Sportsman secondPlace, Sportsman thirdPlace)
			throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSportsmanException {
		if (currentCompetition == null || firstPlace == null || secondPlace == null || thirdPlace == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		if (firstPlace.equals(secondPlace) || firstPlace.equals(thirdPlace) || secondPlace.equals(thirdPlace)) {
			throw new CantAddTheSameSportsmanException();
		}

		PersonalCompetition p = (PersonalCompetition) currentCompetition;
		p.setWinners(firstPlace, secondPlace, thirdPlace);
		fireAddWinnersToCompetitionModelEvent(currentCompetition);
	}

	public void addWinnersToNationalCompetition(Competition currentCompetition, Squad firstPlace, Squad secondPlace,
			Squad thirdPlace) throws OneOfTheFieldsWasNotFilledException, CantAddTheSameSquadException {
		if (currentCompetition == null || firstPlace == null || secondPlace == null || thirdPlace == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		if (firstPlace.equals(secondPlace) || firstPlace.equals(thirdPlace) || secondPlace.equals(thirdPlace)) {
			throw new CantAddTheSameSquadException();
		}

		((NationalCompetition) currentCompetition).setWinners(firstPlace, secondPlace, thirdPlace);
		fireAddWinnersToCompetitionModelEvent(currentCompetition);

	}

	public void showMedalsForCountry(String country) throws OneOfTheFieldsWasNotFilledException {
		if (country==null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		int numberOfPersonalMedals = 0;
		int numberOfNationalMedals = 0;
		for (int i = 0; i < allSportsmen.size(); i++) {
			if (allSportsmen.get(i).getSportsmanNationality().equalsIgnoreCase(country)) {
				numberOfPersonalMedals += allSportsmen.get(i).getNumOfPersonalMedals();
			}
		}
		for (int i = 0; i < allSquads.size(); i++) {
			if (allSquads.get(i).getSquadNationality().equalsIgnoreCase(country)) {
				numberOfNationalMedals += allSquads.get(i).getNumOfSquadMedals();
			}
		}

		fireShowMedalsForCountryModelEvent(numberOfPersonalMedals, numberOfNationalMedals, country);

	}

	private int getNumberOfMedalsForCountry(String country)  {
		
		int numOfMedals = 0;
		for (int i = 0; i < allSportsmen.size(); i++) {
			if (allSportsmen.get(i).getSportsmanNationality().equalsIgnoreCase(country)) {
				numOfMedals += allSportsmen.get(i).getNumOfPersonalMedals();
			}
		}
		for (int i = 0; i < allSquads.size(); i++) {
			if (allSquads.get(i).getSquadNationality().equalsIgnoreCase(country)) {
				numOfMedals += allSquads.get(i).getNumOfSquadMedals();
			}
		}
		return numOfMedals;

	}

	// Remove Competition from model
	public void removeCompetition(Competition selectedCompetition, Boolean pressedExit) throws NoSelectedItemException {
		if (selectedCompetition == null) {
			throw new NoSelectedItemException();
		}
		if (pressedExit) {
			allCompetitions.remove(selectedCompetition);
			fireRemoveCompetitionFromModelEvent(selectedCompetition, pressedExit);
			return;
		}
		if (selectedCompetition instanceof PersonalCompetition) {
			PersonalCompetition pCompeition = (PersonalCompetition) selectedCompetition;
			pCompeition.removeMedalsFromWinners();
		}

		if (selectedCompetition instanceof NationalCompetition) {
			NationalCompetition nCompeition = (NationalCompetition) selectedCompetition;
			nCompeition.removeMedalsFromWinners();
		}

		allCompetitions.remove(selectedCompetition);
		fireRemoveCompetitionFromModelEvent(selectedCompetition, pressedExit);
	}

	public void addJudge(String judgeName, String nation, CompetitionType competitionType)
			throws OneOfTheFieldsWasNotFilledException {
		if (judgeName == null || judgeName.trim().length() == 0 || nation == null || competitionType == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		Judge judge = new Judge(judgeName, nation, competitionType);
		allJudges.add(judge);
		fireAddJudgeModelEvent(judge, false);

	}

	// Add Sportsman to model
	public void addSportsman(String sportsmanName, String sportsmanNationality, String sportsmanCompetitionType)
			throws OneOfTheFieldsWasNotFilledException {
		Sportsman sportsman;
		if (sportsmanName == null ||sportsmanName.equals("")|| sportsmanNationality == null || sportsmanCompetitionType == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		if (sportsmanCompetitionType.equals(runnerSportsman)) {
			sportsman = new Runner(sportsmanName, sportsmanNationality);
			allSportsmen.add(sportsman);
			fireAddSportsmanModelEvent(sportsman, false);
		}
		if (sportsmanCompetitionType.equals(highJumperSportsman)) {
			sportsman = new HighJumper(sportsmanName, sportsmanNationality);
			allSportsmen.add(sportsman);
			fireAddSportsmanModelEvent(sportsman, false);

		}
		if (sportsmanCompetitionType.equals(runnerAndHighJumper)) {
			sportsman = new RunnerAndHighJumper(sportsmanName, sportsmanNationality);
			allSportsmen.add(sportsman);
			fireAddSportsmanModelEvent(sportsman, false);
		}

	}

	// Remove Sportsman from model
	public void removeSportsman(Sportsman sportsman) throws NoSelectedItemException, CantRemoveSportsmanException {
		if (sportsman == null) {
			throw new NoSelectedItemException();
		}
		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i) instanceof PersonalCompetition) {
				PersonalCompetition p = (PersonalCompetition) allCompetitions.get(i);
				if (p.getParticipants().contains(sportsman)) {
					throw new CantRemoveSportsmanException(sportsman);
				}
			}
			if (allCompetitions.get(i) instanceof NationalCompetition) {
				NationalCompetition n = (NationalCompetition) allCompetitions.get(i);
				for (int j = 0; j < n.getSquadParticipants().size(); j++) {
					if (n.getSquadParticipants().get(i).getSquadSportsmen().contains(sportsman)) {
						throw new CantRemoveSportsmanException(sportsman);
					}
				}
			}
		}
		for (int i = 0; i < allSquads.size(); i++) {
			if (allSquads.get(i).checkIfSportsmanExist(sportsman)) {
				allSquads.get(i).getSquadSportsmen().remove(sportsman);
			}
		}
		allSportsmen.remove(sportsman);
		fireRemoveSportsmanFromModelEvent(sportsman);
	}

	// Add Squad to model
	public void addSquad(String nation, CompetitionType competitionType, ArrayList<Sportsman> selectedSportsmen)
			throws OneOfTheFieldsWasNotFilledException, SquadNationExistForCompetitionType {
		if (nation == null || competitionType == null || selectedSportsmen == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		for (int i = 0; i < allSquads.size(); i++) {
			if (allSquads.get(i).getSquadNationality().equalsIgnoreCase(nation)
					&& allSquads.get(i).getCompetitionType() == competitionType) {
				throw new SquadNationExistForCompetitionType(nation, competitionType);
			}
		}
		Squad squad = new Squad(selectedSportsmen, nation, competitionType);
		allSquads.add(squad);
		fireAddSquadToModelEvent(squad, false);

	}

	public void removeSquad(Squad squad) throws NoSelectedItemException, CantRemoveSquadException {
		if (squad == null) {
			throw new NoSelectedItemException();
		}
		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i) instanceof NationalCompetition) {
				NationalCompetition n = (NationalCompetition) allCompetitions.get(i);
				if (n.getSquadParticipants().contains(squad)) {
					throw new CantRemoveSquadException(squad);
				}
			}
		}

		allSquads.remove(squad);
		fireRemoveSquadFromModelEvent(squad);

	}

	// Remove Judge from model
	public void removeJudge(Judge judge) throws NoSelectedItemException, CantRemoveJudgeException {
		if (judge == null) {
			throw new NoSelectedItemException();
		}
		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i).getJudge() == judge) {
				throw new CantRemoveJudgeException(judge);
			}
		}
		allJudges.remove(judge);
		fireRemoveJudgeFromModelEvent(judge);
	}

	// Add Stadium to model
	public void addStadium(String stadiumName, String stadiumLocation, Integer numOfSeatsInStadium)
			throws OneOfTheFieldsWasNotFilledException {
		if (stadiumName == null || stadiumName.trim().length() == 0 || stadiumLocation == null) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		int intNumOfSeatsInStadium = numOfSeatsInStadium;
		Stadium stadium = new Stadium(stadiumName, stadiumLocation, intNumOfSeatsInStadium);
		allStadiums.add(stadium);
		fireAddStadiumModelEvent(stadium, false);
	}

	// Remove Stadium from model
	public void removeStadium(Stadium stadium) throws NoSelectedItemException, CantRemoveStadiumException {
		if (stadium == null) {
			throw new NoSelectedItemException();
		}

		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i).getStadium() == stadium) {
				throw new CantRemoveStadiumException(stadium);
			}
		}

		allStadiums.remove(stadium);
		fireRemoveStadiumFromModelEvent(stadium);
	}

	public void finishOlympicGames() throws FileNotFoundException, NoCountriesParticipatingException {
		String firstPlace = null;
		String secondPlace = null;
		String thirdPlace = null;
		int numberOfFirstPlaceMedals;
		int numberOfSecondPlaceMedals;
		int numberOfThirdPlaceMedals;
		
		

		ArrayList<String> countries = new ArrayList<String>();
		for (int i = 0; i < allSportsmen.size(); i++) {
			if (countries.contains(allSportsmen.get(i).getSportsmanNationality())) {
				continue;
			}
			countries.add(allSportsmen.get(i).getSportsmanNationality());
		}
		
		if (countries.size()==0) {
			throw new NoCountriesParticipatingException();
		}

		sortCountriesByMedals(countries);

		if (countries.size() > 0) {
			firstPlace = countries.get(0);
		}

		if (countries.size() > 1) {
			secondPlace = countries.get(1);
		}
		if (countries.size() > 2) {
			thirdPlace = countries.get(2);
		}

		numberOfFirstPlaceMedals = getNumberOfMedalsForCountry(firstPlace);
		numberOfSecondPlaceMedals = getNumberOfMedalsForCountry(secondPlace);
		numberOfThirdPlaceMedals = getNumberOfMedalsForCountry(thirdPlace);

		fireWinnersModelEvent(firstPlace, secondPlace, thirdPlace, numberOfFirstPlaceMedals, numberOfSecondPlaceMedals,
				numberOfThirdPlaceMedals);
		// clearing the files
		new PrintWriter(fileDates).close();
		new PrintWriter(fileCompetitions).close();
		new PrintWriter(fileJudges).close();
		new PrintWriter(fileSportsmen).close();
		new PrintWriter(fileStadiums).close();
		new PrintWriter(fileSquads).close();
	}

	private void sortCountriesByMedals(ArrayList<String> countries) {
		int n = countries.size();
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (getNumberOfMedalsForCountry(countries.get(j)) < getNumberOfMedalsForCountry(countries.get(j + 1))) {

					String temp = countries.get(j);
					countries.set(j, countries.get(j + 1));
					countries.set(j + 1, temp);
				}

	}

	public void setDates(LocalDate startingDate, LocalDate endingDate) throws OneOfTheFieldsWasNotFilledException, DatesMismatchException {
		if (startingDate == null || endingDate==null ) {
			throw new OneOfTheFieldsWasNotFilledException();
		}
		if (startingDate.isAfter(endingDate)) {
			throw new DatesMismatchException(startingDate,endingDate);
		}
		this.startDate = startingDate;
		this.endDate = endingDate;

		fireDatePickedAndSetModelEvent();

	}

	public void printAllJudges() {
		StringBuffer sbPrintAllJudges = new StringBuffer();
		for (int i = 0; i < allJudges.size(); i++) {
			sbPrintAllJudges.append((i + 1) + ") " + allJudges.get(i).toString() + "\n");
		}
		firePrintAllJudgesModelEvent(sbPrintAllJudges);
	}

	public void printAllStadiums() {
		StringBuffer sbPrintAllStadiums = new StringBuffer();
		for (int i = 0; i < allStadiums.size(); i++) {
			sbPrintAllStadiums.append((i + 1) + ") " + allStadiums.get(i).toString() + "\n");
		}
		firePrintAllStadiumsModelEvent(sbPrintAllStadiums);
	}

	public void printAllSportmen() {
		StringBuffer sbPrintAllSportmen = new StringBuffer();
		for (int i = 0; i < allSportsmen.size(); i++) {
			sbPrintAllSportmen.append((i + 1) + ") " + allSportsmen.get(i).toString() + "\n");
		}
		firePrintAllSportsmenModelEvent(sbPrintAllSportmen);
	}

	public void printAllSquads() {
		StringBuffer sbPrintAllSquads = new StringBuffer();
		for (int i = 0; i < allSquads.size(); i++) {
			sbPrintAllSquads.append((i + 1) + ") " + allSquads.get(i).toString() + "\n");
		}
		firePrintAllSquadsModelEvent(sbPrintAllSquads);
	}

	private void fireSaveFilesModelEvent() {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.saveFileModelEvent();
		}
	}

	private void fireDatePickedAndSetModelEvent() {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.DatePickedAndSetModelEvent();
		}
	}

	private void fireAddCompetitionToModelEvent(Competition competition, boolean readingFromFile) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.addCompetitionToModelEvent(competition, readingFromFile);
		}
	}

	private void fireWinnersModelEvent(String firstPlace, String secondPlace, String thirdPlace,
			int numberOfFirstPlaceMedals, int numberOfSecondPlaceMedals, int numberOfThirdPlaceMedals) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.winnersModelEvent(firstPlace, secondPlace, thirdPlace, numberOfFirstPlaceMedals,
					numberOfSecondPlaceMedals, numberOfThirdPlaceMedals);
		}
	}

	private void fireAddWinnersToCompetitionModelEvent(Competition currentCompetition) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.addWinnersToCompetitionModelEvent(currentCompetition);
		}
	}

	private void fireRemoveCompetitionFromModelEvent(Competition selectedItem, Boolean pressedExit) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.removeCompetitionFromModelEvent(selectedItem, pressedExit);
		}
	}

	private void fireAddSportsmanModelEvent(Sportsman sportsman, boolean readingFromFile) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.addSportsmanModelToEvent(sportsman, readingFromFile);
		}
	}

	private void fireRemoveSportsmanFromModelEvent(Sportsman sportsman) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.removeSportsmanFromModelEvent(sportsman);
		}
	}

	private void firePrintAllSportsmenModelEvent(StringBuffer sbPrintAllSportmen) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.printAllSportsmenModelEvent(sbPrintAllSportmen);
		}
	}

	private void fireAddSquadToModelEvent(Squad squad, boolean readingFromFile) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.AddSquadToModelEvent(squad, readingFromFile);

		}
	}

	private void fireRemoveSquadFromModelEvent(Squad squad) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.removeSquadFromModelEvent(squad);
		}
	}

	private void firePrintAllSquadsModelEvent(StringBuffer sbPrintAllSquads) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.printAllSquadsModelEvent(sbPrintAllSquads);
		}
	}

	private void fireAddJudgeModelEvent(Judge judge, boolean readingFromFile) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.addJudgeToModelEvent(judge, readingFromFile);
		}

	}

	private void fireRemoveJudgeFromModelEvent(Judge judge) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.removeJudgeFromModelEvent(judge);
		}
	}

	private void firePrintAllJudgesModelEvent(StringBuffer sbPrintAllJudges) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.printAllJudgesModelEvent(sbPrintAllJudges);
		}
	}

	private void fireAddStadiumModelEvent(Stadium stadium, boolean readingFromFile) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.addStadiumToModelEvent(stadium, readingFromFile);
		}
	}

	private void fireRemoveStadiumFromModelEvent(Stadium stadium) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.removeCompetitionFromModelEvent(stadium);
		}
	}

	private void firePrintAllStadiumsModelEvent(StringBuffer sbPrintAllStadiums) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.printAllStadiumsModelEvent(sbPrintAllStadiums);
		}
	}

	private void fireShowMedalsForCountryModelEvent(int numberOfPersonalMedals, int numberOfNationalMedals,
			String country) {
		for (OlympicsManagementSystemEventsListener l : systemEventsListeners) {
			l.showMedalsForCountryModelEvent(numberOfPersonalMedals, numberOfNationalMedals, country);
		}
	}

}
