package datastorage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DeviceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public static final String CONTENT_AUTHORITY = "pt.uninova.s4h.citizenhub";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DEVICES = "devices";

    private DeviceContract() {
    }


    public static abstract class DeviceEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DEVICES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;

        public final static String TABLE_NAME = "devices";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_DEVICE_NAME = "name";
        public final static String COLUMN_DEVICE_ADDRESS = "address";
        public final static String COLUMN_DEVICE_TYPE = "type";
        public final static String COLUMN_DEVICE_STATE = "state";


        public static final int UNKNOWN_DEVICE = 0;
        public static final int KNOWN_DEVICE = 1;
    }

}
