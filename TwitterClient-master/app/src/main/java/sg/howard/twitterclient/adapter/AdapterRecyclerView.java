package sg.howard.twitterclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.Tweet;
import com.varunest.sparkbutton.SparkButton;

import org.joda.time.DateTime;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.howard.twitterclient.R;
import sg.howard.twitterclient.util.TimelineConverter;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
    private List<Tweet> data;
    private Context context;
    private int lastPosition = -1;
    public AdapterRecyclerView(Context ctx) {
        this.context = ctx;

    }


    public void setData(List<Tweet> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate( R.layout.row_recyclerview, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Tweet tweet = data.get( position );
        Log.d( "name",data.get( position ).user.name );
        Log.d( "des",data.get( position ).text );
        Log.d( "user",data.get( position ).user.screenName );
       // Log.d( "like",data.get( position ).retweetedStatus.favoriteCount.toString() );
        int count_heart = data.get( position ).retweetCount;
        holder.name.setText( data.get( position ).user.name);
        holder.describe.setText( data.get( position ).text );
        holder.user.setText( "@"+data.get( position ).user.screenName );
        holder.repeat_count.setText( data.get( position ).retweetCount + " " );
        holder.heart_count.setText( data.get( position ).favoriteCount + " ");


        holder.time.setText( TimelineConverter.dateToAge( data.get( position ).createdAt, DateTime.now() ) );
       // holder.time.setText( data.get( position ).createdAt + " " );
        Glide.with(context).load( data.get( position ).user.profileImageUrl)
                .into(holder.image);
        if (data.get( position ).entities.media.size() != 0)
        {
            Glide.with(context).load( data.get( position ).entities.media.get( 0 ).mediaUrl)
                    .into(holder.background);
        }
//                holder.time.setText( data.get( position ).user.createdAt );
//                holder.heart_count.setText( data.get( position ).favoriteCount );
//                holder.repeat_count.setText( data.get( position ).quotedStatusIdStr );
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        public  ImageView background;
        public TextView name;
        public TextView describe;
        public TextView date;
        public TextView user;
        public TextView time;
        public TextView heart_count;
        public TextView repeat_count;
        public ImageButton reply;
        public SparkButton heart;
        public ImageButton repeat;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            name = itemView.findViewById( R.id.name );
            Animation animation = AnimationUtils.loadAnimation( context,R.anim.anim_blink );
            name.setAnimation( animation );
            describe = itemView.findViewById( R.id.describe );
            user = itemView.findViewById( R.id.username );
            image =itemView.findViewById( R.id.image);
            background = itemView.findViewById( R.id.background );
            time = itemView.findViewById( R.id.time );
            heart_count = itemView.findViewById( R.id.heart_num );
            repeat_count= itemView.findViewById( R.id.repeat_num );
            itemView.setOnClickListener( this );

        }

        @Override
        public void onClick(View view) {
            //Toast.makeText( context,"abcd",Toast.LENGTH_LONG ).show();
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
