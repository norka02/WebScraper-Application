package pl.hubertmaka.project.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.hubertmaka.project.enums.*;
import pl.hubertmaka.project.webscraper.*;


import java.awt.Desktop;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.concurrent.Task;

public class MainScreenController {
    private static final Logger logger = LogManager.getLogger(MainScreenController.class);

    private Task<Void> loadDataTask;
    private final Map<String, List<String>> voivodeshipToCities = new HashMap<>();
    private final Map<String, List<String>> normalizedVoivodeshipToCities = new HashMap<>();
    private final ObservableList<ApartmentInfo> apartmentList = FXCollections.observableArrayList();
    public final ScraperApi scraperApi = new ScraperApi();

    public MainScreenController() {
        voivodeshipToCities.put("Dolnośląskie", Arrays.asList("Wrocław", "Wałbrzych", "Legnica", "Jelenia Góra", "Lubin"));
        voivodeshipToCities.put("Kujawsko-Pomorskie", Arrays.asList("Bydgoszcz", "Toruń", "Włocławek", "Grudziądz", "Inowrocław"));
        voivodeshipToCities.put("Lubelskie", Arrays.asList("Lublin", "Chełm", "Zamość", "Biała Podlaska", "Puławy"));
        voivodeshipToCities.put("Lubuskie", Arrays.asList("Zielona Góra", "Gorzów Wielkopolski", "Nowa Sól", "Żary", "Żagań"));
        voivodeshipToCities.put("Łódzkie", Arrays.asList("Łódź", "Piotrków Trybunalski", "Pabianice", "Tomaszów Mazowiecki", "Bełchatów"));
        voivodeshipToCities.put("Małopolskie", Arrays.asList("Kraków", "Tarnów", "Nowy Sącz", "Oświęcim", "Chrzanów"));
        voivodeshipToCities.put("Mazowieckie", Arrays.asList("Warszawa", "Radom", "Płock", "Siedlce", "Pruszków"));
        voivodeshipToCities.put("Opolskie", Arrays.asList("Opole", "Kędzierzyn-Koźle", "Nysa", "Brzeg", "Kluczbork"));
        voivodeshipToCities.put("Podkarpackie", Arrays.asList("Rzeszów", "Przemyśl", "Stalowa Wola", "Mielec", "Tarnobrzeg"));
        voivodeshipToCities.put("Podlaskie", Arrays.asList("Białystok", "Suwałki", "Łomża", "Augustów", "Bielsk Podlaski"));
        voivodeshipToCities.put("Pomorskie", Arrays.asList("Gdańsk", "Gdynia", "Słupsk", "Tczew", "Starogard Gdański"));
        voivodeshipToCities.put("Śląskie", Arrays.asList("Katowice", "Częstochowa", "Sosnowiec", "Gliwice", "Zabrze"));
        voivodeshipToCities.put("Świętokrzyskie", Arrays.asList("Kielce", "Ostrowiec Świętokrzyski", "Starachowice", "Sandomierz", "Skarżysko-Kamienna"));
        voivodeshipToCities.put("Warmińsko-Mazurskie", Arrays.asList("Olsztyn", "Elbląg", "Ełk", "Ostróda", "Iława"));
        voivodeshipToCities.put("Wielkopolskie", Arrays.asList("Poznań", "Kalisz", "Konin", "Piła", "Ostrów Wielkopolski"));
        voivodeshipToCities.put("Zachodniopomorskie", Arrays.asList("Szczecin", "Koszalin", "Stargard", "Kołobrzeg", "Świnoujście"));
    }

    @FXML
    public Label infoLabel;
    @FXML
    private CheckComboBox<String> voivodeshipCheckComboBox;
    @FXML
    private CheckComboBox<String> cityCheckComboBox;
    @FXML
    private Button cancelLoadDataButton;
    @FXML
    private Label loadingLabel;
    @FXML
    private ListView<ApartmentInfo> apartmentListView;
    @FXML
    private Button loadDataButton;

    @FXML
    private ComboBox<String> filterByCityComboBox;
    @FXML
    private ComboBox<String> filterByVoivodeshipComboBox;
    @FXML
    private ComboBox<String> sortByComboBox;

    @FXML
    private RadioButton forSaleRadioButton;
    @FXML
    private RadioButton forRentRadioButton;
    @FXML
    private ToggleGroup purchaseTypeGroup;




    @FXML
    public void initialize() {
        purchaseTypeGroup = new ToggleGroup();
        cancelLoadDataButton.setVisible(false);
        loadingLabel.setVisible(false);
        apartmentListView.setItems(apartmentList);
        initializeVoivodeshipCheckComboBox();
        initializeCityCheckComboBox();
        initializeFilterAndSortComboBoxes();
        normalizeVoivodeshipToCities();
        forSaleRadioButton.setToggleGroup(purchaseTypeGroup);
        forRentRadioButton.setToggleGroup(purchaseTypeGroup);
        apartmentListView.setCellFactory(new Callback<ListView<ApartmentInfo>, ListCell<ApartmentInfo>>() {
            @Override
            public ListCell<ApartmentInfo> call(ListView<ApartmentInfo> listView) {
                return new ListCell<ApartmentInfo>() {
                    @Override
                    protected void updateItem(ApartmentInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox(5); // wertykalny układ dla elementów

                            Text title = new Text("Title: " + item.getTitle());
                            String purchaseTypeInfo;
                            if (item.getPurchaseType().equals(PurchaseType.ON_RENT.getPolishName())) {
                                purchaseTypeInfo = "WYNAJEM";
                            } else {
                                purchaseTypeInfo = "SPRZEDAŻ";
                            }
                            Text purchaseType = new Text("Purchase Type: " + purchaseTypeInfo);
                            Text fromSite = new Text("From Site: " + item.getFromSite());
                            Text voivodeship = new Text("Voivodeship: " + item.getVoivodeship());
                            Text city = new Text("City: " + item.getCity());
                            Text district = new Text("District: " + item.getDistrict());
                            Text street = new Text("Street: " + item.getStreet());
                            String priceInfo;
                            String pricePerMeterInfo;
                            String roomsInfo;
                            if (item.getPrice() < 0) {
                                priceInfo = "Price: Not defined.";
                            } else {
                               priceInfo = "Price: " + item.getPrice().toString() + " zł";
                            }
                            if (item.getPricePerMeter() < 0) {
                                pricePerMeterInfo = "Price per meter: Not defined.";
                            } else {
                                pricePerMeterInfo = "Price per meter: " + item.getPrice().toString() + " zł/m²";
                            }
                            if (item.getRooms() <= 0) {
                                roomsInfo = "Rooms: Not defined.";
                            } else {
                                roomsInfo = "Rooms: " + item.getRooms().toString();
                            }
                            Text price = new Text(priceInfo);
                            Text pricePerMeter = new Text(pricePerMeterInfo);
                            Text rooms = new Text(roomsInfo);
                            Text size = new Text("Size: " + item.getSize().toString() + " m²");
                            Text isBoosted = new Text("Is boosted: " + item.isBoosted());
                            Text additionalInfo = new Text("Additional Info: " + item.getAdditionalInfo());
                            additionalInfo.setWrappingWidth(300);

                            Hyperlink link = new Hyperlink(item.getLinkToAnnouncement());
                            link.setOnAction(e -> {
                                try {
                                    if (Desktop.isDesktopSupported()) {
                                        Desktop.getDesktop().browse(new URI(item.getLinkToAnnouncement()));
                                    }
                                } catch (Exception ex) {
                                    logger.error("Problem with opening the link: ", ex);
                                }
                            });

                            vbox.getChildren().addAll(fromSite, title,purchaseType, voivodeship, city, district, street, price, pricePerMeter, rooms, size,isBoosted, additionalInfo, link);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });

    }


    private void normalizeVoivodeshipToCities() {
        normalizedVoivodeshipToCities.clear();
        for (Map.Entry<String, List<String>> entry : voivodeshipToCities.entrySet()) {
            String normalizedKey = normalizeString(entry.getKey());
            List<String> originalCities = entry.getValue();
            List<String> normalizedCities = new ArrayList<>();

            for (String city : originalCities) {
                normalizedCities.add(normalizeString(city));
            }

            normalizedVoivodeshipToCities.put(normalizedKey, normalizedCities);
        }
    }

    private void initializeFilterAndSortComboBoxes() {
        // Inicjalizacja ComboBoxów dla filtrowania i sortowania
        filterByCityComboBox.setItems(FXCollections.observableArrayList("Wszystkie miasta", "Kraków", "Warszawa", "Wrocław"));
        filterByVoivodeshipComboBox.setItems(FXCollections.observableArrayList("Wszystkie województwa", "Małopolskie", "Mazowieckie", "Dolnośląskie"));
        sortByComboBox.setItems(FXCollections.observableArrayList("Cena", "Rozmiar", "Cena za metr"));

        // Dodaj listenery do ComboBoxów
        filterByCityComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterApartments());
        filterByVoivodeshipComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterApartments());
        sortByComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> sortApartments());
    }
    private void sortApartments() {
        String selectedSort = sortByComboBox.getValue();

        Comparator<ApartmentInfo> comparator;
        switch (selectedSort) {
            case "Cena":
                comparator = Comparator.comparing(ApartmentInfo::getPrice);
                break;
            case "Rozmiar":
                comparator = Comparator.comparing(ApartmentInfo::getSize);
                break;
            case "Cena za metr":
                comparator = Comparator.comparing(ApartmentInfo::getPricePerMeter);
                break;
            default:
                return;
        }

        ObservableList<ApartmentInfo> sortedList = apartmentListView.getItems().sorted(comparator);
        apartmentListView.setItems(sortedList);
    }
    private void filterApartments() {
        String selectedCity = filterByCityComboBox.getValue();
        String selectedVoivodeship = filterByVoivodeshipComboBox.getValue();

        ObservableList<ApartmentInfo> filteredList = apartmentList.stream()
                .filter(apartment -> (selectedCity.equals("Wszystkie miasta") || apartment.getCity().equals(selectedCity)) &&
                        (selectedVoivodeship.equals("Wszystkie województwa") || apartment.getVoivodeship().equals(selectedVoivodeship)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        apartmentListView.setItems(filteredList);
    }


    private void initializeVoivodeshipCheckComboBox() {
        voivodeshipCheckComboBox.getItems().addAll(voivodeshipToCities.keySet());
        voivodeshipCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            updateCityCheckComboBox();
        });
    }

    // Inicjalizacja CheckComboBox dla miast
    private void initializeCityCheckComboBox() {
        cityCheckComboBox.getItems().clear();  // Wyczyść na początku
    }

    // Aktualizacja CheckComboBox dla miast na podstawie wybranych województw
    private void updateCityCheckComboBox() {
        List<String> selectedVoivodeships = voivodeshipCheckComboBox.getCheckModel().getCheckedItems();
        List<String> cities = new ArrayList<>();

        for (String voivodeship : selectedVoivodeships) {
            cities.addAll(voivodeshipToCities.get(voivodeship));
        }

        cityCheckComboBox.getItems().setAll(cities);
    }

    private CityType mapCityNameToEnum(String cityName) {
        String normalizedCityName = normalizeString(cityName);
        return CityType.valueOfLabel(normalizedCityName);
    }

    private VoivodeshipType mapVoivodeshipNameToEnum(String voivodeshipName) {
        String normalizedVoivodeshipName = normalizeString(voivodeshipName);
        return VoivodeshipType.valueOfLabel(normalizedVoivodeshipName);
    }

    private String normalizeString(String input) {
        return input.toLowerCase()
                .replace(" ", "-")
                .replace("ą", "a")
                .replace("ć", "c")
                .replace("ę", "e")
                .replace("ł", "l")
                .replace("ń", "n")
                .replace("ó", "o")
                .replace("ś", "s")
                .replace("ż", "z")
                .replace("ź", "z");
    }

    @FXML
    protected void handleLoadData() {

        PurchaseType purchaseType = forSaleRadioButton.isSelected() ? PurchaseType.FOR_SALE : PurchaseType.ON_RENT;
        infoLabel.setVisible(false);
        loadDataButton.setDisable(true);
        loadingLabel.setVisible(true);
        cancelLoadDataButton.setVisible(true);
        apartmentListView.setItems(null);
        loadingLabel.setText("Loading data... It can take some time depends on cities to get. You can go take some tea and when you will return it should be finished ;)");

        loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Logowanie do konsoli dla debugowania
                System.out.println("Zadanie rozpoczęte.");

                List<String> selectedVoivodeshipsNames = voivodeshipCheckComboBox.getCheckModel().getCheckedItems();
                List<String> selectedCitiesNames = cityCheckComboBox.getCheckModel().getCheckedItems();

                // Logowanie wybranych województw i miast
                System.out.println("Wybrane województwa: " + selectedVoivodeshipsNames);
                System.out.println("Wybrane miasta: " + selectedCitiesNames);

                List<VoivodeshipType> selectedVoivodeships = selectedVoivodeshipsNames
                        .stream()
                        .map(MainScreenController.this::mapVoivodeshipNameToEnum)
                        .collect(Collectors.toList());
                List<CityType> selectedCities = selectedCitiesNames
                        .stream()
                        .map(MainScreenController.this::mapCityNameToEnum)
                        .collect(Collectors.toList());

                // Logowanie przekształconych wartości
                System.out.println("Przekształcone województwa: " + selectedVoivodeships);
                System.out.println("Przekształcone miasta: " + selectedCities);

                scraperApi.createApartmentInfoArrayList(selectedCities, selectedVoivodeships, normalizedVoivodeshipToCities, purchaseType);

                return null;
            }
        };

        loadDataTask.setOnSucceeded(e -> {
            if (apartmentList.isEmpty()) {
                infoLabel.setVisible(true);
            }
            apartmentList.clear();
            apartmentList.addAll(scraperApi.apartmentInfoArrayList);
            scraperApi.apartmentInfoArrayList.clear();
            loadingLabel.setVisible(false);
            apartmentListView.setItems(apartmentList);
            loadDataButton.setDisable(false);
            cancelLoadDataButton.setVisible(false);


            // Logowanie sukcesu
            System.out.println("Zadanie zakończone sukcesem. Dane załadowane.");
        });

        loadDataTask.setOnFailed(e -> {
            scraperApi.apartmentInfoArrayList.clear();
            logger.error("Error during data loading: ", loadDataTask.getException());
            loadingLabel.setText("Error during data loading.");

            // Logowanie błędu
            System.out.println("Zadanie zakończone błędem: " + loadDataTask.getException());
        });

        new Thread(loadDataTask).start();
    }
    @FXML
    protected void handleCancelLoadData() {
        if (loadDataTask != null && loadDataTask.isRunning()) {
            loadDataTask.cancel();
            cancelLoadDataButton.setVisible(false);
            loadDataButton.setDisable(false);
            loadingLabel.setText("Loading cancelled.");
        }
    }

    public void stopRunningTasks() {
        if (loadDataTask != null && loadDataTask.isRunning()) {
            loadDataTask.cancel();
            // Dodatkowe wywołania, aby zatrzymać inne działające procesy
        }
    }

}

