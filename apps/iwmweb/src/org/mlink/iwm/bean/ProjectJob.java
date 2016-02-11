package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Feb 3, 2007
 */
public class ProjectJob extends Job implements Comparable{
    private int sequenceLevel;

    public int getSequenceLevel() {
        return sequenceLevel;
    }

    public void setSequenceLevel(int sequenceLevel) {
        this.sequenceLevel = sequenceLevel;
    }

    public int compareTo(Object o){
        if(o == null) return 1;
        int other = ((ProjectJob)o).getSequenceLevel();
        int thisOne	=	getSequenceLevel();
        return thisOne - other;
    }
}
