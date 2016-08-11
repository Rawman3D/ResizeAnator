package np.com.sayami.resizeranator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        public TextView filterName;
        public RatingBar ratingBar;
        public LinearLayout linearLayout;

        public MyViewHolder(View view, MyClicks listener) {
            super(view);
            mClicks = listener;
            context = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.filterImage);
//            filterName = (TextView) view.findViewById(R.id.filterName);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//            genre = (TextView) view.findViewById(R.id.genre);
//            year = (TextView) view.findViewById(R.id.year);
//            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
//            imageView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
           /* if(v instanceof ImageView){
                mClicks.clickOnImage((ImageView) v, getLayoutPosition());
            } else {*/
                mClicks.clickOnRow(v,getLayoutPosition());
//            }
        }

    }

    /**
     * Interface for checking the clicks and what to do when different clicks occur.
     * **/

    public static interface MyClicks{
//        public void clickOnImage(ImageView imgView, int pos);
        public void clickOnRow(View info, int position);
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
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter, parent, false);

        MoviesAdapter.MyViewHolder myViewHolder = new MyViewHolder(itemView, new MoviesAdapter.MyClicks() {


            public void clickOnRow(View info, int position){
                Toast.makeText(context,"Filter Selected",Toast.LENGTH_SHORT).show();
                // make the position ko picture to save in image holder class
                int src=moviesList.get(position).getImgSrc();

//                ImageHolder imageHolder = new ImageHolder();
//                imageHolder.setImageSource(src);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.imageView.setImageResource(movie.getImgSrc());
//        holder.filterName.setText(movie.getTitle());
//        holder.genre.setText(movie.getGenre());
//        holder.year.setText(movie.getYear());
//        holder.ratingBar.setRating(movie.getRating());

    /*    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),movie.getImgSrc());

        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getLightMutedColor(context.getResources().getColor(android.R.color.white));
                holder.linearLayout.setBackgroundColor(bgColor);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}