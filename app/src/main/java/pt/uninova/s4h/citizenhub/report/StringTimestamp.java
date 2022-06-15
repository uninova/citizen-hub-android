package pt.uninova.s4h.citizenhub.report;

public class StringTimestamp implements LocalizedString {

    private final String timestamp;

    public StringTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString() {
        return timestamp;
    }

}
