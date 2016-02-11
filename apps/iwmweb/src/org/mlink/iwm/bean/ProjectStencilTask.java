package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Feb 12, 2007
 */
public class ProjectStencilTask extends ObjectTask implements Comparable{
    private int sequenceLevel;
    private Long taskSequenceId;
    private String objectRef;
    private String fullLocator;
    private int displayOrder;


    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getSequenceLevel() {
        return sequenceLevel;
    }

    public void setSequenceLevel(int sequenceLevel) {
        this.sequenceLevel = sequenceLevel;
    }

    public Long getTaskSequenceId() {
        return taskSequenceId;
    }

    public void setTaskSequenceId(Long taskSequenceId) {
        this.taskSequenceId = taskSequenceId;
    }

    public String getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public String getFullLocator() {
        return fullLocator;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public int compareTo(Object o){
        if(o == null) return 1;
        int other = ((ProjectStencilTask)o).getSequenceLevel();
        int thisOne	=	getSequenceLevel();
        return thisOne - other;
    }
}
