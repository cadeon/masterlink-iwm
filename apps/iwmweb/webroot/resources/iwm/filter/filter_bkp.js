/* Implementation of IWM Locator and Object Class Filters
LocatorChain is the container for LocatorChainLink objects
*/

log = function(msg){
    if(self.console){console.log(msg);}
    else {
        //alert(msg);
    }
}

var LocatorChain = Class.create();
LocatorChain.prototype = {
    initialize: function(filterDivId, onChangeCallback,storeSelectionInSession) {
        this.selects = new Array();
        this.element;
        this.createChain(filterDivId);
        this.onChangeCallback=onChangeCallback;
        this.storeSelectionInSession=storeSelectionInSession;
        this.currentSelectedId=null;
    },
    createChain: function(filterDivId){
        var labels;
        var LSCallback =  function LSCallback(data){
            labels=data;
        }
        Locators.getLocatorSchemas({callback:LSCallback,async:false});

        for (var i=0; i < labels.length; i++) {
            div = document.createElement("div");
            label = document.createElement("label");
            select = document.createElement("select");
            select.setAttribute("disabled", "true");
            select.id=filterDivId + "_"+i;
            select.className="filterSelect";
            labelText = document.createTextNode(labels[i].label);
            label.appendChild(labelText);
            label.className="filterLabel";
            div.appendChild(label);
            div.appendChild(select);
            $(filterDivId).appendChild(div);
            this.addLink(new LocatorChainLink(select.id,labels[i].label));
        }
    },
    populateChain: function(selectedId) {
        log("populateChain:"+selectedId);
        this.currentSelectedId=selectedId;
        links=this.selects;

        //first reset all chain, resetting the first one triggers all chain
        if(links[0]) links[0].reset();

        if(!selectedId) selectedId=null;

        /* call to DWRSupport-AJAX*/
        /* getParentLocators returns chain of locators from seletedid and up*/
        Locators.getParentLocators(selectedId, {
            callback:
                    function(response) {
                        if (response) {
                            //init each link if selectedIds present use them to prepopulate dropdowns with data
                            for (var i=0; i < links.length; i++) {
                                link = links[i];
                                if(response.items[i]) id = response.items[i].locatorId;
                                else id=null;

                                link.init(id);
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

    showLinks: function(){
        for (var i=0; i < this.selects.length; i++) {
            log("showLinks: " + this.selects[i].uuid + " parent="+this.selects[i].parentUuid);
        }
    },

    getLink: function(widgetUuid) {
        log("getLink: " + widgetUuid);
        var rtn;
        for (var i=0; i <this.selects.length; i++) {
            if(widgetUuid==this.selects[i].uuid) rtn=this.selects[i];
        }
        return rtn;
    }
};
var noSelectionValue='';
var LocatorChainLink = Class.create();
LocatorChainLink.prototype = {
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
        log("reset: for "+this.uuid)
        DWRUtil.removeAllOptions(this.uuid);
        $(this.uuid).disabled = "true";
        var tmp;
        if( this.dependentUuid){
            tmp = this.selectChain.getLink(this.dependentUuid);
            tmp.reset();
        }
    },
    update: function(parentLocatorId) {
        log("update:in update of " +this.uuid + " parentLocatorId=" +parentLocatorId);
        var selectId = this.uuid;
        _label=this.noSelectionLabel;

        /* call to DWRSupport-AJAX*/
        if(!parentLocatorId){
            parentLocatorId=null;  //i.e. parentLocatorId undefined, need to retrieve top level locators
        }

        //must be synced as we need to wait end of update() before processing storeId (important for init() method
        Locators.getFirstChildrenForLocator(parentLocatorId, {
            callback:
                    function(data) {
                        if (data) {

                            if(data.items.length == 0){
                                DWRUtil.addOptions(selectId, [{label:"None Available",value:noSelectionValue}],'value','label');
                            } else {
                                DWRUtil.addOptions(selectId, [{label:_label,value:noSelectionValue}],'value','label');
                                DWRUtil.addOptions(selectId, eval(data.items), 'locatorId', 'name');
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
        startTime = new Date();
        log("onchange: is fired for " +this.element.id);
        /**Need to update Session with the current locator selection.
         if dropdown is deselected (noSelectionLabel is selected)  need to update session with
         the current locator selection from the parent (if parent exists)
         */
        selectedValue=$(this.uuid).options[$(this.uuid).selectedIndex].value;
        if(selectedValue==noSelectionValue){
            if(this.parentUuid){
                selectedValue=$(this.parentUuid).options[$(this.parentUuid).selectedIndex].value;
            }else{
                selectedValue = null;  //results in retrieval top level locators
            }
        }

        this.selectChain.currentSelectedId=selectedValue;

        // set the current selection in the Session
        if(this.selectChain.storeSelectionInSession && this.selectChain.storeSelectionInSession==true){
            SessionUtil.setCurrentLocator(selectedValue,
            {callback:function(data){},async:false});      //must be synced since must make
        }                                                  //sure that no other methods  get current locator before this one completes

        /*callback function executes if available in the document scope
            there is a very nice way to test if a function has been declared, or a variable has been initialized.
            When used in the document itself, you can use the "self" object, which refers to the window object.*/
        if(this.selectChain.onChangeCallback) this.selectChain.onChangeCallback(selectedValue);

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

        endTime = new Date();
        log("onchange: time elapsed " + (endTime-startTime));

    },

    /* this method is resposible for recovering dropdown values given selectedId is the current selection*/
    init: function(selectedId){
        log("init:"+selectedId);
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


