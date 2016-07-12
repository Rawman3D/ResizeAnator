package np.com.sayami.resizeranator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText xPos, yPos, winSize;
    TextView txtV;
    int ref;
    Bitmap tempBmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ref = (R.drawable.budo);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.budo);
        xPos = (EditText) findViewById(R.id.getXpos);
        yPos = (EditText) findViewById(R.id.getYpos);
        winSize = (EditText) findViewById(R.id.getWinSize);
        txtV = (TextView) findViewById(R.id.textView);

    }

    public void doResize(View view) {
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());

        Bitmap getBmp = BitmapFactory.decodeResource(getResources(),R.drawable.budo);

        Bitmap croppedBitmap=Bitmap.createBitmap(getBmp, x,y,size, size);


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
    public void drawRect(View view){
        int x,y,size;
        x=Integer.parseInt(xPos.getText().toString());
        y=Integer.parseInt(yPos.getText().toString());
        size=Integer.parseInt(winSize.getText().toString());

        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.budo);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
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
}
