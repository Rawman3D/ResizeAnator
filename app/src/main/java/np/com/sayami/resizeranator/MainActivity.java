package np.com.sayami.resizeranator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private int x, y, size;
    private ImageButton saveButton;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String TAG = MainActivity.class.getClass().getSimpleName();

    int ref;
    Bitmap tempBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x = 95;
        y = 160;
        size = 250;
        receivedBitmap = setImage(CameraActivity.getBitmapImage());
        ref = R.drawable.thuglifer;
        imageView = (ImageView) findViewById(R.id.imageView);

        /*
        imageView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                x =  scrollX - size / 6;
                y =  scrollY - size / 6;
                drawFilter(ref);
                txtV.setText(x + "X" + y);

            }
        });*/

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "Motion move");
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: up");
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch: down");
                }
                x = (int) event.getX() - size / 6;
                y = (int) event.getY() - size / 6;
                drawFilter(ref);
                txtV.setText(x + "X" + y);

                return false;
            }
        });

        imageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.i(TAG, "onDrag: "+event.toString());
                x = (int) event.getX();
                y = (int) event.getY();

                Toast.makeText(getBaseContext(), x + "X" + y, Toast.LENGTH_SHORT).show();
                drawFilter(ref);
                txtV.setText(x + "X" + y);

                return true;
            }
        });

        saveButton = (ImageButton) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               savePicture();
            }

        });

        imageView.setImageBitmap(receivedBitmap);
        xPos = (EditText) findViewById(R.id.getXpos);
        yPos = (EditText) findViewById(R.id.getYpos);
        winSize = (EditText) findViewById(R.id.getWinSize);
        txtV = (TextView) findViewById(R.id.textView);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);

        backgroundColorChange(receivedBitmap);

        mAdapter = new MoviesAdapter(movieList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
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

    void savePicture(){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {

            // Use the compress method on the BitMap object to write image to the OutputStream


            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), "Photo stored @ " + Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + "/ResizeAnator", Toast.LENGTH_SHORT).show();


            Log.d(TAG, "Photo stored@ ");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private final class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save:
                savePicture();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //RESIZE BUTTON KO KAM
    public void doResize(View view) {
        x = Integer.parseInt(xPos.getText().toString());
        y = Integer.parseInt(yPos.getText().toString());
        size = Integer.parseInt(winSize.getText().toString());
        Bitmap croppedBitmap = Bitmap.createBitmap(receivedBitmap, x, y, size, size);
        imageView.setImageBitmap(croppedBitmap);
        Bitmap resizedBitmap19x19 = getResizedBitmap(croppedBitmap, 19, 19);

        Log.i("TAG", Integer.toString(resizedBitmap19x19.getWidth()));
        Log.i("TAG", Integer.toString(resizedBitmap19x19.getHeight()));
        tempBmp = resizedBitmap19x19;
        imageView.setImageBitmap(resizedBitmap19x19);

    }

    public void testSkin(View view) {
        if (hasEnoughSkin() == true)
            txtV.setText("PASS!!");
        else
            txtV.setText("FAIL!!");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    //FILTER BUTON KO KAM
    public void drawFilter(View view) {
        int x, y, size;
        try {
            x = Integer.parseInt(xPos.getText().toString());
            y = Integer.parseInt(yPos.getText().toString());
            size = Integer.parseInt(winSize.getText().toString());
        } catch (Exception E) {
            x = 95;
            y = 160;
            size = 250;
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        Bitmap bmp = receivedBitmap;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ref);

        Bitmap drawableBitmap = getResizedBitmap(bitmap, size, size);

        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(drawableBitmap, x, y, null);

        imageView.setImageBitmap(mutableBitmap);

        tempBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }


    public void drawFilter(int src) {
        ref = src;
//        try {
//            x = Integer.parseInt(xPos.getText().toString());
//            y = Integer.parseInt(yPos.getText().toString());
//            size = Integer.parseInt(winSize.getText().toString());
//        } catch (Exception E) {
//            x = 95;
//            y = 160;
//            size = 250;
//        }

        Bitmap bmp = receivedBitmap;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), src);


        Bitmap drawableBitmap = getResizedBitmap(bitmap, size, size);

        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(drawableBitmap, x, y, null);

        imageView.setImageBitmap(mutableBitmap);

        tempBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    //DRAW BUTTON KO KAAM
    public void drawRect(View view) {
        int x, y, size;
        x = Integer.parseInt(xPos.getText().toString());
        y = Integer.parseInt(yPos.getText().toString());
        size = Integer.parseInt(winSize.getText().toString());

//        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.budo);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap mutableBitmap = receivedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawRect(x, y, x + size, y + size, paint);

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
        Log.i("TAG", Integer.toString(resizedBitmap.getWidth()));
        Log.i("TAG", Integer.toString(resizedBitmap.getHeight()));
//        bm.recycle();
        return resizedBitmap;
    }


    public boolean hasEnoughSkin() {
        int i;
        int j;
        int nSkinPixels = 0;
//        Tag.("ANATOR",tempBmp.getHeight());
        Log.i("TAG", Integer.toString(tempBmp.getHeight()));
        Log.i("TAG", Integer.toString(tempBmp.getWidth()));

        for (i = 0; i < tempBmp.getWidth(); i++)
            for (j = 0; j < tempBmp.getHeight(); j++) {
                int p = tempBmp.getPixel(i, j);
                int G = Color.green(p);
                int R = Color.red(p);
                int B = Color.blue(p);
                int diff = Math.max(R, Math.max(G, B)) - Math.min(R, Math.min(G, B));
                if (R > 95 && G > 40 && B > 20 && R > G && R > B && R - G > 15 && diff > 15)
                    nSkinPixels++;
            }

        return nSkinPixels / (float) (tempBmp.getHeight() * tempBmp.getWidth()) > 0.4;
    }

    private void prepareMovieData() {
        Movie movie = new Movie(R.drawable.thug1, "Round Glass, Black", "Action, Comedy", "2013", (float) 4.2);
        movieList.add(movie);

        movie = new Movie(R.drawable.thuglifer, "Round Glass, Red", "Love, Comedy", "2014", (float) 4.5);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses_001, "Round Glass, Yellow", "Unknown", "2015", (float) 2);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyeglasses_003, "Round Glass, Purple", "Love, Comedy", "2016", (float) 3.8);
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

        movie = new Movie(R.drawable.sunglasses_new, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyes_6, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyes_7, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.gogs_1, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.gogs_2, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.gogs_3, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.round_glass, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.sexy_eye2, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyes_7, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyes_8, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.histobudo, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);

        movie = new Movie(R.drawable.eyes_9, "Sunglass", "Science Fiction", "1985", (float) 4.3);
        movieList.add(movie);


        mAdapter.notifyDataSetChanged();
    }


    public void moviePicked(View view) {
        imageSelectAdapter.setImagePosition(Integer.parseInt(view.getTag().toString()));
        int filterNo = imageSelectAdapter.getImagePosition();
        Toast.makeText(this, filterNo, Toast.LENGTH_SHORT).show();

        Log.d("ROW", "Image No Clicked: " + filterNo);
    }


    Bitmap setImage(Bitmap bitmap) {
        Matrix returnImage = new Matrix();
        returnImage.postRotate(270);
        returnImage.preScale(1, -1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), returnImage, true);
    }

    private void backgroundColorChange(Bitmap bitmap) {
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

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ResizeAnator");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("ResizeAnator", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Picture_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
