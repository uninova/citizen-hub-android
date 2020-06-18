package pt.uninova.s4h.citizenhub.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    SQLiteDatabase db;
    private MeasurementsTable measurementsTable;
    private SourceTable sourceTable;
    private DeviceTable deviceTable;

    public DatabaseManager(Context context, String filePath) {
        super(context, filePath, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
        deviceTable = new DeviceTable(db);
        measurementsTable = new MeasurementsTable(db);
        sourceTable = new SourceTable(db);
    }

    public void createTables(SQLiteDatabase db) {
        DeviceTable.initialize(db);
        MeasurementsTable.initialize(db);
        SourceTable.initialize(db);
    }

    public DeviceTable getDeviceTable() {
        return this.deviceTable;
    }

    public MeasurementsTable getMeasurementsTable() {
        return this.measurementsTable;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement
        createTables(db);

    }

    public SourceTable getSourceTable() {
        return this.sourceTable;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeTables(db);
    }


    public void upgradeTables(SQLiteDatabase db) {
        DeviceTable.upgrade(db);
        MeasurementsTable.upgrade(db);
        SourceTable.upgrade(db);
        onCreate(db);
    }


}