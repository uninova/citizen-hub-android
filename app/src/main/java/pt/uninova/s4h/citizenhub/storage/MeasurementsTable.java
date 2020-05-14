package pt.uninova.s4h.citizenhub.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import static pt.uninova.s4h.citizenhub.storage.MeasurementsColumns.MeasureEntry.COLUMN_CHARACTERISTIC_NAME;
import static pt.uninova.s4h.citizenhub.storage.MeasurementsColumns.MeasureEntry.COLUMN_MEASUREMENT_VALUE;
import static pt.uninova.s4h.citizenhub.storage.MeasurementsColumns.MeasureEntry.COLUMN_TIMESTAMP;
import static pt.uninova.s4h.citizenhub.storage.MeasurementsColumns.MeasureEntry.TABLE_NAME;
import static pt.uninova.s4h.citizenhub.storage.SourceColumns.SourceEntry.COLUMN_SOURCE_UUID;


public class MeasurementsTable {

    public static final String LOG_TAG = MeasurementsTable.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "measurements.db";

    private static final int DATABASE_VERSION = 2;
    private final SQLiteDatabase db;

    public MeasurementsTable(SQLiteDatabase db) {
        this.db = db;
    }

    public static void initialize(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_MEASUREMENTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + MeasurementsColumns.MeasureEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_SOURCE_UUID + " TEXT NOT NULL, "
                + COLUMN_TIMESTAMP + " TEXT NOT NULL, "
                + COLUMN_MEASUREMENT_VALUE + " TEXT NOT NULL, "
                + COLUMN_CHARACTERISTIC_NAME + " TEXT NOT NULL " + " ) ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MEASUREMENTS_TABLE);
    }

    public static void upgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public long addRecord(String uuid, String timestamp, String value, String name) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SOURCE_UUID, uuid);
        cv.put(COLUMN_TIMESTAMP, timestamp);
        cv.put(COLUMN_MEASUREMENT_VALUE, value);
        cv.put(COLUMN_CHARACTERISTIC_NAME, name);
        return db.insert(TABLE_NAME, null, cv);

    }

    public long addRecord(storage.MeasurementsRecord record) {
        return addRecord(record.getUuid(), record.getTimestamp(), record.getValue(), record.getName());

    }

    public void removeRecord(String value, String column) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + column + "='" + value + "'");
    }

    public void removeRecord(storage.MeasurementsRecord record) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_CHARACTERISTIC_NAME + "='" + record.getName() + "'");
    }

    public List<storage.MeasurementsRecord> getAllRows() throws Exception {
        String selectAllQuery = " SELECT uuid, timestamp, value, characteristic FROM " + TABLE_NAME;
        try (Cursor c = db.rawQuery(selectAllQuery, null)) {
            List<storage.MeasurementsRecord> list = new LinkedList<>();
            while (c.moveToNext()) {
                list.add(parseCursor(c));
            }

            return list;
        }
    }

    public storage.MeasurementsRecord getRow(String name) throws Exception {
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where characteristic =?", new String[]{name});

        if (c.moveToFirst()) {
            return parseCursor(c);
        }

        throw new Exception();
    }

    private storage.MeasurementsRecord parseCursor(Cursor c) {
        storage.MeasurementsRecord r = new storage.MeasurementsRecord(
                c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4));
        return r;
    }


    public long setRecord(String uuid, String timestamp, String value, String name) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_UUID, uuid);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_MEASUREMENT_VALUE, value);
        values.put(COLUMN_CHARACTERISTIC_NAME, name);
        return db.update(TABLE_NAME, values, "uuid=?", new String[]{uuid});
    }

    public long setRecord(storage.MeasurementsRecord record) {
        return setRecord(record.getUuid(), record.getTimestamp(), record.getValue(), record.getName());
    }

}