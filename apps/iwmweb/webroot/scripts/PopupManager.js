//Author: John Paul Mirick (john@mirick.us)
//Each pages should have an instance of PopupManager.  It is used to hide and show popups
//show the overlay, hide select elements (ie bug)
//Some of the ideas here came from the lightbox script
//http://particletree.com/features/lightbox-gone-wild/

function PopupManager() {
    this.currentPopup = null;  //stores the currently displayed popup.
    this.xPos = 0;
    this.yPos = 0;
}

PopupManager.prototype.showPopup = function(popupId){

    //show the overlay
    document.getElementById('overlay').style.visibility = 'visible';
    document.getElementById('overlay').style.height =  "2000px";
    document.getElementById('overlay').style.display = 'block';

    //get popup based in id
    document.getElementById(popupId).style.visibility = 'visible';
    document.getElementById(popupId).style.display = 'block';

    this.currentPopup = document.getElementById(popupId);

    //when we should the popup we scroll to the top and display it there
    //store where the user had previously scrolled to so when the popup
    //is closed we can return them to the same place
    if (self.pageYOffset) {
        this.yPos = self.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop){
        this.yPos = document.documentElement.scrollTop;
    } else if (document.body) {
        this.yPos = document.body.scrollTop;
    }
    window.scrollTo(0, 0);

    this.resizeOverlay("100%", "hidden");
    this.hideSelects("hidden");    

    document.getElementById('contentPane').className = "printView";

}


PopupManager.prototype.hidePopup = function(){
    document.getElementById('overlay').style.height =  "0px";
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('overlay').style.visibility = 'hidden';

    this.currentPopup.style.visibility = 'hidden';
    this.currentPopup.style.display = 'none';

    document.getElementById('contentPane').className = "screenView";

    window.scrollTo(0, this.yPos);
    this.resizeOverlay('auto', 'auto');
    this.hideSelects('visible');

    this.currentPopup = null;
}

//IE6 is a pain, it always puts selects on the top (above our overlay).  Hide em so
//users wont click on em
PopupManager.prototype.hideSelects = function(visibility){
    var selects = document.getElementsByTagName('select');

    for(var i = 0; i < selects.length; i++) {
        if(getParentByClassName(selects[i], "popup") == null){
            selects[i].style.visibility = visibility;
        }
    }
}

//this gets rid of scrollbars. otherwise you could scroll past the end of the overlay.
PopupManager.prototype.resizeOverlay = function(height, overflow){
    var bod = document.getElementsByTagName('body')[0];
    bod.style.height = height;
    bod.style.overflow = overflow;

    var htm = document.getElementsByTagName('html')[0];
    htm.style.height = height;
    htm.style.overflow = overflow;
}


function getParentByTagName(element, tag) {
    while (element != null && element.tagName != tag) {
        element = element.parentNode;
    }
    return element;
}

function getParentByClassName(element, theName) {
    while (element != null && element.className != theName) {
        element = element.parentNode;
    }
    return element;
}
