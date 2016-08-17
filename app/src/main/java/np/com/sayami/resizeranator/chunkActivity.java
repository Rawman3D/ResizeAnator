package np.com.sayami.resizeranator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class chunkActivity extends AppCompatActivity {
    private Bitmap receivedBitmap;
    private ImageView dispImg;
    private Button kamGarneButton;
    private int mheight;
    private int mwidth;
    private float mratio;
    private ImageReceiver getImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chunk);
        receivedBitmap = setImage(CameraActivity.getBitmapImage());
        dispImg = (ImageView) findViewById(R.id.chunkImageView);
        kamGarneButton = (Button) findViewById(R.id.kamGarrButton);
        dispImg.setImageBitmap(receivedBitmap);

        setHeight(receivedBitmap.getHeight());
        setWidth(receivedBitmap.getWidth());
        float ratio = (float) mwidth / (float) mheight;
        mratio=ratio;

        Log.i("TAG2","height"+receivedBitmap.getHeight());
        Log.i("TAG2","width"+receivedBitmap.getWidth());

        IntentFilter filter = new IntentFilter(BackgroundService.ACTION_RETURN_IMAGE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getImage = new ImageReceiver();
        registerReceiver(getImage,filter);

        kamGarneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count=0;

                Bitmap testBmp;

                int newHeight1 = 388;
                Log.i("LOG3", "" + newHeight1);
                setHeight(newHeight1);
                int newWidth1 = (int) Math.ceil(newHeight1 * mratio );
                setWidth(newWidth1);

                testBmp = getResizedBitmap(receivedBitmap, newWidth1, newHeight1);
                dispImg.setImageBitmap(testBmp);

                Log.i("TAG2", "height:" + testBmp.getHeight());
                Log.i("TAG2", "width:" + testBmp.getWidth());

               while(newHeight1>24 && newWidth1>24){

                    Log.i("LOG3", "" + mratio);
                     newHeight1 = (int) (mheight * .9);

                    Log.i("LOG3", "" + newHeight1);
                    setHeight(newHeight1);
                     newWidth1 = (int) (newHeight1 * mratio);
                    setWidth(newWidth1);

                   if(newHeight1<24 || newWidth1<24)
                       break;
                    testBmp = getResizedBitmap(receivedBitmap, newWidth1, newHeight1);
                    dispImg.setImageBitmap(testBmp);

                   Intent resizeService = new Intent(chunkActivity.this, BackgroundService.class);
                   resizeService.putExtra("Bitmap",testBmp);
                           Log.i("TAG2", "height:" + testBmp.getHeight());
                    Log.i("TAG2", "width:" + testBmp.getWidth());
                   count++;
                }
                Log.i("count",""+count);
            }
        });
    }

    public void setHeight(int h){
        mheight = h;
    }

    public void setWidth(int w){
        mwidth = w;
    }

    public class ImageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Bitmap finalArt = intent.getParcelableExtra("Result Image");
            dispImg.setImageBitmap(finalArt);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) (newWidth)) / width;
        float scaleHeight = ((float) (newHeight)) / height;
        Log.i("TAG SCALE","Scaled height"+scaleHeight);
        Log.i("TAG SCALE","Scaled width"+scaleWidth);

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        Log.i("TAG", "New Height"+Integer.toString(resizedBitmap.getHeight()));
        Log.i("TAG", "New Width"+Integer.toString(resizedBitmap.getWidth()));
//        bm.recycle();
        return resizedBitmap;
    }

    Bitmap setImage(Bitmap bitmap) {
        Matrix returnImage = new Matrix();
        returnImage.postRotate(270);
        returnImage.preScale(1, -1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), returnImage, true);
    }

}
