package main;

import main.filehandlers.FileHelper;
import main.suitetemplate.QTPTemplateWriter;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;

public class Controller {

    private QTPTemplateWriter qtpTemplateWriter;

    private static final String FAIL_LOG_LOCATION = "skipfail-results.log";
    private static final String TEMP_XML_LOCATION = "tempFile.xml";

    public void start(String type) throws IOException, JAXBException{
        List<String> failedClasses = FileHelper.getListOfFailedTestClasses(FAIL_LOG_LOCATION);
        if(type.equalsIgnoreCase("qtp")) {
            qtpTemplateWriter = new QTPTemplateWriter(failedClasses);
            qtpTemplateWriter.createQTPFile();
        }
        else if(type.equalsIgnoreCase("selenium")){

        }else{
            throw new IllegalArgumentException("You somehow entered something other than QTP or Selenium as an option");
        }
    }

    private void createTempFile(List<String> lines) throws IOException{
        //Write the new rows into a new file
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(TEMP_XML_LOCATION))){
            for (String row : lines) {
                bw.write(row);
                bw.newLine();
            }
        }
    }
}
