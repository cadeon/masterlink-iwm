<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-logic" prefix="logic" %>


<style type="text/css">
    .upload {
        margin:0;
        padding:0;
    }
</style>


    <logic:messagesPresent>
        <ul>
            <html:messages bundle="iwm.upload" id="error">
                <li><bean:write name="error"/></li>
            </html:messages>
        </ul><hr />
    </logic:messagesPresent>

    <!--
            The most important part is to declare your form's enctype to be "multipart/form-data",
            and to have a form:file element that maps to your ActionForm's FormFile property
    --> &nbsp;
    <html:form action="Upload-submit.do?forward=integration" enctype="multipart/form-data" styleClass="upload">
        <p>Please select the file that you would like to upload: <br />
            <html:file property="theFile" onchange="this.size=(this.value.length-10)" size="30"/></p>
        <p>Please enter a new name for the file (optional) <br />
            <html:text  property="theText" size="30"/></p>

        <!--p>If you would rather write this file to another file, please check here: <br />
            <html:checkbox property="writeFile" /></p>
        <p>If you checked the box to write to a file, please specify the file path here: <br />
            <html:text property="filePath" /></p-->
        <p>
            <html:submit />
        </p>
    </html:form>


&nbsp;


