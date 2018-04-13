package main.suitetemplate.suiteobjects;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "test")
@XmlType(propOrder = {"parameters","classes"})
public class TestTemplate {

    private String testName;
    private List<ParameterTemplate> parameters;
    private List<ClassTemplate> classes;

    public String getTestName() {
        return testName;
    }

    @XmlAttribute(name = "name")
    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<ParameterTemplate> getParameters() {
        return parameters;
    }

    @XmlElement(name = "parameter")
    public void setParameters(List<ParameterTemplate> parameters) {
        this.parameters = parameters;
    }

    public List<ClassTemplate> getClasses() {
        return classes;
    }

    @XmlElementWrapper(name = "classes")
    @XmlElement(name = "class")
    public void setClasses(List<ClassTemplate> classes) {
        this.classes = classes;
    }
}
