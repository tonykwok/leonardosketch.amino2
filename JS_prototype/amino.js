
// 'extend' is From Jo lib, by Dave Balmer
// syntactic sugar to make it easier to extend a class
Function.prototype.extend = function(superclass, proto) {
	// create our new subclass
	this.prototype = new superclass();

	// optional subclass methods and properties
	if (proto) {
		for (var i in proto)
			this.prototype[i] = proto[i];
	}
};


var DEBUG = true;
var tabcount = 0;
function indent() {
    tabcount++;
}
function outdent() {
    tabcount--;
}
function p(s) {
    if(DEBUG) {
        var tab = "";
        for(i=0;i<tabcount;i++) {
            tab = tab + "  ";
        }
        console.log(tab+s);
    }
}




/* The root node class. All nodes have a parent and could
potentially have children */

__node_hash_counter = 0;
function Node() {
    this.parent = null;
    this._hash = __node_hash_counter++;
    var self = this;
    this.setParent = function(parent) { this.parent = parent; return this; };
    this.getParent = function() { return this.parent; };
    this.visible = true;
    this.setVisible = function(visible) {
        self.visible = visible;
        return self;
    };
    return true;
}




/* transforms the children inside of it */

function Transform(n) {
    this.node = n;
    this.rotation = 0;
    this.translateX = 0;
    this.translateY = 0;
    this.setTranslateX = function(tx) {
        this.translateX = tx;
        return this;
    };
    this.setTranslateY = function(ty) {
        this.translateY = ty;
        return this;
    };
    this.draw = function(ctx) {
        ctx.save();
        ctx.translate(this.translateX,this.translateY);
        ctx.rotate(this.rotation*Math.PI/180,0,0);
        this.node.draw(ctx);
        ctx.restore();
    };
    return true;
}





/* A Group holds a set of child nodes. It does not draw anything
by itself, but setting visible to false will hide the children.
*/

function Group() {
    this.children = [];
    this.parent = null;
    var self = this;
    this.add = function(n) {
        self.children[self.children.length] = n;
        n.setParent(self);
        return self;
    };
    this.draw = function(ctx) {
        if(!self.visible) return;
        indent();
        for(var i=0; i<self.children.length;i++) {
            self.children[i].draw(ctx);
        }
        outdent();
    };
    this.clear = function() {
        self.children = [];
        return self;
    };
    this.contains = function(x,y) {
        return false;
    };
    this.hasChildren = function() {
        return true;
    };
    this.childCount = function() {
        return self.children.length;
    };
    this.getChild = function(n) {
        return self.children[n];
    };
    Node.call(this);
    return true;
};
Group.extend(Node, {});




/* The base of all shapes. Shapes are geometric
shapes which have a fill, a stroke, and opacity. They
may be filled or unfilled.  */
function Shape() {
    this.hasChildren = function() { return false; }
    
    this.fill = "red";
    this.setFill = function(fill) {
        this.fill = fill;
        return this;
    };
    this.getFill = function() {
        return this.fill;
    };
    this.strokeWidth = 0;
    this.setStrokeWidth = function(sw) {
        this.strokeWidth = sw;
        return this;
    };
    this.getStrokeWidth = function() {
        return this.strokeWidth;
    };
    this.contains = function() { return false; }
    Node.call(this);
    return true;
}
Shape.extend(Node);





/* A text shape. draws text on the screen with the
desired content, font, and color.
*/
function Text() {
    this.x = 0;
    this.y = 0;
    this.text = "-no text-";
    this.parent = null;
    
    this.draw = function(ctx) {
        var f = ctx.font;
        ctx.font = this.font;
        ctx.fillStyle = this.fill;
        ctx.fillText(this.text,this.x,this.y);
        ctx.font = f;
    };
    this.setText = function(text) {
        this.text = text;
        return this;
    };
    this.setX = function(x) {
        this.x = x;
        return this;
    };
    this.setY = function(y) {
        this.y = y;
        return this;
    };
    
    this.font = "20pt Verdana";
    this.setFont = function(font) {
        this.font = font;
        return this;
    }
    
    this.contains = function() { return false; }
    Shape.call(this);
    return true;    
};
Text.extend(Shape);





/* A circle shape */
function Circle() {
    this.x = 0.0;
    this.y = 0.0;
    this.radius = 10.0;
    this.fill = "black";
    var self = this;
    this.set = function(x,y,radius) {
        self.x = x;
        self.y = y;
        self.radius = radius;
        return self;
    };
    this.setFill = function(fill) {
        self.fill = fill;
        return self;
    };
    this.draw = function(ctx) {
        ctx.fillStyle = self.fill;
        ctx.beginPath();
        ctx.arc(self.x, self.y, self.radius, 0, Math.PI*2, true); 
        ctx.closePath();
        ctx.fill();
    };
    return true;
};
Circle.extend(Shape);




/* A rectangle shape. May be rounded or have straight corners */
function Rect() {
    this.x = 0.0;
    this.y = 0.0;
    this.width = 100.0;
    this.height = 100.0;
    this.set = function(x,y,w,h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    };
    this.setWidth = function(w) {
        this.width = w;
        return this;
    };
    this.setHeight = function(h) {
        this.height = h;
        return this;
    };
    this.setX = function(x) {
        this.x = x;
        return this;
    };
    this.setY = function(y) {
        this.y = y;
        return this;
    };
    this.corner = 0;
    
    this.setCorner = function(c) {
        this.corner = c;
        return this;
    };
    
    this.contains = function(x,y) {
        //console.log("comparing: " + this.x + " " + this.y + " " + this.width + " " + this.height + " --- " + x + " " + y);
        if(x >= this.x && x <= this.x + this.width) {
            if(y >= this.y && y<=this.y + this.height) {
                return true;
            }
        }
        return false;
    };
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        if(this.corner > 0) {
            var x = this.x;
            var y = this.y;
            var width = this.width;
            var height = this.height;
            var radius = this.corner;
            ctx.beginPath();
            ctx.moveTo(x + radius, y);
            ctx.lineTo(x + width - radius, y);
            ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
            ctx.lineTo(x + width, y + height - radius);
            ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
            ctx.lineTo(x + radius, y + height);
            ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
            ctx.lineTo(x, y + radius);
            ctx.quadraticCurveTo(x, y, x + radius, y);
            ctx.closePath();
            ctx.fill();
            if(this.strokeWidth > 0) {
                ctx.lineWidth = this.strokeWidth;
                ctx.stroke();
            }
        } else {
            ctx.fillRect(this.x,this.y,this.width,this.height);
            if(this.strokeWidth > 0) {
                ctx.lineWidth = this.strokeWidth;
                ctx.strokeRect(this.x,this.y,this.width,this.height);
            }
        }
    };
    return true;
};
Rect.extend(Shape);






/* The base mouse event. Has a reference to the node that
the event was on (if any), and the x and y coords. Will have
more functionality in the future. */
function MEvent() {
    this.node = null;
    this.x = -1;
    this.y = -1;
    var self = this;
    this.getNode = function() {
        return this.node;
    }
}






/* An Anim is a property animation. It animates a property
on an object over time, optionally looping and reversing direction
at the end of each loop (making it oscillate).
*/
function Anim(n,prop,start,end,duration) {
    this.node = n;
    this.prop = prop;
    this.started = false;
    this.startValue = start;
    this.endValue = end;
    this.duration = duration;
    this.loop = false;
    this.autoReverse = false;
    this.forward = true;
    
    var self = this;
    
    this.isStarted = function() {
        return self.started;
    };
    
    this.setLoop = function(loop) {
        this.loop = loop;
        return this;
    };
    
    this.setAutoReverse = function(r) {
        this.autoReverse = r;
        return this;
    };
    
    this.start = function(time) {
        self.startTime = time;
        self.started = true;
        self.node[self.prop] = self.startValue;
    };
    
    this.update = function(time) {
        var elapsed = time-self.startTime;
        var fract = 0.0;
        fract = elapsed/(duration*1000);
        if(fract > 1.0) {
            if(self.loop) {
                self.startTime = time;
                if(self.autoReverse) {
                    self.forward = !self.forward;
                }
                fract = 0.0;
            } else {
                return;
            }
        }
        
        if(!self.forward) {
            fract = 1.0-fract;
        }
        var value = (self.endValue-self.startValue)*fract + self.startValue;
        self.node[self.prop] = value;
        
    }
    return true;
}    








/* The core of Amino. It redraws the screen, processes events, and
processes animation. */

function Runner() {
    this.root = "";
    this.background = "gray";
    this.anims = [];
    this.callbacks = [];
    this.listeners = {};
    this.tickIndex = 0;
    this.tickSum = 0;
    this.tickSamples = 30;
    this.tickList = [];
    this.lastTick = 0;
    this.fps = 60;
    
    var self = this;
    
    this.setBackground = function(background) {
        self.background = background;
        return self;
    };
    
    this.setFPS = function(fps) {
        self.fps = fps;
        return self;
    };
    
    this.setCanvas = function(canvas) {
        self.canvas = canvas;
        var _mouse_pressed = false;
        var _drag_target = null;
        canvas.addEventListener('mousedown',function(e){
            _mouse_pressed = true;
            //send target node event first
            var node = self.findNode(self.root,e.offsetX,e.offsetY);
            var evt = new MEvent();
            evt.node = node;
            evt.x = e.offsetX;
            evt.y = e.offsetY;
            if(node) {
                var start = node;
                _drag_target = node;
                while(start) {
                    self.fireEvent(start,evt);
                    start = start.getParent();
                }
            }
            //send general events next
            self.fireEvent("MOUSE_PRESS",evt);
        },false);
        canvas.addEventListener('mousemove', function(e) {
            if(_mouse_pressed) {
                var node = self.findNode(self.root,e.offsetX,e.offsetY);
                var evt = new MEvent();
                
                //redirect events to current drag target, if applicable
                if(_drag_target) {
                    node = _drag_target;
                }
                evt.node = node;
                evt.x = e.offsetX;
                evt.y = e.offsetY;
                if(node) {
                    var start = node;
                    while(start) {
                        self.fireEvent(start,evt);
                        start = start.getParent();
                    }
                }
                //send general events next
                self.fireEvent("MOUSE_DRAG",evt);
            }
        });
        canvas.addEventListener('mouseup',function(e){
            _mouse_pressed = false;
            _drag_target = false;
            //send target node event first
            var node = self.findNode(self.root,e.offsetX,e.offsetY);
            var evt = new MEvent();
            evt.node = node;
            evt.x = e.offsetX;
            evt.y = e.offsetY;
            if(node) {
                var start = node;
                while(start) {
                    self.fireEvent(start,evt);
                    start = start.getParent();
                }
            }
            //send general events next
            self.fireEvent("MOUSE_RELEASE",evt);
        },false);
        return self;
    };
    
    this.findNode = function(node,x,y) {
        //p("findNode:" + node + " " + x + " " + y);
        //p(node);
        if(node.contains(x,y)) {
            //p("node contains it");
            return node;
        }
        if(node.hasChildren()) {
            indent();
            for(var i=0;i<node.childCount();i++) {
                var n = self.findNode(node.getChild(i),x,y);
                if(n) {
                    //p("backing up");
                    outdent();
                    return n;
                }
            }
            outdent();
        }
        return null;
    };
    
    this.fireEvent = function(key,e) {
        //p("firing event for key: ");
        //console.log(key);
        var k = key;
        if(key._hash) {
            k = key._hash;
        }
        //p("Using real key: " + k);
        if(self.listeners[k]) {
            for(var i=0; i<self.listeners[k].length; i++) {
                self.listeners[k][i](e);
            }
        }
    };
    
    this.update = function() {
        var time = new Date().getTime();
        for(i=0;i<self.anims.length; i++) {
            var a = self.anims[i];
            if(!a.isStarted()) {
                a.start(time);
                continue;
            }
            a.update(time);
        }
        for(i=0;i<self.callbacks.length;i++) {
            self.callbacks[i]();
        }
        
        var ctx = self.canvas.getContext("2d");
        //fill the background
        ctx.fillStyle = self.background;
        ctx.fillRect(0,0,self.canvas.width,self.canvas.height);
        
        //draw the scene
        self.root.draw(ctx);
        
        ctx.save();
        ctx.translate(0,self.canvas.height-50);
        ctx.fillStyle = "gray";
        ctx.fillRect(0,-10,200,60);
        //draw a debugging overlay
        ctx.fillStyle = "black";
        ctx.fillText("timestamp " + new Date().getTime(),10,0);
        
        //calc fps
        var delta = time-self.lastTick;
        self.lastTick = time;
        if(self.tickList.length <= self.tickIndex) {
            self.tickList[self.tickList.length] = 0;
        }
        self.tickSum -= self.tickList[self.tickIndex];
        self.tickSum += delta;
        self.tickList[self.tickIndex]=delta;
        ++self.tickIndex;
        if(self.tickIndex>=self.tickSamples) {
            self.tickIndex = 0;
        }
        var fpsAverage = self.tickSum/self.tickSamples;
        ctx.fillText("last msec/frame " + delta,10,10);
        ctx.fillText("last frame msec " + (new Date().getTime()-time),10,20);
        ctx.fillText("avg msec/frame  " + (fpsAverage).toPrecision(3),10,30);
        ctx.fillText("avg fps = " + ((1.0/fpsAverage)*1000).toPrecision(3),10,40);
        ctx.restore();
    };
    
    this.start = function() {
        self.lastTick = new Date().getTime();
        setInterval(this.update,1000/self.fps);
    };
    
    this.addAnim = function(anim) {
        this.anims[this.anims.length] = anim;
        return this;
    };
    
    this.addCallback = function(callback) {
        this.callbacks[this.callbacks.length] = callback;
        return this;
    };
    
    this.listen = function(eventKey, eventTarget, callback) {
        if(eventTarget) {
            if(!this.listeners[eventTarget._hash]) {
                this.listeners[eventTarget._hash] = [];
            }
            this.listeners[eventTarget._hash].push(callback);
        } else {
            if(!this.listeners[eventKey]) {
                this.listeners[eventKey] = [];
            }
            this.listeners[eventKey].push(callback);
        }
    };
    
    return true;
}





