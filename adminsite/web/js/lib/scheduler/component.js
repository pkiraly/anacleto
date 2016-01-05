YAHOO.sched.Component = function(){};
YAHOO.sched.Component.prototype = {

  name : "default",
  baseValue : '*',
  idPrefix : "",
  currentValues : { 
    add : new Array(this.baseValue),
    mod : new Array(this.baseValue)
  },
  periodMode : false,
  actualMode : 'add',

  set : function (id) {
    if(this.periodMode === true) {
      if(this.currentValues[this.actualMode][0] != '*') {
        s._changeStyle($(s._getActualMode() + '_' + this.idPrefix + this.currentValues[this.actualMode][0]));
        this.currentValues[this.actualMode] = new Array();
      }
    }
    var bIsActive = s._changeStyle($(id));
    var currentValue = parseInt(id.substr(5));
    this.changeArray(currentValue, bIsActive);
    s._setScheduler();
  },

  clear : function () {
    for(var i=0,len=this.currentValues[this.actualMode].length; i<len; i+=1) {
      if(this.currentValues[this.actualMode][i] != '*') {
        s._deActivate($(s._getActualMode() + '_' + this.idPrefix + this.currentValues[this.actualMode][i]));
      }
    }
    this.currentValues[this.actualMode] = new Array(this.baseValue);
    s._setScheduler();
  },

  setUp : function () {
    $(this.getContainer()).innerHTML = this.getTabularData();
    this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
  },

  getTabularData : function () {},

  getContainer : function() {
    return this.name + "Container";
  },

  changeArray : function (currentValue, bIsActive) {
    if(bIsActive == 'true') {
      if(this.currentValues[this.actualMode][0] == this.baseValue) {
        this.currentValues[this.actualMode][0] = currentValue;
      } else {
        this.currentValues[this.actualMode].push(currentValue);
        var temp = this.currentValues[this.actualMode];
        this.currentValues[this.actualMode] = temp.sortNum();
      }
    } else {
      if(this.currentValues[this.actualMode] == currentValue) {
        this.currentValues[this.actualMode][0] = this.baseValue;
      } else {
        var i = this.currentValues[this.actualMode].inArray(currentValue);
        var temp = new Array();
        for(var j=0,len=this.currentValues[this.actualMode].length; j<len; j+=1) {
          if(j != i)
            temp.push(this.currentValues[this.actualMode][j]);
        }
        this.currentValues[this.actualMode] = temp.sortNum();
      }
    }
  },
  
  resetAll : function() {
    for(var i=0,len=this.currentValues[this.actualMode].length; i<len; i+=1) {
      if(this.currentValues[this.actualMode][i] != '*') {
        s._changeStyle($(s._getActualMode() + '_' + this.idPrefix + this.currentValues[this.actualMode][i]));
      }
    }
  },
  
  setPrefixed : function(currentVal) {
    this.resetAll();
    if(currentVal != '*') {
      s._changeStyle($(s._getActualMode() + '_' + this.idPrefix + currentVal));
    }
    this.currentValues[this.actualMode] = new Array(currentVal);
  },

  _getActualMode : function() {
    return this.actualMode;
  },

  _setActualMode : function(actualMode) {
    this.actualMode = actualMode;
  }

};

// minutes
YAHOO.sched.minutes = function(){
  arguments.callee.superclass.constructor.call(this);
  this.name = "minutes";
  this.idPrefix = "m";
  this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
}
YAHOO.extend(YAHOO.sched.minutes, YAHOO.sched.Component);
YAHOO.sched.minutes.prototype.getTabularData = function(){
  var text = "";
  for(var i=0; i<60; i+=1) {
    if(i > 0)
      text += ' ';
    if(i < 10)
      text += '&nbsp;';
    text += '<span id="' + this.idPrefix + i + '" onclick="s.set(this.id)">' + i + '</span>';
    if((i+1) % 10 == 0)
      text += "<br/>\n";
  }
  return text;
};

// hours
YAHOO.sched.hours = function(){
  arguments.callee.superclass.constructor.call(this);
  this.name = "hours";
  this.idPrefix = "h";
  this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
}
YAHOO.extend(YAHOO.sched.hours, YAHOO.sched.Component);
YAHOO.sched.hours.prototype.getTabularData = function(){
  var text = "";
  for(var i=0; i<24; i+=1) {
    if(i > 0)
      text += ' ';
    if(i < 10)
      text += '&nbsp;';
    text += '<span id="' + this.idPrefix + i + '" onclick="s.set(this.id)">' + i + '</span>';
    if((i+1) % 8 == 0)
      text += "<br/>";
  }
  return text;
};

// days
YAHOO.sched.days = function(){
  arguments.callee.superclass.constructor.call(this);
  this.name = "days";
  this.idPrefix = "d";
  this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
}
YAHOO.extend(YAHOO.sched.days, YAHOO.sched.Component);
YAHOO.sched.days.prototype.getTabularData = function(){
  var text = "";
  for(var i=1; i<=31; i+=1) {
    if(i > 0)
      text += ' ';
    if(i < 10)
      text += '&nbsp;';
    text += '<span id="' + this.idPrefix + i + '" onclick="s.set(this.id)">' + i + '</span>';
    if(i % 7 == 0)
      text += "<br/>";
  }
  return text;
};

// months
YAHOO.sched.months = function(){
  arguments.callee.superclass.constructor.call(this);
  this.name = "months";
  this.idPrefix = "n";
  this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
}
YAHOO.extend(YAHOO.sched.months, YAHOO.sched.Component);
YAHOO.sched.months.prototype.getTabularData = function(){
  var text = "";
  var monthVals = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
  for(var i=0,len=monthVals.length; i<len; i+=1) {
    text += '<span id="' + this.idPrefix + (i+1) + '" onclick="s.set(this.id)">' + monthVals[i] + '</span><br />';
  }
  return text;
};

// wdays
YAHOO.sched.wdays = function(){
  arguments.callee.superclass.constructor.call(this);
  this.name = "wdays";
  this.idPrefix = "w";
  this.currentValues = {add: [this.baseValue], mod: [this.baseValue]};
}
YAHOO.extend(YAHOO.sched.wdays, YAHOO.sched.Component);
YAHOO.sched.wdays.prototype.getTabularData = function(){
  var text = "";
  var wdayVals = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');
  for(var i=0,len=wdayVals.length; i<len; i+=1) {
    text += '<span id="' + this.idPrefix + i + '" onclick="s.set(this.id)">' + wdayVals[i] + '</span><br />';
  }
  return text;
};
