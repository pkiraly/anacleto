YAHOO.sched.util = function(){};
YAHOO.sched.util.prototype = {

  defaultBackgroundColor : "notSet",
  defaultColor : "notSet",
  actualId : {type: 'SEsimple', component: 'minutes'},
  defaultTargetInputField : 'book_schedulingCronExpression',
  actualMode : "add",
  oSch : null,
  m : null,
  h : null,
  d : null,
  n : null,
  w : null,

  init : function () {
    this.m = new YAHOO.sched.minutes();
    this.h = new YAHOO.sched.hours();
    this.d = new YAHOO.sched.days();
    this.n = new YAHOO.sched.months();
    this.w = new YAHOO.sched.wdays();

    this.m.setUp();
    this.h.setUp();
    this.d.setUp();
    this.n.setUp();
    this.w.setUp();
    this._setScheduler();
  },

  show : function (type, id) {
    if(this.actualId[type] != "") {
      $D.setStyle(this._getActualMode() + '_' + this.actualId[type], 'display', 'none');
    }
    if(id != "") {
      $D.setStyle(this._getActualMode() + '_' + id, 'display', 'block');
    }
 
    if(type == 'component') {
      $D.setStyle(this._getActualMode() + '_' + this.actualId[type] + 'Selector', 'color', '#666');
      $D.setStyle(this._getActualMode() + '_' + id + 'Selector', 'color', '#000');
    }
    this.actualId[type] = id;
  },

  setPredefinited : function () {
    var oDiv = $(this._getActualMode() + '_' + 'predefinited');
    if(oDiv == null) 
      return;
      
    var sValue = oDiv[oDiv.selectedIndex].value;
    var aValues = sValue.split(" ");
  
    var oSpan, bIsActive, currentVal;

    this.m.setPrefixed(aValues[0]);
    this.h.setPrefixed(aValues[1]);
    this.d.setPrefixed(aValues[2]);
    this.n.setPrefixed(aValues[3]);
    this.w.setPrefixed(aValues[4]);
    this._setScheduler();
  },

  togglePeriodMode : function (id) {
    var oChekbox = $(this._getActualMode() + '_' + id + 'PeriodMode');
    var pMode = oChekbox.checked;
    var obj = this.getInstance(id);
    obj.periodMode = pMode; 
    obj.clear();
  },

  // facade function
  clear : function (id) {
    var obj = this.getInstance(id);
    obj.clear();
  },

  // facade function
  set : function (id) {
    var obj = this.getInstance(id.substr(4,1));
    obj.set(id);
  },
  
  _setScheduler : function () {
    if(this.oSch == null)
      this.oSch = $(this._getTargetInputField());

    if(this.oSch != null) {
      this.oSch.value = 
             ((this.m.periodMode == true 
            && this.m.currentValues[this.actualMode][0] != '*') ? '*/' : '')
             + this.m.currentValues[this.actualMode].join(',')
       + ' ' + ((this.h.periodMode == true
            && this.h.currentValues[this.actualMode][0] != '*') ? '*/' : '') 
             + this.h.currentValues[this.actualMode].join(',')
       + ' ' + ((this.d.periodMode == true
            && this.d.currentValues[this.actualMode][0] != '*') ? '*/' : '') 
             + this.d.currentValues[this.actualMode].join(',')
       + ' ' + this.n.currentValues[this.actualMode].join(',')
       + ' ' + this.w.currentValues[this.actualMode].join(',');
    }
  },

  _changeStyle : function (oSpan) {
    if(oSpan == null) {
      alert("_changeStyle: null oSpan");
      return;
    }
    
    if(oSpan.getAttribute('isActive') == 'true') {
      this._deActivate(oSpan);
    } else {
      this._activate(oSpan);
    }
    return oSpan.getAttribute('isActive');
  },

  _activate : function (oSpan) {
    if(oSpan == null) {
      alert("_activate: null oSpan");
      return;
    }

    if(this.defaultColor == "notSet") {
      this.defaultColor = oSpan.style.color;
    }
    oSpan.style.color = "white";

    if(this.defaultBackgroundColor == "notSet") {
      this.defaultBackgroundColor = oSpan.style.backgroundColor;
    }
    oSpan.style.backgroundColor = "black";
    oSpan.setAttribute('isActive', 'true');
  },

  _deActivate : function (oSpan) {
    if(oSpan == null) {
      alert("_deActivate: null oSpan");
      return;
    }
    oSpan.style.color = this.defaultColor;
    oSpan.style.backgroundColor = this.defaultBackgroundColor;
    oSpan.setAttribute('isActive', 'false');
  },
  
  _getTargetInputField : function() {
    return this._getActualMode() + '_' + this.defaultTargetInputField;
  },

  _getActualMode : function() {
    return this.actualMode;
  },

  _setActualMode : function(actualMode) {
    this.actualMode = actualMode;
    this.oSch = $(this._getTargetInputField());
    this.m._setActualMode(actualMode);
    this.h._setActualMode(actualMode);
    this.d._setActualMode(actualMode);
    this.n._setActualMode(actualMode);
    this.w._setActualMode(actualMode);
  },

  getInstance : function(id) {
    switch (id) {
      case 'minutes': 
      case this.m.idPrefix: 
        return this.m; break;
      case 'hours':   
      case this.h.idPrefix:   
        return this.h; break;
      case 'days':    
      case this.d.idPrefix:
        return this.d; break;
      case 'months':
      case this.n.idPrefix:
        return this.n; break;
      case 'wdays':
      case this.w.idPrefix:
        return this.w; break;
    }
  }

};

