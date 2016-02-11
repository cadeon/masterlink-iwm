
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-logic" prefix="logic" %>

    <logic:messagesPresent>
        <ul>
            <html:messages bundle="iwm.upload" id="error">
                <li><bean:write name="error"/></li>
            </html:messages>
        </ul><hr />
    </logic:messagesPresent>

&nbsp;
    <p>
        <b>Stored as:</b>&nbsp;<%= request.getAttribute("text") %>
    </p>
    <p>
        <b>The File name:</b>&nbsp;<%= request.getAttribute("fileName") %>
    </p>
    <p>
        <b>The File content type:</b>&nbsp;<%= request.getAttribute("contentType") %>
    </p>
    <p>
        <b>The File size:</b>&nbsp;<%= request.getAttribute("size") %>
    </p>

<input type="button" class="button" value="Back" onclick="document.location.href='Uploads.do'"/>

&nbsp;
