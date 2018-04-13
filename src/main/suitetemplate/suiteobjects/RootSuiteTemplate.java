package main.suitetemplate.suiteobjects;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "suite")
public class RootSuiteTemplate {

    private String name;
    private String parallel;
    private String threadCount;
    private String verbose;
    private String groupByInstance;
    private String configFailurePolicy;
    private List<TestTemplate> tests;
    private List<ListenerTemplate> listeners;

    @XmlAttribute
    public void setName(String name){
        this.name=name;
    }

    @XmlAttribute
    public void setParallel(String parallel){
        this.parallel=parallel;
    }

    @XmlAttribute(name = "thread-count")
    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    @XmlAttribute
    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }

    @XmlAttribute(name="group-by-instances")
    public void setGroupByInstance(String groupByInstance) {
        this.groupByInstance = groupByInstance;
    }

    @XmlAttribute(name = "configfailurepolicy")
    public void setConfigFailurePolicy(String configFailurePolicy) {
        this.configFailurePolicy = configFailurePolicy;
    }

    @XmlElement(name = "test")
    public void setTests(List<TestTemplate> tests){
        this.tests = tests;
    }

    @XmlElement(name = "listener")
    @XmlElementWrapper(name = "listeners")
    public void setListeners(List<ListenerTemplate> listeners){
        this.listeners = listeners;
    }

    public String getName() {
        return name;
    }

    public String getParallel() {
        return parallel;
    }

    public String getThreadCount() {
        return threadCount;
    }

    public String getVerbose() {
        return verbose;
    }

    public String getGroupByInstance() {
        return groupByInstance;
    }

    public String getConfigFailurePolicy() {
        return configFailurePolicy;
    }

    public List<TestTemplate> getTests(){
        return tests;
    }

    public List<ListenerTemplate> getListeners(){
        return listeners;
    }
}
