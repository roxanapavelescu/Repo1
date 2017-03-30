package roxana.eim.systems.cs.pub.ro.pt1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private static int serviceStatus = Constants.SERVICE_STOPPED;
    Button next = null;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    private IntentFilter intentFilter = new IntentFilter();

    private class OnClickL implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.button5:
                EditText e1 = (EditText) findViewById(R.id.textView4);
                int nr = Integer.parseInt(e1.getText().toString()) + 1;
                e1.setText(String.valueOf(nr));
                    break;
                case R.id.button6:
                    EditText e2 = (EditText) findViewById(R.id.textView5);
                    int nr2 = Integer.parseInt(e2.getText().toString()) + 1;
                    e2.setText(String.valueOf(nr2));
                    break;
                case R.id.button4:
                    EditText e11 = (EditText) findViewById(R.id.textView4);
                    EditText e22 = (EditText) findViewById(R.id.textView5);
                    Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                    int numberOfClicks = Integer.parseInt(e11.getText().toString()) +
                            Integer.parseInt(e22.getText().toString());
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }

            EditText ee1 = (EditText) findViewById(R.id.textView4);
            EditText ee2 = (EditText) findViewById(R.id.textView5);
            int leftNumberOfClicks = Integer.parseInt(ee1.getText().toString());
            int rightNumberOfClicks = Integer.parseInt(ee2.getText().toString());

            // ...

            if (leftNumberOfClicks + rightNumberOfClicks > 10
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PT1Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicks);
                intent.putExtra("secondNumber", rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    private OnClickL buttonClickListener = new OnClickL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button) findViewById(R.id.button4);

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(String.valueOf(Constants.actionTypes[index]));
        }

        EditText e1 = (EditText) findViewById(R.id.textView4);
        EditText e2 = (EditText) findViewById(R.id.textView5);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("button5")) {
                e1.setText(savedInstanceState.getString("button5"));
            } else {
                e1.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("button6")) {
                e2.setText(savedInstanceState.getString("button6"));
            } else {
                e2.setText(String.valueOf(0));
            }
        } else {
            e1.setText(String.valueOf(0));
            e2.setText(String.valueOf(0));
        }

        Button b1 = (Button) findViewById(R.id.button5);
        Button b2 = (Button) findViewById(R.id.button6);
        b1.setOnClickListener(buttonClickListener);
        b2.setOnClickListener(buttonClickListener);
        next.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        EditText e1 = (EditText) findViewById(R.id.textView4);
        EditText e2 = (EditText) findViewById(R.id.textView5);
        savedInstanceState.putString("button5", e1.getText().toString());
        savedInstanceState.putString("button6", e2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        EditText e1 = (EditText) findViewById(R.id.textView4);
        EditText e2 = (EditText) findViewById(R.id.textView5);
        if (savedInstanceState.containsKey("button5")) {
            e1.setText(savedInstanceState.getString("button5"));
        } else {
            e1.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("button6")) {
            e2.setText(savedInstanceState.getString("button6"));
        } else {
            e2.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PT1Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
