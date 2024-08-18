package com.example.final_cal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private EditText editTextOutput;
    private StringBuilder expression = new StringBuilder();
    private ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        editTextOutput = findViewById(R.id.editTextOutput);
        setupButtons();
    }

    private void setupButtons() {
        int[] buttonIds = {
                R.id.button_clear, R.id.button_mod, R.id.button_divide, R.id.button_multiply,
                R.id.button_sin, R.id.button_cos, R.id.button_tan, R.id.button_subtract,
                R.id.button_sqrt, R.id.button_pow, R.id.button_add, R.id.button_log,
                R.id.button_ln, R.id.button_equals,
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(this::onButtonClick);
        }
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        if (buttonText.equals("C")) {
            expression.setLength(0);
            editTextOutput.setText("");
        } else if (buttonText.equals("=")) {
            calculateResult();
            return;
        } else {
            expression.append(buttonText);
        }
        updateInputDisplay();
    }

    private void updateInputDisplay() {
        editTextInput.setText(expression.toString());
    }

    private void calculateResult() {
        try {
            String exp = expression.toString();
            double result = evaluator.evaluate(exp);
            String output = exp + " = " + result;
            editTextOutput.setText(output);
            expression.setLength(0);  // Clear the expression
        } catch (Exception e) {
            editTextOutput.setText("Error");
            expression.setLength(0);
        }
        updateInputDisplay();
    }
}
