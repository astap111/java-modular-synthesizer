package com.synthesizer.javafx.util;

public class FeedbackFeedforwardFilter {
    private double[] coefficients;
    private double[] x;
    private double[] y;
    private int curPos;

    public FeedbackFeedforwardFilter(double... coefficients) {
        this.coefficients = coefficients;
        this.x = new double[coefficients.length];
        this.y = new double[coefficients.length];
        this.curPos = coefficients.length - 1;
    }

    public double result(double curX) {
        double result = coefficients[0] * curX;
        for (int i = 1; i < coefficients.length; i++) {
            double xi = i > curPos ? x[coefficients.length + curPos - i] : x[curPos - i];
            double yi = i > curPos ? y[coefficients.length + curPos - i] : y[curPos - i];

            result += coefficients[i] * xi;
            result -= coefficients[i] * yi;
        }

        x[curPos] = curX;
        y[curPos] = result;

        if (++curPos >= coefficients.length) {
            curPos = 0;
        }

        return result;
    }
}
