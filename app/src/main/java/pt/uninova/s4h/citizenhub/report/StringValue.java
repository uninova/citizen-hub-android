package pt.uninova.s4h.citizenhub.report;

public class StringValue implements LocalizedString{

    private final String value;

    public StringValue (String value){
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
