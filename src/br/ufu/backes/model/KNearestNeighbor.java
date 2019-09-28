package br.ufu.backes.model;

import java.util.*;

public class KNearestNeighbor {

    // K is the number of nearest neighbors
    int K;
    boolean normalize;
    float attributesAverage[];
    float attributesStdDeviation[];
    private int numberOfElements;
    private int numberOfAttributes;

    private List<Element> dataSet;

    private List<Element> foldDataSet;
    private boolean useFoldDataSet = false;
    private boolean debugMode;


    public KNearestNeighbor(int K, int numberOfElements, int numberOfAttribute, boolean normalize, boolean debugMode) {
        this.K = K;
        this.numberOfElements = numberOfElements;
        this.numberOfAttributes = numberOfAttribute;
        this.normalize = normalize;
        this.debugMode = debugMode;
    }

    private void calculateElementEuclideanDistanceFromUnknow(Element element, Element unknownObject){
        double sum = 0;
        for(int i = 0; i < numberOfAttributes; i++){
            double aux = Math.abs(element.getAttribute(i) - unknownObject.getAttribute(i));
            sum += aux * aux;
        }
        element.setDistanceFromUnknowObject(Math.sqrt(sum));
    }


    public void classifyUnknownObject(Element unkownObject, boolean useFoldDataSet){
        this.useFoldDataSet = useFoldDataSet;
        logInfo("Classificando: " + unkownObject.toString());
        classifyUnknownObject(unkownObject);
        this.useFoldDataSet = false;
    }
    public void classifyUnknownObject(Element unkownObject){
        calculateAllElementsEuclideanDistanceFromUnknow(unkownObject);
        unkownObject.setElementClass(getMostPredominantClassOnNeighbors());
    }

    /**
     * Here we sort the dataSet, and get the first K.
     * And after that we just create a hashMap to count which one is the predominant class
     * between the number of selected neighbors.
     * @return the prerdominant class
     */
    private String getMostPredominantClassOnNeighbors(){
        List<Element> copyOfDataset = new ArrayList<>(getDataSet());
        Collections.sort(copyOfDataset, Comparator.comparing(Element::getDistanceFromUnknowObject));

        return getMostAppearing(copyOfDataset, K);

    }

    private String getMostAppearing(List<Element> copyOfDataset, int K) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(int i = 0; i < K; i++){//<2,2>   <3,1> //k = 4
            Element e = copyOfDataset.get(i);
            if(hashMap.containsKey(e.getElementClass())){
                hashMap.put(e.getElementClass(), hashMap.get(e.getElementClass()) + 1);
            } else {
                hashMap.put(e.getElementClass(), 1);
            }
        }

        debugDataSet();

        Integer maior = 0;
        Integer segundoMaior = 0;
        String mostAppearingClass = "";

        for(Map.Entry<String, Integer> entry : hashMap.entrySet()){
            if (entry.getValue() >= maior) {
                segundoMaior = maior;
                maior = entry.getValue();
                mostAppearingClass = entry.getKey();
            }
        }

        if(maior.equals(segundoMaior) && K > 1){
            logInfo("Diminuido K pra " + (K-1));
            return getMostAppearing(copyOfDataset, K-1);
        }


        return mostAppearingClass;
    }

    private void debugDataSet() {
        logInfo("ID  DISTANCIA   CLASSE");
        for(Element e : getDataSet()){
            logInfo(e.getId() + "    " + e.getDistanceFromUnknowObject() + "  " + e.getElementClass());
        }
    }

    private void calculateAllElementsEuclideanDistanceFromUnknow(Element unknownObject){
        for(Element element : getDataSet()){
            calculateElementEuclideanDistanceFromUnknow(element , unknownObject);
        }
    }

    private void normalizeDataSet(){
        attributesAverage = new float[numberOfAttributes];
        attributesStdDeviation = new float[numberOfAttributes];

        //calculating the average
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                attributesAverage[i] += element.getAttribute(i);
            }
        }
        for(int i = 0; i < numberOfAttributes; i++){
            attributesAverage[i] = attributesAverage[i]/(float)getDataSet().size();
        }

        //calculating the StandardDeviation
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                double aux = element.getAttribute(i) - attributesAverage[i];
                attributesStdDeviation[i] += aux * aux;
            }
        }

        for(int i = 0; i < numberOfAttributes; i++){
            attributesStdDeviation[i] = (float)Math.sqrt(attributesStdDeviation[i]/(float)(getDataSet().size()-1));
        }

        //changing element values to the normalized ones
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                element.setAttribute(i, (element.getAttribute(i) - attributesAverage[i])/attributesStdDeviation[i]);
            }
        }
    }

    public void setDataSetAndNormalize(List<Element> dataSet, boolean normalize) {
        this.dataSet = dataSet;

        if(normalize){
            normalizeDataSet();
        }
    }

    public List<Element> getDataSet() {
        if(useFoldDataSet){
            return foldDataSet;
        } else {
            return dataSet;
        }
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public List<Element> getFoldDataSet() {
        return foldDataSet;
    }

    public void setFoldDataSet(List<Element> foldDataSet) {
        this.foldDataSet = foldDataSet;
    }

    private void logInfo(String msg){
        if(debugMode){
            System.out.println(msg);
        }
    }
}
