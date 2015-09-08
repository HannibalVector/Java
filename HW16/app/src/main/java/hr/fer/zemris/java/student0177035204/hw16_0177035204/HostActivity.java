package hr.fer.zemris.java.student0177035204.hw16_0177035204;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Application's launcher activity. Provides user interface for entering operation arguments,
 * choosing operation and executing commands.
 */
public class HostActivity extends AppCompatActivity {
    /** Label used to display operation result. */
    private TextView tvResult;
    /** Label used to display error. */
    private TextView tvError;
    /** Text field used to input first argument. */
    private EditText etVariableA;
    /** Text field used to input second argument. */
    private EditText etVariableB;
    /** Button that triggers calculation. */
    private Button btnCalculate;
    /** Button that triggers phone call. */
    private Button btnCall;
    /** Button that triggers opening web browser and searching Google for result. */
    private Button btnView;
    /** Button that triggers sending of the report by e-mail. */
    private Button btnReport;
    /** Maps radio buttons to operations. */
    private Map<RadioButton, Operations> operationsMap = new HashMap<>();
    /** Selected operation. */
    private Operations selectedOperation;
    /** Value of the first operand. */
    private int valueA;
    /** Value of the second operand. */
    private int valueB;
    /** Value of the result. */
    private int result;
    /** Error message. */
    private String errorMessage;

    /** Key for the first operand. */
    public static final String EXTRAS_VALA = "VALA";
    /** Key for the second operand. */
    public static final String EXTRAS_VALB = "VALB";
    /** Key for the operation result. */
    public static final String EXTRAS_RES = "RESULT";
    /** Key for the selected operation. */
    public static final String EXTRAS_OPER = "OPERATION";
    /** Key for the error message. */
    public static final String EXTRAS_ERR = "ERROR";

    /** E-mails to send the report to. */
    private static final String[] emails = new String[]{"ana.baotic@infinum.hr"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvError = (TextView) findViewById(R.id.tvError);
        etVariableA = (EditText) findViewById(R.id.etFirstVariable);
        etVariableB = (EditText) findViewById(R.id.etSecondVariable);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnView = (Button) findViewById(R.id.btnView);
        btnReport = (Button) findViewById(R.id.btnReport);

        RadioButton rbAdd = (RadioButton) findViewById(R.id.rbAdd);
        RadioButton rbSubtract = (RadioButton) findViewById(R.id.rbSubtract);
        RadioButton rbMultiply = (RadioButton) findViewById(R.id.rbMultiply);
        RadioButton rbDivide = (RadioButton) findViewById(R.id.rbDivide);

        operationsMap.put(rbAdd, Operations.ADD);
        operationsMap.put(rbSubtract, Operations.SUBTRACT);
        operationsMap.put(rbMultiply, Operations.MULTIPLY);
        operationsMap.put(rbDivide, Operations.DIVIDE);

        RadioGroup rgOperation = (RadioGroup) findViewById(R.id.rgOperation);
        rgOperation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedOperation = operationsMap.get(findViewById(checkedId));
                btnReport.setEnabled(false);
            }
        });

        rbAdd.setChecked(true);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVariables() == Status.FAIL) {
                    return;
                }

                Intent i = new Intent(
                        HostActivity.this,
                        CalculusActivity.class);

                i.putExtra(EXTRAS_VALA, valueA);
                i.putExtra(EXTRAS_VALB, valueB);
                i.putExtra(EXTRAS_OPER, selectedOperation);

                startActivityForResult(i, 666);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:011234567"));
                startActivity(i);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVariables() == Status.FAIL) {
                    return;
                }

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://www.google.hr/?#safe=off&q="
                                + Uri.encode(
                                    valueA
                                            + selectedOperation.getSymbol()
                                            + valueB)
                ));

                startActivity(i);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, emails);
                    email.putExtra(Intent.EXTRA_SUBJECT, "0177035204: dz report");
                    email.setType("plain/text");
                    String mailText =
                            "Rezultat operacije "
                                    + valueA + " "
                                    + selectedOperation.getSymbol()
                                    + " " + valueB + " je " +
                                    +result + ".";

                    if (errorMessage != null) {
                        mailText +=
                                "\nIzvođenje je bilo neuspješno, uzrok: " +
                                        errorMessage + ".";
                    }

                    email.putExtra(Intent.EXTRA_TEXT, mailText);
                    startActivity(email);
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666 && resultCode == RESULT_OK && data != null) {

            result = data.getIntExtra(EXTRAS_RES, 0);
            errorMessage = data.getStringExtra(EXTRAS_ERR);

            if (errorMessage == null) {
                tvResult.setText(String.valueOf(result));
            } else {
                tvError.setText(getString(R.string.error) + ": " + errorMessage);
                result = 0;
                tvResult.setText(String.valueOf(result));
            }

            btnReport.setEnabled(true);
        }
    }

    private Status getVariables() {
        String varA = etVariableA.getText().toString();
        String varB = etVariableB.getText().toString();

        try {
            valueA = Integer.parseInt(varA);
            valueB = Integer.parseInt(varB);
            return Status.SUCCESS;
        } catch (Exception ex) {
            displayError(getString(R.string.enter_args_message));
            return Status.FAIL;
        }
    }

    /**
     * Displays error message on pop-up dialog.
     * @param errorMessage message to be displayed.
     */
    private void displayError(String errorMessage) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(HostActivity.this);

        dlgAlert.setMessage(errorMessage);
        dlgAlert.setTitle(getString(R.string.error));
        dlgAlert.setPositiveButton(R.string.ok, null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    /**
     * Marks if input of variables was successful.
     */
    private enum Status {
        SUCCESS, FAIL
    }
}
