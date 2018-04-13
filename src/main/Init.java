package main;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Init {

    public static void main(String[] args) throws IOException, JAXBException{
        if(args.length==1 && (args[0].equalsIgnoreCase("qtp")||args[0].equalsIgnoreCase("selenium"))) {
            new Controller().start(args[0]);
        }else{
            throw new IllegalArgumentException("You must specify what kind of suite file to make. Either QTP or Selenium.");
        }
    }
}
