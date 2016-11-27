package tn.example.asus_octadev.tunitour.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Marwen octadev on 10/11/2016.
 */

public class AngledTextView extends TextView
{
    public AngledTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //Save the current matrix
        canvas.save();
        //Rotate this View at its center
        canvas.rotate(-15, 0, this.getHeight()/2);
        //Draw it
        super.onDraw(canvas);
        //Restore to the previous matrix
        canvas.restore();
    }
}
