package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import controller.SystemController;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import listeners.OlympicsManagementSystemUIEventsListener;
import model.Competition;
import model.Competition.CompetitionType;
import model.HighJumper;
import model.Judge;
import model.NationalCompetition;
import model.PersonalCompetition;
import model.Runner;
import model.RunnerAndHighJumper;
import model.Sportsman;
import model.Squad;
import model.Stadium;

public class GUI implements AbstractView {

	private ArrayList<OlympicsManagementSystemUIEventsListener> UIEventsListeners = new ArrayList<OlympicsManagementSystemUIEventsListener>();

	private Text textAllJudges = new Text();
	private Text textAllStadiums = new Text();
	private Text textAllSportmen = new Text();
	private Text textAllSquads = new Text();
	private Text textNumberOfMedalsForCountry = new Text();
	private Text textWinners = new Text();

	private Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);

	private ComboBox<Sportsman> cmbSportsmen = new ComboBox<Sportsman>();

	private ComboBox<Judge> cmbJudges = new ComboBox<Judge>();
	private ComboBox<Stadium> cmbStadium = new ComboBox<Stadium>();
	private ComboBox<CompetitionType> cmbCompetitionType = new ComboBox<CompetitionType>();
	private ComboBox<String> cmbSportsmanCompetitionType = new ComboBox<String>();
	private ComboBox<Sportsman> cmbFirstPlaceSportsman = new ComboBox<Sportsman>();
	private ComboBox<Sportsman> cmbSecondPlaceSportsman = new ComboBox<Sportsman>();
	private ComboBox<Sportsman> cmbThirdPlaceSportsman = new ComboBox<Sportsman>();
	private ComboBox<Squad> cmbFirstPlaceSquad = new ComboBox<Squad>();
	private ComboBox<Squad> cmbSecondPlaceSquad = new ComboBox<Squad>();
	private ComboBox<Squad> cmbThirdPlaceSquad = new ComboBox<Squad>();

	private Competition currentCompetition;

	private Boolean isOlympicGamesDate = false;

	private ListView<Competition> lvCompetitions = new ListView<Competition>();
	private ListView<Sportsman> lvRunners = new ListView<Sportsman>();
	private ListView<Sportsman> lvHighJumpers = new ListView<Sportsman>();
	private ListView<Squad> lvSquads = new ListView<Squad>();
	private ListView<Squad> lvSquadCompeteRunning = new ListView<Squad>();
	private ListView<Squad> lvSquadCompeteHighJumping = new ListView<Squad>();
	private ListView<Sportsman> allSprotsmenByNation = new ListView<Sportsman>();

	public GUI(Stage primaryStage) {
		TabPane tabPane = new TabPane();

		VBox vBox = new VBox(tabPane);
		vBox.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, null, null)));
		vBox.setSpacing(20);
		primaryStage.setTitle("Olympic Games management App");
		GridPane gridInstructions = new GridPane();
		GridPane gridGeneral = new GridPane();
		GridPane gridCompetition = new GridPane();
		GridPane gridJudge = new GridPane();
		GridPane gridStadium = new GridPane();
		GridPane gridSportsman = new GridPane();
		GridPane gridSquad = new GridPane();

		Label lblVadimAndDiana = new Label("By Vadim&Diana©\nCopyright 2020\nAll Rights Reserved");
		Label lblFillAllFields = new Label("Please fill all the fields: ");

		Label lblGeneralInstructions = new Label(
				"Here you can finish the Olympic games and see the winners, \nsave data and show medals for selected country: ");
		Label lblCompetitionInstructions = new Label(
				"Here you can add Competition, remove Competition and show all Competitions: ");
		Label lblSportsmanInstructions = new Label(
				"Here you can add Sportsmen, remove Sportsman and show all Sportsmen: ");
		Label lblSquadInstructions = new Label("Here you can add Squads, remove Squad and show all Squads: ");
		Label lblJudgeInstructions = new Label("Here you can add Judges, remove Judge and show all Judges: ");
		Label lblStadiumInstructions = new Label("Here you can add Stadiums, remove Stadium and show all Stadiums: ");

		Text txtInstructions = new Text("Hello ! welcome to the Olympic games management app. \n"
				+ "In this software you can keep track of the Olympic competitions."
				+ "\nYou can add sportsmen, squads, judges, stadiums and accordingly add competitions\nBy dates under the number of medals per country."
				+ "\nPlease follow the instructions below: \n\n"
				+ "\nAt the end of the Olympics you can finish the program and get the winning countries\nOf the Olympics according to the number of medals."
				+ "\nEnter the dates of the Olympic games before you use any of the features in the Application.\nTo enter the dates use the \"Add dates of the olympic games\" button\n\n"
				+ "\nOnce you enter the date, the data will be saved so that the next time you enter the program\n everything will be up to date."
				+ "\nIn addition once you want to finish the current Olympics you can go to the GENERAL tab\nAnd click on end the competition and print the winners.\nEnjoy!");

		ObservableList<String> countries = Stream.of(Locale.getISOCountries()).map(locales -> new Locale("", locales))
				.map(Locale::getDisplayCountry).collect(Collectors.toCollection(FXCollections::observableArrayList));
		ComboBox<String> cmbCountries = new ComboBox<String>(countries);

		cmbCompetitionType.getItems().add(CompetitionType.HighJumpCompetition);
		cmbCompetitionType.getItems().add(CompetitionType.RunningCompetition);

		lvSquadCompeteHighJumping.setMaxSize(300, 400);
		lvSquadCompeteRunning.setMaxSize(300, 400);
		lvRunners.setMaxSize(300, 400);
		lvHighJumpers.setMaxSize(300, 400);

		txtInstructions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
		gridInstructions.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

		cmbSportsmanCompetitionType.getItems().add("Runner");
		cmbSportsmanCompetitionType.getItems().add("High jumper");
		cmbSportsmanCompetitionType.getItems().add("Runner and high jumper");
		FileInputStream input1 = null;

		try {
			input1 = new FileInputStream("olympicsImg.gif");
		} catch (FileNotFoundException e1) {

		}
		Image image = new Image(input1);
		ImageView picGeneral = new ImageView(image);
		picGeneral.setFitWidth(200);
		picGeneral.setFitHeight(150);

		lblVadimAndDiana.setTextFill(Color.BLACK);
		lblGeneralInstructions.setTextFill(Color.BLACK);
		lblCompetitionInstructions.setTextFill(Color.BLACK);
		lblSportsmanInstructions.setTextFill(Color.BLACK);
		lblSquadInstructions.setTextFill(Color.BLACK);
		lblJudgeInstructions.setTextFill(Color.BLACK);
		lblStadiumInstructions.setTextFill(Color.BLACK);
		lblJudgeInstructions.setTextFill(Color.BLACK);
		lblFillAllFields.setTextFill(Color.BLACK);

		lblVadimAndDiana.setFont(font);
		lblGeneralInstructions.setFont(font);
		lblCompetitionInstructions.setFont(font);
		lblSportsmanInstructions.setFont(font);
		lblSquadInstructions.setFont(font);
		lblJudgeInstructions.setFont(font);
		lblStadiumInstructions.setFont(font);
		lblJudgeInstructions.setFont(font);
		lblFillAllFields.setFont(font);

		gridInstructions.setHgap(7);
		gridInstructions.setVgap(10);
		gridInstructions.setPadding(new Insets(10));
		gridGeneral.setHgap(7);
		gridGeneral.setVgap(10);
		gridGeneral.setPadding(new Insets(10));
		gridCompetition.setHgap(7);
		gridCompetition.setVgap(10);
		gridCompetition.setPadding(new Insets(10));
		gridJudge.setHgap(7);
		gridJudge.setVgap(10);
		gridJudge.setPadding(new Insets(10));
		gridStadium.setHgap(7);
		gridStadium.setVgap(10);
		gridStadium.setPadding(new Insets(10));
		gridStadium.setHgap(7);
		gridStadium.setVgap(10);
		gridStadium.setPadding(new Insets(10));
		gridSportsman.setHgap(7);
		gridSportsman.setVgap(10);
		gridSportsman.setPadding(new Insets(10));
		gridSquad.setHgap(7);
		gridSquad.setVgap(10);
		gridSquad.setPadding(new Insets(10));

		Tab instructionsTab = new Tab("Instructions");
		Tab generalTab = new Tab("General");
		Tab competitionTab = new Tab("Competition");
		Tab judgeTab = new Tab("Judge");
		Tab stadiumTab = new Tab("Stadium");
		Tab sportsmanTab = new Tab("Sportsman");
		Tab squadTab = new Tab("Squad");

		Button btnAddDatesOfOlympics = new Button("Add dates of the olympic games");
		Button btnFinishOlympicGame = new Button("Finish the Olympic games and show winners");

		Button btnSaveAndExit = new Button("Save data and Exit");

		Button btnAddCompetition = new Button("Add new Competition");
		Button btnRemoveCompetition = new Button("Remove Competition");
		Button btnShowAllCompetitions = new Button("Show all Competitions");
		Button btnShowCountryMedals = new Button("Show medals of selected Country");

		Button btnAddJudge = new Button("Add new Judge");
		Button btnRemoveJudge = new Button("Remove Judge");
		Button btnShowAllJudges = new Button("Show all Judges");

		Button btnAddStadium = new Button("Add new Stadium");
		Button btnRemoveStadium = new Button("Remove Stadium");
		Button btnShowAllStadiums = new Button("Show all Stadiums");

		Button btnAddSportsman = new Button("Add Sportsman");
		Button btnRemoveSportsman = new Button("Remove Sportsman");
		Button btnShowAllSportsmen = new Button("Show all Sportsmen");

		Button btnAddSquad = new Button("Add Squad");
		Button btnRemoveSquad = new Button("Remove Squad");
		Button btnShowAllSquads = new Button("Show all Squads");

		btnFinishOlympicGame.setTextFill(Color.FORESTGREEN);
		btnSaveAndExit.setTextFill(Color.RED);
		btnShowCountryMedals.setTextFill(Color.DARKGOLDENROD);

		btnAddCompetition.setFont(font);
		btnAddDatesOfOlympics.setFont(font);
		btnAddJudge.setFont(font);
		btnAddSportsman.setFont(font);
		btnAddSquad.setFont(font);
		btnAddStadium.setFont(font);
		btnFinishOlympicGame.setFont(font);
		btnRemoveCompetition.setFont(font);
		btnRemoveJudge.setFont(font);
		btnRemoveSportsman.setFont(font);
		btnRemoveSquad.setFont(font);
		btnRemoveStadium.setFont(font);
		btnSaveAndExit.setFont(font);
		btnShowAllCompetitions.setFont(font);
		btnShowAllJudges.setFont(font);
		btnShowAllSportsmen.setFont(font);
		btnShowAllSquads.setFont(font);
		btnShowAllStadiums.setFont(font);
		btnShowCountryMedals.setFont(font);

		tabPane.getTabs().add(instructionsTab);
		tabPane.getTabs().add(generalTab);
		tabPane.getTabs().add(competitionTab);
		tabPane.getTabs().add(judgeTab);
		tabPane.getTabs().add(stadiumTab);
		tabPane.getTabs().add(sportsmanTab);
		tabPane.getTabs().add(squadTab);

		gridInstructions.add(txtInstructions, 0, 1);
		gridInstructions.add(btnAddDatesOfOlympics, 0, 2);

		gridGeneral.add(lblGeneralInstructions, 0, 2);
		gridGeneral.add(btnFinishOlympicGame, 0, 4);
		gridGeneral.add(btnSaveAndExit, 0, 5);
		gridGeneral.add(btnShowCountryMedals, 0, 6);
		gridGeneral.add(lblVadimAndDiana, 15, 11);
		gridGeneral.add(picGeneral, 0, 8);

		gridCompetition.add(lblCompetitionInstructions, 0, 1);
		gridCompetition.add(btnAddCompetition, 0, 2);
		gridCompetition.add(btnRemoveCompetition, 0, 3);
		gridCompetition.add(btnShowAllCompetitions, 0, 4);

		gridJudge.add(lblJudgeInstructions, 0, 1);
		gridJudge.add(btnAddJudge, 0, 2);
		gridJudge.add(btnRemoveJudge, 0, 3);
		gridJudge.add(btnShowAllJudges, 0, 4);

		gridStadium.add(lblStadiumInstructions, 0, 1);
		gridStadium.add(btnAddStadium, 0, 2);
		gridStadium.add(btnRemoveStadium, 0, 3);
		gridStadium.add(btnShowAllStadiums, 0, 4);

		gridSportsman.add(lblSportsmanInstructions, 0, 1);
		gridSportsman.add(btnAddSportsman, 0, 2);
		gridSportsman.add(btnRemoveSportsman, 0, 3);
		gridSportsman.add(btnShowAllSportsmen, 0, 4);

		gridSquad.add(lblSquadInstructions, 0, 1);
		gridSquad.add(btnAddSquad, 0, 2);
		gridSquad.add(btnRemoveSquad, 0, 3);
		gridSquad.add(btnShowAllSquads, 0, 4);

		instructionsTab.setContent(gridInstructions);
		generalTab.setContent(gridGeneral);
		competitionTab.setContent(gridCompetition);
		judgeTab.setContent(gridJudge);
		stadiumTab.setContent(gridStadium);
		sportsmanTab.setContent(gridSportsman);
		squadTab.setContent(gridSquad);

		judgeTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});
		generalTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});
		competitionTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});
		stadiumTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});
		sportsmanTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});
		squadTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (!isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null,
							"Please pick the dates of the Olympic games\nbefore using any features !");
					tabPane.getSelectionModel().select(0);
				}

			}
		});

		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		cmbSportsmen.setCellFactory(new Callback<ListView<Sportsman>, ListCell<Sportsman>>() {
			@Override
			public ListCell<Sportsman> call(ListView<Sportsman> p) {
				ListCell cell = new ListCell<Sportsman>() {
					@Override
					protected void updateItem(Sportsman sportsman, boolean empty) {
						super.updateItem(sportsman, empty);
						if (empty) {
							setText("");
						} else {
							setText(sportsman.toString());
						}
					}
				};
				return cell;
			}
		});

		cmbSportsmen.setButtonCell(new ListCell<Sportsman>() {
			@Override
			protected void updateItem(Sportsman sportsman, boolean bln) {
				super.updateItem(sportsman, bln);
				if (bln) {
					setText("");
				} else {
					setText(sportsman.getSportsmanName() + "," + sportsman.getSportsmanNationality());
				}
			}
		});

		cmbJudges.setCellFactory(new Callback<ListView<Judge>, ListCell<Judge>>() {
			@Override
			public ListCell<Judge> call(ListView<Judge> p) {
				ListCell cell = new ListCell<Judge>() {
					@Override
					protected void updateItem(Judge judge, boolean empty) {
						super.updateItem(judge, empty);
						if (empty) {
							setText("");
						} else {
							setText(judge.getJudgeName() + "," + judge.getJudgeNation() + ","
									+ judge.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});

		cmbJudges.setButtonCell(new ListCell<Judge>() {
			@Override
			protected void updateItem(Judge judge, boolean bln) {
				super.updateItem(judge, bln);
				if (bln) {
					setText("");
				} else {
					setText(judge.getJudgeName());
				}
			}
		});

		cmbStadium.setCellFactory(new Callback<ListView<Stadium>, ListCell<Stadium>>() {
			@Override
			public ListCell<Stadium> call(ListView<Stadium> p) {
				ListCell cell = new ListCell<Stadium>() {
					@Override
					protected void updateItem(Stadium stadium, boolean empty) {
						super.updateItem(stadium, empty);
						if (empty) {
							setText("");
						} else {
							setText(stadium.getStadiumName() + " ," + stadium.getLocation() + " ,"
									+ stadium.getNumOfSeatsInStadium());
						}
					}
				};
				return cell;
			}
		});

		cmbStadium.setButtonCell(new ListCell<Stadium>() {
			@Override
			protected void updateItem(Stadium stadium, boolean bln) {
				super.updateItem(stadium, bln);
				if (bln) {
					setText("");
				} else {
					setText(stadium.getStadiumName());
				}
			}
		});

		lvCompetitions.setCellFactory(new Callback<ListView<Competition>, ListCell<Competition>>() {
			@Override
			public ListCell<Competition> call(ListView<Competition> p) {
				ListCell cell = new ListCell<Competition>() {
					@Override
					protected void updateItem(Competition competition, boolean empty) {
						super.updateItem(competition, empty);
						if (empty) {
							setText("");
						} else {
							setText("Competition date: " + competition.getDateOfCompetition() + "\n"
									+ "Compeititon type: " + competition.getCompetitionType() + "\n" + "Judge: "
									+ competition.getJudge().getJudgeName() + "\n" + "Stadium: "
									+ competition.getStadium().getStadiumName());
						}
					}
				};
				return cell;
			}
		});

		lvSquads.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});

		lvSquadCompeteHighJumping.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});

		lvSquadCompeteRunning.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});

		cmbFirstPlaceSquad.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});
		cmbSecondPlaceSquad.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});
		cmbThirdPlaceSquad.setCellFactory(new Callback<ListView<Squad>, ListCell<Squad>>() {
			@Override
			public ListCell<Squad> call(ListView<Squad> p) {
				ListCell cell = new ListCell<Squad>() {
					@Override
					protected void updateItem(Squad squad, boolean empty) {
						super.updateItem(squad, empty);
						if (empty) {
							setText("");
						} else {
							setText("Nationality: " + squad.getSquadNationality() + "\n" + "Squad Competition type: "
									+ squad.getCompetitionType().toString());
						}
					}
				};
				return cell;
			}
		});

		cmbFirstPlaceSquad.setButtonCell(new ListCell<Squad>() {
			@Override
			protected void updateItem(Squad squad, boolean bln) {
				super.updateItem(squad, bln);
				if (bln) {
					setText("");
				} else {
					setText(squad.getSquadNationality());
				}
			}
		});
		cmbSecondPlaceSquad.setButtonCell(new ListCell<Squad>() {
			@Override
			protected void updateItem(Squad squad, boolean bln) {
				super.updateItem(squad, bln);
				if (bln) {
					setText("");
				} else {
					setText(squad.getSquadNationality());
				}
			}
		});
		cmbThirdPlaceSquad.setButtonCell(new ListCell<Squad>() {
			@Override
			protected void updateItem(Squad squad, boolean bln) {
				super.updateItem(squad, bln);
				if (bln) {
					setText("");
				} else {
					setText(squad.getSquadNationality());
				}
			}
		});

		cmbJudges.setMaxWidth(200);
		cmbStadium.setMinWidth(200);

		btnSaveAndExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					try {
						l.saveAndExitUI();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Error with saving the file !");
					}
				}
				primaryStage.close();
			}
		});
		btnShowCountryMedals.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Stage showMedalsStage = new Stage();
				showMedalsStage.setTitle("Show medals");
				GridPane gpShowMedals = new GridPane();
				gpShowMedals.setHgap(7);
				gpShowMedals.setVgap(10);
				gpShowMedals.setPadding(new Insets(10));
				Button btnChoose = new Button("Show medals");
				btnChoose.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.showMedalsForTheCountryUIEvent(cmbCountries.getValue());
							} catch (OneOfTheFieldsWasNotFilledException e) {
								JOptionPane.showMessageDialog(null, e.getMessage());
								showMedalsStage.close();
								return;
							}

						}
						showMedalsStage.close();
						cmbCountries.getSelectionModel().clearSelection();

						Stage NumberOfMedalsStage = new Stage();
						NumberOfMedalsStage.setTitle("Medals");
						VBox vbNumberOfMedals = new VBox();
						vbNumberOfMedals.setPadding(new Insets(10));
						vbNumberOfMedals.setSpacing(10);

						vbNumberOfMedals.getChildren().add(textNumberOfMedalsForCountry);

						NumberOfMedalsStage.setScene(new Scene(vbNumberOfMedals, 300, 150));
						NumberOfMedalsStage.show();
					}
				});

				gpShowMedals.add(btnChoose, 1, 3);
				gpShowMedals.add(cmbCountries, 1, 2);
				showMedalsStage.setScene(new Scene(gpShowMedals, 300, 150));
				showMedalsStage.show();

			}
		});
		btnFinishOlympicGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					try {
						l.finishOlympicGamesUIEvent();
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "Error with the files !");
					} catch (NoCountriesParticipatingException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
						return;
					}

				}
				primaryStage.close();
				Stage finishOlympicGamesStage = new Stage();
				finishOlympicGamesStage.setTitle("The Winners !");

				VBox vbFinishOlympicGames = new VBox();
				FileInputStream f = null;
				try {
					f = new FileInputStream("winners.jpg");
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Error with the Images !");
				}
				Image img = new Image(f, 350, 410, false, false);
				textWinners.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

				BackgroundImage bk = new BackgroundImage(img, null, null, null, null);
				vbFinishOlympicGames.setBackground(new Background(bk));
				vbFinishOlympicGames.setPadding(new Insets(10));
				vbFinishOlympicGames.setSpacing(10);
				textWinners.setFill(Color.BLACK.darker());

				vbFinishOlympicGames.getChildren().addAll(textWinners);

				finishOlympicGamesStage.setScene(new Scene(vbFinishOlympicGames, 350, 410));
				finishOlympicGamesStage.show();

			}
		});
		btnAddCompetition.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage addCompetitionStage = new Stage();
				addCompetitionStage.initOwner(primaryStage);
				addCompetitionStage.initModality(Modality.WINDOW_MODAL);
				addCompetitionStage.setTitle("Add competition");
				GridPane gpAddCompetition = new GridPane();
				gpAddCompetition.setHgap(15);
				gpAddCompetition.setVgap(10);
				gpAddCompetition.setPadding(new Insets(10));

				CheckBox chkIsPersonal = new CheckBox("Personal Competition");
				CheckBox chkIsNational = new CheckBox("National Competition");

				chkIsPersonal.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						chkIsNational.setSelected(false);
						chkIsPersonal.setTextFill(Color.BLUE);
						chkIsNational.setTextFill(Color.BLACK);
					}
				});
				chkIsNational.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						chkIsPersonal.setSelected(false);
						chkIsNational.setTextFill(Color.BLUE);
						chkIsPersonal.setTextFill(Color.BLACK);
					}
				});

				Label lblEnterCompetitionDate = new Label("Pick the date of the Competition");
				Label lblSelectCompetitionType = new Label("Select Competition type:");
				Label lblPersonalOrNational = new Label("Select Personal Competition/national Competition:");
				Label lblSelectJudge = new Label("Select judge:");
				Label lblSelectStadium = new Label("Select stadium:");

				DatePicker pickDateOfCompetition = new DatePicker();

				Button btnNextToAddingParticipants = new Button("Next-To choosing participants");

				btnNextToAddingParticipants.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						if (pickDateOfCompetition.getValue() == null || cmbJudges.getValue() == null
								|| cmbCompetitionType.getValue() == null || cmbStadium.getValue() == null
								|| !(chkIsNational.isSelected() || chkIsPersonal.isSelected())) {
							JOptionPane.showMessageDialog(null, "All fields must be filled!");
							addCompetitionStage.close();
						}

						GridPane gpAddingParticipants = new GridPane();
						gpAddingParticipants.setHgap(15);
						gpAddingParticipants.setVgap(10);
						gpAddingParticipants.setPadding(new Insets(10));
						ArrayList<Sportsman> selectedSportsmen = new ArrayList<Sportsman>();
						ArrayList<Squad> selectedSquads = new ArrayList<Squad>();

						Label lblAllRunnersPresented = new Label("All Runners presented below: ");
						Label lblAllHighJumpersPresented = new Label("All High jumpers presented below: ");
						Label lblAddSportsmenToSquadInstructions = new Label(
								"For selecting multiple Sportmen hold the Ctrl button and click the sportsman.");
						Label lblAddSquadInstructions = new Label(
								"For selecting multiple squads hold the Ctrl button and click the squad.");
						Label lblChooseFirst = new Label("Select first place " + "(number of medals is "
								+ Competition.FIRST_PLACE_NUMBER_OF_MEDALS + ")" + " :");
						Label lblChooseSecond = new Label("Select second place " + "(number of medals is "
								+ Competition.SECOND_PLACE_NUMBER_OF_MEDALS + ")" + " :");
						Label lblChooseThird = new Label("Select third place " + "(number of medals is "
								+ Competition.THIRD_PLACE_NUMBER_OF_MEDALS + ")" + " :");

						Label lblAllSquadRunnersPresented = new Label(
								"All Squads that can compete running Competition: ");
						Label lblAllSquadHighJumpersPresented = new Label(
								"All Squads that can compete high jumping Competition: ");
						Button btnChooseWinners = new Button("Next-To choosing winners");
						Button btnAddTheWinners = new Button("Add the winners and create the competition");

						if (chkIsPersonal.isSelected()) {
							if (cmbCompetitionType.getValue() == CompetitionType.RunningCompetition) {
								lvRunners.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

								btnChooseWinners.setOnAction(new EventHandler<ActionEvent>() {

									@Override
									public void handle(ActionEvent event) {

										addCompetitionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

											@Override
											public void handle(WindowEvent event) {
												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {

													try {
														l.removeCompetitionFromUI(lvCompetitions.getItems()
																.get(lvCompetitions.getItems().size() - 1), true);
													} catch (NoSelectedItemException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}
												}

											}
										});

										ObservableList<Sportsman> selectedItems = lvRunners.getSelectionModel()
												.getSelectedItems();
										for (Sportsman sportsman : selectedItems) {
											selectedSportsmen.add(sportsman);
										}
										for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
											try {
												l.addPersonalCompetitionToUI(cmbCompetitionType.getValue(),
														cmbJudges.getValue(), cmbStadium.getValue(),
														pickDateOfCompetition.getValue(), selectedSportsmen);
											} catch (OneOfTheFieldsWasNotFilledException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (DateOfTheCompetitionIsNotValidException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (SportsmanAlreadyParticipatingException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (WrongJudgeForTheCompetitionTypeException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											}

										}

										cmbCountries.getSelectionModel().clearSelection();
										cmbCompetitionType.getSelectionModel().clearSelection();
										cmbJudges.getSelectionModel().clearSelection();
										cmbStadium.getSelectionModel().clearSelection();
										lvRunners.getSelectionModel().clearSelection();

										GridPane gpChooseWinners = new GridPane();
										gpChooseWinners.setHgap(15);
										gpChooseWinners.setVgap(10);
										gpChooseWinners.setPadding(new Insets(10));

										btnAddTheWinners.setOnAction(new EventHandler<ActionEvent>() {

											@Override
											public void handle(ActionEvent event) {

												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
													try {
														l.addWinnersToPersonalCompetitionUI(currentCompetition,
																cmbFirstPlaceSportsman.getValue(),
																cmbSecondPlaceSportsman.getValue(),
																cmbThirdPlaceSportsman.getValue());
													} catch (OneOfTheFieldsWasNotFilledException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													} catch (CantAddTheSameSportsmanException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}

												}
												cmbFirstPlaceSportsman.getSelectionModel().clearSelection();
												cmbSecondPlaceSportsman.getSelectionModel().clearSelection();
												cmbThirdPlaceSportsman.getSelectionModel().clearSelection();

												addCompetitionStage.close();
											}
										});

										addCompetitionStage.setScene(new Scene(gpChooseWinners, 540, 350));
										gpChooseWinners.add(lblChooseFirst, 0, 1);
										gpChooseWinners.add(cmbFirstPlaceSportsman, 0, 2);
										gpChooseWinners.add(lblChooseSecond, 0, 3);
										gpChooseWinners.add(cmbSecondPlaceSportsman, 0, 4);
										gpChooseWinners.add(lblChooseThird, 0, 5);
										gpChooseWinners.add(cmbThirdPlaceSportsman, 0, 6);

										gpChooseWinners.add(btnAddTheWinners, 0, 8);

									}
								});

								gpAddingParticipants.add(lblAllRunnersPresented, 0, 2);
								gpAddingParticipants.add(lblAddSportsmenToSquadInstructions, 0, 1);
								gpAddingParticipants.add(lvRunners, 0, 3);
								gpAddingParticipants.add(btnChooseWinners, 0, 4);
							}
							if (cmbCompetitionType.getValue() == CompetitionType.HighJumpCompetition) {

								lvHighJumpers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
								btnChooseWinners.setOnAction(new EventHandler<ActionEvent>() {

									@Override
									public void handle(ActionEvent event) {

										addCompetitionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

											@Override
											public void handle(WindowEvent event) {
												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {

													try {
														l.removeCompetitionFromUI(lvCompetitions.getItems()
																.get(lvCompetitions.getItems().size() - 1), true);
													} catch (NoSelectedItemException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}
												}

											}
										});
										ObservableList<Sportsman> selectedItems = lvHighJumpers.getSelectionModel()
												.getSelectedItems();
										for (Sportsman sportsman : selectedItems) {
											selectedSportsmen.add(sportsman);
										}
										for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
											try {
												l.addPersonalCompetitionToUI(cmbCompetitionType.getValue(),
														cmbJudges.getValue(), cmbStadium.getValue(),
														pickDateOfCompetition.getValue(), selectedSportsmen);
											} catch (OneOfTheFieldsWasNotFilledException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (DateOfTheCompetitionIsNotValidException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (SportsmanAlreadyParticipatingException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (WrongJudgeForTheCompetitionTypeException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											}

										}
										cmbCountries.getSelectionModel().clearSelection();
										cmbCompetitionType.getSelectionModel().clearSelection();
										cmbJudges.getSelectionModel().clearSelection();
										cmbStadium.getSelectionModel().clearSelection();
										lvHighJumpers.getSelectionModel().clearSelection();
										GridPane gpChooseWinners = new GridPane();
										gpChooseWinners.setHgap(15);
										gpChooseWinners.setVgap(10);
										gpChooseWinners.setPadding(new Insets(10));

										btnAddTheWinners.setOnAction(new EventHandler<ActionEvent>() {

											@Override
											public void handle(ActionEvent event) {

												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
													try {
														l.addWinnersToPersonalCompetitionUI(currentCompetition,
																cmbFirstPlaceSportsman.getValue(),
																cmbSecondPlaceSportsman.getValue(),
																cmbThirdPlaceSportsman.getValue());
													} catch (OneOfTheFieldsWasNotFilledException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);

													} catch (CantAddTheSameSportsmanException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);

													}

												}
												cmbFirstPlaceSportsman.getSelectionModel().clearSelection();
												cmbSecondPlaceSportsman.getSelectionModel().clearSelection();
												cmbThirdPlaceSportsman.getSelectionModel().clearSelection();

												addCompetitionStage.close();
											}
										});

										addCompetitionStage.setScene(new Scene(gpChooseWinners, 540, 350));
										gpChooseWinners.add(lblChooseFirst, 0, 1);
										gpChooseWinners.add(cmbFirstPlaceSportsman, 0, 2);
										gpChooseWinners.add(lblChooseSecond, 0, 3);
										gpChooseWinners.add(cmbSecondPlaceSportsman, 0, 4);
										gpChooseWinners.add(lblChooseThird, 0, 5);
										gpChooseWinners.add(cmbThirdPlaceSportsman, 0, 6);

										gpChooseWinners.add(btnAddTheWinners, 0, 8);

									}

								});

								gpAddingParticipants.add(lblAllHighJumpersPresented, 0, 2);
								gpAddingParticipants.add(lblAddSportsmenToSquadInstructions, 0, 1);

								gpAddingParticipants.add(lvHighJumpers, 0, 3);
								gpAddingParticipants.add(btnChooseWinners, 0, 4);

							}
						}

						else if (chkIsNational.isSelected()) {

							if (cmbCompetitionType.getValue() == CompetitionType.RunningCompetition) {

								lvSquadCompeteRunning.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
								btnChooseWinners.setOnAction(new EventHandler<ActionEvent>() {

									@Override
									public void handle(ActionEvent event) {

										addCompetitionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

											@Override
											public void handle(WindowEvent event) {
												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {

													try {
														l.removeCompetitionFromUI(lvCompetitions.getItems()
																.get(lvCompetitions.getItems().size() - 1), true);
													} catch (NoSelectedItemException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}
												}

											}
										});

										ObservableList<Squad> selectedItems = lvSquadCompeteRunning.getSelectionModel()
												.getSelectedItems();
										for (Squad squad : selectedItems) {
											selectedSquads.add(squad);
										}
										for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
											try {
												l.addNationalCompetitionToUI(cmbCompetitionType.getValue(),
														cmbJudges.getValue(), cmbStadium.getValue(),
														pickDateOfCompetition.getValue(), selectedSquads);
											} catch (OneOfTheFieldsWasNotFilledException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (DateOfTheCompetitionIsNotValidException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (SquadAlreadyParticipatingException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (WrongJudgeForTheCompetitionTypeException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											}

										}
										cmbCompetitionType.getSelectionModel().clearSelection();
										cmbJudges.getSelectionModel().clearSelection();
										cmbStadium.getSelectionModel().clearSelection();
										lvSquadCompeteRunning.getSelectionModel().clearSelection();

										GridPane gpChooseWinners = new GridPane();
										gpChooseWinners.setHgap(15);
										gpChooseWinners.setVgap(10);
										gpChooseWinners.setPadding(new Insets(10));

										btnAddTheWinners.setOnAction(new EventHandler<ActionEvent>() {

											@Override
											public void handle(ActionEvent event) {

												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
													try {
														l.addWinnersToNationalCompetitionUI(currentCompetition,
																cmbFirstPlaceSquad.getValue(),
																cmbSecondPlaceSquad.getValue(),
																cmbThirdPlaceSquad.getValue());
													} catch (OneOfTheFieldsWasNotFilledException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													} catch (CantAddTheSameSquadException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}

												}
												cmbFirstPlaceSquad.getSelectionModel().clearSelection();
												cmbSecondPlaceSquad.getSelectionModel().clearSelection();
												cmbThirdPlaceSquad.getSelectionModel().clearSelection();

												addCompetitionStage.close();
											}
										});

										addCompetitionStage.setScene(new Scene(gpChooseWinners, 540, 350));
										gpChooseWinners.add(lblChooseFirst, 0, 1);
										gpChooseWinners.add(cmbFirstPlaceSquad, 0, 2);
										gpChooseWinners.add(lblChooseSecond, 0, 3);
										gpChooseWinners.add(cmbSecondPlaceSquad, 0, 4);
										gpChooseWinners.add(lblChooseThird, 0, 5);
										gpChooseWinners.add(cmbThirdPlaceSquad, 0, 6);

										gpChooseWinners.add(btnAddTheWinners, 0, 8);

									}

								});

								gpAddingParticipants.add(lvSquadCompeteRunning, 0, 3);
								gpAddingParticipants.add(lblAllSquadRunnersPresented, 0, 2);
								gpAddingParticipants.add(lblAddSquadInstructions, 0, 1);
								gpAddingParticipants.add(btnChooseWinners, 0, 4);

							}
							if (cmbCompetitionType.getValue() == CompetitionType.HighJumpCompetition) {
								lvSquadCompeteHighJumping.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

								lvSquadCompeteHighJumping.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
								btnChooseWinners.setOnAction(new EventHandler<ActionEvent>() {

									@Override
									public void handle(ActionEvent event) {

										addCompetitionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

											@Override
											public void handle(WindowEvent event) {
												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {

													try {
														l.removeCompetitionFromUI(lvCompetitions.getItems()
																.get(lvCompetitions.getItems().size() - 1), true);
													} catch (NoSelectedItemException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}
												}

											}
										});
										ObservableList<Squad> selectedItems = lvSquadCompeteHighJumping
												.getSelectionModel().getSelectedItems();
										for (Squad squad : selectedItems) {
											selectedSquads.add(squad);
										}
										for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
											try {
												l.addNationalCompetitionToUI(cmbCompetitionType.getValue(),
														cmbJudges.getValue(), cmbStadium.getValue(),
														pickDateOfCompetition.getValue(), selectedSquads);
											} catch (OneOfTheFieldsWasNotFilledException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (DateOfTheCompetitionIsNotValidException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (SquadAlreadyParticipatingException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											} catch (WrongJudgeForTheCompetitionTypeException e) {
												JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
														JOptionPane.ERROR_MESSAGE);
												addCompetitionStage.close();
											}

										}

										cmbCompetitionType.getSelectionModel().clearSelection();
										cmbJudges.getSelectionModel().clearSelection();
										cmbStadium.getSelectionModel().clearSelection();
										lvSquadCompeteHighJumping.getSelectionModel().clearSelection();
										GridPane gpChooseWinners = new GridPane();
										gpChooseWinners.setHgap(15);
										gpChooseWinners.setVgap(10);
										gpChooseWinners.setPadding(new Insets(10));

										btnAddTheWinners.setOnAction(new EventHandler<ActionEvent>() {

											@Override
											public void handle(ActionEvent event) {

												for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
													try {
														l.addWinnersToNationalCompetitionUI(currentCompetition,
																cmbFirstPlaceSquad.getValue(),
																cmbSecondPlaceSquad.getValue(),
																cmbThirdPlaceSquad.getValue());
													} catch (OneOfTheFieldsWasNotFilledException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													} catch (CantAddTheSameSquadException e) {
														JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
																JOptionPane.ERROR_MESSAGE);
													}

												}
												cmbFirstPlaceSquad.getSelectionModel().clearSelection();
												cmbSecondPlaceSquad.getSelectionModel().clearSelection();
												cmbThirdPlaceSquad.getSelectionModel().clearSelection();

												addCompetitionStage.close();
											}
										});

										addCompetitionStage.setScene(new Scene(gpChooseWinners, 540, 350));
										gpChooseWinners.add(lblChooseFirst, 0, 1);
										gpChooseWinners.add(cmbFirstPlaceSquad, 0, 2);
										gpChooseWinners.add(lblChooseSecond, 0, 3);
										gpChooseWinners.add(cmbSecondPlaceSquad, 0, 4);
										gpChooseWinners.add(lblChooseThird, 0, 5);
										gpChooseWinners.add(cmbThirdPlaceSquad, 0, 6);

										gpChooseWinners.add(btnAddTheWinners, 0, 8);

									}

								});

								gpAddingParticipants.add(lblAllSquadHighJumpersPresented, 0, 2);
								gpAddingParticipants.add(lblAddSquadInstructions, 0, 1);
								gpAddingParticipants.add(lvSquadCompeteHighJumping, 0, 3);
								gpAddingParticipants.add(btnChooseWinners, 0, 4);

							}

						}

						addCompetitionStage.setScene(new Scene(gpAddingParticipants, 600, 500));

					}
				});
				gpAddCompetition.add(lblEnterCompetitionDate, 1, 2);
				gpAddCompetition.add(pickDateOfCompetition, 1, 3);
				gpAddCompetition.add(cmbJudges, 1, 8);
				gpAddCompetition.add(cmbStadium, 2, 8);
				gpAddCompetition.add(cmbCompetitionType, 1, 5);
				gpAddCompetition.add(lblSelectCompetitionType, 1, 4);
				gpAddCompetition.add(lblSelectJudge, 1, 7);
				gpAddCompetition.add(lblSelectStadium, 2, 7);
				gpAddCompetition.add(btnNextToAddingParticipants, 1, 10);

				gpAddCompetition.add(lblPersonalOrNational, 2, 4);
				gpAddCompetition.add(chkIsPersonal, 2, 5);
				gpAddCompetition.add(chkIsNational, 2, 6);

				addCompetitionStage.setScene(new Scene(gpAddCompetition, 540, 350));
				addCompetitionStage.show();
			}
		});
		btnRemoveCompetition.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				lvCompetitions.setMinSize(400, 400);
				Button btnRemoveTheCompetition = new Button("Remove the Competition");
				Label lblSelectCompetitionToRemove = new Label("Select a competition to remove: ");
				Stage removeCompetitionStage = new Stage();
				removeCompetitionStage.initOwner(primaryStage);
				removeCompetitionStage.initModality(Modality.WINDOW_MODAL);
				removeCompetitionStage.setTitle("Remove competition");
				GridPane gpRemoveCompetition = new GridPane();
				gpRemoveCompetition.setHgap(15);
				gpRemoveCompetition.setVgap(10);
				gpRemoveCompetition.setPadding(new Insets(10));

				gpRemoveCompetition.add(lblSelectCompetitionToRemove, 1, 1);
				gpRemoveCompetition.add(lvCompetitions, 1, 2);
				gpRemoveCompetition.add(btnRemoveTheCompetition, 1, 4);
				btnRemoveTheCompetition.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.removeCompetitionFromUI(lvCompetitions.getSelectionModel().getSelectedItem(), false);
							} catch (NoSelectedItemException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						lvCompetitions.getSelectionModel().clearSelection();
						removeCompetitionStage.close();
					}
				});
				removeCompetitionStage.setScene(new Scene(gpRemoveCompetition, 500, 520));

				removeCompetitionStage.show();

			}
		});
		btnShowAllCompetitions.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				lvCompetitions.setMinSize(400, 400);
				Stage showAllCompetitionStage = new Stage();
				showAllCompetitionStage.initOwner(primaryStage);
				showAllCompetitionStage.initModality(Modality.WINDOW_MODAL);
				showAllCompetitionStage.setTitle("Show all Competition");
				GridPane gpShowAllCompetition = new GridPane();
				gpShowAllCompetition.setHgap(15);
				gpShowAllCompetition.setVgap(10);
				gpShowAllCompetition.setPadding(new Insets(10));

				lvCompetitions.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						Stage showParticipantsStage = new Stage();
						showParticipantsStage.setTitle("All participants");
						GridPane gpShowParticipants = new GridPane();
						gpShowParticipants
								.add(new Label(lvCompetitions.getSelectionModel().getSelectedItem().toString()), 2, 1);
						ScrollPane spShowParticipants = new ScrollPane();
						spShowParticipants.setContent(gpShowParticipants);

						showParticipantsStage.setScene(new Scene(spShowParticipants, 400, 400));
						showParticipantsStage.show();
						lvCompetitions.getSelectionModel().clearSelection();
					}
				});

				gpShowAllCompetition.add(lvCompetitions, 1, 2);

				showAllCompetitionStage.setScene(new Scene(gpShowAllCompetition, 500, 500));

				showAllCompetitionStage.show();
			}
		});
		btnAddDatesOfOlympics.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (isOlympicGamesDate) {
					JOptionPane.showMessageDialog(null, "The dates has been already picked ");
					return;
				}
				Stage stage = new Stage();

				stage.setTitle("Add dates of Olympic games");
				stage.initOwner(primaryStage);
				stage.initModality(Modality.WINDOW_MODAL);
				GridPane gp = new GridPane();
				HBox hb = new HBox();
				hb.setPadding(new Insets(10));
				hb.setSpacing(10);

				gp.add(hb, 0, 1);
				gp.setPadding(new Insets(20));

				gp.setVgap(10);

				DatePicker fromDate = new DatePicker();
				DatePicker toDate = new DatePicker();

				Label lblInstructions = new Label(
						"Please pick the dates of the olympic games to start using the App: ");
				Label lblFromDate = new Label("From: ");
				Label lblToDate = new Label(" To:");

				Button btn = new Button("Start");

				hb.getChildren().addAll(lblFromDate, fromDate, lblToDate, toDate);

				btn.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {

						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.olympicsDatePickedUIEvnet(fromDate.getValue(), toDate.getValue());
							} catch (OneOfTheFieldsWasNotFilledException e) {
								JOptionPane.showMessageDialog(null, e.getMessage());

							} catch (DatesMismatchException e) {
								JOptionPane.showMessageDialog(null, e.getMessage());
							}

						}

						stage.close();
					}
				});

				gp.add(lblInstructions, 0, 0);
				gp.add(btn, 0, 3);
				stage.setScene(new Scene(gp, 500, 200));
				stage.show();

			}
		});

		btnAddJudge.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Stage addJudgeStage = new Stage();
				addJudgeStage.initOwner(primaryStage);
				addJudgeStage.initModality(Modality.WINDOW_MODAL);
				addJudgeStage.setTitle("Add Judge");
				Button btnAddTheJudge = new Button("Add the Judge");

				GridPane gpAddJudge = new GridPane();
				gpAddJudge.setHgap(15);
				gpAddJudge.setVgap(10);
				gpAddJudge.setPadding(new Insets(10));

				Label lblSelectCompetitionType = new Label("Select The jurisdiction of the judge:");

				Label lblAddJudgeName = new Label("Enter the judge name: ");
				TextField tfAddJudgeName = new TextField();

				Label lblAddJudgeNation = new Label("Choose the judge Nation: ");

				tfAddJudgeName.setMaxWidth(150);

				btnAddTheJudge.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.addJudgeToUI(tfAddJudgeName.getText(), cmbCountries.getValue(),
										cmbCompetitionType.getValue());
							} catch (OneOfTheFieldsWasNotFilledException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						addJudgeStage.close();
						cmbCompetitionType.getSelectionModel().clearSelection();
						cmbCountries.getSelectionModel().clearSelection();

					}

				});
				gpAddJudge.add(lblFillAllFields, 1, 1);
				gpAddJudge.add(lblAddJudgeName, 1, 2);
				gpAddJudge.add(tfAddJudgeName, 1, 3);
				gpAddJudge.add(lblSelectCompetitionType, 2, 2);

				gpAddJudge.add(cmbCompetitionType, 2, 3);
				gpAddJudge.add(lblAddJudgeNation, 1, 4);
				gpAddJudge.add(cmbCountries, 1, 5);
				gpAddJudge.add(btnAddTheJudge, 1, 7);
				addJudgeStage.setScene(new Scene(gpAddJudge, 550, 230));
				addJudgeStage.show();

			}
		});
		btnRemoveJudge.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label lblSelectJudgeRemove = new Label("Select a judge to remove: ");
				cmbJudges.setMinSize(100, 20);
				Button btnRemoveTheJudge = new Button("Remove the Judge");
				Stage removeJudgeStage = new Stage();
				removeJudgeStage.initOwner(primaryStage);
				removeJudgeStage.initModality(Modality.WINDOW_MODAL);
				removeJudgeStage.setTitle("Remove Judge");
				GridPane gpRemoveJudge = new GridPane();
				gpRemoveJudge.setHgap(15);
				gpRemoveJudge.setVgap(10);
				gpRemoveJudge.setPadding(new Insets(10));

				gpRemoveJudge.add(lblSelectJudgeRemove, 1, 1);
				gpRemoveJudge.add(cmbJudges, 1, 2);
				gpRemoveJudge.add(btnRemoveTheJudge, 1, 4);
				btnRemoveTheJudge.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.removeJudgeFromUI(cmbJudges.getValue());
							} catch (NoSelectedItemException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							} catch (CantRemoveJudgeException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						cmbJudges.getSelectionModel().clearSelection();
						removeJudgeStage.close();
					}
				});
				removeJudgeStage.setScene(new Scene(gpRemoveJudge, 300, 150));

				removeJudgeStage.show();

			}
		});
		btnShowAllJudges.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage showAllJudgesStage = new Stage();

				showAllJudgesStage.initOwner(primaryStage);
				showAllJudgesStage.initModality(Modality.WINDOW_MODAL);
				showAllJudgesStage.setTitle("Show all Judges");

				GridPane gpShowAllJudges = new GridPane();

				javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
				scrollPane.setContent(gpShowAllJudges);

				Label lblAllJudgesPresented = new Label("All Judges presented below: ");

				gpShowAllJudges.setHgap(15);
				gpShowAllJudges.setVgap(10);
				gpShowAllJudges.setPadding(new Insets(10));

				gpShowAllJudges.add(lblAllJudgesPresented, 0, 1);
				gpShowAllJudges.add(textAllJudges, 0, 2);

				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					l.printAllJudgesUI();
				}
				showAllJudgesStage.setScene(new Scene(scrollPane, 500, 400));
				showAllJudgesStage.show();
			}

		});

		btnAddStadium.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage addStadiumStage = new Stage();
				addStadiumStage.initOwner(primaryStage);
				addStadiumStage.initModality(Modality.WINDOW_MODAL);
				addStadiumStage.setTitle("Add Stadium");
				Button btnAddTheStadium = new Button("Add the Stadium");

				GridPane gpAddStadium = new GridPane();
				gpAddStadium.setHgap(15);
				gpAddStadium.setVgap(10);
				gpAddStadium.setPadding(new Insets(10));

				Spinner<Integer> spinnerNumberOfSeats = new Spinner<Integer>(0, 100000, 0);
				spinnerNumberOfSeats.setEditable(true);
				spinnerNumberOfSeats.setPrefSize(85, 25);

				Label lblEnterStadiumName = new Label("Enter the Stadium name: ");
				Label lblEnterStadiumLocation = new Label("Choose the Stadium location(country): ");
				Label lblEnterNumOfSeatsInStadium = new Label("Enter the number of seats in the Stadium: ");

				TextField tfEnterStadiumName = new TextField();

				tfEnterStadiumName.setMaxWidth(150);

				gpAddStadium.add(lblFillAllFields, 1, 1);
				gpAddStadium.add(lblEnterStadiumName, 1, 2);
				gpAddStadium.add(tfEnterStadiumName, 1, 3);
				gpAddStadium.add(lblEnterStadiumLocation, 2, 2);

				gpAddStadium.add(cmbCountries, 2, 3);
				gpAddStadium.add(lblEnterNumOfSeatsInStadium, 1, 4);
				gpAddStadium.add(spinnerNumberOfSeats, 1, 5);
				gpAddStadium.add(btnAddTheStadium, 1, 7);
				addStadiumStage.setScene(new Scene(gpAddStadium, 600, 230));
				addStadiumStage.show();

				btnAddTheStadium.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.addStadiumToUI(tfEnterStadiumName.getText(), cmbCountries.getValue(),
										spinnerNumberOfSeats.getValue());
							} catch (OneOfTheFieldsWasNotFilledException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						addStadiumStage.close();
						cmbCountries.getSelectionModel().clearSelection();
					}
				});
			}
		});
		btnRemoveStadium.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				cmbStadium.setMinSize(100, 20);
				Button btnRemoveTheStadium = new Button("Remove the Stadium");
				Label lblSelectStadiumToRemove = new Label("Select stadium to remove: ");
				Stage removeStadiumStage = new Stage();
				removeStadiumStage.initOwner(primaryStage);
				removeStadiumStage.initModality(Modality.WINDOW_MODAL);
				removeStadiumStage.setTitle("Remove Stadium");
				GridPane gpRemoveStadium = new GridPane();
				gpRemoveStadium.setHgap(15);
				gpRemoveStadium.setVgap(10);
				gpRemoveStadium.setPadding(new Insets(10));

				gpRemoveStadium.add(lblSelectStadiumToRemove, 1, 1);
				gpRemoveStadium.add(cmbStadium, 1, 2);
				gpRemoveStadium.add(btnRemoveTheStadium, 1, 4);
				btnRemoveTheStadium.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.removeStadiumFromUI(cmbStadium.getValue());
							} catch (NoSelectedItemException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							} catch (CantRemoveStadiumException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						cmbStadium.getSelectionModel().clearSelection();
						removeStadiumStage.close();
					}
				});
				removeStadiumStage.setScene(new Scene(gpRemoveStadium, 300, 150));

				removeStadiumStage.show();
			}
		});

		btnShowAllStadiums.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage showAllStadiumsStage = new Stage();

				showAllStadiumsStage.initOwner(primaryStage);
				showAllStadiumsStage.initModality(Modality.WINDOW_MODAL);
				showAllStadiumsStage.setTitle("Show all Stadiums");
				GridPane gpShowAllStadiums = new GridPane();
				javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
				scrollPane.setContent(gpShowAllStadiums);

				gpShowAllStadiums.setHgap(15);
				gpShowAllStadiums.setVgap(10);
				gpShowAllStadiums.setPadding(new Insets(10));

				Label lblShowAllStadiums = new Label("All Stadiums presented below:");
				gpShowAllStadiums.add(lblShowAllStadiums, 0, 1);
				gpShowAllStadiums.add(textAllStadiums, 0, 2);
				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					l.printAllStadiumsUI();
				}
				showAllStadiumsStage.setScene(new Scene(scrollPane, 500, 400));
				showAllStadiumsStage.show();
			}
		});

		btnAddSportsman.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage addSportsmanStage = new Stage();

				addSportsmanStage.initOwner(primaryStage);
				addSportsmanStage.initModality(Modality.WINDOW_MODAL);
				addSportsmanStage.setTitle("Add Sportsman");
				GridPane gpAddSportsman = new GridPane();

				gpAddSportsman.setHgap(15);
				gpAddSportsman.setVgap(10);
				gpAddSportsman.setPadding(new Insets(10));

				Label lblEnterSportsmanName = new Label("Enter sportsman name: ");
				Label lblChooseNationality = new Label("Choose Nationality: ");
				Label lblSelectSportsmanCompetitionType = new Label("Select sportsman's competition type: ");

				lblEnterSportsmanName.setFont(font);
				lblSelectSportsmanCompetitionType.setFont(font);
				lblChooseNationality.setFont(font);

				TextField tfSportsmanName = new TextField();

				Button btnAddTheSportsman = new Button("Add the Sportsman");
				btnAddTheSportsman.setFont(font);

				gpAddSportsman.add(lblEnterSportsmanName, 0, 1);
				gpAddSportsman.add(tfSportsmanName, 0, 2);
				gpAddSportsman.add(lblSelectSportsmanCompetitionType, 0, 5);
				gpAddSportsman.add(cmbSportsmanCompetitionType, 0, 6);
				gpAddSportsman.add(btnAddTheSportsman, 0, 8);
				gpAddSportsman.add(lblChooseNationality, 0, 3);
				gpAddSportsman.add(cmbCountries, 0, 4);

				btnAddTheSportsman.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.addSportsmanToUI(tfSportsmanName.getText(), cmbCountries.getValue(),
										cmbSportsmanCompetitionType.getValue());
							} catch (OneOfTheFieldsWasNotFilledException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						addSportsmanStage.close();
						cmbCountries.getSelectionModel().clearSelection();
						cmbSportsmanCompetitionType.getSelectionModel().clearSelection();
					}

				});

				addSportsmanStage.setScene(new Scene(gpAddSportsman, 300, 250));
				addSportsmanStage.show();

			}

		});
		btnRemoveSportsman.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage removeSportsmanStage = new Stage();
				removeSportsmanStage.initOwner(primaryStage);
				removeSportsmanStage.initModality(Modality.WINDOW_MODAL);
				removeSportsmanStage.setTitle("Remove Sportsman");
				GridPane gpRemoveSportsman = new GridPane();

				gpRemoveSportsman.setHgap(15);
				gpRemoveSportsman.setVgap(10);
				gpRemoveSportsman.setPadding(new Insets(10));

				Label lblSelectSportsmanToRemove = new Label("Select Sportsman to remove: ");

				Button btnRemoveTheSportsman = new Button("Remove the Sportsman");

				gpRemoveSportsman.add(lblSelectSportsmanToRemove, 0, 1);
				gpRemoveSportsman.add(cmbSportsmen, 0, 2);
				gpRemoveSportsman.add(btnRemoveTheSportsman, 0, 4);

				btnRemoveTheSportsman.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.removeSportsmanFromUI(cmbSportsmen.getValue());
							} catch (NoSelectedItemException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							} catch (CantRemoveSportsmanException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}

						}
						cmbSportsmen.getSelectionModel().clearSelection();
						removeSportsmanStage.close();

					}
				});

				removeSportsmanStage.setScene(new Scene(gpRemoveSportsman, 500, 300));
				removeSportsmanStage.show();
			}
		});

		btnShowAllSportsmen.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage showAllSportsmenStage = new Stage();
				showAllSportsmenStage.initOwner(primaryStage);
				showAllSportsmenStage.initModality(Modality.WINDOW_MODAL);
				showAllSportsmenStage.setTitle("Show all Sportsmen");
				GridPane gpShowAllSportsmen = new GridPane();
				javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
				scrollPane.setContent(gpShowAllSportsmen);

				gpShowAllSportsmen.setHgap(15);
				gpShowAllSportsmen.setVgap(10);
				gpShowAllSportsmen.setPadding(new Insets(10));

				Label lblAllSportsman = new Label("All Sportsmen presented below: ");

				gpShowAllSportsmen.add(lblAllSportsman, 0, 1);
				gpShowAllSportsmen.add(textAllSportmen, 0, 2);

				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					l.printAllSportsmenUI();
				}

				showAllSportsmenStage.setScene(new Scene(scrollPane, 500, 300));
				showAllSportsmenStage.show();
			}
		});

		btnAddSquad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Stage addSquadStage = new Stage();

				addSquadStage.initOwner(primaryStage);
				addSquadStage.initModality(Modality.WINDOW_MODAL);
				addSquadStage.setTitle("Add Squad");
				GridPane gpAddSquad = new GridPane();

				gpAddSquad.setHgap(15);
				gpAddSquad.setVgap(10);
				gpAddSquad.setPadding(new Insets(10));

				Label lblEnterSquadNationality = new Label("Choose nationality:");
				Label lblSelectSquadCompetitionType = new Label("Select squad Competition type:");

				lblEnterSquadNationality.setFont(font);
				lblSelectSquadCompetitionType.setFont(font);

				Button btnNext = new Button("Next-Adding Sportsmen");
				btnNext.setFont(font);

				gpAddSquad.add(lblEnterSquadNationality, 0, 1);
				gpAddSquad.add(cmbCountries, 0, 2);
				gpAddSquad.add(lblSelectSquadCompetitionType, 0, 3);
				gpAddSquad.add(cmbCompetitionType, 0, 4);
				gpAddSquad.add(btnNext, 0, 5);

				lvRunners.setMinSize(400, 400);
				lvHighJumpers.setMinSize(400, 400);

				btnNext.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						GridPane gpAddSportsmenToSquad = new GridPane();
						Label lblAddSportsmenToSquadInstructions = new Label(
								"For selecting multiple Sportmen hold the Ctrl button and click the sportsman.");
						gpAddSportsmenToSquad.setHgap(15);
						gpAddSportsmenToSquad.setVgap(10);
						gpAddSportsmenToSquad.setPadding(new Insets(10));
						lblAddSportsmenToSquadInstructions.setFont(font);

						Button btnAddTheSquad = new Button("Add the squad");
						btnAddTheSquad.setMinSize(30, 30);
						btnAddTheSquad.setFont(font);

						gpAddSportsmenToSquad.add(lblAddSportsmenToSquadInstructions, 1, 1);
						gpAddSportsmenToSquad.add(btnAddTheSquad, 1, 4);

						ArrayList<Sportsman> selectedSportsmen = new ArrayList<Sportsman>();

						allSprotsmenByNation.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

						if (cmbCompetitionType.getValue() == CompetitionType.RunningCompetition) {
							Label lblAllRunners = new Label(
									"list of all sportsmen who can compete Running Competition: ");

							lblAllRunners.setFont(font);

							for (Sportsman sportsman : lvRunners.getItems()) {
								if (cmbCountries.getValue().equals(sportsman.getSportsmanNationality())) {
									allSprotsmenByNation.getItems().add(sportsman);
								}
							}

							gpAddSportsmenToSquad.add(lblAllRunners, 1, 2);

						} else {
							Label lblAllHighJumpers = new Label(
									"list of all sportsmen who can compete High jumping Competition: ");
							lblAllHighJumpers.setFont(font);

							for (Sportsman sportsman : lvHighJumpers.getItems()) {
								if (cmbCountries.getValue().equals(sportsman.getSportsmanNationality())) {
									allSprotsmenByNation.getItems().add(sportsman);
								}
							}

							gpAddSportsmenToSquad.add(lblAllHighJumpers, 1, 2);

						}

						gpAddSportsmenToSquad.add(allSprotsmenByNation, 1, 3);

						addSquadStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

							@Override
							public void handle(WindowEvent event) {

								allSprotsmenByNation.getItems().clear();
							}
						});

						btnAddTheSquad.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								ObservableList<Sportsman> selectedItems = allSprotsmenByNation.getSelectionModel()
										.getSelectedItems();
								for (Sportsman sportsman : selectedItems) {
									selectedSportsmen.add(sportsman);
								}
								for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
									try {
										l.addSquadToUI(cmbCountries.getValue(), cmbCompetitionType.getValue(),
												selectedSportsmen);
									} catch (OneOfTheFieldsWasNotFilledException e) {
										JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
									} catch (SquadNationExistForCompetitionType e) {
										JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
									}
								}
								cmbCountries.getSelectionModel().clearSelection();
								cmbCompetitionType.getSelectionModel().clearSelection();
								allSprotsmenByNation.getItems().clear();
								addSquadStage.close();
							}

						});

						addSquadStage.setScene(new Scene(gpAddSportsmenToSquad, 560, 530));
					}

				});

				addSquadStage.setScene(new Scene(gpAddSquad, 300, 200));
				addSquadStage.show();

			}
		});
		btnRemoveSquad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Stage removeSquadStage = new Stage();
				removeSquadStage.initOwner(primaryStage);
				removeSquadStage.initModality(Modality.WINDOW_MODAL);
				removeSquadStage.setTitle("Remove Squad");
				GridPane gpRemoveSquad = new GridPane();

				gpRemoveSquad.setHgap(15);
				gpRemoveSquad.setVgap(10);
				gpRemoveSquad.setPadding(new Insets(10));

				Label lblSelectSportsmanToRemove = new Label("Select Squad to remove: ");

				Button btnRemoveTheSquad = new Button("Remove the Squad");

				gpRemoveSquad.add(lblSelectSportsmanToRemove, 0, 1);
				gpRemoveSquad.add(lvSquads, 0, 2);
				gpRemoveSquad.add(btnRemoveTheSquad, 0, 4);

				removeSquadStage.setScene(new Scene(gpRemoveSquad, 500, 300));
				removeSquadStage.show();

				btnRemoveTheSquad.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
							try {
								l.removeSquadFromUI(lvSquads.getSelectionModel().getSelectedItem());
							} catch (NoSelectedItemException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							} catch (CantRemoveSquadException e) {
								JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						lvSquads.getSelectionModel().clearSelection();
						removeSquadStage.close();
					}
				});
			}

		});
		btnShowAllSquads.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage showAllSquadStage = new Stage();
				showAllSquadStage.initOwner(primaryStage);
				showAllSquadStage.initModality(Modality.WINDOW_MODAL);
				showAllSquadStage.setTitle("Show All Squads");
				GridPane gpShowAllSquads = new GridPane();

				gpShowAllSquads.setHgap(15);
				gpShowAllSquads.setVgap(10);
				gpShowAllSquads.setPadding(new Insets(10));

				Label lblAllSquadsPresentedBelow = new Label("All Squads presented below: ");

				gpShowAllSquads.add(lblAllSquadsPresentedBelow, 0, 1);
				gpShowAllSquads.add(textAllSquads, 0, 2);

				ScrollPane spShowAllSquads = new ScrollPane();
				spShowAllSquads.setContent(gpShowAllSquads);

				for (OlympicsManagementSystemUIEventsListener l : UIEventsListeners) {
					l.printAllSquadsUI();
				}

				showAllSquadStage.setScene(new Scene(spShowAllSquads, 500, 300));
				showAllSquadStage.show();
			}
		});

		primaryStage.setScene(new Scene(vBox, 670, 450));
		primaryStage.show();
	}

	@Override
	public void registerListener(SystemController systemController) {
		UIEventsListeners.add(systemController);

	}

	@Override
	public void addCompetitionToUI(Competition competition, Boolean readFromFile) {
		currentCompetition = competition;
		cmbFirstPlaceSportsman.getItems().clear();
		cmbSecondPlaceSportsman.getItems().clear();
		cmbThirdPlaceSportsman.getItems().clear();
		cmbFirstPlaceSquad.getItems().clear();
		cmbSecondPlaceSquad.getItems().clear();
		cmbThirdPlaceSquad.getItems().clear();

		if (competition instanceof PersonalCompetition) {
			PersonalCompetition p = (PersonalCompetition) competition;
			lvCompetitions.getItems().add(p);
			cmbFirstPlaceSportsman.getItems().addAll(p.getParticipants());
			cmbSecondPlaceSportsman.getItems().addAll(p.getParticipants());
			cmbThirdPlaceSportsman.getItems().addAll(p.getParticipants());
		}

		if (competition instanceof NationalCompetition) {
			NationalCompetition p = (NationalCompetition) competition;
			lvCompetitions.getItems().add(p);
			cmbFirstPlaceSquad.getItems().addAll(p.getSquadParticipants());
			cmbSecondPlaceSquad.getItems().addAll(p.getSquadParticipants());
			cmbThirdPlaceSquad.getItems().addAll(p.getSquadParticipants());
		}

	}

	@Override
	public void addJudgeToUI(Judge judge, Boolean readFromFile) {
		cmbJudges.getItems().add(judge);
		if (!readFromFile)
			JOptionPane.showMessageDialog(null, "The judge " + judge.getJudgeName() + " is added !");

	}

	@Override
	public void addStadiumToUI(Stadium stadium, Boolean readFromFile) {
		cmbStadium.getItems().add(stadium);
		if (!readFromFile)
			JOptionPane.showMessageDialog(null, "the Stadium " + stadium.getStadiumName() + " added !");
	}

	@Override
	public void addSportsmanToUI(Sportsman sportsman, Boolean readFromFile) {
		cmbSportsmen.getItems().add(sportsman);
		if (sportsman instanceof Runner || sportsman instanceof RunnerAndHighJumper) {
			lvRunners.getItems().add(sportsman);
		}
		if (sportsman instanceof HighJumper || sportsman instanceof RunnerAndHighJumper) {
			lvHighJumpers.getItems().add(sportsman);
		}
		if (!readFromFile)
			JOptionPane.showMessageDialog(null,
					"Sportsman " + sportsman.getSportsmanName() + " added to all sportsmen !");

	}

	@Override
	public void addSquadToUI(Squad squad, Boolean readFromFile) {
		lvSquads.getItems().add(squad);
		if (squad.getCompetitionType() == CompetitionType.RunningCompetition) {
			lvSquadCompeteRunning.getItems().add(squad);
		} else {
			lvSquadCompeteHighJumping.getItems().add(squad);
		}
		if (!readFromFile)
			JOptionPane.showMessageDialog(null,
					"The " + squad.getSquadNationality() + " Squad has been added to all squads !");

	}

	@Override
	public void removeCompetitionFromUI(Competition selectedItem, Boolean pressedExit) {
		lvCompetitions.getItems().remove(selectedItem);
		if (!pressedExit) {
			JOptionPane.showMessageDialog(null, "The Competition has been removed !");
		}

	}

	@Override
	public void removeJudgeFromUI(Judge judge) {
		cmbJudges.getItems().remove(judge);
		JOptionPane.showMessageDialog(null, "the Judge " + judge.getJudgeName() + " has been removed !");

	}

	@Override
	public void removeStadium(Stadium stadium) {
		cmbStadium.getItems().remove(stadium);
		JOptionPane.showMessageDialog(null, "the Stadium " + stadium.getStadiumName() + " has been removed !");

	}

	@Override
	public void removeSportmanFromUI(Sportsman sportsman) {
		cmbSportsmen.getItems().remove(sportsman);
		if (sportsman instanceof Runner || sportsman instanceof RunnerAndHighJumper) {
			lvRunners.getItems().remove(sportsman);
		}
		if (sportsman instanceof HighJumper || sportsman instanceof RunnerAndHighJumper) {
			lvHighJumpers.getItems().remove(sportsman);
		}

		JOptionPane.showMessageDialog(null,
				"Sportsman " + sportsman.getSportsmanName() + " removed from all sportsmen !");

	}

	@Override
	public void removeSquadFromUI(Squad squad) {
		lvSquads.getItems().remove(squad);

		if (squad.getCompetitionType() == CompetitionType.RunningCompetition) {
			lvSquadCompeteRunning.getItems().remove(squad);
		} else {
			lvSquadCompeteHighJumping.getItems().remove(squad);
		}

		JOptionPane.showMessageDialog(null,
				"The " + squad.getSquadNationality() + " Squad has been removed from all squads !");
	}

	@Override
	public void printAllJudges(StringBuffer sbPrintAllJudges) {
		textAllJudges.setText(sbPrintAllJudges.toString());
	}

	@Override
	public void printAllStadiums(StringBuffer sbPrintAllStadiums) {
		textAllStadiums.setText(sbPrintAllStadiums.toString());
	}

	@Override
	public void printAllSportmenUI(StringBuffer sbPrintAllSportmen) {
		textAllSportmen.setText(sbPrintAllSportmen.toString());
	}

	@Override
	public void printAllSquadsUI(StringBuffer sbPrintAllSquads) {
		textAllSquads.setText(sbPrintAllSquads.toString());

	}

	@Override
	public void addWinnersToCompetitionUI(Competition currentCompetition) {

		JOptionPane.showMessageDialog(null, "Competition created!");

	}

	@Override
	public void saveAndExitUI() {
		JOptionPane.showMessageDialog(null, "Your data has been saved !");
	}

	@Override
	public void showMedalsForCountryUI(int numberOfPersonalMedals, int numberOfNationalMedals, String country) {
		textNumberOfMedalsForCountry.setText("Number of medals for " + country + " : "
				+ (numberOfPersonalMedals + numberOfNationalMedals) + "\n\nNumber of personal medals: "
				+ numberOfPersonalMedals + "\nNumber of national medals: " + numberOfNationalMedals);

	}

	@Override
	public void showWinnersUI(String firstPlace, String secondPlace, String thirdPlace, int numberOfFirstPlaceMedals,
			int numberOfSecondPlaceMedals, int numberOfThirdPlaceMedals) {
		StringBuffer sbWinners = new StringBuffer();
		if (firstPlace == null && secondPlace == null && thirdPlace == null) {
			sbWinners.append("No countries");
			return;
		}
		sbWinners.append("The winners: \n\n");
		if (firstPlace != null) {
			sbWinners.append("First Place: " + firstPlace + "\nNumber of medals: " + numberOfFirstPlaceMedals + "\n\n");
		}
		if (secondPlace != null) {
			sbWinners.append(
					"Second Place: " + secondPlace + "\nNumber of medals: " + numberOfSecondPlaceMedals + "\n\n");
		}

		if (thirdPlace != null) {
			sbWinners.append("Third Place: " + thirdPlace + "\nNumber of medals: " + numberOfThirdPlaceMedals);
		}
		textWinners.setText(sbWinners.toString());

	}

	@Override
	public void DatePickedAndSet() {
		isOlympicGamesDate = true;

	}

}
