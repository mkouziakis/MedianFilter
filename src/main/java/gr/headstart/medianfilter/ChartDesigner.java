package gr.headstart.medianfilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

/**
 * Singleton chart designer
 *
 * @author KouziaMi
 */
public class ChartDesigner {

    // Singleton code
    private static final ChartDesigner instance = new ChartDesigner();

    private ChartDesigner() {
    }

    public static ChartDesigner getInstance() {
        return instance;
    }
    // End singleton code    

    private ObjectProperty<ObservableList<XYChart.Series<Number, Number>>> data;
    private final Map<SeriesType, List<Pair<Number, Number>>> series = new HashMap<>();

    /**
     * Returns the data property
     *
     * @return ObjectProperty
     */
    public ObjectProperty getData() {
        return data;
    }

    /**
     * Sets the data property
     *
     * @param data
     */
    public void setData(ObjectProperty data) {
        this.data = data;
    }

    /**
     * Adds or replaces series by resolving the data
     *
     * @param seriesType
     */
    public void addOrReplaceSeries(SeriesType seriesType) {
        List<Pair<Number, Number>> data = DataResolver.resolveData(seriesType);
        series.put(seriesType, data);
        redesign();
    }

    /**
     * Removes selected series
     *
     * @param seriesType
     */
    public void removeSeries(SeriesType seriesType) {
        series.remove(seriesType);
        redesign();
    }

    /**
     * Redesigns the chart
     */
    private void redesign() {
        // clear everything first
        data.get().clear();

        // rebuild observable lists
        Map<SeriesType, XYChart.Series<Number, Number>> seriesList = new HashMap<>();
        for (SeriesType type : series.keySet()) {
            List<Pair<Number, Number>> list = series.get(type);
            XYChart.Series<Number, Number> xyChartData = convertToSeries(list, type);
            xyChartData.setName(type.getName());
            data.get().add(xyChartData);
            seriesList.put(type, xyChartData);
        }

        for (SeriesType type : seriesList.keySet()) {
            // line style
            XYChart.Series<Number, Number> xyChartData = seriesList.get(type);
            Node line = xyChartData.getNode().lookup(".chart-series-line");
            line.getStyleClass().add(type.getStyle());

            // point style
            ObservableList<XYChart.Data<Number, Number>> dataList = xyChartData.getData();
            for (XYChart.Data<Number, Number> point : dataList) {
                point.getNode().getStyleClass().add(type.getStyle() + "-symbol");
            }
        }
    }

    /**
     * Converts the data list to a Series object that will be used for designing
     * the actual chart
     *
     * @param list
     * @param type
     * @return
     */
    private XYChart.Series<Number, Number> convertToSeries(List<Pair<Number, Number>> list, SeriesType type) {
        XYChart.Series series = new XYChart.Series();
        for (Pair p : list) {
            final XYChart.Data point = new XYChart.Data(p.getKey(), p.getValue());
            series.getData().add(point);
        }
        return series;
    }
}
