<script type='text/javascript' src="dwr/interface/SkillTypes.js"></script>

<script type="text/javascript" >
function showData(itemType, skillTypeId) {
    var fillItemsTable = function (response) {
        DWRUtil.removeAllRows("ItemsTableBody");
        DWRUtil.addRows("ItemsTableBody", response.items, [ function(item){return item.code;}, function(item){return item.description;}]);
        var titleVar = document.getElementById('title');
        var descriptionVar = document.getElementById('itemDescription');
        titleVar.innerHTML=itemType;
        if(itemType=='Workers'){
        	descriptionVar.innerHTML='Active';
        }else if(itemType=='Jobs'){
        	descriptionVar.innerHTML='Status';
        }else if(itemType=='Templates'){
        	descriptionVar.innerHTML='# of Tasks';
        }
        thePopupManager.showPopup('skillType_show_cnt');
    }
    var searchCriteria = new Object();
    searchCriteria.id=skillTypeId;
    searchCriteria.type=itemType;
    SkillTypes.getData(fillItemsTable, searchCriteria, 0, 1000, 'code','ASC');
}
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#skillType_show_cnt {
        width: 400px;
    }
    div#skillType_show_cnt table#skillTypesTable {
        width: 100%;
    }

</style>

<div class="popup" id="skillType_show_cnt">
    <div class="popupHeader">
        <h2><span id="title"></span></h2>
    </div>

    <div class="popupBody">
        <div style="height: 400px; width: 370px;overflow: auto; border: 2px inset; margin-top: 5px;">

            <table id="skillTypeItemsTable" class="dataTable">
                <tr>
                    <th class="codeCol" id='sort_code'>Name</th>
                    <th class="descriptionCol" id='sort_description'><span id="itemDescription">Active</span></th>
                </tr>
                <tbody id="ItemsTableBody">
                </tbody>
            </table>
        </div>
        <input type="button" value="Cancel" class="button" onClick="thePopupManager.hidePopup();">
    </div>
</div>

<script type="text/javascript">
new Draggable("skillType_show_cnt",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});    
</script>