package org.mlink.iwm.struts.form;


/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: Mar 5, 2006
 */
public class WorldLI  extends BaseForm {
    private boolean isActive;
    private boolean isSchemaOwner;
    private String schemaName;
    private String schemaDesc;
    private String parent;
    private String lastModified;
    private String status;
    
    protected java.lang.String createdDate;

    public String getSchemaName()    {return schemaName;}
    public String getSchemaDesc()    {return schemaDesc;}
    public String getCreatedDate()   {return createdDate;}
    public boolean getIsActive()     {return isActive;}
    public boolean getIsSchemaOwner(){return isSchemaOwner;}
    public String getLastModified()  {return lastModified;}
    public String getParent()        {return parent;}
    public String getStatus()        {return status;}

    public void setSchemaName(String schemaName)     {this.schemaName = schemaName;}
    public void setSchemaDesc(String schemaDesc)     {this.schemaDesc = schemaDesc;}
    public void setCreatedDate(String createdDate)   {this.createdDate = createdDate;}
    public void setIsActive(boolean active)          {this.isActive = active;}
    public void setIsSchemaOwner(boolean schemaOwner){this.isSchemaOwner = schemaOwner;}
    public void setLastModified(String lastModified) {this.lastModified = lastModified;}
    public void setStatus(String status)             {this.status = status;}
    public void setParent(String parent)             {this.parent = parent;}
}
