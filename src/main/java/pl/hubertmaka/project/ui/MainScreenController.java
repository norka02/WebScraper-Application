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

    private  Task<Void> loadDataTask;
    private final Map<String, List<String>> voivodeshipToCities = new HashMap<>();

    public MainScreenController() {
        voivodeshipToCities.put("Dolnośląskie", Arrays.asList("Wrocław", "Wałbrzych", "Legnica", "Jelenia Góra", "Lubin", "Głogów", "Świdnica", "Bolesławiec"));
        voivodeshipToCities.put("Kujawsko-Pomorskie", Arrays.asList("Bydgoszcz", "Toruń", "Włocławek", "Grudziądz", "Inowrocław", "Brodnica", "Chełmno", "Świecie"));
        voivodeshipToCities.put("Lubelskie", Arrays.asList("Lublin", "Chełm", "Zamość", "Biała Podlaska", "Puławy", "Świdnik", "Kraśnik", "Lubartów"));
        voivodeshipToCities.put("Lubuskie", Arrays.asList("Zielona Góra", "Gorzów Wielkopolski", "Nowa Sól", "Żary", "Żagań", "Świebodzin", "Międzyrzecz", "Kostrzyn nad Odrą"));
        voivodeshipToCities.put("Łódzkie", Arrays.asList("Łódź", "Piotrków Trybunalski", "Pabianice", "Tomaszów Mazowiecki", "Bełchatów", "Zgierz", "Skierniewice", "Radomsko"));
        voivodeshipToCities.put("Małopolskie", Arrays.asList("Kraków", "Tarnów", "Nowy Sącz", "Oświęcim", "Chrzanów", "Olkusz", "Nowy Targ", "Bochnia"));
        voivodeshipToCities.put("Mazowieckie", Arrays.asList("Warszawa", "Radom", "Płock", "Siedlce", "Pruszków", "Ostrołęka", "Legionowo", "Ciechanów"));
        voivodeshipToCities.put("Opolskie", Arrays.asList("Opole", "Kędzierzyn-Koźle", "Nysa", "Brzeg", "Kluczbork", "Prudnik", "Strzelce Opolskie", "Głubczyce"));
        voivodeshipToCities.put("Podkarpackie", Arrays.asList("Rzeszów", "Przemyśl", "Stalowa Wola", "Mielec", "Tarnobrzeg", "Krosno", "Sanok", "Jasło"));
        voivodeshipToCities.put("Podlaskie", Arrays.asList("Białystok", "Suwałki", "Łomża", "Augustów", "Bielsk Podlaski", "Zambrów", "Hajnówka", "Siemiatycze"));
        voivodeshipToCities.put("Pomorskie", Arrays.asList("Gdańsk", "Gdynia", "Słupsk", "Tczew", "Starogard Gdański", "Wejherowo", "Rumia", "Sopot"));
        voivodeshipToCities.put("Śląskie", Arrays.asList("Katowice", "Częstochowa", "Sosnowiec", "Gliwice", "Zabrze", "Bytom", "Bielsko-Biała", "Ruda Śląska"));
        voivodeshipToCities.put("Świętokrzyskie", Arrays.asList("Kielce", "Ostrowiec Świętokrzyski", "Starachowice", "Sandomierz", "Skarżysko-Kamienna", "Końskie", "Busko-Zdrój", "Jędrzejów"));
        voivodeshipToCities.put("Warmińsko-Mazurskie", Arrays.asList("Olsztyn", "Elbląg", "Ełk", "Ostróda", "Iława", "Kętrzyn", "Szczytno", "Giżycko"));
        voivodeshipToCities.put("Wielkopolskie", Arrays.asList("Poznań", "Kalisz", "Konin", "Piła", "Ostrów Wielkopolski", "Gniezno", "Leszno", "Śrem"));
        voivodeshipToCities.put("Zachodniopomorskie", Arrays.asList("Szczecin", "Koszalin", "Stargard", "Kołobrzeg", "Świnoujście", "Szczecinek", "Wałcz", "Białogard"));
    }
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

    private final ObservableList<ApartmentInfo> apartmentList = FXCollections.observableArrayList();
    public final ScraperApi scraperApi = new ScraperApi();

    public ScraperApi getScraperApi() {
        return this.scraperApi;
    }
    private final Scraper   scraper = new Scraper(
            PropertyType.APARTMENTS,
            PurchaseType.FOR_SALE,
            CityType.KRAKOW,
            VoivodeshipType.LESSER_POLAND,
            Limit.LIMIT_24
    );

    private final ScraperOlx scraperOlx = new ParserOlx(
            PropertyType.APARTMENTS_OLX,
            PurchaseType.FOR_SALE,
            CityType.KRAKOW,
            VoivodeshipType.LESSER_POLAND
    );


    @FXML
    public void initialize() {
        cancelLoadDataButton.setVisible(false);
        loadingLabel.setVisible(false);
        apartmentListView.setItems(apartmentList);
        initializeVoivodeshipCheckComboBox();
        initializeCityCheckComboBox();
        initializeFilterAndSortComboBoxes();
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


                            Text fromSite = new Text("From Site: " + item.getFromSite());
                            Text title = new Text("Title: " + item.getTitle());
                            Text voivodeship = new Text("Voivodeship: " + item.getVoivodeship());
                            Text city = new Text("City: " + item.getCity());
                            Text district = new Text("District: " + item.getDistrict());
                            Text street = new Text("Street: " + item.getStreet());
                            String priceInfo;
                            if (item.getPrice() < 0) {
                                priceInfo = "Price: Not defined.";
                            } else {
                               priceInfo = "Price: " + item.getPrice().toString() + " zł";
                            }
                            Text price = new Text(priceInfo);
                            Text pricePerMeter = new Text("Price per meter: " + item.getPricePerMeter().toString() + " zł/m²");
                            Text rooms = new Text("Rooms: " + item.getRooms().toString());
                            Text size = new Text("Size: " + item.getSize().toString() + " m²");
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

                            vbox.getChildren().addAll(fromSite, title, voivodeship, city, district, street, price, pricePerMeter, rooms, size, additionalInfo, link);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });

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

    @FXML
    protected void handleLoadData() {
        loadDataButton.setDisable(true);
        loadingLabel.setVisible(true);
        cancelLoadDataButton.setVisible(true);
        apartmentListView.setItems(null);
        loadingLabel.setText("Loading data... It can take some time depends on cities to get. You can go take some tea and when you will return it should be finished ;)");

        loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                scraperApi.createApartmentInfoArrayList();
                return null;
            }
        };

        loadDataTask.setOnSucceeded(e -> {
            apartmentList.clear();
            apartmentList.addAll(scraperApi.apartmentInfoArrayList);
            loadingLabel.setVisible(false);
            apartmentListView.setItems(apartmentList);
            loadDataButton.setDisable(false);
            cancelLoadDataButton.setVisible(false);
        });

        loadDataTask.setOnFailed(e -> {
            logger.error("Error during data loading: ", loadDataTask.getException());
            loadingLabel.setText("Error during data loading.");
            // Dodatkowa obsługa błędów, np. wyświetlenie komunikatu
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

