package np.com.sayami.resizeranator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText xPos, yPos, winSize;
    TextView txtV;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private ImageSelectAdapter imageSelectAdapter;
    private RelativeLayout mRelativeLayout;
    private Bitmap receivedBitmap;

    int ref;
    Bitmap tempBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receivedBitmap = setImage(CameraActivity.getBitmapImage());

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(receivedBitmap);
        xPos = (EditText) findViewById(R.id.getXpos);
        yPos = (EditText) findViewById(R.id.getYpos);
        winSize = (EditText) findViewById(R.id.getWinSize);
        txtV = (TextView) findViewById(R.id.textView);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);

        backgroundColorChange(receivedBitmap);

        mAdapter = new MoviesAdapter(movieList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();

        Button filterDrawButton = (Button) findViewById(R.id.drawFilter);
        filterDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawFilter(v);
            }
        });


    }

    public void doResize(View view) {
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());
        Bitmap croppedBitmap=Bitmap.createBitmap(receivedBitmap, x,y,size, size);
        imageView.setImageBitmap(croppedBitmap);
        Bitmap resizedBitmap19x19=getResizedBitmap(croppedBitmap,19,19);

        Log.i("TAG",Integer.toString(resizedBitmap19x19.getWidth()));
        Log.i("TAG",Integer.toString(resizedBitmap19x19.getHeight()));
        tempBmp=resizedBitmap19x19;
        imageView.setImageBitmap(resizedBitmap19x19);

    }

    public void testSkin(View view){
        if(hasEnoughSkin()==true)
            txtV.setText("PASS!!");
        else
            txtV.setText("FAIL!!");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( MainActivity.this,CameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void drawFilter(View view){
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        //Add a png image
        Bitmap bmp =  receivedBitmap;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sunglasses);

        Bitmap drawableBitmap = getResizedBitmap(bitmap,size,size);

        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(drawableBitmap,x,y,null);

        imageView.setImageBitmap(mutableBitmap);

        tempBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }


    public void drawFilter(View view,int src){
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());


        Bitmap bmp = receivedBitmap;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        //Add a png image
        //histo budo 400,500,250


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),src);


        Bitmap drawableBitmap = getResizedBitmap(bitmap,size,size);

        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(drawableBitmap,x,y,null);

        imageView.setImageBitmap(mutableBitmap);

        tempBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void drawRect(View view){
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());

//        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.budo);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);




        Bitmap mutableBitmap = receivedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawRect(x, y, x+size, y+size, paint);


        imageView.setImageBitmap(mutableBitmap);

        tempBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        Log.i("TAG",Integer.toString(resizedBitmap.getWidth()));
        Log.i("TAG",Integer.toString(resizedBitmap.getHeight()));
//        bm.recycle();
        return resizedBitmap;
    }


    public  boolean hasEnoughSkin(){
        int i;
        int j;
        int nSkinPixels = 0;
//        Tag.("ANATOR",tempBmp.getHeight());
        Log.i("TAG",Integer.toString(tempBmp.getHeight()));
        Log.i("TAG",Integer.toString(tempBmp.getWidth()));

        for( i = 0; i < tempBmp.getWidth() ; i++)
            for( j = 0; j < tempBmp.getHeight(); j++){
                int p= tempBmp.getPixel(i,j);
                int G= Color.green(p);
                int R= Color.red(p);
                int B= Color.blue(p);
                int diff = Math.max(R, Math.max(G, B))- Math.min(R, Math.min(G, B));
                if(R > 95 && G > 40 && B > 20 && R > G && R > B && R-G > 15 && diff > 15)
                    nSkinPixels++;
            }

        return nSkinPixels/(float)(tempBmp.getHeight()*tempBmp.getWidth()) > 0.4;
    }

    private void prepareMovieData() {
        Movie movie = new Movie(R.drawable.thug1,"Round Glass, Black", "Action, Comedy", "2013", (float) 4.2);
        movieList.add(movie);

        movie = new Movie(R.drawable.thuglifer,"Round Glass, Red", "Love, Comedy", "2014", (float) 4.5);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses_001,"Round Glass, Yellow", "Unknown", "2015", (float) 2);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses_003,"Round Glass, Purple", "Love, Comedy", "2016",(float) 3.8);
        movieList.add(movie);

        movie = new Movie(R.drawable.glass1, "Round Glass, Green", "Comedy", "2015", (float) 4);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses_005, "John Lenon", "Serious, Reality", "2016", (float) 4.6);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses, "Hipster Black", "Animation, Fantasy", "2016", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_001, "Hipster Red", "Horror", "2016", (float) 4);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_002, "Uncle Style", "Animation", "2014", (float) 4.4);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_003, "Round Edge Green", "Action & Adventure", "2008", (float) 3.8);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_004, "Round Edge Blue", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses, "Round Edge Yellow", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_006, "Round Edge Red", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_007, "Round Edge Black", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_008, "Round Edge Pink", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_009, "Pink Uncle", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_010, "Green Uncle", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.glasses_011, "Blue Uncle", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.sunglasses, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);


        mAdapter.notifyDataSetChanged();
    }


    public void moviePicked(View view){
        imageSelectAdapter.setImagePosition(Integer.parseInt(view.getTag().toString()));
        int filterNo = imageSelectAdapter.getImagePosition();
        Toast.makeText(this,filterNo,Toast.LENGTH_SHORT).show();

        Log.d("ROW","Image No Clicked: " + filterNo);
    }


    Bitmap setImage(Bitmap bitmap){
        Matrix returnImage = new Matrix();
        returnImage.postRotate(270);
        returnImage.preScale(1, -1);
        return Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(),bitmap.getHeight(), returnImage,true);
    }

    private void backgroundColorChange(Bitmap bitmap){
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getLightMutedColor(getResources().getColor(android.R.color.black));
//                int barColor = palette.getVibrantColor(getResources().getColor(android.R.color.black));
                mRelativeLayout.setBackgroundColor(bgColor);
//                getActionBar().setBackgroundDrawable(new ColorDrawable(barColor));
            }
        });
    }

}
