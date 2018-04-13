package main.suitetemplate;

import main.suitetemplate.suiteobjects.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QTPTemplateWriter {

    private static final String SUITE_FILE_NAME="testng-run-legacyqtp.xml";
    private static final String QTP_PROPERTIES_FILE_NAME="powerschool-legacyQtp-template.properties";
    private static final String QTP_HEADER = "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">";
    private static final String GROUP_REGEX = "psqa\\.sis\\.testsuite\\.legacyqtpsuite\\.(.*?)\\.";
    private static final String LOGIN_PARAMETER = "login";

    private RootSuiteTemplate rootSuiteTemplate;
    private List<TestTemplate> tests;
    private TreeMap<String, List<String>> groups;

    public QTPTemplateWriter(List<String> failedClasses) throws JAXBException{
        //Create the root Element
        rootSuiteTemplate = new RootSuiteTemplate();
        rootSuiteTemplate.setName("legacyqtp");
        rootSuiteTemplate.setParallel("tests");
        rootSuiteTemplate.setThreadCount("13");
        rootSuiteTemplate.setVerbose("2");
        rootSuiteTemplate.setGroupByInstance("true");
        rootSuiteTemplate.setConfigFailurePolicy("continue");

        //Create groups and classes
        createGroupMap(failedClasses);

        //Create Test elements
        tests = new ArrayList<>();
        int loginId=0;
        for (Map.Entry<String, List<String>> h: groups.entrySet()) {
            loginId++;
            createTestElement(h.getKey(),h.getValue(), loginId);
        }

        rootSuiteTemplate.setTests(tests);

        ArrayList<String> listeners = new ArrayList<>();
        listeners.add("org.uncommons.reportng.HTMLReporter");
        listeners.add("org.uncommons.reportng.JUnitXMLReporter");
        listeners.add("psqa.testfw.testng.MethodListener");
        listeners.add("psqa.testfw.testng.ConfigurationListener");
        listeners.add("psqa.testfw.testng.TestNGListenerLogger");

        ArrayList<ListenerTemplate> listenerTemplates = new ArrayList<>();
        //Create Parameter Elements
        listeners.forEach(s ->{
            ListenerTemplate listenerTemplate = new ListenerTemplate();
            listenerTemplate.setClassName(s);
            listenerTemplates.add(listenerTemplate);
        });
        rootSuiteTemplate.setListeners(listenerTemplates);
    }

    private void createGroupMap(List<String> failedClasses){
        groups = new TreeMap<>();
        Pattern pattern = Pattern.compile(GROUP_REGEX);
        List<String> keys = new ArrayList<>();
        //Get list of keys
        failedClasses.forEach(s ->{
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()){
                keys.add(matcher.group(1));
            }
        });

        keys.forEach(s ->{
            List<String> filteredList = failedClasses.stream().filter(s1 -> s1.contains("."+s+".")).collect(Collectors.toList());
            groups.put(s, filteredList);
        });
    }

    private void createTestElement(String key, List<String> values, int loginId) throws JAXBException{
        //Create Class Elements
        ArrayList<ClassTemplate> classes = new ArrayList<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(ClassTemplate.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        for (String v : values) {
            StringReader sr = new StringReader(v);
            ClassTemplate classTemplate = (ClassTemplate) unmarshaller.unmarshal(sr);
            classes.add(classTemplate);
        }

        //Create Parameter Elements
        ArrayList<ParameterTemplate> parameters = new ArrayList<>();
        ParameterTemplate parameterTemplate = new ParameterTemplate();
        parameterTemplate.setName(QTP_PROPERTIES_FILE_NAME);
        parameterTemplate.setValue(QTP_PROPERTIES_FILE_NAME);
        parameters.add(parameterTemplate);
        parameterTemplate = new ParameterTemplate();
        parameterTemplate.setName(LOGIN_PARAMETER);
        parameterTemplate.setValue("a"+loginId+";a"+loginId);
        parameters.add(parameterTemplate);

        TestTemplate testTemplate = new TestTemplate();
        testTemplate.setTestName(key);
        testTemplate.setClasses(classes);
        testTemplate.setParameters(parameters);
        tests.add(testTemplate);
    }

    public void createQTPFile() throws JAXBException, IOException{
        try(FileOutputStream fos = new FileOutputStream(SUITE_FILE_NAME)){
            //Format new XML file
            JAXBContext jaxbContext = JAXBContext.newInstance(RootSuiteTemplate.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", QTP_HEADER);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            //Print elements to output file
            marshaller.marshal(rootSuiteTemplate, fos);
        }
    }
}
