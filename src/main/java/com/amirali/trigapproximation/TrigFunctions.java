package com.amirali.trigapproximation;

public class TrigFunctions {

    private record FunctionValueResult(double value, boolean isNegative) {}

    public static double sin(double x) {
        var valueResult = optimizeInput(x, true);
        final var result = createSinTaylorPolynomials(valueResult.value(), 7);

        if (valueResult.isNegative())
            return -result;
        return result;
    }

    public static double cos(double x) {
        var valueResult = optimizeInput(x, false);
        final var result = createCosTaylorPolynomials(valueResult.value(), 7);

        if (valueResult.isNegative())
            return -result;
        return result;
    }

    public static double tan(double x) {
        final var sin = sin(x);
        final var cos = cos(x);

        if (cos == 0)
            throw new NotDefinedException("Tan of " + x + " is not defined");

        return sin/cos;
    }

    public static long factorial(int n) {
        if (n == 0)
            return 1;
        return n*factorial(n - 1);
    }

    private static FunctionValueResult optimizeInput(double x, boolean isSin) {
        var value = Math.abs(x);
        var isNegative = false;
        if (value > Math.PI/2 && value <= Math.PI) {
            value = Math.PI - value;
            if (!isSin)
                isNegative = true;
        } else if (value > Math.PI && value <= 1.5*Math.PI) {
            value = value - Math.PI;
            isNegative = true;
        } else if (value > 1.5*Math.PI && value <= 2*Math.PI) {
            value = 2*Math.PI - value;
            if (value != 0) {
                isNegative = isSin;
            }
        } else if (value > 2*Math.PI) {
            value = value - 2*Math.PI;
            return optimizeInput(x > 0 ? value : -value, isSin);
        }

        if (x < 0 && value != 0 && isSin)
            isNegative = !isNegative;

        return new FunctionValueResult(value, isNegative);
    }

    private static double createCosTaylorPolynomials(double value, int terms) {
        var result = 0.0;
        for (int n = 0; n < terms; n++) {
            result += Math.pow(-1, n)*(Math.pow(value, 2*n)/factorial(2*n));
        }

        return result;
    }

    private static double createSinTaylorPolynomials(double value, int terms) {
        var result = 0.0;
        for (int n = 0; n < terms; n++) {
            result += Math.pow(-1, n)*(Math.pow(value, 2*n + 1)/factorial(2*n + 1));
        }

        return result;
    }
}
