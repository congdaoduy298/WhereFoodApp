package com.chinh.wherefoodapp.custom.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class Ample extends AppCompatTextView {

    public Ample(@NonNull Context context) {
        super(context);
        setFontsTextview();
    }

    public Ample(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextview();
    }

    public Ample(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextview();
    }

    private  void setFontsTextview(){
        Typeface typeface = CustomTextView.getAmpleSoft(getContext());
        setTypeface(typeface);
    }
}
