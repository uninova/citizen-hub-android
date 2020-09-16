package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic.GenericBluetoothAgent;

import java.util.List;

public class AgentFactory {

    private List<Protocol> protocolList;
    private List<Protocol> supportedProtocols;
    private List<Agent> agentList;

    public AgentFactory() {

    }


    public AgentFactory(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }


    //ver nome se é conhecido e inicializar os agents
//    public void createAgent(){
//        for (Feature feature:featureList) {
//            if (supportedFeatures.contains(feature) == true) {//create genericAgent
//
//            } else if (supportedFeatures.contains(feature) == 1) {//create Miband2Agent
//                //...
//            }
//        }
//
//        }

    public List<Protocol> getFeatureList() {
        return protocolList;
    }

    public void setFeatureList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }

    // create


    // gerar miband, hexoskin, ?? (escolher atrás dos serviços e caracteristicas)
}
