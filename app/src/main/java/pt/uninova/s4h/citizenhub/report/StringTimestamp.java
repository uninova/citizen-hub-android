package pt.uninova.s4h.citizenhub.report;

public class LabelTimestamp implements LocalizedLabel{

    private final String timestamp;

    public LabelTimestamp(String timestamp){
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
