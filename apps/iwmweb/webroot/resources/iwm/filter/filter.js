/* Implementation of IWM Filters
SelectChain is the container for SelectChainLink objects
*/

log = function(msg){
    //if(self.console){console.log(msg);
    //} else {
    ///    //alert(msg);
    //}
}

var SelectChain = Class.create();
SelectChain.prototype = {
    initialize: function() {
    },

    init: function(filterType, filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal, chainLength) {
        if(isHorizontal == true){
            this.isHorizontal = true;
        } else {
            this.isHorizontal = false;
        }
        this.selects = new Array();
        this.element;
        this.currentSelectedId=null;
        log("in SelectChain init " + filterType);
        this.onChangeCallback=onChangeCallback;
        this.storeSelectionInSession=storeSelectionInSession;
        this.filterType = filterType;  // expected LOCATOR_FILTER | OBJECT_CLASS_FILTER | ORGANIZATION_FILTER
        this.chainLength=chainLength;
        this.createChain(filterDivId);
        this.isModified=false;
    },

    reset: function(){
        //for(var i = 0; i<selects.length; i++){
           // this.selects[0].element.selectedIndex = 0;
            this.populateChain();
//        }
    },

    createChain: function(filterDivId){
        //log("in createChain " + this.filterType);

        var labels;
        var LSCallback =  function LSCallback(data){
            labels=data;
        }
        FilterSupport.getLabels(this.filterType, {callback:LSCallback,async:false});
        var size = labels.length;
        if (this.chainLength) {
            size = Math.min(size, this.chainLength);
        }
        if (this.isHorizontal) {
            var theTable = document.createElement("table");
            theTable.className = "fitlerTable";
            var selectRow = document.createElement("tr");
            var footerRow = document.createElement("tr");
            var theTBody = document.createElement("tbody");
            theTBody.className="filterEdit";
            theTBody.id = "edit";
            var theTFoot = document.createElement("tfoot");

            for (var i=0; i < size; i++) {
                var currentSelectTD = document.createElement("td");
                var select = document.createElement("select");
                select.setAttribute("disabled", "true");
                select.id= filterDivId + "_"+i;
                select.className="filterSelect";
                currentSelectTD.appendChild(select);
                selectRow.appendChild(currentSelectTD);

                /*var currentLabelTD = document.createElement("td");
                var labelText = document.createTextNode(labels[i].label);
                label = document.createElement("label");
                label.appendChild(labelText);
                label.className="filterLabel";
                currentLabelTD.appendChild(label);
                footerRow.appendChild(currentLabelTD);*/
            }
            theTBody.appendChild(selectRow);
            theTFoot.appendChild(footerRow);
            theTable.appendChild(theTBody);
            theTable.appendChild(theTFoot);
            $(filterDivId).appendChild(theTable);

            for (var i=0; i < size; i++) {
                this.addLink(new SelectChainLink(filterDivId + "_"+i,labels[i].label));
            }


        } else {
            for (var i=0; i < size; i++) {
                div = document.createElement("div");
                label = document.createElement("label");
                select = document.createElement("select");
                select.setAttribute("disabled", "true");
                select.id=filterDivId + "_"+i;
                select.className="filterSelect";
                labelText = document.createTextNode(labels[i].label + ":");
                label.appendChild(labelText);
                label.className="filterLabel";
                div.appendChild(label);
                div.appendChild(select);
                $(filterDivId).appendChild(div);
                this.addLink(new SelectChainLink(select.id,labels[i].label));
            }
        }
    },
    populateChain: function(selectedId) {
        this.currentSelectedId=selectedId;
        links=this.selects;

        //first reset all chain, resetting the first one triggers all chain
        if(links[0]) links[0].reset();

        if(!selectedId) selectedId=null;
        /* call to DWRSupport-AJAX*/
        /* getAncestors returns chain of options from selectedid and up*/
        FilterSupport.getAncestors(this.filterType, selectedId, {
            callback:
                    function(data) {
                        if (data) {
                            //init each link if selectedIds present use them to prepopulate dropdown options with data
                            for (var i=0; i < links.length; i++) {
                                var id;
                                if(data[i]) id = data[i].value;
                                else id=null;

                                links[i].init(id);
                            }
                        }
                    },
            async:false
        });
    },

    addLink: function(selectLink) {
        selectLink.selectChain=this;
        if(this.selects.length > 0) {
            parentLink = this.selects[this.selects.length-1];
            parentLink.dependentUuid=selectLink.uuid;
            selectLink.parentUuid=parentLink.uuid;
        }
        this.selects.push(selectLink);
    },

    refresh: function(){
        this.populateChain(this.currentSelectedId);
    },

    showLinks: function(){
        for (var i=0; i < this.selects.length; i++) {
            log("showLinks: " + this.selects[i].uuid + " parent="+this.selects[i].parentUuid);
        }
    },

    getLink: function(widgetUuid) {
        //log("getLink: " + widgetUuid);
        var rtn;
        for (var i=0; i <this.selects.length; i++) {
            if(widgetUuid==this.selects[i].uuid) rtn=this.selects[i];
        }
        return rtn;
    }
};
var noSelectionValue='';
var SelectChainLink = Class.create();
SelectChainLink.prototype = {
    initialize: function(widgetUuid,label) {
        this.uuid =widgetUuid;
        this.parentUuid;
        this.dependentUuid;
        this.selectChain;
        this.noSelectionLabel='-Select ' + label +' -';
        this.element = $(widgetUuid);
        //assigning our onchange method to select onchange event
        this.element.onchange = this.onchange.bindAsEventListener(this);
    },
    reset: function(){
        //log("reset: for "+this.uuid)
        DWRUtil.removeAllOptions(this.uuid);
        $(this.uuid).disabled = "true";
        DWRUtil.addOptions(this.uuid, [{label:"N/A   ",value:noSelectionValue}],'value','label');
        var tmp;
        if( this.dependentUuid){
            tmp = this.selectChain.getLink(this.dependentUuid);
            tmp.reset();
        }
    },
    update: function(parentId) {
        //log("update:in update of " +this.uuid + " parentId=" +parentId);
        var selectId = this.uuid;
        _label=this.noSelectionLabel;

        /* call to DWRSupport-AJAX*/
        if(!parentId){
            parentId=null;  //i.e. parentId undefined, need to make it null to retrieve top level dropdown
        }

        //must be synced as we need to wait end of update() before processing storeId (important for init() method
        FilterSupport.getDescendants(this.selectChain.filterType, parentId, {
            callback:
                    function(options) {
                        if (options) {
                            DWRUtil.removeAllOptions(selectId);
                            if(options.length == 0){
                                DWRUtil.addOptions(selectId, [{label:"N/A   ",value:noSelectionValue}],'value','label');
                            } else {
                                DWRUtil.addOptions(selectId, [{label:_label,value:noSelectionValue}],'value','label');
                                DWRUtil.addOptions(selectId, eval(options), 'value', 'label');
                            }
                        }

                        if($(selectId).options.length < 2) {
                            $(selectId).disabled = true;
                        } else {
                            $(selectId).disabled = false;
                        }
                    },
            async:false
        });
    },

//updates dependent dropdown
    onchange: function() {
        this.selectChain.isModified=true;
        startTime = new Date();
        //DWREngine.beginBatch();
        /**Need to update Session with the current  selection.
         if dropdown is deselected (noSelectionLabel is selected)  need to update session with
         the current  selection from the parent (if parent exists)
         */
        selectedValue=$(this.uuid).options[$(this.uuid).selectedIndex].value;
        log("onchange: is fired for " +this.element.id + " selectedValue="+selectedValue);

        if(selectedValue==noSelectionValue){
            if(this.parentUuid){
                selectedValue=$(this.parentUuid).options[$(this.parentUuid).selectedIndex].value;
            }else{
                selectedValue = null;  //results in retrieval top level dropdown
            }
        }

        this.selectChain.currentSelectedId=selectedValue;

        // set the current selection in the Session
        if(this.selectChain.storeSelectionInSession && this.selectChain.storeSelectionInSession==true){
            FilterSupport.setSelectedId(this.selectChain.filterType,selectedValue,
                                    {callback:function(data){},async:false});      //must be synced since must make
        }                                                                           //sure that no other methods  get stored value before this one completes


        //update dependent if exists
        if(this.dependentUuid){
            dependentWidget = this.selectChain.getLink(this.dependentUuid);
            log("onchange:updating " + dependentWidget.uuid);
            selectedValue=this.element.options[this.element.selectedIndex].value;
            dependentWidget.reset();
            if(selectedValue!=noSelectionValue){
                dependentWidget.update(selectedValue);
            }
        }

        /*callback function executes if available in the document scope
            there is a very nice way to test if a function has been declared, or a variable has been initialized.
            When used in the document itself, you can use the "self" object, which refers to the window object.*/
        if(this.selectChain.onChangeCallback) this.selectChain.onChangeCallback(this.selectChain.currentSelectedId);
        
        //DWREngine.endBatch();
        //endTime = new Date();
        //log("onchange: time elapsed " + (endTime-startTime));

    },

    /* this method is resposible for recovering dropdown values given selectedId is the current selection*/
    init: function(selectedId){
        //log("init:"+selectedId);
        if(this.parentUuid) {
            var parentSelectedId;
            if($(this.parentUuid).options.length > 0)
                parentSelectedId=$(this.parentUuid).options[$(this.parentUuid).selectedIndex].value;
            if(parentSelectedId && noSelectionValue!=parentSelectedId){
                this.update(parentSelectedId);
            }
        }else{ //ie undefined
            this.update(); //top level dropdown which has no parent
        }

        if(selectedId){
            for (var i=0; i < this.element.options.length; i++) {
                if(this.element.options[i].value==selectedId){
                    log("init: option="+this.element.options[i].value);
                    this.element.options[i].selected=true;
                }
            }
        }
    }
};

LocatorChain = Class.create();
LocatorChain.prototype = new SelectChain();
LocatorChain.prototype.initialize=function(filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal,chainLength) {
    this.init("LOCATOR_FILTER",filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal,chainLength);
};

ObjectClassChain = Class.create();
ObjectClassChain.prototype = new SelectChain();
ObjectClassChain.prototype.initialize=function(filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal) {
    this.init("OBJECT_CLASS_FILTER",filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal);
};

OrganizationChain = Class.create();
OrganizationChain.prototype = new SelectChain();
OrganizationChain.prototype.initialize=function(filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal,chainLength) {
    this.init("ORGANIZATION_FILTER",filterDivId, onChangeCallback,storeSelectionInSession, isHorizontal,chainLength);
};



