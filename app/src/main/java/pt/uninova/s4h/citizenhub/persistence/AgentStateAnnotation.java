package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AgentStateAnnotation {

        public static final int DISABLED = 0;
        public static final int INACTIVE = 1;
        public static final int ACTIVE = 2;

        @IntDef({DISABLED, INACTIVE, ACTIVE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface StateAnnotation {
        }

}
