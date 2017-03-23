package com.lyb.besttimer.pluginwidget.view.complex;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lyb.besttimer.pluginwidget.R;
import com.lyb.besttimer.pluginwidget.utils.ColorStateListUtil;
import com.lyb.besttimer.pluginwidget.utils.DataUtil;
import com.lyb.besttimer.pluginwidget.utils.DisplayUtil;
import com.lyb.besttimer.pluginwidget.utils.ViewState;

/**
 * number edit view
 * Created by linyibiao on 2017/3/22.
 */

public class NumberEditView extends LinearLayout {

    public NumberEditView(Context context) {
        this(context, null);
    }

    public NumberEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private ImageButton minusImageButton;
    private EditText numberEditText;
    private ImageButton addImageButton;

    private int minValue = 1;
    private int maxValue = 9999;

    private int editEnabledColor = 0xFF000000;
    private int editUnEnabledColor = 0xFF999999;

    private int inValidEnabledColor = 0xFFFF0000;
    private int inValidUnEnabledColor = 0xFF990000;

    private ValueChangeListener valueChangeListener;

    private void init(Context context) {

        setOrientation(LinearLayout.HORIZONTAL);

        int buttonPad = DisplayUtil.dip2px(context, 5);

        minusImageButton = new ImageButton(context);
        minusImageButton.setImageResource(R.drawable.selector_image_minus);
        minusImageButton.setBackgroundResource(R.drawable.selector_left_raduis);
        minusImageButton.setPadding(buttonPad, buttonPad, buttonPad, buttonPad);
        addView(minusImageButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        numberEditText = new EditText(context);
        numberEditText.setBackgroundResource(R.mipmap.bg_edit_text);
        numberEditText.setKeyListener(new DigitsKeyListener(false, false));
        numberEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        numberEditText.setHintTextColor(0xFFCCCCCC);
        numberEditText.setLines(1);
        numberEditText.setGravity(Gravity.CENTER);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(9);
        numberEditText.setFilters(filterArray);
        numberEditText.setSelectAllOnFocus(true);
        addView(numberEditText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        addImageButton = new ImageButton(context);
        addImageButton.setImageResource(R.drawable.selector_image_add);
        addImageButton.setBackgroundResource(R.drawable.selector_right_raduis);
        addImageButton.setPadding(buttonPad, buttonPad, buttonPad, buttonPad);
        addView(addImageButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        numberEditText.addTextChangedListener(textWatcher);
        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(false);
            }
        });
        minusImageButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new LongOperationRun(minusImageButton, false).go();
                return false;
            }
        });
        addImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(true);
            }
        });
        addImageButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new LongOperationRun(addImageButton, true).go();
                return false;
            }
        });

        initData();

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        numberEditText.setEnabled(enabled);
        loadButton();
    }

    private void initData() {
        numberEditText.setText(minValue + "");
        loadColor();
    }

    private void loadColor() {
        int rawValue = getRawValue();
        if (rawValue >= minValue && rawValue <= maxValue) {
            numberEditText.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(editEnabledColor, editUnEnabledColor, android.R.attr.state_enabled)));
        } else {
            numberEditText.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(inValidEnabledColor, inValidUnEnabledColor, android.R.attr.state_enabled)));
        }
    }

    private void loadButton() {
        int realValue = getRealValue();
        minusImageButton.setEnabled(realValue > minValue && isEnabled());
        addImageButton.setEnabled(realValue < maxValue && isEnabled());
    }

    private class LongOperationRun implements Runnable {

        private final ImageButton targetImageButton;
        private final boolean add;

        public LongOperationRun(ImageButton targetImageButton, boolean add) {
            this.targetImageButton = targetImageButton;
            this.add = add;
        }

        public void go() {
            targetImageButton.postDelayed(this, 100);
        }

        @Override
        public void run() {
            if (targetImageButton.isPressed()) {
                operation(add);
                go();
            }
        }

    }

    private TextWatcher textWatcher = new TextWatcher() {

        private int preValue = Integer.MIN_VALUE;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handleValue();
        }

        private void handleValue() {
            loadButton();
            loadColor();
            int realValue = getRealValue();
            if (valueChangeListener != null) {
                if (preValue == Integer.MIN_VALUE) {
                    valueChangeListener.change(preValue, realValue);
                } else if (preValue != realValue) {
                    valueChangeListener.change(preValue, realValue);
                }
            }
            preValue = realValue;
        }

    };

    public interface ValueChangeListener {
        void change(int preValue, int currValue);
    }

    private int getRawValue() {
        return DataUtil.strToInt(numberEditText.getText().toString());
    }

    private int getRealValue() {
        int showValue = getRawValue();
        if (showValue < minValue) {
            showValue = minValue;
        }
        if (showValue > maxValue) {
            showValue = maxValue;
        }
        return showValue;
    }

    private void setRealValue(int value) {
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        numberEditText.setText(value + "");
    }

    private void operation(boolean add) {
        int realValue = getRealValue();
        if (add) {
            if (realValue < maxValue) {
                setRealValue(realValue + 1);
            }
        } else {
            if (realValue > minValue) {
                setRealValue(realValue - 1);
            }
        }
    }

    public void setRangeValue(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        int rawValue = getRawValue();
        if (rawValue < minValue) {
            numberEditText.setText(minValue + "");
        }
        if (rawValue > maxValue) {
            numberEditText.setText(maxValue + "");
        }
    }

    public void setEditColor(int editEnabledColor, int editUnEnabledColor) {
        this.editEnabledColor = editEnabledColor;
        this.editUnEnabledColor = editUnEnabledColor;
        loadColor();
    }

    public void setInValidColor(int inValidEnabledColor, int inValidUnEnabledColor) {
        this.inValidEnabledColor = inValidEnabledColor;
        this.inValidUnEnabledColor = inValidUnEnabledColor;
        loadColor();
    }

    public void setValueChangeListener(ValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public int getValue() {
        return getRealValue();
    }

    public void setValue(int value) {
        setRealValue(value);
    }

}
