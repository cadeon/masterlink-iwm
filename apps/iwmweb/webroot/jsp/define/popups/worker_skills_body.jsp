<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-logic-el" prefix="logic-el" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>



    <table class="dataTable" id="workerSkillsTable" style="width:370px;">
        <tr>
            <th class="skill"  style="width:240px;"><bean:message key="skill"/></th>
            <th class="level" style="width:70px;"><bean:message key="skillLevel"/></th>
            <th class="action" style="width:60px;"></th>
        </tr>

        <logic-el:iterate collection ="${skills}" id="item"  indexId="nIndex" >
            <tr>
                <td><bean:write name="item" property="skillType"/></td>

                <td>
                    <html-el:select property="skillLevelId" styleId="${item.skillType}" name="item" onchange="theWorkerSkillsPopup.updateSkillLevel(${item.skillTypeId},this.options[selectedIndex].value);" >
                        <html:optionsCollection property="options"  name="SkillLevelRef"/>
                    </html-el:select>
                </td>

                <td align="center"><img src="images/trash_16.gif" alt="Delete" name="Delete" id="<c:out value="${item.skillType}"/>" onclick="theWorkerSkillsPopup.removeSkill(<c:out value="${item.skillTypeId}"/>);"/></td>
            </tr>
        </logic-el:iterate>

        <tr class="shaded">
            <td colspan="3">
                <html:select styleId="addSkill" property="skillLevelId"  value="" onchange="theWorkerSkillsPopup.addSkill(this.options[selectedIndex].value);">
                    <option>Add <bean:message key="skill"/></option>
                    <html:optionsCollection property="options"  name="SkillTypeRefDyna"/>
                </html:select>


            </td>
        </tr>
    </table>
