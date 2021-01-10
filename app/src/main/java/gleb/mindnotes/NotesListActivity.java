package gleb.mindnotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NotesListActivity extends AppCompatActivity {

    private static final String TAG = NotesListActivity.class.getSimpleName();
    static final String SELECTED_NOTE = "NOTE";

    private ListView notesList;
    private ImageView newNoteButton;
    private TextView noNotesMessage;
    private NotesCursorAdapter adapter;

    private DatabaseHelper helper;
    private Cursor cursor;

    static int colorHighlight;
    static int colorSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        colorHighlight = getResources().getColor(R.color.colorHighlight);
        colorSelect = getResources().getColor(R.color.colorSelect);

        helper = new DatabaseHelper(NotesListActivity.this);
        //TODO: singletone scenario? Here and in other "Notes" classes
        cursor = helper.getNotesCursor();

        noNotesMessage = (TextView) findViewById(R.id.no_notes_text);
        newNoteButton = (ImageView) findViewById(R.id.new_note_button);
        notesList = (ListView) findViewById(R.id.notes_list);

        ((View) newNoteButton).setOnTouchListener(OnTouchViewListener.getInstance());

        // Create adapter passing in the sample user data
        adapter = new NotesCursorAdapter(this, cursor);
        // Attach the adapter to the listview to populate items
        notesList.setAdapter(adapter);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = helper.getNote((int) id);
                Intent intent = new Intent(NotesListActivity.this, NoteActivity.class);
                intent.putExtra(SELECTED_NOTE, note);
                startActivity(intent);
            }
        });
        //TODO: why item click listener is in AdapterView class, not ListView?

        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesListActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        cursor.close();
        helper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "In onResume");
        cursor = helper.getNotesCursor();
        if (cursor.getCount() > 0) {
            noNotesMessage.setVisibility(View.GONE);
        } else {
            noNotesMessage.setVisibility(View.VISIBLE);
        }

        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }
}
