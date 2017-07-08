package com.mumineendownloads.mumineenpdf.Helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Hussain on 7/6/2017.
 */

public class CustomDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;

    /**
     * Default divider will be used
     */
    public CustomDivider(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public CustomDivider(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = 24;
        outRect.left = 24;

        if(parent.getChildAdapterPosition(view) == state.getItemCount()-1){
            outRect.bottom = 24;
            outRect.top = 0; //don't forget about recycling...
        }else {
            outRect.bottom = 1;
        }
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = 24;
            outRect.bottom = 1;
        }
    }
}