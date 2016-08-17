package np.com.sayami.resizeranator;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by ramen on 8/16/2016.
 */
public class BackgroundService extends IntentService {
    public static final String ACTION_RETURN_IMAGE = chunkActivity.class.getSimpleName() + "Result_Image";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundService(String name) {
        super(name);
    }

    public BackgroundService() {
        super(BackgroundService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bitmap testBmp = null;
        Bundle bundle=intent.getBundleExtra("imageUploadBundle");
        testBmp = BitmapFactory.decodeByteArray(bundle.getByteArray("Bitmap"),0,bundle.getByteArray("Bitmap").length);
        /*String filename = intent.getStringExtra("Bitmap");
        try{
            FileInputStream is = this.openFileInput(filename);
            testBmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e1){
            e1.printStackTrace();
        }
        *///intent.getParcelableExtra("Bitmap");
        Intent broadcastImg = new Intent();
        broadcastImg.addCategory(Intent.CATEGORY_DEFAULT);
        int count=0;
        for (int i = 0; i <= (testBmp.getHeight() - 24); i++) {
            for (int j = 0; j <= (testBmp.getWidth() - 24); j++) {
                Bitmap sub = Bitmap.createBitmap(testBmp, j, i, 24, 24);
                count++;
                broadcastImg.putExtra("Result Image",sub);
            }
        }
        Log.i("TAG:INTENT SERVICE:",""+count);
    }

}
