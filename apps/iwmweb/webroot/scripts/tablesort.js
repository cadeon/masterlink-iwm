
//preload the images
var arrowup = new Image();
arrowup.src='images/arrow_up.gif';
var arrowdwn = new Image();
arrowdwn.src='images/arrow_down.gif';


// Implements column sorting. Uses  Prototype's library bindAsEventListener
// Prototype docs at http://wiki.script.aculo.us/scriptaculous/show/Prototype
var SortColumn = Class.create();
//defining the rest of the class implementation
SortColumn.prototype = {
    initialize: function(colName) {
        this.element = $("sort_"+colName);
        this.element.style.cursor = 'pointer';
        this.element.style.textDecoration = 'underline';
        this.sortColumnName = colName;
        this.initialLabel = $(this.element).innerHTML;
        this.sortDirection = "ASC";   //initial value
        this.tableSort;
        //assigning our method to the event
        this.element.onclick = this.execute.bindAsEventListener(this);

    },
//toggles the image and executes sortFunction
    execute: function(event) {
        if(this.tableSort) this.tableSort.reset();
        if("ASC"==this.sortDirection){
            this.sortDirection="DESC";
            this.element.innerHTML=this.initialLabel +'<img src="'+arrowup.src+ '" border="0">';
        }else{
            this.sortDirection="ASC";
            this.element.innerHTML=this.initialLabel +'<img src="'+arrowdwn.src+'" border="0">';
        }
        //script = this.sortFunctionName + "('" + this.sortColumnName +"','" + this.sortDirection + "')";
        //script = this.sortFunctionName + "()";
        //alert(script);
        //setTimeout(script,0);


        this.tableSort.currentSortColumn=this.sortColumnName;
        this.tableSort.currentSortDirection=this.sortDirection;

        this.tableSort.callback();

    },
//resests all column labels
    reset: function(){
        this.element.innerHTML=this.initialLabel;
    }
};

/*TableSort is the container for SortColumn objects
columnNamesArray must correspond to java bean property names to sort by
<th> element ids must have the following naming convention
sort_propertyname in  columnNamesArray
*/

var TableSort = Class.create();
TableSort.prototype = {
    initialize: function(callback, columnNamesArray) {
        this.currentSortColumn=null;
        this.columns = new Array();
        for (var i=0; i <columnNamesArray.length; i++) {
            if(i==0) this.currentSortColumn=columnNamesArray[i];
            this.addColumn(columnNamesArray[i]);
        }
        this.currentSortDirection="ASC";
        this.callback=callback;

    },
    addColumn: function(sortColumnName) {
        var _callback =this.callback;    //JS closure trick
        sortCol = new SortColumn(sortColumnName);
        sortCol.tableSort=this;
        this.columns.push(sortCol);
    },
    reset: function(sortColumnName) {
        for (var i=0; i <this.columns.length; i++) {
            this.columns[i].reset();
        }
    }
};
