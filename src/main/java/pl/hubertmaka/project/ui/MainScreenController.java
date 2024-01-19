package pl.hubertmaka.project.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
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
import java.util.stream.Stream;

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
    private final ObservableList<ApartmentInfo> allApartments = FXCollections.observableArrayList();

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
    private RadioButton forSaleRadioButton;
    @FXML
    private RadioButton forRentRadioButton;
    @FXML
    private ToggleGroup purchaseTypeGroup;
    @FXML
    private ComboBox<String> filterByCityComboBox;
    @FXML
    private ComboBox<String> filterByDistrictComboBox;
    @FXML
    private ComboBox<String> sortByComboBox;

    public MainScreenController() {
        initializeVoivodeshipToCities();
        normalizeVoivodeshipToCities();
    }

    @FXML
    public void initialize() {
        purchaseTypeGroup = new ToggleGroup();
        cancelLoadDataButton.setVisible(false);
        loadingLabel.setVisible(false);
        apartmentListView.setItems(apartmentList);
        initializeVoivodeshipCheckComboBox();
        initializeCityCheckComboBox();
        forSaleRadioButton.setToggleGroup(purchaseTypeGroup);
        forRentRadioButton.setToggleGroup(purchaseTypeGroup);
        initializeSortOptions();
        apartmentListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ApartmentInfo> call(ListView<ApartmentInfo> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ApartmentInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox(5);
                            String priceInfo;
                            String pricePerMeterInfo;
                            String roomsInfo;
                            String additionalInfoCheck;
                            String isBoostedInfo;
                            String purchaseTypeInfo;

                            Text title = new Text("Tytuł ogłoszenia: " + item.getTitle());
                            title.getStyleClass().add("title-text");

                            if (item.getPurchaseType().equals(PurchaseType.ON_RENT.getPolishName())) {
                                purchaseTypeInfo = "WYNAJEM";
                            } else {
                                purchaseTypeInfo = "SPRZEDAŻ";
                            }
                            Text purchaseType = new Text("Rodzaj zakupu: " + purchaseTypeInfo);
                            Text fromSite = new Text("Informacje ze strony: " + item.getFromSite());
                            Text voivodeship = new Text("Województwo: " + item.getVoivodeship());
                            Text city = new Text("Miasto: " + item.getCity());
                            Text district = new Text("Dzielnica: " + item.getDistrict());
                            Text street = new Text("Ulica: " + item.getStreet());

                            if (item.getPrice() < 0) {
                                priceInfo = "Cenia: Niezdefiniowana";
                            } else {
                                priceInfo = "Cena: " + item.getPrice().toString() + " zł";
                            }
                            if (item.getPricePerMeter() < 0) {
                                pricePerMeterInfo = "Cena za metr: niezdefiniowana";
                            } else {
                                pricePerMeterInfo = "Cena za metr: " + item.getPricePerMeter().toString() + " zł/m²";
                            }
                            if (item.getRooms() <= 0) {
                                roomsInfo = "Liczba pokoi: Niezdefiniowana.";
                            } else {
                                roomsInfo = "Liczba pokoi: " + item.getRooms().toString();
                            }
                            Text price = new Text(priceInfo);
                            Text pricePerMeter = new Text(pricePerMeterInfo);
                            Text rooms = new Text(roomsInfo);
                            Text size = new Text("Rozmiar mieszkania: " + item.getSize().toString() + " m²");
                            if (item.isBoosted()) {
                                isBoostedInfo = "Tak";
                            } else {
                                isBoostedInfo = "Nie";
                            }
                            Text isBoosted = new Text("Czy niedawno podbite: " + isBoostedInfo);
                            if (item.getAdditionalInfo().contains("Not defined")) {
                                additionalInfoCheck = "Brak";
                            } else {
                                additionalInfoCheck = item.getAdditionalInfo();
                            }
                            Text additionalInfo = new Text("Dodatkowe informacje: " + additionalInfoCheck);
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

                            vbox.getChildren().addAll(fromSite, title, purchaseType, voivodeship, city, district, street, price, pricePerMeter, rooms, size, isBoosted, additionalInfo, link);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });

    }

    private void updateComboBoxes() {
        Set<String> cities = new HashSet<>();
        Set<String> districts = new HashSet<>();

        for (ApartmentInfo apartment : allApartments) {
            if (apartment.getCity() != null) {
                cities.add(apartment.getCity());
            }
            if (apartment.getDistrict() != null) {
                districts.add(apartment.getDistrict());
            }
        }

        filterByCityComboBox.setItems(FXCollections.observableArrayList(cities));
        filterByDistrictComboBox.setItems(FXCollections.observableArrayList(districts));
    }

    private void initializeSortOptions() {
        sortByComboBox.setItems(FXCollections.observableArrayList(
                "Domyślnie",
                "Cena rosnąco",
                "Cena malejąco",
                "Cena za metr rosnąco",
                "Cena za metr malejąco",
                "Rozmiar rosnąco",
                "Rozmiar malejąco",
                "Alfabetycznie dzielnice A-Z",
                "Alfabetycznie dzielnice Z-A",
                "Alfabetycznie miasta A-Z",
                "Alfabetycznie miasta Z-A",
                "Alfabetycznie województwa A-Z",
                "Alfabetycznie województwa Z-A"
        ));
        sortByComboBox.setValue("Domyślnie");
    }

    private void initializeVoivodeshipToCities() {
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
    private String normalizeString(String input) {
        return input.toLowerCase()
                .replace("-", "--")
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

    private void initializeVoivodeshipCheckComboBox() {
        voivodeshipCheckComboBox.getItems().addAll(voivodeshipToCities.keySet());
        voivodeshipCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> updateCityCheckComboBox());
    }

    private void initializeCityCheckComboBox() {
        cityCheckComboBox.getItems().clear();
    }

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
    @FXML
    protected void handleFilterAndSort() {
        String selectedCity = filterByCityComboBox.getValue();
        String selectedDistrict = filterByDistrictComboBox.getValue();

        Stream<ApartmentInfo> stream = allApartments.stream()
                .filter(apartment -> (selectedCity == null || apartment.getCity().equals(selectedCity)))
                .filter(apartment -> (selectedDistrict == null || apartment.getDistrict().equals(selectedDistrict)));

        Comparator<ApartmentInfo> comparator = getComparator(sortByComboBox.getValue());
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }

        apartmentListView.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));
    }

    private Comparator<ApartmentInfo> getComparator(String sortOption) {
        if (sortOption == null || sortOption.equals("Domyślnie")) return null;
        return switch (sortOption) {
            case "Cena rosnąco" -> Comparator.comparing(ApartmentInfo::getPrice);
            case "Cena malejąco" -> Comparator.comparing(ApartmentInfo::getPrice).reversed();
            case "Cena za metr rosnąco" -> Comparator.comparing(ApartmentInfo::getPricePerMeter);
            case "Cena za metr malejąco" -> Comparator.comparing(ApartmentInfo::getPricePerMeter).reversed();
            case "Rozmiar rosnąco" -> Comparator.comparing(ApartmentInfo::getSize);
            case "Rozmiar malejąco" -> Comparator.comparing(ApartmentInfo::getSize).reversed();
            case "Alfabetycznie dzielnice A-Z" -> Comparator.comparing(ApartmentInfo::getDistrict);
            case "Alfabetycznie dzielnice Z-A" -> Comparator.comparing((ApartmentInfo::getDistrict)).reversed();
            case "Alfabetycznie miasta A-Z" -> Comparator.comparing(ApartmentInfo::getCity);
            case "Alfabetycznie miasta Z-A" -> Comparator.comparing((ApartmentInfo::getCity)).reversed();
            case "Alfabetycznie województwa A-Z" -> Comparator.comparing(ApartmentInfo::getVoivodeship);
            case "Alfabetycznie województwa Z-A" -> Comparator.comparing((ApartmentInfo::getVoivodeship)).reversed();
            default -> null;
        };
    }

    @FXML
    protected void handleLoadData() {
        scraperApi.apartmentInfoArrayList.clear();
        PurchaseType purchaseType = forSaleRadioButton.isSelected() ? PurchaseType.FOR_SALE : PurchaseType.ON_RENT;
        loadDataButton.setDisable(true);
        loadingLabel.setVisible(true);
        cancelLoadDataButton.setVisible(true);
        apartmentListView.setItems(null);
        loadingLabel.setText("Ładowanie danych... W zależności od ilości wybranych miast oraz województw może to zająć chwilę. Pójdź sobie zaparzyć herbatę i kiedy wrócisz wszystko powinno być już gotowe do przeglądu ;)");
        loadDataTask = new Task<>() {
            @Override
            protected Void call() {

                List<String> selectedVoivodeshipsNames = voivodeshipCheckComboBox.getCheckModel().getCheckedItems();
                List<String> selectedCitiesNames = cityCheckComboBox.getCheckModel().getCheckedItems();

                List<VoivodeshipType> selectedVoivodeships = selectedVoivodeshipsNames
                        .stream()
                        .map(MainScreenController.this::mapVoivodeshipNameToEnum)
                        .collect(Collectors.toList());
                List<CityType> selectedCities = selectedCitiesNames
                        .stream()
                        .map(MainScreenController.this::mapCityNameToEnum)
                        .collect(Collectors.toList());

                scraperApi.createApartmentInfoArrayList(selectedCities, selectedVoivodeships, normalizedVoivodeshipToCities, purchaseType);

                return null;
            }
        };

        loadDataTask.setOnSucceeded(e -> {
            if (scraperApi.apartmentInfoArrayList.isEmpty()) {
                loadingLabel.setVisible(true);
                loadingLabel.setText("Sprawdź połączenie z Internetem lub poprawność wybranych opcji");
            } else {
                loadingLabel.setVisible(false);
            }
            allApartments.clear();
            allApartments.addAll(scraperApi.apartmentInfoArrayList);
            updateComboBoxes();
            apartmentList.addAll(scraperApi.apartmentInfoArrayList);
            logger.info("Loaded apartments number: " + allApartments.size());

            apartmentListView.setItems(apartmentList);
            loadDataButton.setDisable(false);
            cancelLoadDataButton.setVisible(false);


        });

        loadDataTask.setOnFailed(e -> {
            scraperApi.apartmentInfoArrayList.clear();

            logger.error("Error during data loading: ");
            loadingLabel.setText("Wystąpił błąd podczas ładowania danych.");

        });

        new Thread(loadDataTask).start();
    }
    @FXML
    protected void handleCancelLoadData() {
        if (loadDataTask != null && loadDataTask.isRunning()) {
            loadDataTask.cancel();
            cancelLoadDataButton.setVisible(false);
            loadDataButton.setDisable(false);
            loadingLabel.setText("Pobieranie danych anulowane.");
        }
    }

    public void stopRunningTasks() {
        if (loadDataTask != null && loadDataTask.isRunning()) {
            loadDataTask.cancel();
        }
    }

}

