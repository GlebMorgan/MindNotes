package gleb.mindnotes;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener,
        ConfirmationDialog.ConfirmedActionPerformer, AlarmDialog.ConfirmedNoteAlarmSaver {

    private static final String TAG = NoteActivity.class.getSimpleName();

    private Note note;
    private DatabaseHelper helper;
    private NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(this);

    private ImageView backButton;
    private ImageView alarmButton;
    private ImageView deleteButton;
    private ImageView doneButton;
    private EditText noteText;

    private int frequency = 4;
    private int alarmRepeatCount = 10;

    private boolean noteIsEdited = false;
    private int shortClicksPerformed = 0;

    @Override
    public void confirmedDeleteNote() {
        helper.deleteNote(note.getId());
        finish();
    }

    @Override
    public void confirmedExit() {
        finish();
    }

    @Override
    public void confirmSaveAlarmSettings(int frequency, int repeatCount) {
        //TODO: database related stuff (if needed) is upcoming (+ alarmSign triggering)
        Log.i(TAG, "OK, NoteActivity received data from AlarmDialog fragment");
        this.frequency = frequency;
        this.alarmRepeatCount = repeatCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        helper = new DatabaseHelper(NoteActivity.this);

        Intent intent = getIntent();
        note = intent.getParcelableExtra(NotesListActivity.SELECTED_NOTE);
        //TODO: should Parcelable name be local string?
        backButton = (ImageView) findViewById(R.id.back_button);
        alarmButton = (ImageView) findViewById(R.id.set_alarm_button);
        deleteButton = (ImageView) findViewById(R.id.delete_note_button);
        doneButton = (ImageView) findViewById(R.id.done_button);
        noteText = (EditText) findViewById(R.id.note_text);
        alarmButton = (ImageView) findViewById(R.id.set_alarm_button);

        backButton.setOnClickListener(this);
        alarmButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        OnTouchViewListener onTouchViewListener = OnTouchViewListener.getInstance();
        ((View) backButton).setOnTouchListener(onTouchViewListener);
        ((View) alarmButton).setOnTouchListener(onTouchViewListener);
        ((View) deleteButton).setOnTouchListener(onTouchViewListener);
        ((View) doneButton).setOnTouchListener(onTouchViewListener);

        if (note != null) {
            noteText.setText(note.getText());
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        noteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                noteIsEdited = true;
            }
        });

        alarmButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (note != null) {
                    shortClicksPerformed = 3; //just make it greater than 2
                    DialogFragment alarmDialog = new AlarmDialog();
                    alarmDialog.show(getFragmentManager(), "AlarmDialog");
                    Log.d(TAG, "in onLongClick ---> isAlarmSelected = " + String.valueOf(alarmButton.isSelected()));
                }
                return true;
            }
        });
        Log.d(TAG, "in onCreate ---> isAlarmSelected = " + String.valueOf(alarmButton.isSelected()));

    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (noteIsEdited) {
            showConfirmationDialog(ConfirmationDialog.DIALOG_BACK,
                    "Discard changes?", "All edits will be lost");
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmationDialog(int type, String title, String message) {
        DialogFragment confirmationDialog = new ConfirmationDialog();
        Bundle dialogDataBundle = new Bundle();
        dialogDataBundle.putInt("type", type);
        dialogDataBundle.putString("title", title);
        dialogDataBundle.putString("message", message);
        confirmationDialog.setArguments(dialogDataBundle);
        confirmationDialog.show(getFragmentManager(), "ConfirmationDialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                Log.d(TAG, "Done clicked");
                String newNoteText = noteText.getText().toString();
                if (!newNoteText.isEmpty()) {
                    if (note != null) {
                        if (noteIsEdited) {
                            note.setText(newNoteText);
                            helper.updateNote(note);
                            if (alarmButton.isSelected())
                                notificationAlarmManager.scheduleAlarm(frequency, alarmRepeatCount);
                            else notificationAlarmManager.cancelAlarm(alarmRepeatCount);
                            finish();
                        }
                    } else {
                        helper.addNote(new Note(newNoteText));
                        finish();
                    }
                }
                break;
            case R.id.delete_note_button:
                Log.d(TAG, "Delete clicked");
                showConfirmationDialog(ConfirmationDialog.DIALOG_DEL,
                        "Delete note?", "This can't be undone");
                break;
            case R.id.back_button:
                Log.d(TAG, "Back clicked");
                if (noteIsEdited) {
                    showConfirmationDialog(ConfirmationDialog.DIALOG_BACK,
                            "Discard changes?", "All edits will be lost");
                } else finish();
                break;
            case R.id.set_alarm_button:
                if (note != null) {
                    noteIsEdited = true;
                    shortClicksPerformed++;
                    if (shortClicksPerformed == 2) {
                        Toast.makeText(getApplicationContext(),
                                "Hold to setup a reminder", Toast.LENGTH_SHORT).show();
                    }
                    if (!alarmButton.isSelected()) {
                        Log.d(TAG, "Notification launched");
                        alarmButton.setColorFilter(NotesListActivity.colorSelect);
                        alarmButton.setSelected(true);
                    } else {
                        alarmButton.setColorFilter(Color.WHITE);
                        alarmButton.setSelected(false);
                    }
                    Log.d(TAG, "in onClick (short) ---> isAlarmSelected = " + String.valueOf(alarmButton.isSelected()));
                }
        }
    }

    private void startNotificationService(int noteId, int frequency, int repeatCount) {
        Log.d(TAG, "startNotificationService ---> " +
                "noteId: " + noteId + " , Frequency: " + frequency + " , Repeat: " + repeatCount);
        //noteId

    }

    private void stopNotificationService() {
    }
}

