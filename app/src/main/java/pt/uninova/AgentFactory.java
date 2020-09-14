package pt.uninova;

import java.util.List;

public class AgentFactory {

    private List<Feature> featureList;
    private List<Feature> supportedFeatures;

    public AgentFactory(List<Feature> featureList) {
        this.featureList = featureList;
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

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    // create


    // gerar miband, hexoskin, ?? (escolher atrás dos serviços e caracteristicas)
}
