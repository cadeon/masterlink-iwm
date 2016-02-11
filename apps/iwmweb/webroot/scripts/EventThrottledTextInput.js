/* EventThrottledTextInput ***************
*
* Calls a function after the user has stopped typing for 500 milliseconds.
* This should throttle the number of calls made (as opposed to calling on each keyup)
*
* theTextInput - the element you are putting the event throttle on
* theFunction	- the function that you want called when the user pauses typing
*
*
* USEAGE
* function myLoad(){
* 	var myTextSearch = new EventThrottledTextInput(document.getElementById('crap'), function(){ alert('test') } );
* }
*
*******************************************/
var EventThrottledTextInput = Class.create();

EventThrottledTextInput.prototype = {
    initialize: function(theTextInput, theFunction){
        var _this = this;
        this.textSearchInput = theTextInput;		//store the element we check for changes
        this.textSearchInput.onkeyup = _this.textInput;
        this.textSearchInput.nextSubmitTime = 9000000000000;
        this.lastSubmittedText = ""; 			//last text used in search
        this.theFunction = theFunction
        setInterval(function(){_this.doTextSearch()}, 500)
    },

    doTextSearch: function(){

        //if the text has changed since last ajax call.
        if(this.lastSubmittedText != this.textSearchInput.value){

            var now = new Date().getTime();
            if(now > this.textSearchInput.nextSubmitTime) {
                this.lastSubmittedText = this.textSearchInput.value;
                this.theFunction();
            }
        }
    },

    textInput: function(){
        this.nextSubmitTime = parseInt(new Date().getTime()) + 500;
    }
}