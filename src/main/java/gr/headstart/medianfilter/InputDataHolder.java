package gr.headstart.medianfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import gr.headstart.medianfilter.data.Measurement;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 * Singleton utilized for loading and holding measurements from a
 * measurements.txt file and keeping the sliding window value It also holds the
 * state and updates some UI controls
 *
 * @author KouziaMi
 */
public class InputDataHolder {

    private static final Logger LOG = Logger.getLogger(InputDataHolder.class.getName());

    // Decimal format for parsing comma-separated decimal values
    private static final DecimalFormat DF = new DecimalFormat();

    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        DF.setDecimalFormatSymbols(otherSymbols);
    }

    // Singleton code
    private static final InputDataHolder instance = new InputDataHolder();

    private InputDataHolder() {
    }

    public static InputDataHolder getInstance() {
        return instance;
    }
    // End singleton code    

    private List<Measurement> measurements;
    private Integer slidingWindow;
    private StringProperty statusBarText;
    private BooleanProperty realMenu;
    private BooleanProperty estimatedMenu;
    private BooleanProperty medianFilterMenu;
    private BooleanProperty wMedianFilterMenu;
    private BooleanProperty estimatedRealMenu;
    private BooleanProperty medianRealMenu;
    private BooleanProperty wMedianRealMenu;
    private BooleanProperty allMenu;

    /**
     * Method for loading measurements data
     *
     * @param file
     */
    public void load(File file) {
        try {
            if (file.exists() && file.canRead()) {
                List<Measurement> tempMeas = new ArrayList<>();
                Scanner s = new Scanner(file);
                while (s.hasNextLine()) {
                    String[] values = s.nextLine().split("\\t");
                    Measurement m = new Measurement(
                            DF.parse(values[0]),
                            DF.parse(values[1]),
                            DF.parse(values[2]),
                            DF.parse(values[3]));
                    tempMeas.add(m);
                }
                measurements = tempMeas;
                updateUI();
            }
        } catch (FileNotFoundException | ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a list of measurements
     *
     * @return
     */
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    /**
     * Returns the sliding window length value
     *
     * @return
     */
    public Integer getSlidingWindow() {
        return slidingWindow;
    }

    /**
     * Sets the sliding window value
     *
     * @param slidingWindow
     */
    public void setSlidingWindow(Integer slidingWindow) {
        this.slidingWindow = slidingWindow;
        updateUI();
    }

    /**
     *
     * @return
     */
    public StringProperty getStatusBarText() {
        return statusBarText;
    }

    /**
     * Sets the status bar text
     *
     * @param statusBarText
     */
    public void setStatusBarText(StringProperty statusBarText) {
        this.statusBarText = statusBarText;
        updateUI();
    }

    /**
     * Updates the data related UI controls
     *
     */
    private void updateUI() {
        StringBuilder text = new StringBuilder();
        if (measurements == null) {
            text.append("No measurements loaded");
        } else {
            text.append(measurements.size()).append(" measurement(s) loaded");
        }
        text.append(", ");
        if (slidingWindow == null) {
            text.append("no sliding window defined");
        } else {
            text.append("sliding window of ").append(slidingWindow);
        }
        statusBarText.setValue(text.toString());

        if (measurements == null) {
            changeAllControls(true);
        } else if (measurements != null && slidingWindow == null) {
            enableDataRelatedControls();
        } else if (measurements != null && slidingWindow != null) {
            changeAllControls(false);
        }
    }

    /**
     * Enables/disables check menus
     */
    private void enableDataRelatedControls() {
        realMenu.setValue(false);
        estimatedMenu.setValue(false);
        medianFilterMenu.setValue(true);
        wMedianFilterMenu.setValue(true);
        estimatedRealMenu.setValue(false);
        medianRealMenu.setValue(true);
        wMedianRealMenu.setValue(true);
        allMenu.setValue(true);
    }

    /**
     * Enables/disables check menus
     */
    private void changeAllControls(boolean disable) {
        realMenu.setValue(disable);
        estimatedMenu.setValue(disable);
        medianFilterMenu.setValue(disable);
        wMedianFilterMenu.setValue(disable);
        estimatedRealMenu.setValue(disable);
        medianRealMenu.setValue(disable);
        wMedianRealMenu.setValue(disable);
        allMenu.setValue(disable);
    }

    /**
     * Returns a list of real location data
     *
     * @return List<Pair<Number, Number>>
     */
    public List<Pair<Number, Number>> getRealData() {
        if (measurements == null) {
            throw new RuntimeException("Measurements data has not been loaded yet!");
        }
        List<Pair<Number, Number>> realList = new ArrayList<>();
        for (Measurement m : measurements) {
            realList.add(m.getReal());
        }
        return realList;
    }

    /**
     * Returns a list of estimated(measured) location data
     *
     * @return List<Pair<Number, Number>>
     */
    public List<Pair<Number, Number>> getEstimatedData() {
        if (measurements == null) {
            throw new RuntimeException("Measurements data has not been loaded yet!");
        }
        List<Pair<Number, Number>> estimatedList = new ArrayList<>();
        for (Measurement m : measurements) {
            estimatedList.add(m.getMeasured());
        }
        return estimatedList;
    }

    /**
     * Enables/disables real check menu
     *
     * @param disableProperty
     */
    public void setRealMenu(BooleanProperty disableProperty) {
        realMenu = disableProperty;
    }

    /**
     * Enables/disables estimated check menu
     *
     * @param disableProperty
     */
    public void setEstimatedMenu(BooleanProperty disableProperty) {
        estimatedMenu = disableProperty;
    }

    /**
     * Enables/disables median filter check menu
     *
     * @param disableProperty
     */
    public void setMedianFilterMenu(BooleanProperty disableProperty) {
        medianFilterMenu = disableProperty;
    }

    /**
     * Enables/disables weighted median filter check menu
     *
     * @param disableProperty
     */
    public void setWMedianFilterMenu(BooleanProperty disableProperty) {
        wMedianFilterMenu = disableProperty;
    }

    /**
     * Enables/disables estimated-real menu
     *
     * @param estimatedRealMenu
     */
    public void setEstimatedRealMenu(BooleanProperty estimatedRealMenu) {
        this.estimatedRealMenu = estimatedRealMenu;
    }

    /**
     * Enables/disables median-real menu
     *
     * @param medianRealMenu
     */
    public void setMedianRealMenu(BooleanProperty medianRealMenu) {
        this.medianRealMenu = medianRealMenu;
    }

    /**
     * Enables/disables weighted median-real menu
     *
     * @param wMedianRealMenu
     */
    public void setwMedianRealMenu(BooleanProperty wMedianRealMenu) {
        this.wMedianRealMenu = wMedianRealMenu;
    }

    /**
     * Enables/disables all menu
     *
     * @param allMenu
     */
    public void setAllMenu(BooleanProperty allMenu) {
        this.allMenu = allMenu;
    }

}
