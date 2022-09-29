package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole.DigitsoleActivityProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole.DigitsoleAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzBodyProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;

public class BluetoothAgentMatcher {

    private List<UUID> serviceList;
    private Context context;
    private Map<Class<?>, List<UUID>> agentList;


    public BluetoothAgentMatcher(List<BluetoothGattService> serviceList, Context context) {
        this.serviceList = serviceToUUIDList(serviceList);
        this.context = context;
        agentList = new HashMap<>();
        fillAgentList();
    }

    private Class<?> matchAgent() {


    }

    public List<UUID> serviceToUUIDList(List<BluetoothGattService> serviceList) {
        List<UUID> uuidList = new ArrayList<>();
        for (BluetoothGattService service : serviceList
        ) {
            uuidList.add(service.getUuid());
        }
        System.out.println("Lista: " + uuidList);
        return uuidList;
    }

    private Map<Class<?>, List<UUID>> fillAgentList() {
        agentList.put(MiBand2Agent.class, setMiBand2Services());
        agentList.put(KbzBodyProtocol.class, setKbzServices());
        agentList.put(DigitsoleAgent.class, setDigitSoleServices());
        return agentList;
    }

    private List<UUID> setDigitSoleServices() {
        List<UUID> digitSoleUUIDS = new ArrayList<>();
        digitSoleUUIDS.add(DigitsoleActivityProtocol.UUID_SERVICE_DATA);
        return digitSoleUUIDS;
    }

    private List<UUID> setMiBand2Services() {
        List<UUID> miBandUUIDS = new ArrayList<>();
        miBandUUIDS.add(MiBand2Agent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1);
        miBandUUIDS.add(MiBand2Agent.XIAOMI_MIBAND2_SERVICE_AUTH);
        miBandUUIDS.add(MiBand2Agent.UUID_SERVICE_HEART_RATE);
        return setMiBand2Services();
    }

    private List<UUID> setKbzServices() {
        List<UUID> kbzUUIDS = new ArrayList<>();
        kbzUUIDS.add(KbzBodyProtocol.KBZ_SERVICE);
        return kbzUUIDS;
    }

    public List<UUID> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<UUID> serviceList) {
        this.serviceList = serviceList;
    }

    public Map<Class<?>, List<UUID>> getAgentList() {
        return agentList;
    }

    public void setAgentList(Map<Class<?>, List<UUID>> agentList) {
        this.agentList = agentList;
    }
}
