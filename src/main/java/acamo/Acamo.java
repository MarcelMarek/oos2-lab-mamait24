package acamo;

import de.saring.leafletmap.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jsonstream.PlaneDataServer;
import messer.BasicAircraft;
import messer.Messer;
import observer.Observer;
import senser.Senser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class Acamo extends Application implements Observer<BasicAircraft> {
    private TableView<BasicAircraft> table = new TableView<BasicAircraft>();
    private static double latitude = 48.7433;
    private static double longitude = 9.3201;
    private ActiveAircrafts activeAircrafts = new ActiveAircrafts();
    private ArrayList<String> fields;
    private BasicAircraft selectedAircraft = null;
    private ObservableList<BasicAircraft> list = FXCollections.observableArrayList();
    private CompletableFuture<Worker.State> loadState;
    private Marker marker;
    private MapConfig mapconf;
    private HashMap<String, Marker> mapMarker = new HashMap<String, Marker>();
    private Label icao = new Label("-");
    private Label operator = new Label("-");
    private Label posTime = new Label("-");
    private Label coordinate = new Label("-");
    private Label speed = new Label("-");
    private Label track = new Label("-");
    private LeafletMapView map;
    int selectedIndex;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage arg0) {

        String urlString = "https://opensky-network.org/api/states/all";
        PlaneDataServer server;

        server = new PlaneDataServer(urlString, latitude, longitude, 50);

        Senser senser = new Senser(server);
        new Thread(server).start();
        new Thread(senser).start();

        Messer messer = new Messer();
        new Thread(messer).start();

        senser.addObserver(messer);
        messer.addObserver((Observer<BasicAircraft>) activeAircrafts);
//        messer.addObserver(this);

        fields = BasicAircraft.getAttributesNames();
        for (int i = 0; i < fields.size(); i++) {
            TableColumn<BasicAircraft, String> tableColumn = new TableColumn<BasicAircraft, String>(fields.get(i));
            tableColumn.setCellValueFactory(new PropertyValueFactory<BasicAircraft, String>(fields.get(i)));
            table.getColumns().addAll(tableColumn);

        }

        table.setEditable(false);
        table.autosize();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            processSelection((BasicAircraft) newSelection);
        });

        //*****
        BorderPane outborder = new BorderPane();
        BorderPane border = new BorderPane();
        BorderPane border2 = new BorderPane();

        HBox tableleft = new HBox();
        tableleft.getChildren().add(table);

        HBox titlebox = new HBox();
        Label title = new Label("Active Aircrafts");
        title.setFont(Font.font("Arial", 18));
        titlebox.getChildren().add(title);
        border.setTop(titlebox);

        VBox test = new VBox();
        Label title2 = new Label("Selected		...");
        title2.setFont(Font.font("Arial", 18));
        test.getChildren().add(title2);
        Label l1 = new Label("icao");
        Label l2 = new Label("operator");
        Label l3 = new Label("posTime");
        Label l4 = new Label("coordinate");
        Label l5 = new Label("speed");
        Label l6 = new Label("Track");

        test.getChildren().add(l1);
        test.getChildren().add(l2);
        test.getChildren().add(l3);
        test.getChildren().add(l4);
        test.getChildren().add(l5);
        test.getChildren().add(l6);
        tableleft.getChildren().add(test);

        VBox data = new VBox();
        Label title3 = new Label("		Aircraft:");
        title3.setFont(Font.font("Arial", 18));
        data.getChildren().add(title3);

        data.getChildren().add(icao);
        data.getChildren().add(operator);
        data.getChildren().add(posTime);
        data.getChildren().add(coordinate);
        data.getChildren().add(speed);
        data.getChildren().add(track);
        tableleft.getChildren().add(data);

        map = new LeafletMapView();
        map.setLayoutX(0);
        map.setLayoutY(0);
        List<MapLayer> config = new LinkedList<>();
        config.add(MapLayer.OPENSTREETMAP);

        mapconf = new MapConfig(config, new ZoomControlConfig(), new ScaleControlConfig(),
                new LatLong(latitude, longitude));

        loadState = map.displayMap(mapconf);

        loadState.whenComplete((state, throwable) -> {
            marker = new Marker(new LatLong(latitude, longitude), "Home", "Home", 0);
            map.addCustomMarker("Home", "icons/basestation.png");
            map.addMarker(marker);

            for (int i = 0; i <= 25; i++) {
                String number = String.format("%02d", i);
                map.addCustomMarker("plane" + number, "icons/plane" + number + ".png");
            }

            map.onMapClick((LatLong latlong) -> {
                markerChange(latlong.getLatitude(), longitude = latlong.getLongitude(), 150, server);
            });
        });

        HBox buttons = new HBox();

        Label latitude = new Label("Latitude: ");
        TextField inputLat = new TextField();
        buttons.getChildren().add(latitude);
        buttons.getChildren().add(inputLat);

        Label space = new Label("			");
        buttons.getChildren().add(space);

        Label longitude = new Label("Longitude: ");
        TextField inputLong = new TextField();
        buttons.getChildren().add(longitude);
        buttons.getChildren().add(inputLong);

        Label space2 = new Label("		");
        buttons.getChildren().add(space2);

        Button submit = new Button("Submit");
        buttons.getChildren().add(submit);

        submit.setOnAction(e -> {
            this.markerChange(Double.parseDouble(inputLat.getText()), Double.parseDouble(inputLong.getText()), 100, server);
        });

        border2.setTop(map);
        border2.setCenter(buttons);
        outborder.setLeft(border2);
        border.setLeft(tableleft);
        outborder.setRight(border);

        arg0.setScene(new Scene(outborder, 1600, 900));
        arg0.show();

        //*****

    }

    private void markerChange(double latitude, double longitude, int distance, PlaneDataServer s) {

        for (Marker mark : mapMarker.values()) {
            map.removeMarker(mark);
        }
        marker.move(new LatLong(latitude, longitude));
        map.panTo(new LatLong(latitude, longitude));
        this.mapMarker.clear();
        s.resetLocation(latitude, longitude, distance);
        list.clear();

    }

    private void processSelection(BasicAircraft aircraft) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectedAircraft = aircraft;
                if (aircraft != null) {
                    icao.setText("			" + aircraft.getIcao());
                    operator.setText("			" + aircraft.getOperator());
                    posTime.setText("			" + aircraft.getPosTime().toString());
                    coordinate.setText("			" + aircraft.getCoordinate().toString());
                    speed.setText("			" + Double.toString(aircraft.getSpeed()));
                    track.setText("			" + Double.toString(aircraft.getTrak()));
                }
            }
        });
    }

    private ObservableList<BasicAircraft> updateList(BasicAircraft newAircraft) {
        ObservableList<BasicAircraft> list = FXCollections.observableArrayList(activeAircrafts.values());
        list.add(newAircraft);
        return list;
    }


    // TODO: When messer updates Acamo (and activeAircrafts) the aircraftList must be updated as well
    @Override
    public void update(observer.Observable<BasicAircraft> observable, BasicAircraft newAircraft) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list = updateList(newAircraft);
                table.setItems(list);

                list.clear();
                list.addAll(activeAircrafts.values());

                for (BasicAircraft plane : activeAircrafts.values()) {
                    LatLong pos = new LatLong(plane.getCoordinate().getLongitude(),
                            plane.getCoordinate().getLatitude());
                    int dir = (int) plane.getTrak();
                    String icon = "plane" + String.format("%02d", dir / 15);
                    String icao = plane.getIcao();
                    if (mapMarker.containsKey(icao)) {
                        Marker ac = mapMarker.get(icao);
                        ac.move(pos);
                        ac.changeIcon(icon);
                    } else {
                        loadState.whenComplete((state, throwable) -> {
                            Marker acr = new Marker(pos, icao, icon, 0);
                            mapMarker.put(icao, acr);
                            map.addMarker(acr);
                        });
                    }
                }
            }

        });

    }
}

