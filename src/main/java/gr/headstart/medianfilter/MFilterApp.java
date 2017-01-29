package gr.headstart.medianfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import gr.headstart.medianfilter.data.CalculationType;
import gr.headstart.medianfilter.data.Result;
import gr.headstart.medianfilter.ui.AcceleratedCheckMenuItem;
import gr.headstart.medianfilter.ui.ResultDialog;

/**
 * Main javafx application, most ui and menu initialization takes place here
 *
 * @author KouziaMi
 */
public class MFilterApp extends Application {

    /**
     * Executes when starting application
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        //root.getChildren().add(btn);

        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("style.css");

        initMenus(primaryStage, root);

        primaryStage.setTitle("Median Filter Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initialization of menu bars and menu items
     *
     * @param primaryStage
     * @param root
     */
    private void initMenus(final Stage primaryStage, BorderPane root) {
        //Menubar initialization
        MenuBar menuBar = new MenuBar();

        //File menu initialization
        Menu menuFile = new Menu("File");
        MenuItem openFile = new MenuItem("Open measurements.txt");
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Measurements File");
                    File workingDirectory = new File(System.getProperty("user.home"));
                    fileChooser.setInitialDirectory(workingDirectory);
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        InputDataHolder.getInstance().load(file);
                    }
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });

        //Sliding window menu initialization
        MenuItem setSWindow = new MenuItem("Set Sliding Window");
        setSWindow.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        setSWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
//                String input = Dialogs.showInputDialog(primaryStage, "Sliding window range:", "Please enter a range of the sliding window", "Sliding window configuration");
                String input = readInput("Sliding window range:", "Please enter a range of the sliding window", "Sliding window configuration");

                try {
                    Integer sw = Integer.parseInt(input);
                    InputDataHolder.getInstance().setSlidingWindow(sw);
                } catch (NumberFormatException nfe) {
                    showError("Invalid entry, you can only enter an integer");
                }
            }
        });

        menuFile.getItems().addAll(openFile, setSWindow);

        //Visualization menu initialization
        Menu menuVis = new Menu("Visualization");

        //Real path menu initialization
        final AcceleratedCheckMenuItem realPath = new AcceleratedCheckMenuItem("Real Path (Black)");
        realPath.disableProperty().setValue(Boolean.FALSE);
        realPath.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        realPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                boolean isSelected = realPath.toggleCheckStatus();
                if (isSelected) {
                    ChartDesigner.getInstance().addOrReplaceSeries(SeriesType.REAL);
                } else {
                    ChartDesigner.getInstance().removeSeries(SeriesType.REAL);
                }
            }
        });
        InputDataHolder.getInstance().setRealMenu(realPath.disableProperty());

        //Estimated path menu initialization
        final AcceleratedCheckMenuItem estimatedPath = new AcceleratedCheckMenuItem("Estimated Path (Blue)");
        estimatedPath.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        estimatedPath.disableProperty().setValue(Boolean.FALSE);
        estimatedPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                boolean isSelected = estimatedPath.toggleCheckStatus();
                if (isSelected) {
                    ChartDesigner.getInstance().addOrReplaceSeries(SeriesType.ESTIMATED);
                } else {
                    ChartDesigner.getInstance().removeSeries(SeriesType.ESTIMATED);
                }
            }
        });
        InputDataHolder.getInstance().setEstimatedMenu(estimatedPath.disableProperty());

        //Median filter menu initialization        
        final AcceleratedCheckMenuItem medianFilter = new AcceleratedCheckMenuItem("Median Filter (Green)");
        medianFilter.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
        medianFilter.disableProperty().setValue(Boolean.FALSE);
        medianFilter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                boolean isSelected = medianFilter.toggleCheckStatus();
                if (isSelected) {
                    ChartDesigner.getInstance().addOrReplaceSeries(SeriesType.MEDIAN);
                } else {
                    ChartDesigner.getInstance().removeSeries(SeriesType.MEDIAN);
                }
            }
        });
        InputDataHolder.getInstance().setMedianFilterMenu(medianFilter.disableProperty());

        //Weighted median filter menu initialization
        final AcceleratedCheckMenuItem wMedianFilter = new AcceleratedCheckMenuItem("Weighted Median (Red)");
        wMedianFilter.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        wMedianFilter.disableProperty().setValue(Boolean.FALSE);
        wMedianFilter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                boolean isSelected = wMedianFilter.toggleCheckStatus();
                if (isSelected) {
                    ChartDesigner.getInstance().addOrReplaceSeries(SeriesType.WEIGHTED_MEDIAN);
                } else {
                    ChartDesigner.getInstance().removeSeries(SeriesType.WEIGHTED_MEDIAN);
                }
            }
        });
        InputDataHolder.getInstance().setWMedianFilterMenu(wMedianFilter.disableProperty());
        menuVis.getItems().addAll(realPath, estimatedPath, new SeparatorMenuItem(), medianFilter, wMedianFilter);

        //Average Deviation/Euclidean Distance menu initialization
        Menu menuCalc = new Menu("Average Deviation/Euclidean Distance");

        //Estimated-Real distance menu initialization
        MenuItem estimated = new MenuItem(CalculationType.ESTIMATED_REAL.getTitle());
        estimated.setAccelerator(KeyCombination.keyCombination(CalculationType.ESTIMATED_REAL.getAccelerator()));
        estimated.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Number distance = DataResolver.getEstimatedRealDistance();
                ResultDialog.showResults(primaryStage.getScene().getWindow(), Arrays.asList(new Result(CalculationType.ESTIMATED_REAL, distance)));
            }
        });
        InputDataHolder.getInstance().setEstimatedRealMenu(estimated.disableProperty());

        //Median-Real distance menu initialization
        MenuItem median = new MenuItem(CalculationType.MEDIAN_REAL.getTitle());
        median.setAccelerator(KeyCombination.keyCombination(CalculationType.MEDIAN_REAL.getAccelerator()));
        median.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Number distance = DataResolver.getMedianRealDistance();
                ResultDialog.showResults(primaryStage.getScene().getWindow(), Arrays.asList(new Result(CalculationType.MEDIAN_REAL, distance)));
            }
        });
        InputDataHolder.getInstance().setMedianRealMenu(median.disableProperty());

        //Weighted Median-Real distance menu initialization
        MenuItem weightedMedian = new MenuItem(CalculationType.WMEDIAN_REAL.getTitle());
        weightedMedian.setAccelerator(KeyCombination.keyCombination(CalculationType.WMEDIAN_REAL.getAccelerator()));
        weightedMedian.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Number distance = DataResolver.getWMedianRealDistance();
                ResultDialog.showResults(primaryStage.getScene().getWindow(), Arrays.asList(new Result(CalculationType.WMEDIAN_REAL, distance)));
            }
        });
        InputDataHolder.getInstance().setwMedianRealMenu(weightedMedian.disableProperty());

        //All menu initialization        
        MenuItem all = new MenuItem(CalculationType.ALL.getTitle());
        all.setAccelerator(KeyCombination.keyCombination(CalculationType.ALL.getAccelerator()));
        all.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                List<Result> results = new ArrayList<>();
                results.add(new Result(CalculationType.ESTIMATED_REAL, DataResolver.getEstimatedRealDistance()));
                results.add(new Result(CalculationType.MEDIAN_REAL, DataResolver.getMedianRealDistance()));
                results.add(new Result(CalculationType.WMEDIAN_REAL, DataResolver.getWMedianRealDistance()));
                ResultDialog.showResults(primaryStage.getScene().getWindow(), results);
            }
        });
        InputDataHolder.getInstance().setAllMenu(all.disableProperty());

        menuCalc.getItems().addAll(estimated, median, weightedMedian, new SeparatorMenuItem(), all);
        menuBar.getMenus().addAll(menuFile, menuVis, menuCalc);
        root.setTop(menuBar);

        // Chart initialization
        final NumberAxis longitudeAxis = new NumberAxis();
        final NumberAxis latitudeAxis = new NumberAxis();
        longitudeAxis.setLabel("x");
        latitudeAxis.setLabel("y");

        final LineChart<Number, Number> lineChart
                = new LineChart<>(longitudeAxis, latitudeAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        ObjectProperty<ObservableList<Series<Number, Number>>> data = lineChart.dataProperty();
        ChartDesigner.getInstance().setData(data);
        root.setCenter(lineChart);

        // Status bar initialization
        Label statusBar = new Label();
        StringProperty statusBarText = statusBar.textProperty();
        InputDataHolder.getInstance().setStatusBarText(statusBarText);

        root.setBottom(statusBar);
    }

    private String readInput(String prompt, String header, String title) {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(prompt);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
    }

    /**
     * Application startup
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
