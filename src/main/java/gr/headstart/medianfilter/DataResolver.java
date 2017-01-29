package gr.headstart.medianfilter;

import java.util.List;
import javafx.util.Pair;

/**
 * Utility class, static methods to read numeric data that will be presented in
 * the UI
 *
 * @author KouziaMi
 */
public class DataResolver {

    /**
     * Use this method to read chart data in a List<Pair<Number, Number>> format
     *
     * @param type
     */
    public static List<Pair<Number, Number>> resolveData(SeriesType type) {
        final InputDataHolder holder = InputDataHolder.getInstance();
        switch (type) {
            case REAL:
                return holder.getRealData();
            case ESTIMATED:
                return holder.getEstimatedData();
            case MEDIAN:
                return MedianFilterCalculator.calculateFilter(holder.getEstimatedData(), holder.getSlidingWindow(), false);
            case WEIGHTED_MEDIAN:
                return MedianFilterCalculator.calculateFilter(holder.getEstimatedData(), holder.getSlidingWindow(), true);
            default:
                throw new RuntimeException("Unknown or unsupported chart data");
        }
    }

    /**
     * Use this method to read chart data in a List<Pair<Number, Number>> format
     */
    public static Number getEstimatedRealDistance() {
        final List<Pair<Number, Number>> realData = resolveData(SeriesType.REAL);
        final List<Pair<Number, Number>> estimatedData = resolveData(SeriesType.ESTIMATED);
        Number distance = MedianFilterCalculator.calculateEuclideanDistance(realData, estimatedData);
        return distance;
    }

    /**
     * Use this method to read the Median-Real distance for the stored data
     */
    public static Number getMedianRealDistance() {
        final List<Pair<Number, Number>> realData = resolveData(SeriesType.REAL);
        final List<Pair<Number, Number>> medianData = resolveData(SeriesType.MEDIAN);
        Number distance = MedianFilterCalculator.calculateEuclideanDistance(realData, medianData);
        return distance;
    }

    /**
     * Use this method to read the Weighted Median-Real distance for the stored
     * data
     */
    public static Number getWMedianRealDistance() {
        final List<Pair<Number, Number>> realData = resolveData(SeriesType.REAL);
        final List<Pair<Number, Number>> wMedianData = resolveData(SeriesType.WEIGHTED_MEDIAN);
        Number distance = MedianFilterCalculator.calculateEuclideanDistance(realData, wMedianData);
        return distance;
    }
}
