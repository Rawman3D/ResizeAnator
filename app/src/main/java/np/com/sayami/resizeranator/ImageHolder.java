package np.com.sayami.resizeranator;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by sayami on 7/11/2016.
 */
public class ImageHolder {
    Bitmap Image;
    int winSize;
    int xPos;
    int yPos;

    ImageHolder(){

    }
    ImageHolder(Bitmap bmp, int x, int y, int size){
        Image=bmp;
        winSize=size;
        xPos=x;
        yPos=y;

    }






}
