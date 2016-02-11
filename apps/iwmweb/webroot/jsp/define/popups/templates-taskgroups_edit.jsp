<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >
    var GroupEditPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(theId){
            _this = this;
            if(theId != null && theId != -1){
                $("popupTitle").innerHTML= "Edit Task Group";
                TemplateTaskGroups.getItem(function(response){_this.populate(response);}, theId);

            } else {
                //if no locator id is passed in that means it is an add. make a blank form.
                $("popupTitle").innerHTML= "Add Task Group";
                var objectDefId=<%= request.getParameter("id") %>;
                TemplateTaskGroups.getNewItem(function(response){_this.populate(response);}, objectDefId);
            }
        },
        populate: function(taskGroup){
            this.theData = taskGroup;
            FormValuesUtil.setFormValues($("TaskGroupForm"),this.theData);
            this.populateTasks(taskGroup.tasks);
            thePopupManager.showPopup('templates-taskgroups_edit');
            $('description').focus();            
        },
        populateTasks: function(tasks){
            DWRUtil.removeAllRows("taskGroupTableBody");
            getDescription = function(item){return item.description;}
            getSkillType = function(item){return item.skillType;}
            getTaskGroup = function(item){if(item.groupId) return 'Y'; else return 'N';}
            var theTaskGroup = this.theData;
            getSelected = function(item){
                var checked=''; var disabled='';
                if(item.groupId==theTaskGroup.id) checked=' checked '; else checked='';
                if(item.groupId!=null && item.groupId!=theTaskGroup.id ) disabled=' disabled '; else disabled='';  <%/*disable tasks that already in another group*/%>
            <%/*if not disabled, check for skill type and disable ones not matching group skill type. Must Assign Skill is exception*/%>
                //if(DWRUtil.getValue($("skillTypeId"))!=item.skillTypeId && !item.skillType.match(/Must Assign/)) disabled=' disabled ';
                //if(theGroupEditPopup.theData.skillType!=item.skillType && !item.skillType.match(/Must Assign/)) disabled=' disabled ';
                var rtn = "<input id=\'"+item.id+"\' type='checkbox'"+checked+disabled+" onclick=' return theGroupEditPopup.onSelectedClick(this)'>";
                return rtn;
            }

            DWRUtil.addRows("taskGroupTableBody", tasks, [getSelected,getDescription,getSkillType,getTaskGroup]);
        },
        onSelectedClick:function(checkbox){
            var groupId;
            if(checkbox.checked)
                groupId=this.theData.id;
            else
                groupId=null;

            TemplateTaskGroups.updateTaskMembership(checkbox.id,groupId);
            TemplateTaskGroups.extractGroupSkillType(function(str){$("skillType").value=str},this.theData.id);
            return true;
        },
        onSkillUpdate:function(select){
            TemplateTaskGroups.reset(function(response){_this.populateTasks(response.items);},this.theData.id);
        },
        save: function(){
            if(this.validate()){
                FormValuesUtil.getFormValues($("TaskGroupForm"),this.theData);
                var _this = this;
                //this.persist(this.theData);   this popup is called by dfferent parents (groups adn tasks), therefore this framework call to persist is not going to work
                TemplateTaskGroups.saveItem(this.theData,{callback:function(message){_this.showCallBackMessage(message);}});
                return true;
            }else return false;
        }
    });

callOnLoad(function(){
    //theGroupEditPopup = new GroupEditPopup("templates-taskgroups_edit", "TaskGroupForm", theTaskGroupsTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
div#templates-taskgroups_edit {
    width: 370px;
    font-size: 8px;
}

div#templates-taskgroups_edit h2 {
    font-size: 12px;
}
</style>

<!--  -->
<div class="popup" id="templates-taskgroups_edit">
    <div class="popupHeader">
        <h2><span id="popupTitle"></span></h2>
    </div>

    <div class="popupBody">
        <form id="TaskGroupForm" name="TaskGroupForm" action="">
            Task Group Name:<br>
            <input  width="150" maxlength="150" id="description" type="text">

            <h2>Select Tasks for Group</h2>
            Skill:
            <%/*html:select styleClass="filterSelect" property="skillTypeId" styleId="skillTypeId" value="" onchange="theGroupEditPopup.onSkillUpdate(this)">
                <option value="">- Select Skill -</option>
                <html:optionsCollection property="options"  name="SkillTypeRef"/>
            </html:select*/%>

            <span><input type="text" class="textField" disabled  id="skillType"></span>

            <div class="fieldComment">Only task with the same skill can be grouped.
                Tasks with Must Assign Skill type are exceptions. Tasks can only belong to one Group at a time.
            </div>


            <div style="height: 300px; width: 340px;overflow: auto; border: 2px inset; margin-top: 5px;">
                <table class="dataTable">
                    <tr>
                        <th></th>
                        <th>Task</th>
                        <th>Skill</th>
                        <th>In<br>Group</th>
                    </tr>
                <tbody id="taskGroupTableBody"></tbody>
                </table>

            </div>
        </form>
            <input type="button" value="Save" class="button" onClick="theGroupEditPopup.save();">
            <input type="button" value="Cancel" class="button" onClick="theGroupEditPopup.close();">
    </div>
</div>

<%/*Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="TaskGroupForm" dynamicJavascript="true" staticJavascript="false"/>

