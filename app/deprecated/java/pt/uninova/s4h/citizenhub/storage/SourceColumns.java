package pt.uninova.s4h.citizenhub.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SourceColumns {

    public static final String CONTENT_AUTHORITY = "pt.uninova.s4h.citizenhub";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SOURCE = "source";

    private SourceColumns() {
    }


    public static abstract class SourceEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SOURCE);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOURCE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOURCE;

        public final static String TABLE_NAME = "source";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SOURCE_UUID = "uuid";
        public final static String COLUMN_DEVICE_UUID = "address";
        public final static String COLUMN_SOURCE_TYPE = "type";
        public final static String COLUMN_SOURCE_INTERVAL = "interval";
    }

}
