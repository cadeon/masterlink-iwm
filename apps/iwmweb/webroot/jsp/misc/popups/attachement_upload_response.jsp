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

    <ul>
        <li>
            <b>Stored as:</b>&nbsp;<%= request.getAttribute("text") %>
        </li>
        <li>
            <b>The File name:</b>&nbsp;<%= request.getAttribute("fileName") %>
        </li>
        <li>
            <b>The File content type:</b>&nbsp;<%= request.getAttribute("contentType") %>
        </li>
        <li>
            <b>The File size:</b>&nbsp;<%= request.getAttribute("size") %>
        </li>
    </ul>
