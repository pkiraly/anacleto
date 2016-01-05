function buildUpSpecialForms() {
  var aContainers = [ 'Add', 'Mod' ];
  var oFormData;
  for(var i=0; i<aContainers.length; i++) {
    for(sFormName in aFormDefinitions) {
      oFormData = aFormDefinitions[sFormName];
      if(oFormData.type == 'NodeType') {
        var oDiv = eval( 'o' 
				                 + aContainers[i] 
												 + sFormName.substr(0,sFormName.length-4) 
												 + 'Container' );
        drawFrom(oDiv, sFormName, aContainers[i].toLowerCase());
      }
    }
  }
  
  for(sFormName in aFormDefinitions) {
		oFormData = aFormDefinitions[sFormName];
    if(oFormData.type == 'contentType') {
      drawFrom(document.body, sFormName);
    }
  }
}

function drawFrom(oParent, sFormName) {
  var oFormData = aFormDefinitions[sFormName];
  var prefix = (arguments.length == 3) ? arguments[2] : '';

  if(oFormData.type == 'contentType') {
    var oForm = $(sFormName + '_form');
    if(oForm == null) {
      oParent.appendChild(document.createTextNode("\n"));
      oParent.appendChild(document.createComment('*  ' + sFormName + '_form  *'));
      oParent.appendChild(document.createTextNode("\n"));
      oForm = document.createElement("div");
      oForm.setAttribute('id', sFormName + '_form');
      oParent.appendChild(oForm);
    }
    oForm.innerHTML = "";
    var oFS = document.createElement("fieldset");
    oForm.appendChild(oFS);
  } else {
    var oFS = document.createElement("fieldset");
    oParent.appendChild(oFS);
  }

  var oLNG = document.createElement("legend");
  oLNG.appendChild(document.createTextNode(oFormData.legend));
  oFS.appendChild(oLNG);
  var iFieldCounter = 0;
  var bHiden = false;
  var bIsPrevHiden = false;
  for(sFieldName in oFormData.fields) {
    var oFieldData = oFormData.fields[sFieldName];
    
    bIsPrevHiden = bHiden; 
    bHiden = false;
    if(typeof oFieldData != 'undefined' && oFieldData.hide == true) {
      bHiden = true;
    }

    if(bHiden) {
      var oDiv = document.createElement("span");
      oDiv.setAttribute('id', prefix + '_' + sFieldName + 'Container');
      oFS.appendChild(oDiv);
      $D.setStyle(oDiv, 'display', 'none');
    }
    var oParentDiv = (bHiden) ? oDiv : oFS;
      
    if(iFieldCounter > 0) { // && bIsPrevHiden == false
      oParentDiv.appendChild(document.createElement("br"));
    }

    // label
    var label = (typeof oFieldData['label'] != "undefined")
              ? oFieldData.label
              : sFieldName;
    oParentDiv.appendChild(document.createTextNode("\n"));
    oParentDiv.appendChild(document.createComment(' ['+label+'] '));
    oParentDiv.appendChild(document.createTextNode("\n"));
    var oEl = document.createElement("label");
    oEl.appendChild(document.createTextNode(label + ': '));
    oEl.htmlFor = ((prefix == '') ? '' : prefix + '_') 
		            + oFormData.prefix + '_' + sFieldName;
    oParentDiv.appendChild(oEl);
    oParentDiv.appendChild(document.createTextNode("\n"));

    // input
    if(oFieldData.type == 'text') {
      oEl = document.createElement("input");
      oEl.setAttribute('type', oFieldData.type);
      oEl.setAttribute('name', oFormData.prefix + '_' + sFieldName);
      if(prefix != ''){
        oEl.setAttribute('id', prefix + '_' + oFormData.prefix + '_' + sFieldName);
      } else {
        oEl.setAttribute('id', oFormData.prefix + '_' + sFieldName);
      }
      if(typeof oFieldData.onchange != 'undefined') {
        oEl.onchange = function() { oFieldData.onchange };
      }
      
      oParentDiv.appendChild(oEl);
    }

    else if(oFieldData.type == 'radio') {
      var ids = new Array();
      for(var j=0; j<oFieldData.values.length; j++) {
        var oEl;
        try{
          oEl = document.createElement('<input type="radio" name="' + oFormData.prefix + '_' + sFieldName + '" />');
        }catch(err){
          oEl = document.createElement('input');
          oEl.setAttribute('type','radio');
          oEl.setAttribute('name', oFormData.prefix + '_' + sFieldName);
        }
        oEl.setAttribute('id', prefix + '_' + oFormData.prefix + '_' + sFieldName + j);
        oEl.setAttribute('value', oFieldData.values[j][0]);
        // oEl.setAttribute('class', 'radio');
        oEl.className = 'radio';
        if(typeof oFieldData.values[j][2] != 'undefined' && oFieldData.values[j][2] == 'checked') {
          oEl.setAttribute('checked', 'checked');
        }

        if(typeof oFieldData.onchange != 'undefined') {
          var bRet = $E.addListener(oEl, "click", oFieldData.onchange);
        }
        oParentDiv.appendChild(oEl);

        oEl = document.createElement("label");
        oParentDiv.appendChild(oEl);
        oEl.className = 'radio';
        oEl.htmlFor = prefix + '_' + oFormData.prefix + '_' + sFieldName + j;
        oEl.innerHTML = oFieldData.values[j][1];
      }
    }

    else if(oFieldData.type == 'select') {
      var oSel = document.createElement("select");
      oSel.setAttribute('name', oFormData.prefix + '_' + sFieldName);
      oSel.setAttribute('id', prefix + '_' + oFormData.prefix + '_' + sFieldName);
      if(typeof oFieldData.onchange != 'undefined') {
        var bRet = $E.addListener(oSel, "change", oFieldData.onchange);
      }
      for(var j=0; j<oFieldData.values.length; j++) {
        var aOption = oFieldData.values[j];
        oEl = document.createElement("option");
        oEl.setAttribute('value', aOption[0]);
        if(typeof aOption[2] != 'undefined') {
          oEl.setAttribute('disabled', aOption[2]);
          oEl.className = 'disabled';
        }
        oEl.appendChild(document.createTextNode(aOption[1]));
        oSel.appendChild(oEl);
      }
      oParentDiv.appendChild(oSel);
    }

    iFieldCounter++;
  }
}

function showActionPanel (e, obj) {
  currentID = this.id.substr(4);
  
  // move panel
  $D.setStyle("actionPanel", "display", "block");
  // var xy = [$E.getPageX(e)+20, $E.getPageY(e)];
	var x = ($D.getViewportWidth()-parseInt($D.getStyle('actionPanel', 'width'))-20);
  var xy = [x, 20]; //$E.getPageY(e)
  $D.setXY('actionPanel', xy);
	
	$D.setStyle('span' + currentID, "font-weight", "bold");

  // get content type
  var oEl = $('node'+currentID);
  var sNodeType = oEl.getAttribute('node_type');
  var sContentType = oEl.getAttribute('contentType');

  var oForm = $(sContentType + '_form');
  if(oForm != null) {
    $('mod_special_container').innerHTML = oForm.innerHTML;
    $('add_special_container').innerHTML = oForm.innerHTML;
	}

  // set basic values
  oAddForm.elements['name'].value = '';
  oModForm.elements['name'].value = oEl.getAttribute('name');

  setRadioValue(oAddForm.elements['node_type'], sNodeType);
  setRadioValue(oModForm.elements['node_type'], sNodeType);

  // update add_active_type_container
  showNodeTypeForm(sNodeType, 'Add');
  showNodeTypeForm(sNodeType, 'Mod');
  
  // populate mod's NodeType form
  /*
  var map = {};
  var text = '';
  for(var i=0; i<oModForm.elements.length; i+=1) {
    map[oModForm.elements[i].name] = i;
    text += ' ' + oModForm.elements[i].name + '=' + i + oModForm.elements[i].nodeName + oModForm.elements[i].type;
  }
  // alert(text);
  */
 
  var oNode = aFormDefinitions[sNodeType + 'Node'];
  for(sFieldName in oNode.fields) {
    var oFieldData = oNode.fields[sFieldName];
    var sFieldNameInForm = 'mod_' + oNode.prefix + '_' + sFieldName;
    // var sFieldNameInForm = sFieldName;
    var sFieldValue = oEl.getAttribute(sFieldName);
		if(sFieldValue == null) {
			sFieldValue = '';
		}
		
    if(oFieldData.type == 'text') {
      oModForm.elements[sFieldNameInForm].value = sFieldValue;
    }
    else if(oFieldData.type == 'radio') {
      setRadioValue(oModForm.elements[oNode.prefix + '_' + sFieldName], sFieldValue);
    }
    else if(oFieldData.type == 'select') {
      setSelectValue(oModForm.elements[sFieldNameInForm], sFieldValue);
    }
  }

  // update special_container
  if(sContentType != "") {
    var oSourceDiv = $(sContentType + '_form');
		
    var oTargetDiv = $('add_special_container');
    loadDivContentInto(oSourceDiv, oTargetDiv);
    $D.setStyle('add_special_container', "display", "block");
    setSelectValue(oAddForm.elements["add_book_contentType"], sContentType);

    oTargetDiv = $('mod_special_container');
    loadDivContentInto(oSourceDiv, oTargetDiv);
    $D.setStyle('mod_special_container', "display", "block");
    setSelectValue(oModForm.elements["mod_book_contentType"], sContentType);
  }

  // set special values
  if(sNodeType == "book" && sContentType != "") {
    var prefix = aFormDefinitions[sContentType].prefix;
    for(sFieldName in aFormDefinitions[sContentType].fields) {
      var field = prefix + '_' + sFieldName;
      oModForm.elements[field].value = oEl.getAttribute(sFieldName);
    }
  }
};

function showPanel(id) {
  for(var i=0; i<aPanels.length; i+=1) {
    if(aPanels[i] == id) {
      $D.setStyle(aPanels[i], "display", "block");
      $D.addClass('cmd_' + aPanels[i], 'selected');
    }
    else {
      $D.setStyle(aPanels[i], "display", "none");
      $D.removeClass('cmd_' + aPanels[i], 'selected');
    }
  }
  this.activePanel = id;
  if(id == 'panel_add') {
    s._setActualMode('add');
  } else if(id == 'panel_modify') {
    s._setActualMode('mod');
  }
}

function showSpecialFields(e, oEl, sTargetDiv) {
  var prefix = oEl.id.substr(0, 3);
  var oTargetDiv = $(prefix + '_' + sTargetDiv);
  var oSel = oEl;
  if(oSel.options[oSel.selectedIndex].disabled == true) {
    alert("This is not implemented yet");
    oSel.selectedIndex = savedContentTypeIndex;
    return true;
  }
  var sType = oSel.options[oSel.selectedIndex].value;
  savedContentTypeIndex = oSel.selectedIndex;

  if(sType == "") {
    $D.setStyle(oTargetDiv, "display", "none");
  }
  else {
    $D.setStyle(oTargetDiv, "display", "block");
  }
  
  var oSourceDiv = $(sType + '_form');
  loadDivContentInto(oSourceDiv, oTargetDiv);
}

function fnShowBasicForm () {
  $D.setStyle("basicFormPanel", "display", "block");
  $D.setXY('basicFormPanel', $D.getXY("actionPanel"));
};

function onNodeTypeChange(oEl) {
  var bSelected = false, sNodeType = '';
  
  var oNodeTypes = oEl.elements["node_type"];
  for (var i=0; i<oNodeTypes.length; i++) {
    if (oNodeTypes[i].checked){
      sNodeType = oNodeTypes[i].value;
      bSelected = true;
    }
  }
  if(bSelected == true) {
    var prefix = (oEl.id == 'add_form') ? 'Add' : 'Mod';
    showNodeTypeForm(sNodeType, prefix);
    var sShowHide = (sNodeType == 'Shelf') ? 'none' : 'block'; 
    var oDiv = eval('o' + prefix + 'SpecialContainer');
    $D.setStyle(oDiv, 'display', sShowHide);
    // sNodeType
  }
  return true;
}

function onSchedChange(e, oDiv) {
  var prefix = oDiv.id.substr(0,4);
  var bSelected = false, sNodeType = '';
  
  var oScheduled = (prefix == 'add_')
	               ? oAddForm.elements["book_scheduled"]
	               : oModForm.elements["book_scheduled"];
  if(oScheduled == null) {
    // alert(prefix + "book_scheduled is null");
  }
  for (var i=0; i<oScheduled.length; i++) {
    if (oScheduled[i].checked){
      bScheduled = oScheduled[i].value;
      bSelected = true;
    }
  }
  if(bSelected == true) {
    var showType = (bScheduled == 'true') ? 'inline' : 'none';
    $D.setStyle(prefix + 'schedulingCronExpressionContainer', 'display', showType);
  }
  return true;
}

function showNodeTypeForm(sNodeTypeName, prefix) {
  var active, passive;
  if(sNodeTypeName == 'shelf') {
    active = eval('o' + prefix + 'shelfContainer');
    passive = eval('o' + prefix + 'bookContainer');
  }
  else if(sNodeTypeName == 'book') {
    active = eval('o' + prefix + 'bookContainer');
    passive = eval('o' + prefix + 'shelfContainer');
  }
  $D.setStyle(active, 'display', 'block');
  $D.setStyle(passive, 'display', 'none');

  return true;
}

