/**
 * remove \n, \r from JSON string, and convert quote entities to char
 * @param {String} sJSON
 * @return {String} sJSON
 */

function cleanJSON(sJSON) {
  sJSON = sJSON.replace(/&lt;/g, '<');
  sJSON = sJSON.replace(/&gt;/g, '>');
  sJSON = sJSON.replace(/&quot;/g, '"');
  sJSON = sJSON.replace(/&#39;/g, "'");
  sJSON = sJSON.replace(/\r/g, '');
  sJSON = sJSON.replace(/\n/g, ' ');
  return sJSON;
}

/**
 * receive data from server
 */
var AJAXcallback = {
  success: function(o) {
    var oResp, sResp = o.responseText;
    var func = oConn.getCallback(o.tId);
    sResp = cleanJSON(sResp);
    if(func != null){
      func.apply(new Object(), new Array(sResp));
    }
  }, 
 
  failure: function(o) {
    alert('Connection failure: ' + o.status);
  },
       
  argument: null
};

/**
 * AJAX object
 */
function AJAX() {

  this.sUrl = document.location.href;
  this.sUrl = this.sUrl.substr(this.sUrl.lastIndexOf('/')+1);
  this.callbacks = {};
  this.sParams = '';

  this.setUrl = function(psUrl) {
    this.sUrl = psUrl;
  }
  
  this.getUrl = function() {
    return this.sUrl;
  }
  
  this.setCallback = function(id, func) {
  	this.callbacks[id] = func;
  }
  
  this.getCallback = function(id) {
  	return this.callbacks[id];
  }
  
  this.setParam = function(name, value) {
    this.sParams += (this.sParams == '') ? '' : '&';
  	this.sParams += name + '=' + value;
  }

  /**
   * send data to server
   */
  this.transact = function(fnFunc) {
    var transaction = YAHOO.util.Connect.asyncRequest('POST', this.sUrl, AJAXcallback, this.sParams);
    this.setCallback(transaction.tId, fnFunc);
	this.sParams = '';
    return transaction;
  }
}
