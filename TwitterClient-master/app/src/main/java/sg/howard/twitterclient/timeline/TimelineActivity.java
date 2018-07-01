package sg.howard.twitterclient.timeline;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.adapter.AdapterRecyclerView;
import sg.howard.twitterclient.adapter.EndlessScroll;
import sg.howard.twitterclient.compose.ComposeTweetActivity;
import sg.howard.twitterclient.imageview.ImageViewActivity;

import static android.widget.GridLayout.VERTICAL;

public class TimelineActivity extends AppCompatActivity implements TimelineContract.View {
    private static String TAG = TimelineActivity.class.getSimpleName();
    RecyclerView rvTimeline;
    ProgressBar loader;
    FloatingActionButton fab;
    TimelineContract.Presenter presenter;
    AdapterRecyclerView adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ActionBar actionBar;
    EndlessScroll scrollListener;
    int pageCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rvTimeline = findViewById(R.id.rvTimeline);
        loader = findViewById(R.id.loader);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled( true );
        actionBar.setLogo( R.drawable.ic_twitter );
        actionBar.setDisplayUseLogoEnabled( true );

        fab = findViewById(R.id.fab);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        presenter = new TimelinePresenter(this, TwitterCore.getInstance().getSessionManager().getActiveSession());

        fab.setOnClickListener(view -> {
           startActivity(new Intent(this, ComposeTweetActivity.class));
           overridePendingTransition( R.anim.anim_status,R.anim.anim_exit );
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getTimeline(20);
    }

    @Override
    public void setPresenter(TimelineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading(boolean isShow) {
        loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetStatusesSuccess(List<Tweet> data) {
       Log.d( TAG,"Loaded" + data.size() );

        //adapter = new AdapterRecyclerView( getApplicationContext() );
        init( data );
        refresh( data );
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void init(List<Tweet> data){
        adapter = new AdapterRecyclerView( getApplicationContext() );


        //adapter.setData( tweet );
        Log.d( "user",data.get( 0 ).user.screenName );
        Log.d( "describe",data.get( 0 ).text );
        Log.d( "name",data.get( 0 ).user.name );
        adapter.setData( data );
        rvTimeline.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        rvTimeline.setLayoutManager( layoutManager );
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        rvTimeline.addItemDecoration(itemDecor);
        scrollListener = new EndlessScroll((LinearLayoutManager) layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }

        };

        rvTimeline.addOnScrollListener(scrollListener);

    }
    private void loadNextDataFromApi() {

        Log.d("Load more", "Load");

        pageCount = pageCount + 20;

        presenter.getTimeline(pageCount);

    }
    public void refresh(List<Tweet> data){
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing( false );
                loadNextDataFromApi();
                scrollListener.resetState();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose_m:
                startActivity(new Intent(this, ComposeTweetActivity.class));
                overridePendingTransition( R.anim.anim_status,R.anim.anim_exit );
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void clickHeart(View view){
            //Toast.makeText( getApplicationContext(),"heart",Toast.LENGTH_LONG ).show();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_heart);
        view.startAnimation(animation);
    }
    public void clickReply(View view){
        Toast.makeText( getApplicationContext(),"reply",Toast.LENGTH_LONG ).show();
    }

    public void clickImage(View view) {
        Toast.makeText( getApplicationContext(),"image",Toast.LENGTH_LONG ).show();
    }

}
