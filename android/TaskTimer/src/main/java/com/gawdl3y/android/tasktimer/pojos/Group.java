package com.gawdl3y.android.tasktimer.pojos;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A category for Task objects; contains an array of Tasks if necessary
 * @author Schuyler Cebulskie
 */
public class Group implements Parcelable {
    private String name;
    private int id, position;
    private ArrayList<Task> tasks;

    /**
     * Default constructor
     */
    public Group() {
        this("EMPTY NAME", -1, -1, null);
    }

    /**
     * Name constructor
     * @param name The name of the Group
     */
    public Group(String name) {
        this(name, -1, -1, null);
    }

    /**
     * ID constructor
     * @param id The ID of the Group
     */
    public Group(int id) {
        this("EMPTY NAME", id, -1, null);
    }

    /**
     * Fill constructor
     * @param name     The name of the Group
     * @param id       The ID of the Group
     * @param position The position of the Group
     * @param tasks    The tasks in the Group
     */
    public Group(String name, int id, int position, ArrayList<Task> tasks) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.tasks = tasks;
    }

    /**
     * Parcel constructor
     * @param parcel The parcel to read from
     */
    public Group(Parcel parcel) {
        readFromParcel(parcel);
    }


    /**
     * Gets the name of the Group
     * @return The name of the Group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Group
     * @param name The name of the Group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the Group for database use
     * @return The ID of the Group
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the Group for database use
     * @param id The ID of the Group
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the position of the Group
     * @return The position of the Group
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of the group
     * @param position The position of the group
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the Tasks assigned to the Group
     * @return the tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the Tasks assigned to the Group
     * @param tasks the tasks to set
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Fills the Group from a parcel
     * @param in The parcel to read from
     */
    private void readFromParcel(Parcel in) {
        name = in.readString();
        tasks = in.createTypedArrayList(Task.CREATOR);
        position = in.readInt();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(tasks);
        dest.writeInt(position);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != getClass()) return false;
        return id == ((Group) obj).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Group { name=\"" + name + "\" id=" + id + " position=" + position + "}";
    }

    /**
     * Creates a ContentValues object from this Group to use for the database
     * @return The ContentValues for this Group
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Columns._ID, id);
        values.put(Columns.NAME, name);
        values.put(Columns.POSITION, position);
        return values;
    }


    /**
     * The Parcel creator used to create new instances of the Group from a parcel
     */
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };


    /**
     * Comparator for comparing group names
     */
    public static final Comparator<Group> NAME_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            return g1.getName().compareTo(g2.getName());
        }
    };

    /**
     * Comparator for comparing group positions
     */
    public static final Comparator<Group> POSITION_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            if(g1.getPosition() < g2.getPosition()) return -1;
            if(g1.getPosition() > g2.getPosition()) return 1;
            return 0;
        }
    };

    /**
     * Comparator for comparing group IDs
     */
    public static final Comparator<Group> ID_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            if(g1.getId() < g2.getId()) return -1;
            if(g1.getId() > g2.getId()) return 1;
            return 0;
        }
    };

    /**
     * Database columns for Groups
     * @author Schuyler Cebulskie
     */
    public static final class Columns implements BaseColumns {
        /**
         * The content:// URL for the table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://com.gawdl3y.android.tasktimer.provider/groups");

        /**
         * The name of the Group (Type: STRING)
         */
        public static final String NAME = "name";

        /**
         * The position of the Group (Type: INTEGER)
         */
        public static final String POSITION = "position";

        /*
         * These save calls to cursor.getColumnIndexOrThrow()
         * THEY MUST BE KEPT IN SYNC WITH ABOVE QUERY COLUMNS
         */
        public static final int ID_INDEX = 0;
        public static final int NAME_INDEX = 1;
        public static final int POSITION_INDEX = 2;

        /**
         * The default sort order for queries
         */
        public static final String DEFAULT_SORT_ORDER = POSITION + " ASC";
    }
}
