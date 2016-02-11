/**
*
*  prompts user for session extension or expires sesssion after timeout
*
**/

var SessionMonitor = {
    promptInterval  : 1000*60*110 ,       // 110min
    expireInterval  : 1000*60*119 ,       // 119min
    expireHandle    : -1 ,              // initial value
    expired         : false ,           // initial value
    start : function() {
        SessionMonitor.expired=false;
        setTimeout(SessionMonitor.promptUser, SessionMonitor.promptInterval);
        SessionMonitor.expireHandle = setTimeout(SessionMonitor.expire, SessionMonitor.expireInterval);
    },

    expire : function() {
        SessionMonitor.expired=true;
        var url=document.location.href;
        new Ajax.Request('Login.do', {parameters:'forward=logout',onSuccess:function(t){
            document.location.href=url;
        }});
        //document.location.href='Logout.do';
    },

    extend : function(){
        new Ajax.Request('Login.do', {parameters:'forward=extend'});
        SessionMonitor.start(); //start monitoring anew
    },

    promptUser : function() {
        var _this=SessionMonitor;   /*save into local variable to preserve the data if page is reloaded before user responds to confirm dialaog*/
        if(confirm("Your session is about to expire. Click OK to extend session ok Cancel to logout.")){
            if(!_this.expired) {
                 clearTimeout(_this.expireHandle);
                _this.extend();
            }else{
                alert("Session has already expired")
            }
        }else{
            if(!_this.expired) {
                _this.expire();
            }
        }
    }

}

// andrei commented July 08 . to annoying and we are introducing autologin SessionMonitor.start();