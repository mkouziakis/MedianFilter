/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.headstart.medianfilter.data;

/**
 * Data object used to store distance calculation results
 *
 * @author KouziaMi
 */
public class Result {

    private CalculationType calculationType;
    private Number result;

    public Result(CalculationType calculationType, Number result) {
        this.calculationType = calculationType;
        this.result = result;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType resultType) {
        this.calculationType = resultType;
    }

    public Number getResult() {
        return result;
    }

    public void setResult(Number result) {
        this.result = result;
    }
}
