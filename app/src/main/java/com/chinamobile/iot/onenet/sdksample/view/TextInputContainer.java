package com.chinamobile.iot.onenet.sdksample.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TextInputContainer extends TextInputLayout {

    public TextInputContainer(Context context) {
        this(context, null);
    }

    public TextInputContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getEditText() != null) {
            getEditText().addTextChangedListener(mTextWatcher);
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                setError(null);
                setErrorEnabled(false);
            }
        }
    };

}
