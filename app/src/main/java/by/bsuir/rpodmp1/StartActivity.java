package by.bsuir.rpodmp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class StartActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Timer timer = new Timer();
    private GifImageView loadingImage;
    private final int gifRepeats = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        try {
            Database.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadingImage = findViewById(R.id.LoadingImage);
        ((GifDrawable) loadingImage.getDrawable()).setLoopCount(gifRepeats);

        timer.schedule(
                new TimerTask() {
            @Override
            public void run() { handler.post(() -> checkAnimationEnding());
            }
        },
                0, 20);
    }

    private void checkAnimationEnding() {
        if (((GifDrawable) loadingImage.getDrawable()).isAnimationCompleted()) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }
}