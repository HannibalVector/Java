package hr.fer.zemris.java.student0177035204.hw16_0177035204;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Performs selected operation using data provided through intent.
 */
public class CalculusActivity extends AppCompatActivity {
    /** Label used to display result. */
    private TextView tvLabel;
    /** Button used to return to caller activity. */
    private Button btnReturn;
    /** Intent used to send result and error description back to caller activity. */
    private Intent i = new Intent();
    /** Result of operation. */
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);

        tvLabel = (TextView) findViewById(R.id.tvLabel);
        btnReturn = (Button) findViewById(R.id.btnReturn);

        Bundle bundle = getIntent().getExtras();

        Operations operation = (Operations) bundle.get(HostActivity.EXTRAS_OPER);

        setTitle(operation.nameID());

        try {
            int valueA = bundle.getInt(HostActivity.EXTRAS_VALA, 0);
            int valueB = bundle.getInt(HostActivity.EXTRAS_VALB, 0);


            result = operation.getOperation().performOperation(valueA, valueB);

            tvLabel.setText(getString(R.string.result_is) + String.valueOf(result) + ".");
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();

            i.putExtra(HostActivity.EXTRAS_ERR, errorMessage);

            TextView tvError = (TextView) findViewById(R.id.tvError);
            tvError.setText(getString(R.string.error) + ": " + errorMessage);
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra(HostActivity.EXTRAS_RES, result);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
