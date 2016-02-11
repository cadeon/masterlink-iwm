/*Implements PageNavigator component
 Usage: see pagegenerator jMaki component
 in JSP  1. add   <a:dwr id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
         2  create (AFTER window.onload!) an instance of PageGenerator and define a callback function to be called when PageNavigator links (First,Next,Prev etc arev cliked)
            eample: pageNavigator = new PageNavigator(offset, pageSize,"pagemeter",pageNavCallback);

            use page navigator properties to sync data with page display. See locators.jsp as sample.

*/

var PageNavigator = Class.create();
PageNavigator.prototype = {
    initialize: function(displayElement, callback) {
        this.uuid = displayElement;
        this.element = $(displayElement);
        this.offset = 0;
        this.pageSizeElement = $(displayElement + "_pageSize");
        var _this=this;
        SessionUtil.getPagesize({
            callback:function(storedValue){
                if(storedValue){
                    $(_this.pageSizeElement).value=storedValue;
                }
            },
            async:true});
        this.totalCount;
        this.counter = $(displayElement +"_pagecounter");
        this.first = $(displayElement+"_first");
        this.next  = $(displayElement+"_next");
        this.prev  = $(displayElement+"_prev");

        this.first.onclick = this.firstPage.bindAsEventListener(this);
        this.next.onclick = this.nextPage.bindAsEventListener(this);
        this.prev.onclick = this.prevPage.bindAsEventListener(this);
        this.pageSizeElement.onchange = this.changePageSize.bindAsEventListener(this);

        this.callback=callback;
    },

    getPageSize: function(){
        //return this.pageSizeElement[this.pageSizeElement.selectedIndex].value
        return $F(this.pageSizeElement);
    },

    reset: function(){
         this.offset = 0;
    },
    update: function(totalCount) {
        this.element.style.visibility = "visible";
        this.totalCount= totalCount;
        var from = this.offset+1;

       // $('pageSize').options[$('pageSize').selectedIndex].value

        var to = parseInt(this.offset) + parseInt(this.getPageSize());
        if(to > this.totalCount) to = this.totalCount;
        if(totalCount == 0){           //uncoment if need to hide navigator when number of records < pageSize
            $(this.uuid + "_noResults").style.display = "block";
            $(this.uuid + "_results").style.display = "none";
        }else{
            this.counter.innerHTML=from + '-' + to + ' of ' + this.totalCount;
            $(this.uuid + "_noResults").style.display = "none";
            $(this.uuid + "_results").style.display = "block";

            if(from == 1 && to == this.totalCount){
                this.first.style.display = 'none';
                this.next.style.display = 'none';
                this.prev.style.display = 'none';
            } else {
                this.first.style.display = 'inline';
                this.next.style.display = 'inline';
                this.prev.style.display = 'inline';
            }

        }
    },

    changePageSize:function(evt){
        SessionUtil.setPagesize($(this.pageSizeElement).value);        
        this.callback();
    },
    firstPage: function(evt){
        this.offset=0;
        this.callback();
    },
    nextPage: function(evt){
        oldOffset = this.offset;
        this.offset = parseInt(this.offset)+parseInt(this.getPageSize());
        if(this.offset >= this.totalCount) this.offset=oldOffset;
        this.callback();

    },
    prevPage: function (evt){
        oldOffset = this.offset;
        this.offset = this.offset-this.getPageSize();
        if(this.offset < 0) this.offset=oldOffset;
        this.callback();
    }
};