package br.ufu.backes.util;

import br.ufu.backes.model.Element;
import br.ufu.backes.model.KFold;
import br.ufu.backes.model.KNearestNeighbor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Classification {

    private List<Element> dataSet;
    private KFold kFold;
    private KNearestNeighbor kNN;
    private int numberOfElements;
    private int numberOfAttributes;
    private boolean debugMode;

    public Classification(String filePath, int numberOfNeighbors, int folds, boolean normalize, boolean debugMode){
        this.debugMode = debugMode;

        fillDataSetAndParamsFromFile(filePath);
        kNN = new KNearestNeighbor(numberOfNeighbors, numberOfElements, numberOfAttributes, normalize, debugMode);
        kNN.setDataSetAndNormalize(dataSet, normalize);

        kFold = new KFold(folds, debugMode);
    }

    public Element classifyObject(double... attributes){
        Element object = new Element(attributes);

        kNN.classifyUnknownObject(object);
        return object;
    }

    public void avaliarQualidadeDeClassificador() throws Exception {
        kFold.avaliarQualidadeDeClassificador(kNN);
        double percentRightGuesses = (kFold.getRightGuesses() * 100.0)/numberOfElements;
        double percentWrongGuesses = (kFold.getWrongGuesses() * 100.0)/numberOfElements;
        System.out.println("Correctly Classified Instances:\t\t" + kFold.getRightGuesses() + "\t" + percentRightGuesses + " %");
        System.out.println("Incorrectly Classified Instances:\t" + kFold.getWrongGuesses() +  "\t\t" + percentWrongGuesses + " %");
        System.out.println("Total Number of Instances:\t\t\t" + numberOfElements);
        System.out.println("=== Confusion Matrix ===");


    }

    public void fillDataSetAndParamsFromFile(String filePath){
        dataSet = new ArrayList<Element>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] firstLineValues  = scanner.nextLine().split(" ");
        numberOfElements = Integer.parseInt(firstLineValues[0]);
        numberOfAttributes = Integer.parseInt(firstLineValues[1]);

        int j = 0;
        while(scanner.hasNextLine()){
            //process each line
            String[] lineValues = scanner.nextLine().split(" ");
            Element e = arrayOfStringToElement(lineValues);
            e.setId(j);
            dataSet.add(e);
            j++;
        }
        scanner.close();
    }

    private Element arrayOfStringToElement(String[] strArray){
        Element element;
        //getting the elementClass
        int elementClass = Integer.parseInt(strArray[strArray.length-1]);

        //eliminating last element, that is related to the elementClass
        strArray = Arrays.copyOfRange(strArray, 0, (strArray.length-1));

        double[] doubleArray = new double[strArray.length];
        for(int i = 0; i < strArray.length; i++){
            doubleArray[i] = Double.parseDouble(strArray[i]);
        }
        element = new Element(String.valueOf(elementClass),doubleArray);
        return element;
    }

    public List<Element> getDataSet() {
        return dataSet;
    }

}
