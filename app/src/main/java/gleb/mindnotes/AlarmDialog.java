package gleb.mindnotes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class AlarmDialog extends DialogFragment
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = AlarmDialog.class.getSimpleName();
    private final int seekBarMaxValue = 8;

    private SeekBar seekBar;
    private TextView okButton;
    private TextView cancelButton;
    private TextView frequencyText;
    private EditText repeatCount;
    private int frequency = 4;

    private final String alarmFrequencies[] = {
            "EXTREMELY SELDOM",
            "VERY SELDOM",
            "SELDOM",
            "QUITE RARELY",
            "NORMAL",
            "FAIRLY OFTEN",
            "FREQUENTLY",
            "VERY OFTEN",
            "INSANELY OFTEN"
    };

    interface ConfirmedNoteAlarmSaver {
        void confirmSaveAlarmSettings(int frequency, int repeatCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("SET ALARM");
        View alarmDialogView = inflater.inflate(R.layout.fragment_alarm_dialog, null);

        seekBar = (SeekBar) alarmDialogView.findViewById(R.id.frequency_seek_bar);
        okButton = (TextView) alarmDialogView.findViewById(R.id.ok_button);
        cancelButton = (TextView) alarmDialogView.findViewById(R.id.cancel_button);
        frequencyText = (TextView) alarmDialogView.findViewById(R.id.frequency_text);
        repeatCount = (EditText) alarmDialogView.findViewById(R.id.repeat_count_text);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        ((View) okButton).setOnTouchListener(OnTouchViewListener.getInstance());
        ((View) cancelButton).setOnTouchListener(OnTouchViewListener.getInstance());
        ((View) repeatCount).setOnTouchListener(OnTouchViewListener.getInstance());

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(seekBarMaxValue);
        frequency = seekBar.getProgress();

        repeatCount.setSelectAllOnFocus(true);

        return alarmDialogView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                try {
                    ((ConfirmedNoteAlarmSaver) getActivity()).
                            confirmSaveAlarmSettings(frequency,
                                    Integer.valueOf(repeatCount.getText().toString()));
                } catch (ClassCastException e) {
                    Log.e(TAG, "Parent activity should implement " +
                            "ConfirmedNoteAlarmSaver interface in order to " +
                            "receive data from ConfirmedNoteAlarmSaver fragment", e);
                }
                dismiss();
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            default:
                Log.w(TAG, "Unexpected view has sent an onClickEvent. That is suspicious!");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        frequency = seekBarMaxValue-progress;
        if (progress < 0) frequency = seekBarMaxValue;
        else if (progress > seekBarMaxValue) frequency = 0;
        frequencyText.setText(alarmFrequencies[progress]);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "in onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "in onStopTrackingTouch");
    }

}
