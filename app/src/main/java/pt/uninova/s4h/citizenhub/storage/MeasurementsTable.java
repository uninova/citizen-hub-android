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

    private static final int DATABASE_VERSION = 1;
    private final SQLiteDatabase db;

    public MeasurementsTable(SQLiteDatabase db) {
        this.db = db;
    }

    public static void initialize(SQLiteDatabase db) {
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

    public long addRecord(MeasurementsRecord record) {
        return addRecord(record.getUuid(), record.getTimestamp(), record.getValue(), record.getName());

    }

    public List<MeasurementsRecord> getAllRecords() throws Exception {
        String selectAllQuery = " SELECT uuid, timestamp, value, characteristic FROM " + TABLE_NAME;
        try (Cursor c = db.rawQuery(selectAllQuery, null)) {
            List<MeasurementsRecord> list = new LinkedList<>();
            while (c.moveToNext()) {
                list.add(parseCursor(c));
            }

            return list;
        }
    }

    public void getRecordsAverageDay(String columnName, String date) throws Exception {
        Cursor c = db.rawQuery("select avg(columnName) from " + TABLE_NAME + " where timestamp =?", new String[]{date});
    }

    public void getRecordsSumDay(String columnName, String date) throws Exception {
        Cursor c = db.rawQuery("select sum(columnName) from " + TABLE_NAME + " where timestamp =?", new String[]{date});
    }


    public List<MeasurementsRecord> getAllRecordsWithCharacteristic(String characteristic) throws Exception {

        try (Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where characteristic =?", new String[]{characteristic})) {
            List<MeasurementsRecord> list = new LinkedList<>();
            while (c.moveToNext()) {
                list.add(parseCursor(c));
            }

            return list;
        }
    }

    public MeasurementsRecord getRecord(String name) throws Exception {
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where characteristic =?", new String[]{name});

        if (c.moveToFirst()) {
            return parseCursor(c);
        }

        throw new Exception();
    }

    private MeasurementsRecord parseCursor(Cursor c) {
        MeasurementsRecord r = new MeasurementsRecord(
                c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4));
        return r;
    }

    public void removeRecord(String value, String column) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + column + "='" + value + "'");
    }

    public void removeRecord(MeasurementsRecord record) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_CHARACTERISTIC_NAME + "='" + record.getName() + "'");
    }

    public long setRecord(String uuid, String timestamp, String value, String name) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_UUID, uuid);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_MEASUREMENT_VALUE, value);
        values.put(COLUMN_CHARACTERISTIC_NAME, name);
        return db.update(TABLE_NAME, values, "uuid=?", new String[]{uuid});
    }

    public long setRecord(MeasurementsRecord record) {
        return setRecord(record.getUuid(), record.getTimestamp(), record.getValue(), record.getName());
    }

}