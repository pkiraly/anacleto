function getRadioValue(oRadioGroup) {
  var i = oRadioGroup.length;
  do {
    if (oRadioGroup[--i].checked)
    return oRadioGroup[i].value;
  } while (i)
  return "";
}

function setRadioValue(oRadioGroup, sValue){
  var len = oRadioGroup.length;
  for(var i=0; i<len; i++) {
    if (oRadioGroup[i].value == sValue) {
      oRadioGroup[i].checked = true;
      return true;
    }
  }
  return false;
}

function getSelectValue(oSel) {
  return oSel.options[oSel.selectedIndex].value;
}

function setSelectValue(oSel, sValue) {
	var len = oSel.length;
  for(var i=0; i<len; i++) {
    if (oSel[i].value == sValue) {
      oSel[i].checked = true;
      oSel.selectedIndex = i;
      return true;
    }
  }
  return false;
}

function loadDivContentInto(oSourceDiv, oTargetDiv) {
  if(oTargetDiv != null && oSourceDiv != null) {
    oTargetDiv.innerHTML = oSourceDiv.innerHTML;
  }
}

function toJSNotation(text){
  text = text.substr(0,1).toUpperCase() + text.substr(1);
  return text.replace(/_(.)/g, "$1".toUpperCase());
}

function toHTMLNotation(text){
  text = text.substr(0,1).toLowerCase() + text.substr(1);
  return text.replace(/([A-Z])/g, '_' + "$1").toLowerCase();
}
