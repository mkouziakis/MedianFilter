/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.headstart.medianfilter.data;

import javafx.util.Pair;

/**
 * Data object, used to store measurement file records
 *
 * @author KouziaMi
 */
public class Measurement {

    // Real value
    private Pair<Number, Number> real;
    // Measured value
    private Pair<Number, Number> measured;

    public Measurement(Number realX, Number realY, Number measuredX, Number measuredY) {
        real = new Pair(realX, realY);
        measured = new Pair(measuredX, measuredY);
    }

    public Pair<Number, Number> getReal() {
        return real;
    }

    public void setReal(Pair<Number, Number> real) {
        this.real = real;
    }

    public Pair<Number, Number> getMeasured() {
        return measured;
    }

    public void setMeasured(Pair<Number, Number> measured) {
        this.measured = measured;
    }
}
