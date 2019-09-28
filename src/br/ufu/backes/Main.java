package br.ufu.backes;

import br.ufu.backes.util.Classification;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
//        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//        System.out.println("File path:");
//        String filepath = myObj.nextLine();
//        System.out.println("Number of Neiughbors:");
//        int numberOfNeighbors = myObj.nextInt();
//        System.out.println("Number of Folds:");
//        int folds = myObj.nextInt();
//
//        System.out.println("Normalise data?(y/n)");
//        String option = myObj.next();
//        boolean normalize;
//        if("y".equals(option)){
//            normalize = true;
//        } else {
//            normalize = false;
//        }

//        System.out.println("Debug mode?(y/n)");
//        String debugModeStr = myObj.next();
//        boolean debugMode;
//        if("y".equals(debugModeStr)){
//            debugMode = true;
//        } else {
//            debugMode = false;
//        }
//        Classification c = new Classification(filepath, numberOfNeighbors, folds, normalize, debugMode);
//        c.avaliarQualidadeDeClassificador();

//        Classification c = new Classification("src/br/ufu/backes/resources/data.txt", 5, 15, true);

        Classification c = new Classification("src/br/ufu/backes/resources/data.txt", 15, 2, true, true);

        c.avaliarQualidadeDeClassificador();
    }
}
