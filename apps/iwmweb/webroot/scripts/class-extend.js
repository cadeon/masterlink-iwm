/*
 *  Copyright (c) 2006, Greg Wiley
 *
 *  Permission is hereby granted, free of charge, to
 *  any person obtaining a copy of this software and
 *  associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is
 *  furnished to do so, subject to the following
 *  conditions:
 *
 *    The above copyright notice and this permission
 *    notice shall be included in all copies or
 *    substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY
 *  OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 *  LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 *  OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 *
 */


/*
 * Class creation. Create a new class following
 * Prototype initialization conventions.
 * (note: can still use the no-argument syntax with
 * the only semantic difference being that an
 * initializer will be created if no prototype
 * is provided).
 *
 * param proto: object to install as prototype
 *    of returned constructor
 */

Class.create = function( proto ) {
    var base =  function() {
      this.initialize.apply(this, arguments);
    }
    if ( proto )
	    // prototype provided, assign it
    	base.prototype = proto;
    	
    if ( !proto || !proto.initialize )
    	// provide a default initializer
		base.prototype.initialize = function() {
		}
		    	
    return base; 
}

/**
 * Class extension with constructor chain. This inserts
 * a method, "superInit," that should be called from within
 * a subclass's "initialize" method. superInit calls
 * the initialize method defined by the superclass.
 * 
 * Like Class.create above, this inserts an "initialize"
 * member if one is not provided
 *
 * naming note: sorry for the lame "superInit" member.
 *  "super" is reserved for future use by ECMA (plus
 *  using it blows up in IE). I thought of "init" but
 *  that's not descriptive enough and doesn't really
 *  differentiate it from "initialize"
 *
 * param superclass: constructor of superclass--
 *   assumed to have been created with Class.create
 *   (i.e. uses the initialize convention)
 * param proto: object to merge with subclass constructor
 *   prototype
 */
Class.extend = function( superclass, proto ) {
	// create new Prototype class
	
	var subclass =  function() {
	  // make sure object has a copy of init chain
	  //  -- fixes muli-object create bug 3/3/2006 -gjw
	  this.__inits = $A( subclass.prototype.__inits);
      this.initialize.apply(this, arguments);
    }
	
	// copy the superclass prototype
	Object.extend( subclass.prototype, superclass.prototype );
	
	if ( !subclass.prototype.superInit ) {
		// superclass is not an extension, so
		// set it up
		
		// create the initializer stack
		subclass.prototype.__inits = new Array();
		
		// create the superinitializer
		subclass.prototype.superInit = function() {
			// pop the immediate superclass initializer
			var init = this.__inits.pop();
			// ...and call it
			init.apply(this,arguments);
		} 
	} else {
		// make a shallow copy of the superclass init chain
		subclass.prototype.__inits = $A(superclass.prototype.__inits);
	}
	// push the superclass initializer
	subclass.prototype.__inits.push(superclass.prototype.initialize);

	// merge provided prototype
	if ( proto )
		// prototype provided
		Object.extend( subclass.prototype, proto );
		
	if (!proto || !proto.initialize )
		// provide a default initializer
		subclass.prototype.initialize = function() {
			this.superInit.apply(this,arguments);
		}
	
	return subclass;

} 

/*
 * class-extend.js-0.2
 * Feb-2006 Greg Wiley <greg@orthogony.com>
 */

/*
 * Changes:
 *   0.2; gjw -- fixed problem where object construction
 *    of a derived class would pop its initializer functions
 *    from the prototype's copy of the chain.
 */

/*
 * "I can't complain but sometimes I still do." -- Walsh
 */

