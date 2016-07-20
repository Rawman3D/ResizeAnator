package np.com.sayami.resizeranator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by xitiz on 6/20/16.
 * This is a custom adapter made for presenting our elements.
 * RecyclerView provides two methods that we usually override:
 *      - onCreateViewHolder()
 *      - onBindViewHolder()
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    public MyClicks mClicks;
    private Context context;

    /**
     * A viewHolder for the
     * **/
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView title, year, genre;
        public RatingBar ratingBar;

        public MyViewHolder(View view, MyClicks listener) {
            super(view);
            mClicks = listener;
            context = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            imageView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(v instanceof ImageView){
                mClicks.clickOnImage((ImageView) v, getLayoutPosition());
            } else {
                mClicks.clickOnRow(v);
            }
        }

    }

    /**
     * Interface for checking the clicks and what to do when different clicks occur.
     * **/

    public static interface MyClicks{
        public void clickOnImage(ImageView imgView, int pos);
        public void clickOnRow(View info);
    }


    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
//        this.context = context;
    }

    /**
     * Work of onCreateViewHolder
     * To inflate the view from movie_list_row layout.
     * **/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        MoviesAdapter.MyViewHolder myViewHolder = new MyViewHolder(itemView, new MoviesAdapter.MyClicks() {

            //Clicks for interaction.
            public void clickOnImage(ImageView imgView,int pos){
                Movie movie = moviesList.get(pos);
                Toast.makeText(context,"Image Click",Toast.LENGTH_SHORT).show();
                Log.d("Rating ","The click works");
            }

            public void clickOnRow(View info){
                Toast.makeText(context,"Whole row clicked",Toast.LENGTH_SHORT).show();
                Log.d("Movie Adapter","The Second click also works");
            }
        });

        return myViewHolder;
    }

    /**
     * Work of onBindViewHolder
     * Set the appropriate data to each value in the row.
     * Match the different elements with the corresponding value.
     * **/

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.imageView.setImageResource(movie.getImgSrc());
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.ratingBar.setRating(movie.getRating());
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}