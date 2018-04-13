package main.suitetemplate.suiteobjects;

import javax.xml.bind.annotation.XmlAttribute;

public class ListenerTemplate {

    private String className;

    public String getClassName() {
        return className;
    }

    @XmlAttribute(name = "class-name")
    public void setClassName(String className) {
        this.className = className;
    }
}
