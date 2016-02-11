		function IWMNavigator() {
			this.defaultMenu = "define";
			this.defaultSubMenu = "templates";
			this.currentMenu = "define";
		}

		IWMNavigator.prototype.showDefault = function(e){
			//once the user moves down away from the header we will set the
			//menu back to reflect their current page


			if(Event.pointerY(e)  > 75 ){

				//unhighligh the previously selected header and hide its submenu
				$(this.currentMenu + 'TopNav').className = 'topNavItem';
				$(this.currentMenu + 'SubNav').style.visibility = 'hidden';

				//highlight the newly selected header and show the submenu
				$(this.defaultMenu + 'TopNav').className = 'selectNav';
				$(this.defaultMenu + 'SubNav').style.visibility = 'visible';

				$(this.defaultSubMenu + 'SubNav').className = 'selectNav';
				this.currentMenu = this.defaultMenu;
	 		}
		}


		IWMNavigator.prototype.selectHeader = function(e) {
			if (!e) { var e = window.event; }

            if(Event.element(e).tagName == 'H4'){
                Event.element(e).className='highLight';

                var theSection = Event.element(e).parentNode.id.split('TopNav')[0];

                //unhighligh the previously selected header and hide its submenu
                $(this.currentMenu + 'TopNav').className = 'topNavItem';
                $(this.currentMenu + 'SubNav').style.visibility = 'hidden';

                //highlight the newly selected header and show the submenu
                $(theSection + 'TopNav').className = 'selectNav';
                $(theSection + 'SubNav').style.visibility = 'visible';

                //record this header as the current menu
                this.currentMenu = theSection;
            }
        }

		IWMNavigator.prototype.init = function(section, sectionItem){
			this.defaultMenu = section;
			this.currentMenu = section;
			this.defaultSubMenu = sectionItem;

			//var theTopNavsItems = document.getElementsByClassName('navHeader');
            var theTopNavsItems = document.getElementsByClassName('topNavItem');
            var  _this = this;

            //if you leave the top of the page go back to the default menu
             $('sectionLinks').onmouseout = function(e) {
                if(Event.element){
                    if (!e) { var e = window.event; }
                    if(Event.element(e).id == "sectionLinks"){
                        _this.showDefault(e);
                    }
                }
             }

			for (var x in theTopNavsItems) {
//				theTopNavsItems[x].onmouseover = function(e) {_this.selectHeader(e) };
                theTopNavsItems[x].onmouseover = function(e) {
                    if(Event.element){

                        if (!e) {var e = window.event;}
                        //Event.element(e).style.textDecoration = 'underline';
                        _this.selectHeader(e);
                    }
                }
		        theTopNavsItems[x].onmouseout = function(e) {
                    if(Event.element){                                        
                        if (!e) {var e = window.event;}
                        Event.element(e).style.textDecoration = 'none';
                    }
                }

/*
                theTopNavsItems[x].onclick = function(e) {
                    _this.selectHeader(e) };
*/
			}

			//init all the event handlers
			var theSubNavsItems = document.getElementsByClassName('subNavItem');
			for (var x in theSubNavsItems) {
				theSubNavsItems[x].onmouseover = function(e) {
                    if(Event.element){
                        if (!e) { var e = window.event;}
                        Event.element(e).className='highLight';
                    }
                }

				theSubNavsItems[x].onmousedown = function(e) {
                    if(Event.element){
                        if (!e) {var e = window.event;}
                        Event.element(e).className='selectNav';
                    }
                }

				theSubNavsItems[x].onmouseout = function(e) {
                    if(Event.element){
                        if (!e) {var e = window.event; }
                        Event.element(e).className='subNavItem';
                    }
                }
			}

			$(this.defaultMenu + 'TopNav').className = 'selectNav';
			$(this.defaultMenu + 'SubNav').style.visibility = 'visible';
			$(this.defaultSubMenu + 'SubNav').className = 'selectNav';
		}

