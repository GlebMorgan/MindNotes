package gleb.mindnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NOTES.db";

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_TEXT = "text";

    public static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_TEXT + " TEXT)";

    public static final String DELETE_TABLE_NOTES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Wipe older tables if existed
            db.execSQL(DELETE_TABLE_NOTES);
            // Create tables again
            onCreate(db);
        }
    }

    /**
     *
     * @param note
     */
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase(); //(here "writable" means available on write)
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TEXT, note.getText());
        db.insert(TABLE_NAME, null, values);
        this.close();
    }

    /**
     *
     * @return
     */
    public Cursor getNotesCursor() {
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ID
                + " = " + '"' + id + '"';
        Cursor cursor = db.rawQuery(query, null);

        Note note = new Note();
        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setText(cursor.getString(1));
        }
        cursor.close();
        this.close();
        return note;
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id" + "=" + id, null);
        this.close();
    }

    public int updateNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TEXT, note.getText());
        int updateRowsCount = db.update(TABLE_NAME, values, COLUMN_NAME_ID + "= ?", new String[] {String.valueOf(note.getId())});
        this.close();
        return updateRowsCount;
    }
}
