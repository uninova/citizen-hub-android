package pt.uninova.s4h.citizenhub.report;

public class StringType implements LocalizedString {

    private final String type;

    public StringType(String type){
        this.type = type;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString() {
        return type;
    }

}
