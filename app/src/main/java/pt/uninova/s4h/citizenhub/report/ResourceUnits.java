package pt.uninova.s4h.citizenhub.report;

public class StringUnits implements LocalizedString {

    private final String units;

    public StringUnits(String units){
        this.units = units;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString() {
        return units;
    }

}
