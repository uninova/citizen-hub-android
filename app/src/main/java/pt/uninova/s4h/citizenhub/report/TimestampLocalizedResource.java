package pt.uninova.s4h.citizenhub.report;

public class ResourceTimestamp implements LocalizedResource {

    private final String timestamp;

    public ResourceTimestamp(String timestamp){
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
