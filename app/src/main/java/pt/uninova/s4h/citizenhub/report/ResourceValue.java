package pt.uninova.s4h.citizenhub.report;

public class ResourceValue implements LocalizedResource {

    private final String value;

    public ResourceValue(String value){
        this.value = value;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString() {
        return value;
    }
}
