package gleb.mindnotes;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

class OnTouchViewListener implements View.OnTouchListener {

    private static final OnTouchViewListener instance = new OnTouchViewListener();

    private OnTouchViewListener() {}

    static OnTouchViewListener getInstance() {
        return instance;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("OnTouchViewListener", "inOnTouch!");
        int color, inactiveColor, activeColor;
        switch (v.getId()) {
            case R.id.done_button:
            case R.id.delete_note_button:
            case R.id.back_button:
            case R.id.new_note_button:
            case R.id.repeat_count_text:
                inactiveColor = Color.WHITE;
                activeColor = NotesListActivity.colorHighlight;
                break;
            case R.id.set_alarm_button:
                inactiveColor = v.isSelected() ? NotesListActivity.colorSelect : Color.WHITE;
                activeColor = NotesListActivity.colorHighlight;
                break;
            default:
                inactiveColor = Color.BLACK;
                activeColor = NotesListActivity.colorSelect;
        }
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN:
            case android.view.MotionEvent.ACTION_HOVER_ENTER:
                color = activeColor;
                break;
            case android.view.MotionEvent.ACTION_CANCEL:
            case android.view.MotionEvent.ACTION_UP:
            case android.view.MotionEvent.ACTION_HOVER_EXIT:
                color = inactiveColor;
                break;
            default:
                return false;
        }

        if (v instanceof ImageView) {
            ((ImageView) v).setColorFilter(color);
        } else if (v instanceof EditText) {
            ((EditText) v).setHighlightColor(color);
        } else if (v instanceof TextView) {
            ((TextView) v).setTextColor(color);
        }
        return false;
    }
}
