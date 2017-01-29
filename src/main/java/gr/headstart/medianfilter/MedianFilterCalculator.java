package gr.headstart.medianfilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.util.Pair;

/**
 * Utility class where all the calculations for filters and distances will take
 * place
 *
 * @author KouziaMi
 */
public class MedianFilterCalculator {

    /**
     * Calculate filter values for the given data
     *
     * @param originalValues
     * @param slidingWindow
     * @param weighted
     * @return
     */
    public static List<Pair<Number, Number>> calculateFilter(List<Pair<Number, Number>> originalValues, int slidingWindow, boolean weighted) {
        // do a few checks first 
        if (originalValues == null || originalValues.isEmpty()) {
            throw new RuntimeException("No measurement values provided");
        }

        final int originalSize = originalValues.size();
        if (slidingWindow > originalSize) {
            throw new RuntimeException("Sliding window cannot exceed the number of measurements provided");
        }

        List<Pair<Number, Number>> calculatedValues = new ArrayList<>();
        final int loops = originalSize;
        for (int i = 0; i < loops; i++) {
            //iterate through the list and get the windows
            int startIndex = i - slidingWindow + 1 > 0 ? i - slidingWindow + 1 : 0;
            List<Pair<Number, Number>> window = originalValues.subList(startIndex, i + 1);
            List<Number> xValues = getXValues(window, weighted);
            List<Number> yValues = getYValues(window, weighted);

            //Calculate medians for each window and store the values
            Number x = calculateMedian(xValues);
            Number y = calculateMedian(yValues);

            //Store the calculated value
            Pair<Number, Number> value = new Pair<>(x, y);
            calculatedValues.add(value);
        }
        return calculatedValues;
    }

    /**
     * Calculates the distance for the given values
     *
     * @param originalValues
     * @param estimatedValues
     * @return
     */
    public static Number calculateEuclideanDistance(List<Pair<Number, Number>> originalValues, List<Pair<Number, Number>> estimatedValues) {
        List<Number> distances = new ArrayList<>();
        int loops = estimatedValues.size();

        // find the distances for all points
        for (int i = 0; i < loops; i++) {
            double xOrig = originalValues.get(i).getKey().doubleValue();
            double xEst = estimatedValues.get(i).getKey().doubleValue();
            double yOrig = originalValues.get(i).getValue().doubleValue();
            double yEst = estimatedValues.get(i).getValue().doubleValue();
            double distance = Math.sqrt(Math.pow((xEst - xOrig), 2) + Math.pow((yEst - yOrig), 2));
            distances.add(distance);
        }

        //calculate the mean distance
        Number meanDistance = calculateMean(distances);
        return meanDistance;
    }

    /**
     * Returns a list of x axis values for the given window
     *
     * @param window
     * @param weighted
     * @return
     */
    private static List<Number> getXValues(List<Pair<Number, Number>> window, boolean weighted) {
        List<Number> values = new ArrayList<>();
        for (int i = 0; i < window.size(); i++) {
            final Number value = window.get(i).getKey();
            values.add(value);
            // Add the value as many times as the weight value dictates
            if (weighted) {
                for (int j = 0; j < i; j++) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    /**
     * Returns a list of y axis values for the given window
     *
     * @param window
     * @param weighted
     * @return
     */
    private static List<Number> getYValues(List<Pair<Number, Number>> window, boolean weighted) {
        List<Number> values = new ArrayList<>();
        for (int i = 0; i < window.size(); i++) {
            final Number value = window.get(i).getValue();
            values.add(value);
            // Add the value as many times as the weight value dictates
            if (weighted) {
                for (int j = 0; j < i; j++) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    /**
     * Calculates the median for the given unsorted list of numeric values
     *
     * @param values
     * @return
     */
    private static Number calculateMedian(List<Number> values) {
        //Sort the collection 
        Collections.sort(values, new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                Double d1 = (o1 == null) ? Double.POSITIVE_INFINITY : o1.doubleValue();
                Double d2 = (o2 == null) ? Double.POSITIVE_INFINITY : o2.doubleValue();
                return d1.compareTo(d2);
            }
        });

        final int size = values.size();
        if (size % 2 == 1) {
            //Odd
            return values.get((int) Math.ceil(size / 2));
        } else {
            //Even
            return (values.get(size / 2 - 1).doubleValue() + values.get(size / 2).doubleValue()) / 2;
        }
    }

    /**
     * Calculates the mean(average) of the given list of values
     *
     * @param values
     * @return
     */
    private static Number calculateMean(List<Number> values) {
        double sum = 0;
        for (Number value : values) {
            sum += value.doubleValue();
        }
        return sum / values.size();
    }
}
