package roxana.eim.systems.cs.pub.ro.pt1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PT1Service extends Service {
    private ProcessingThread processingThread = null;
    public PT1Service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int firstNumber = intent.getIntExtra("firstNumber", -1);
        int secondNumber = intent.getIntExtra("secondNumber", -1);
        processingThread = new ProcessingThread(this, firstNumber, secondNumber);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }

}
