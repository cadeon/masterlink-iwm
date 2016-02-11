package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 2, 2006
 */
public class TemplateTaskAction implements Comparable{
    private String  id;
    private String  sequence;
    private String  verb;
    private String  name;
    private String  modifier;
    private String  taskDefId;
    private float  fSequence; //helper property to support resequensing

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(String taskDefId) {
        this.taskDefId = taskDefId;
    }

    public float getfSequence() {
        return fSequence;
    }

    public void setfSequence(float fSequence) {
        this.fSequence = fSequence;
    }

    public int compareTo(Object o){
        if(o == null) return 1;
        Float other = ((TemplateTaskAction)o).getfSequence();
        Float thisOne	=	getfSequence();
        return thisOne.compareTo(other);
    }
}
