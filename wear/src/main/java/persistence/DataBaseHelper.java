package persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String MEASUREMENT_TABLE = "MEASUREMENT_TABLE";
    public static final String COLUMN_VALUE = "VALUE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_KIND = "KIND";
    public static final String COLUMN_IS_SENT = "IS_SENT";
    public static final String COLUMN_TIME_STAMP = "TIME_STAMP";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "Measurements.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + MEASUREMENT_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_KIND + " TEXT, " + COLUMN_VALUE + " INT, " + COLUMN_IS_SENT + " BOOL, " + COLUMN_TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableStatement);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Measurement measurement){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_KIND, measurement.getKind());
        cv.put(COLUMN_VALUE, measurement.getValue());
        cv.put(COLUMN_IS_SENT, measurement.getSent());


        long insert = db.insert(MEASUREMENT_TABLE,null ,cv);
        if (insert== -1){
            return false;
        }
        else {
            return true;
        }
    }
    public int getUnsentSize(){
        String queryString = "SELECT * FROM " + MEASUREMENT_TABLE + " WHERE " + COLUMN_IS_SENT + "= 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        return cursor.getCount();
    }
    public ContentValues getUnsent(){

        String queryString = "SELECT * FROM " + MEASUREMENT_TABLE + " WHERE " + COLUMN_IS_SENT + "= 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        ContentValues contentValues = new ContentValues();
        cursor.moveToFirst();


        if(cursor != null && cursor.getCount()>2) {
            while(cursor != null && cursor.getCount()>0) {
                int cursorID = cursor.getInt(0);
                String cursorKind = cursor.getString(1);
                Double cursorValue = cursor.getDouble(2);
                boolean cursorIsSent = true;
                Timestamp cursorTimeStamp = Timestamp.valueOf(cursor.getString(4));

                contentValues.put(COLUMN_ID, cursorID);
                contentValues.put(COLUMN_KIND, cursorKind);
                contentValues.put(COLUMN_VALUE, cursorValue);
                contentValues.put(COLUMN_IS_SENT, cursorIsSent);
                contentValues.put(COLUMN_TIME_STAMP, String.valueOf(cursorTimeStamp));

                String stringCursorID = cursorID + "";
                db.update(MEASUREMENT_TABLE, contentValues, "ID = ?", new String[]{stringCursorID});
                cursor.moveToNext();
                return contentValues;
            }
        }else{
            int cursorID = cursor.getInt(0);
            String cursorKind = cursor.getString(1);
            Double cursorValue = cursor.getDouble(2);
            boolean cursorIsSent = true;
            Timestamp cursorTimeStamp = Timestamp.valueOf(cursor.getString(4));

            contentValues.put(COLUMN_ID, cursorID);
            contentValues.put(COLUMN_KIND, cursorKind);
            contentValues.put(COLUMN_VALUE, cursorValue);
            contentValues.put(COLUMN_IS_SENT, cursorIsSent);
            contentValues.put(COLUMN_TIME_STAMP, String.valueOf(cursorTimeStamp));

            String stringCursorID = cursorID + "";
            db.update(MEASUREMENT_TABLE, contentValues, "ID = ?", new String[]{stringCursorID});
        }



        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();

        return contentValues;
    }
}
