package com.example.final_cal;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {

    private final Map<String, Double> constants = new HashMap<>();

    public ExpressionParser() {
        // Initialize constants, if needed
        constants.put("PI", Math.PI);
        constants.put("E", Math.E);
    }

    public double parse(String expression) {
        try {
            return evaluate(expression);
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    private double evaluate(String expression) {
        expression = expression.replaceAll("\\s+", ""); // Remove all whitespace

        // Handle constants
        for (Map.Entry<String, Double> entry : constants.entrySet()) {
            expression = expression.replace(entry.getKey(), entry.getValue().toString());
        }

        // Convert infix expression to postfix
        String postfix = infixToPostfix(expression);

        // Evaluate postfix expression
        return evaluatePostfix(postfix);
    }

    private String infixToPostfix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<String> ops = new Stack<>();

        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?|[+*/()-]|sin|cos|tan|sqrt|log|ln|pow");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String token = matcher.group();

            if (token.matches("\\d+(\\.\\d+)?")) {
                result.append(token).append(" ");
            } else if (token.equals("sin") || token.equals("cos") || token.equals("tan") ||
                    token.equals("sqrt") || token.equals("log") || token.equals("ln")) {
                ops.push(token);
            } else if (token.equals("(")) {
                ops.push("(");
            } else if (token.equals(")")) {
                while (!ops.peek().equals("(")) {
                    result.append(ops.pop()).append(" ");
                }
                ops.pop(); // Remove '('
                if (!ops.isEmpty() && (ops.peek().equals("sin") || ops.peek().equals("cos") ||
                        ops.peek().equals("tan") || ops.peek().equals("sqrt") ||
                        ops.peek().equals("log") || ops.peek().equals("ln"))) {
                    result.append(ops.pop()).append(" ");
                }
            } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(String.valueOf(token.charAt(0)))) {
                    result.append(ops.pop()).append(" ");
                }
                ops.push(String.valueOf(token.charAt(0)));
            }
        }

        while (!ops.isEmpty()) {
            result.append(ops.pop()).append(" ");
        }

        return result.toString();
    }

    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.matches("[+*/-]")) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOp(token.charAt(0), b, a));
            } else if (token.equals("sin") || token.equals("cos") || token.equals("tan") ||
                    token.equals("sqrt") || token.equals("log") || token.equals("ln")) {
                double a = stack.pop();
                stack.push(applyFunction(token, a));
            } else if (token.equals("pow")) {
                double exponent = stack.pop();
                double base = stack.pop();
                stack.push(Math.pow(base, exponent));
            }
        }

        return stack.pop();
    }

    private int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3; // Exponentiation has higher precedence
        }
        return -1;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    private double applyFunction(String function, double value) {
        switch (function) {
            case "sin":
                return Math.sin(Math.toRadians(value)); // Convert degrees to radians
            case "cos":
                return Math.cos(Math.toRadians(value)); // Convert degrees to radians
            case "tan":
                return Math.tan(Math.toRadians(value)); // Convert degrees to radians
            case "sqrt":
                return Math.sqrt(value);
            case "log":
                return Math.log10(value);
            case "ln":
                return Math.log(value);
            default:
                throw new UnsupportedOperationException("Function not supported");
        }
    }
}
