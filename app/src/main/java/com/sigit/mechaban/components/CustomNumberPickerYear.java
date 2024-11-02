package com.sigit.mechaban.components;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

public class CustomNumberPickerYear extends NumberPicker {

    public CustomNumberPickerYear(Context context) {
        super(context);
        init();
    }

    public CustomNumberPickerYear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNumberPickerYear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            Field wheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
            wheelPaintField.setAccessible(true);
            Paint paint = (Paint) wheelPaintField.get(this);
            paint.setTextSize(64f); // Set ukuran teks yang diinginkan

            // Cari dan perbesar semua TextView internal
            Field[] fields = NumberPicker.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(TextView.class)) {
                    field.setAccessible(true);
                    TextView textView = (TextView) field.get(this);
                    if (textView != null) {
                        textView.setTextSize(32f); // Set ukuran teks yang diinginkan
                    }
                }
            }

            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
