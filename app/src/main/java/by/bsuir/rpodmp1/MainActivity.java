package by.bsuir.rpodmp1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static UserInfo currentUser;

    private GoogleSignInClient googleSignInClient;

    private long satiety = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutItem:
                signOut();
                return true;
            case R.id.scoreItem:
                Intent statIntent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(statIntent);
                return true;
            case R.id.infoItem:
                View view = this.findViewById(R.id.feedButton);
                Snackbar.make(view,
                        "Developer: Viachaslau Viarbitski 951007",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                return true;
            case R.id.dotsItem:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUser();
        initGoogle();

        TextView satietyView = this.findViewById(R.id.satietyBox);
        satietyView.setText(getString(R.string.satiety, 0));
        ImageView cat = this.findViewById(R.id.catImg);

        Button shareButton = this.findViewById(R.id.share_button);
        shareButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "My score in FeedTheCat has reached " + currentUser.getHighestScore() + "! Try to beat me!";
            String shareSub = "FeedTheCat";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(myIntent, "Share using"));
        });

        Button feedButton = this.findViewById(R.id.feedButton);
        feedButton.setOnClickListener(view -> {
            satiety++;
            if (satiety % 15 == 0) {
                RotateAnimation rotate = new RotateAnimation(0, 360, cat.getHeight() / 2,cat.getWidth() / 2);
                rotate.setDuration(1000);
                cat.startAnimation(rotate);
            }
            satietyView.setText(getString(R.string.satiety, satiety));
        });
    }

    @Override
    public void onBackPressed() {
        signOut();
    }


    private void signOut() {
        saveScore();
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                });
    }

    private void saveScore() {
        if (satiety > 0) {
            MainActivity.currentUser.AddNewGame(new UserInfo.GameInfo(new Date(), satiety));
            Database.getInstance().saveDatabaseToFile();
        }
    }

    private void setUser() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("user");
        String id = intent.getStringExtra("id");
        currentUser = Database.getInstance().getUser(id);

        if (currentUser == null) {
            UserInfo user = new UserInfo(name, new ArrayList<>());
            currentUser = Database.getInstance().putNewUser(id, user);
        }
        TextView greetLabel = this.findViewById(R.id.GreetingLabel);
        greetLabel.setText(getString(R.string.greeting_string, currentUser.getUsername()));
    }

    private void initGoogle() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

}