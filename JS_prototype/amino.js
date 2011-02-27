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
        //ctx.rotate(-this.rotation*Math.PI/180,0,0);
        //ctx.translate(-this.tx,-this.ty);
    };
    return true;
}

function Group() {
    var self = this;
    this.children = [];
    this.add = function(n) {
        this.children[this.children.length] = n;
        return this;
    }
    this.draw = function(ctx) {
        for(i=0; i<this.children.length;i++) {
            this.children[i].draw(ctx);
        }
    }
};

function Text() {
    this.x = 0;
    this.y = 0;
    this.text = "-no text-";
    this.fill = "black";
    
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        ctx.fillText(this.text,this.x,this.y);
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
    this.setFill = function(fill) {
        this.fill = fill;
        return this;
    };
    return true;    
};

function Circle() {
    this.x = 0.0;
    this.y = 0.0;
    this.radius = 10.0;
    this.fill = "black";
    this.setFill = function(fill) {
        this.fill = fill;
        return this;
    }
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI*2, true); 
        ctx.closePath();
        ctx.fill();
    };
    return true;
};

function Rect() {
    this.x = 0.0;
    this.y = 0.0;
    this.w = 100.0;
    this.height = 100.0;
    this.fill = "red";
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        ctx.fillRect(this.x,this.y,this.w,this.height);
    };
    this.set = function(x,y,w,h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.height = h;
        return this;
    };
    this.setWidth = function(w) {
        this.w = w;
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
    this.setFill = function(fill) {
        this.fill = fill;
        return this;
    }
    
    return true;
};

function Runner() {
    this.root = "";
    this.anims = [];
    this.callbacks = [];
    this.tickIndex = 0;
    this.tickSum = 0;
    this.tickSamples = 100;
    this.tickList = [];
    this.lastTick = 0;
    
    var self = this;
    
    this.setCanvas = function(canvas) {
        self.canvas = canvas;
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
        ctx.fillStyle = "#bbbbbb";
        ctx.fillRect(0,0,500,500);
        
        //draw the scene
        self.root.draw(ctx);
        
        //draw a debugging overlay
        ctx.fillStyle = "black";
        ctx.fillText("timestamp " + new Date().getTime(),10,470);
        
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
        ctx.fillText("last msec/frame " + delta,10,480);
        ctx.fillText("avg msec/frame  " + fpsAverage,10,490);
        ctx.fillText("avg fps = " + (1.0/fpsAverage)*1000,10,500);
        //1/(x msec/frame ) = frames/msec
    };
    
    this.start = function() {
        var fps = 60;
        self.lastTick = new Date().getTime();
        setInterval(this.update,1000/fps);
    };
    
    this.addAnim = function(anim) {
        this.anims[this.anims.length] = anim;
        return this;
    };
    this.addCallback = function(callback) {
        this.callbacks[this.callbacks.length] = callback;
        return this;
    }
    return true;
}

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

