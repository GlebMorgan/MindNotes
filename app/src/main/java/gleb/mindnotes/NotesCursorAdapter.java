package gleb.mindnotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NotesCursorAdapter extends CursorAdapter {

    public NotesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView title = (TextView) view.findViewById(R.id.note_title);

        // Extract properties from cursor
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow("text"));

        // Populate fields with extracted properties
        title.setText(getTitle(noteText));
    }

    private String getTitle(String text) {
        // check if starts with a newline
        text = text.trim();

        // contains newline character
        if (!(text.indexOf('\n') < 0)) {
            return text.substring(0, text.indexOf('\n'));
        }

        if (text.length() < 20) {
            return text;
        } else {
            return text.substring(0, 20);
        }
    }

}