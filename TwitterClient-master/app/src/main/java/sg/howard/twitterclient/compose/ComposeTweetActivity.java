package sg.howard.twitterclient.compose;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.timeline.TimelineActivity;

public class ComposeTweetActivity extends AppCompatActivity implements ComposeContract.View{
    Button btnSend;
    ImageButton exit;
    EditText edtCompose;
    ProgressBar loader;
    TextView count_text;
    ComposeContract.Presenter presenter;
    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled( true );
        actionBar.setTitle( "Tweet" );
        actionBar.setLogo( R.drawable.ic_twitter );
        actionBar.setDisplayUseLogoEnabled( true );
        btnSend = findViewById(R.id.btnSend);
        exit = findViewById( R.id.exit );
        edtCompose = findViewById(R.id.edtCompose);
        count_text =findViewById( R.id.count_edit_text );
        loader = findViewById(R.id.loader);
        presenter = new ComposeTweetPresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

        btnSend.setOnClickListener( view -> presenter.sendTweet(edtCompose.getText().toString()));
        exit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComposeTweetActivity.this,TimelineActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.anim_exit,R.anim.anim_status );
            }
        } );
        edtCompose.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtCompose.getText().toString();
                int symbols = text.length();
                count_text.setText( "" + symbols );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
    }

    @Override
    public void setPresenter(ComposeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendTweetSuccess(Result<Tweet> result) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
    }
}
