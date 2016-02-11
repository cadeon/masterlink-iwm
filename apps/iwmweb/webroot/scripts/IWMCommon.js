/******************************
 * Other Nice To Have Functions
 */
    if (typeof String.prototype.startsWith != 'function') {
    	  String.prototype.startsWith = function (str){
    	    return this.slice(0, str.length) == str;
    	  };
    	}  


/********************************************************************************************
AbstractCrubPopup: Subclass for CRUD popups

Input:  formName - the form on the crud screen
        dataTable - data table that launched the crud screen. used if we need to update the table after
            saving info from the popup

John Mirick (john@mirick.us)
*/
var AbstractCrudPopup = Class.create();
AbstractCrudPopup.prototype = {

    //formName - name of the form on the popup. used for validation and clearing
    //dataTable - the dataTable form which the popup was launched. used to refresh table if needed. null if no table for popup.
    //is wizard - boolean that tells if this popup is going to be used as a step in a wizard. false by default.
    initialize: function(popupId, formName, dataTable){
        // from the shopping cart demo
        new Draggable(popupId,{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
        this.popupId = popupId;
        this.formName = formName;
        this.dataTable = dataTable;
        this.isWizard = false;

    },

    //theId - if it is null this is an add. if we have a value for id we use that
    //to find the object we should be showing
    show: function(theId, isWizard){
         throw new Error("Abstract method 'show' must be defined in subclass.");
    },

    //AJAX callback that populates the form. should be called from show method.
    populate: function(item){
        throw new Error("Abstract method 'populate' must be defined in subclass.");
    },

    //AJAX callback that populates the form. should be called from show method.
    save: function(item){
        throw new Error("Abstract method 'save' must be defined in subclass.");
    },

    persist: function(jsObject){
        if(this.isWizard){
            //if wizard then the subclass must override this method. and implement save.
            throw new Error("persist should be in the subclass.");
        } else {
            if(!isAlreadySubmitted()){
                var _this = this;
                this.dataTable.DWRObject.saveItem(jsObject,{ callback:function(message){
                    resetAlreadySubmitted();
                    _this.showCallBackMessage(message);
                },async:false}
                        );
            }
        }
    },

    //resets the form. used to set the form back to an empty state.  since form is
    //initially blank and is populated with AJAX this will set the form back to a
    //blank state.
    clean: function(){
        Form.reset($(this.formName));
    },

    //calls the struts validator. overried if needed
    validate: function(){
        return eval("validate" + this.formName + "($('" + this.formName + "'))");;
    },

    //base class. override if needed
    showCallBackMessage: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            this.dataTable.update();
       }
    },

    //base class. override if needed
    showWizardCallBackMessage: function(message){
        throw new Error("If wizard. Abstract method 'showWizardCallBackMessage' must be defined in subclass.");
    },

    close: function(){
        thePopupManager.hidePopup();
        $(this.popupId).style.top = "18px";
        $(this.popupId).style.marginLeft = "50%";


        //$(this.popupId).style.left = "-" + $(this.popupId).style.width/2 + "px";
        //$(this.popupId).style.left = "-170px";

        //alert($(this.popupId).style.left);
        this.clean();
    }
}



/****************************************************************************************************
IWMDataTable: Controls our dynamic dataTable.
Input:  theConfig - this is a DataTableConfig object
See: This object is used in LocatorsList.  That should be a good example of how to use it.

John Mirick (john@mirick.us)
*/
function showCallbackMessage(message){
     if(message && message.length > 0) alert(message);
}
var IWMDataTable = Class.create();
IWMDataTable.prototype = {
    initialize: function(theConfig){
        //make sure a DataTableConfig object was passed in.
        if(theConfig.isDataTableConfig() != true){
            alert("Init Error: IWMDataTable expects a DataTableConfig object as a param");
            return;
        }
        this.getSearchCriteria = theConfig.getSearchCriteria;
        this.theTableFormaters = theConfig.getFormaters();
        this.theSortCols = theConfig.getSortCols();
        this.theToolTipCols = theConfig.getToolTipCols();
        this.DWRObject = theConfig.getDWRObject();
//        this.pageSizeElement = $('pageSize');
        this.dataTableId=theConfig.getDataTableId(); //default value
        this.pagemeter=theConfig.getPageMeter();; //default value

        var _this = this;

        this.pageNavigator =
                new PageNavigator(this.pagemeter,function(){_this.update();});

        this.tableSort =
                new TableSort(function(){_this.update();}, _this.theSortCols);
        this.hook;
        this.size;

        this.selectedRow = null;
    },


    deleteItem: function(id, warningMsg){
        if (confirm("Are you sure you want to delete " + warningMsg + "?")) {
            this.DWRObject.deleteItem(id,{ callback:showCallbackMessage,async:false});
            this.update();
        }
    },

    saveItem: function(jsObject){   //do not remove, saveItem IS USED
        this.DWRObject.saveItem(jsObject,{ callback:showCallbackMessage,async:false});
        this.update();
    },

    deleteItems: function(ids, warningMsg){
        if (confirm("Are you sure you want to delete " + warningMsg + "?")) {
            DWREngine.beginBatch();
            for (var i = 0; i < ids.length; i++) {
                this.DWRObject.deleteItem(ids[i],
                    { callback:showCallbackMessage,async:true}
                );
            }
            this.update();
            DWREngine.endBatch();
        }
    },
    onUpdateHook: function(items){
       if(this.hook) this.hook(items);
    },
    setOnUpdateHook: function(func){
        this.hook = func;
    },

    highlightRow: function(theRow, theColor) {
        if(this.selectedRow != theRow){
            theRow.style.backgroundColor = theColor;
        }
    },
    selectRow: function(theRow) {
        //this here will be the TR that this function is attached to NOT the dataTable object
        theRow.style.backgroundColor = '#FFFF77';
        if(this.selectedRow){
            this.selectedRow.style.backgroundColor = '#FFFFFF';
        }
        this.selectedRow = theRow;
    },


    update: function () {
        var _this = this;
        this.DWRObject.getData(
            //call back function fills the dataTable
            function(response){
                _this.size=response.items.length;
                DWRUtil.removeAllRows(_this.dataTableId);
//                DWRUtil.addRows(_this.dataTableId, response.items, _this.theTableFormaters);

                 DWRUtil.addRows(_this.dataTableId, response.items, _this.theTableFormaters,
                         {

                        rowCreator: function(options) {
                          var row = document.createElement("tr");
                          row.onmouseover = function(){_this.highlightRow(row, '#FFFFCC')};
                          row.onmouseout = function(){_this.highlightRow(row, '#FFFFFF')};
                          row.onclick = function(){_this.selectRow(row)};

                          return row;
                        },

                        cellCreator:function(options) {


                          var td = document.createElement("td");

                            for(var x in _this.theToolTipCols){
                                var curCol = _this.theToolTipCols[x];
                                if(options.cellNum == curCol){
                                    td.onmouseover = function(){
                                        if(typeof(theToolTipsManager)!="undefined"){
                                            theToolTipsManager.showToolTip(this);
                                        }
                                    };

                                    td.onmouseout = function(){
                                        if(typeof(theToolTipsManager)!="undefined"){
                                            theToolTipsManager.hideToolTip(this);
                                        }
                                    };
                                }
                            }
                          return td;
                        }
                      });


                _this.pageNavigator.update(response.totalCount);
                _this.onUpdateHook(response.items);
            },
            this.getSearchCriteria(),
            this.pageNavigator.offset,
            this.pageNavigator.getPageSize(),
            this.tableSort.currentSortColumn,
            this.tableSort.currentSortDirection);
    }
};

/** generic cellCreator with all cells supported by ToolTips. Intended to use for tables not implemented with IWMDataTable**/
function cellsWithToolTips(options){
    var td = document.createElement("td");
    td.onmouseover = function(){
        if(typeof(theToolTipsManager)!="undefined"){
            theToolTipsManager.showToolTip(this);
        }
    };

    td.onmouseout = function(){
        if(typeof(theToolTipsManager)!="undefined"){
            theToolTipsManager.hideToolTip(this);
        }
    };
    return td;
}


/*********************************************************************************************
DataTableConfig: Used to set up the MaintainanceTable object. This class would be abstract
    if we could do that in js. Subclasses will need to implement the
    getFormaters method.

Input:  theDWRObject - this is the DWR generated object that represents the type of data being
          displayed in the table (Locators, Templates, Jobs, etc). The DWR object MUST contain
          a deleteItem and a getData method.
        theSortCols - an array containing the ids of the colunms you can sort by

John Mirick (john@mirick.us)
*/
var DataTableConfig = Class.create();

DataTableConfig.prototype = {
    initialize: function(theDWRObject, theSortCols, theToolTipCols, thePageMeter, theDataTableBody){
        this.theDWRObject = theDWRObject;
        this.theSortCols = theSortCols;
        this.theToolTipCols = theToolTipCols;
        if(thePageMeter){
            this.pageMeter = thePageMeter;
        } else {
            this.pageMeter = "pagemeter";
        }
        if(theDataTableBody){
            this.dataTableId = theDataTableBody;
        } else {
            this.dataTableId = "dataTableBody";
        }
    },
    getSearchCriteria: function(){
        //should return a js object
        alert("populateCriteria: This method needs to be implemented in extended class");
    },

    getFormaters: function(){
        alert("getFormaters: This method needs to be implemented in extended class");
    },
    getToolTipCols: function(){
        return this.theToolTipCols;
    },
    getSortCols: function(){
        return this.theSortCols;
    },
    getDWRObject: function() {
        return this.theDWRObject;
    },
    getPageMeter: function(){
        return this.pageMeter;
    },
    
    getDataTableId: function(){
        return this.dataTableId;
    },

    isDataTableConfig: function(){
        return true;
    }
}

/*
var Sample = Class.create();

Sample.prototype = {
    initialize: function(){
    },
    method2: function(){
    }
}
*/





/*********************************************************************************************
ToolTips: When you mouse over a cell in a datatable sometimes we will want to show the full
value in a tool tip. Thats what this does

John Mirick (john@mirick.us)
*/
var ToolTips = Class.create();

ToolTips.prototype = {
    initialize: function(){
    },

    showToolTip: function(theCell){
        var elementPos = this.findPos(theCell);
        var theToolTip = document.getElementById('toolTip');
        theToolTip.style.left = (elementPos[0]  + 10) + 'px';
        theToolTip.style.top = (elementPos[1] + 26) + 'px';
        theToolTip.innerHTML = theCell.innerHTML;
        theToolTip.style.display = 'inline';
    },

    hideToolTip: function(){
        var theToolTip = document.getElementById('toolTip');
        theToolTip.style.display = "none";
    },

    findPos: function(obj){
        var curleft = curtop = 0;
        if (obj.offsetParent) {
            curleft = obj.offsetLeft;
            curtop = obj.offsetTop;
            while (obj = obj.offsetParent) {
                curleft += obj.offsetLeft;
                curtop += obj.offsetTop;
            }
        }
        return [curleft,curtop];
    }
}



/*********************************************************************************************
SpinnerWidget: controls a spinner control.  buttons increment and decrement the controls values.
    The widget is a JMaki component. Look there for the html and css needed for the spinner.

Input:  theTextField - the text field of the spinner
        floor - the min value allowed by the spinner. null if no min
        ceiling - the max value allowed by the spinner. null if no max

John Mirick (john@mirick.us)
*/
var SpinnerWidget = Class.create();
SpinnerWidget.prototype = {

    initialize: function(theTextField, floor, ceiling){
        this.theTextField = theTextField
        this.floor = floor;
        this.ceiling = ceiling;
        //this.theTextField.onkeydown = this.onMouseDown;
        var _this = this;

        this.theTextField.onkeydown = function(evt){
            if (!evt) {
                evt = window.event;
            }

            var theKey = evt.keyCode;
            if(theKey == 38){
                _this.increase();
            } else if(theKey == 40){
                _this.decrease();
            }
        };

    },

    onMouseDown: function(evt) {
        var _this = this;
        var theKey = evt.keyCode;
        if(theKey == 38){
            _this.increase();
        } else if(theKey == 40){
            _this.decrease();
        }
    },

    increase: function(){
        var newValue = parseInt(this.theTextField.value) + 1;
        if(newValue > this.ceiling) {
            this.theTextField.value = this.floor;
        } else {
            this.theTextField.value = newValue;
        }
    },

    decrease: function(){
        var newValue = parseInt(this.theTextField.value) - 1;

        if(newValue < this.floor) {
            this.theTextField.value = this.ceiling;
        } else {
            this.theTextField.value = newValue;
        }
    },

    validate: function(){

        var re = new RegExp('^-*[0-9]+$');
        var m = re.exec(this.theTextField.value);
        if(m == null) {
            alert("Value must be an integer.");
            this.theTextField.value=0;
        } else if(this.theTextField.value < this.floor) {
            alert("Minimum value allowed is " + this.floor + ".");
        } else if(this.theTextField.value > this.ceiling) {
            alert("Maximum value allowed is " + this.ceiling + ".");
        }
    }
    
}