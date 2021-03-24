package pt.uninova.s4h.citizenhub.connectivity.wearos;

import pt.uninova.s4h.citizenhub.persistence.Device;

public abstract class WearOSDevice {
    public final Device device;

    protected WearOSDevice(Device d){
        device=d;

    }

    protected String getAddress(){
        return device.getAddress();
    }
}
