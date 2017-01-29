/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.headstart.medianfilter.data;

/**
 * Enumeration of distance calculations. Used throughout the application
 *
 * @author KouziaMi
 */
public enum CalculationType {
    ESTIMATED_REAL("Estimated - Real", "Ctrl+Shift+E"),
    MEDIAN_REAL("Median-Real", "Ctrl+Shift+M"),
    WMEDIAN_REAL("Weighted Median-Real", "Ctrl+Shift+W"),
    ALL("All", "Ctrl+Shift+A");

    // Title as presented in menu
    final private String title;
    // Keyboard shortcut for invoking the calculation
    final private String accelerator;

    CalculationType(String title, String accelerator) {
        this.title = title;
        this.accelerator = accelerator;
    }

    public String getTitle() {
        return title;
    }

    public String getAccelerator() {
        return accelerator;
    }
}
