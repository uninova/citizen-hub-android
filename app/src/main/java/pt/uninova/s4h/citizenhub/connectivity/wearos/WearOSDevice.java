package pt.uninova.s4h.citizenhub.connectivity.wearos;

import pt.uninova.s4h.citizenhub.persistence.DeviceRecord;

public abstract class WearOSDevice {
    public final DeviceRecord deviceRecord;

    protected WearOSDevice(DeviceRecord d){
        deviceRecord =d;

    }

    protected String getAddress(){
        return deviceRecord.getAddress();
    }
}
