package org.mlink.iwm.util;

import java.util.ArrayList;

/**
 * Created by Andrei Povodyrev
 * Date: Sep 8, 2005
 *
 * This class is created to support struts logic:iterate tags
 * If ActionForm has a property of a Collection type, use this class to hold the collection.
 * When input elemnet is submitted
 * <input type="hidden" name="items[10].cpName" value="ABN_SWAP">
 * Struts PropertyUtilsBean.getIndexedProperty() is trying to retreve an item from a colletion which
 * is not initialized at the moment the form-values will be copied into the bean.
 * As result ArrayIndexOutOfBoundsException.
 * This List solves the problem by creating an empty element in the collection.
 *
 * Apparanlty there was a change in java's Introspector.getBeanInfo() since jdk1.4.2_06 which
 * caused us significant problems in support for logic:iterate
 * In jdk1.4.2_03 if property items had 3 methods
 * List getItems()
 * Object getItems(int i)
 * void setItems(Object item, int i)
 * property was considered Indexed (IndexedPropertyDescriptor) and PropertyUtilsBean.getIndexedProperty() would call getItems(int i)
 * In jdk1.4.2_06 one must have
 *  void setItems(List items)
 * so   Introspector will find conventional getter getItems() to display collection
 * This however made property not Indexed and PropertyUtilsBean.getIndexedProperty() would result in calls
 * to not initializied list.
 */
public class AutoGrowingList extends ArrayList{
    private Class clazz;

    public AutoGrowingList(Class clazz) {
        super();
        this.clazz=clazz;
    }

    /**
     * Returns the element at the specified position in this list. If element at the position does not exist cretate an empty
     * instance of class specified by constructor
     * @param i
     */
    public Object get(int i) {
        while (this.size() - 1 < i ){
            this.add(null);
        }
        Object temp = super.get(i);
        if (temp == null){
            try {
                temp = clazz.newInstance();
            } catch (Exception e) {
                //how not to get here:
                //clazz must have no-argument public constructor
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            this.set(i,temp);
        }

        return temp;
    }
}
