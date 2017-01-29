package gr.headstart.medianfilter;

/**
 * Enumeration of series types used throughout the application
 *
 * @author KouziaMi
 */
public enum SeriesType {
    REAL("Real Path", "real"),
    ESTIMATED("Estimated Path", "estimated"),
    MEDIAN("Median Filter", "median"),
    WEIGHTED_MEDIAN("Weighted Median", "weightedMedian");

    // name used in menus
    private final String name;
    // style name used when designing the lines(defined in style.css file)
    private final String style;

    private SeriesType(String name, String style) {
        this.name = name;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }
}
