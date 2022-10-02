package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2AgentMatcher;

public class BluetoothAgentMatchers {

    private static final List<AgentMatcher> agentList = new ArrayList<>();
    private static final MiBand2AgentMatcher miBand2AgentMatcher = new MiBand2AgentMatcher();
    private final BluetoothConnection connection;
    //        static {
//            agentList = Collections.unmodifiableList(Arrays.asList(new MiBand2AgentMatcher()));
//        }


    public BluetoothAgentMatchers(BluetoothConnection connection) {
        this.connection = connection;
        agentList.add(miBand2AgentMatcher);
    }

    public Class<?> runAgentMatchers() {
        Class<?> matchedAgent = null;
        for (AgentMatcher agent : agentList
        ) {
            if (agent.doesMatch(connection)) {
                matchedAgent = agent.getAgentClass();
            }
        }
        return matchedAgent;
    }

//    public Class<?> matchAgent() {
//        Class<?> key = null;
//        for (Map.Entry<Class<?>, List<UUID>> entry : agentList.entrySet()) {
//                boolean doesMatch = true;
//            for (UUID uuid: entry.getValue()
//                 ) {
//                if(serviceList.contains(uuid)){
//                    System.out.println("Possible agent: "+ entry.getKey() + "\nHas service: " + uuid);
//                }
//                else{
//                    System.out.println("XXX Not a: "+ entry.getKey() + "\nXXX No service: " + uuid);
//                    doesMatch = false;
//                    key = null;
//                }
//                if(doesMatch) {
//                    System.out.println("MATCHOU: " + key);
//                    key = entry.getKey();
//                }
//            }
//        }
//        return key;
//    }
//
//    public List<UUID> serviceToUUIDList(List<BluetoothGattService> serviceList) {
//        List<UUID> uuidList = new ArrayList<>();
//        for (BluetoothGattService service : serviceList
//        ) {
//            uuidList.add(service.getUuid());
//        }
//        System.out.println("Lista: " + uuidList);
//        return uuidList;
//    }
//
//    private void fillAgentList() {
//        agentList.put(MiBand2Agent.class, setMiBand2Services());
//        agentList.put(KbzBodyProtocol.class, setKbzServices());
//        agentList.put(DigitsoleAgent.class, setDigitSoleServices());
//    }
//
//    private List<UUID> setDigitSoleServices() {
//        List<UUID> digitSoleUUIDS = new ArrayList<>();
//        digitSoleUUIDS.add(DigitsoleActivityProtocol.UUID_SERVICE_DATA);
//        return digitSoleUUIDS;
//    }
//
//    private List<UUID> setMiBand2Services() {
//        List<UUID> miBandUUIDS = new ArrayList<>();
//        miBandUUIDS.add(MiBand2Agent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1);
//        miBandUUIDS.add(MiBand2Agent.XIAOMI_MIBAND2_SERVICE_AUTH);
//        miBandUUIDS.add(MiBand2Agent.UUID_SERVICE_HEART_RATE);
//        return miBandUUIDS;
//    }
//
//    private List<UUID> setKbzServices() {
//        List<UUID> kbzUUIDS = new ArrayList<>();
//        kbzUUIDS.add(KbzBodyProtocol.KBZ_SERVICE);
//        return kbzUUIDS;
//    }
//
//    public List<UUID> getServiceList() {
//        return serviceList;
//    }
//
//    public void setServiceList(List<UUID> serviceList) {
//        this.serviceList = serviceList;
//    }
//
//
//    public Map<Class<?>, List<UUID>> getAgentList() {
//        return agentList;
//    }
//
//    public void setAgentList(Map<Class<?>, List<UUID>> agentList) {
//        this.agentList = agentList;
//    }
}
