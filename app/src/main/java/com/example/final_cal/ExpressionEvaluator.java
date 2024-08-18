package com.example.final_cal;

public class ExpressionEvaluator {
    public double evaluate(String expression) {
        return new ExpressionParser().parse(expression);
    }
}
