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

    public Classification(String filePath, int numberOfNeighbors, int folds, boolean normalize){
        fillDataSetAndParamsFromFile(filePath);
        kNN = new KNearestNeighbor(numberOfNeighbors, numberOfElements, numberOfAttributes, normalize);
        kNN.setDataSetAndNormalize(dataSet, normalize);

        kFold = new KFold(folds);
    }

    public Element classifyObject(double... attributes){
        Element object = new Element(attributes);

        kNN.classifyUnknownObject(object);
        return object;
    }

    public void avaliarQualidadeDeClassificador() throws Exception {
        kFold.avaliarQualidadeDeClassificador(kNN);
        System.out.println("Acertos: " + kFold.getRightGuesses());
        System.out.println("Erros: " + kFold.getWrongGuesses());

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
