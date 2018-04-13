package main.filehandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHelper {

    private FileHelper(){
        throw new IllegalStateException("Cannot instantiate helper class.");
    }

    public static List<String> getListOfFailedTestClasses(String failLogLocation) throws IOException {
        ArrayList<String> rows = new ArrayList<>();
        try(Scanner sc = new Scanner(new FileInputStream(new File(failLogLocation)))) {
            while (sc.hasNextLine()) {
                if (sc.nextLine().contains("Reason: FAIL")) {
                    //If test failed, write the class xml line underneath this line
                    String xmlLine =sc.nextLine();
                    if(!rows.contains(xmlLine)) {
                        rows.add(xmlLine);
                    }
                } else {
                    //Otherwise skip this line
                    sc.nextLine();
                }
            }
        }
        //When done adding rows to arraylist, we sort
        rows.sort(String.CASE_INSENSITIVE_ORDER);
        return rows;
    }
}
