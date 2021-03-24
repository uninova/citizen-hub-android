package pt.uninova.s4h.citizenhub.connectivity.wearos;

public interface WearOSScannerListener {
    void onDeviceFound(String address, String name);
}
