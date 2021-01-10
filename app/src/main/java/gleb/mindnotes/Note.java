package gleb.mindnotes;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String text;
    private int id;

    public Note(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public Note(String text) {
        this.text = text;
    }

    public Note() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ", " + text;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.id);
    }

    protected Note(Parcel in) {
        this.text = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}

