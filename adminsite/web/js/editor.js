var ID = 1,
    currentID,
    oModForm,
    oAddForm,
    aPanels = ['panel_add', 'panel_move', 'panel_modify'],
    activePanel = 'panel_add',
    activeNodeTypeForm = 'shelfNode_form',
    savedContentTypeIndex = 0,
    oAddshelfContainer, oModshelfContainer, oAddbookContainer, oModbookContainer,
    actualFileName = 'noname.xml',
    oBasicForm = null,
    oConn = new AJAX(),
    XMLfiles = new Array(),
    fileListPos = [],
    IDs = [],
    s = null;

var $D = YAHOO.util.Dom;
var $E = YAHOO.util.Event;
var $ = $D.get;

function init() {
  s = new YAHOO.sched.util();
  s.init();

  oAddForm = $("add_form");
  oModForm = $("modify_form");
  oAddshelfContainer = $("add_shelf_container");
  oModshelfContainer = $("mod_shelf_container");
  oAddbookContainer = $("add_book_container");
  oModbookContainer = $("mod_book_container");
  oAddSpecialContainer = $("add_special_container");
  oModSpecialContainer = $("mod_special_container");
  $("add_basic_container").innerHTML = $("basicValues").innerHTML;
  $("mod_basic_container").innerHTML = $("basicValues").innerHTML;

  buildUpSpecialForms();
  
  addOnClickHandler(1);
  $E.addListener('importXMLlink', "click", getFileList);
  $E.addListener('exportXMLlink', "click", exportXML);
  $E.addListener('saveAndApplylink', "click", saveAndApply);

  var aModes = new Array('add', 'mod');
	var oSchedEditor = $('schedulerEditor');
  var HTML = oSchedEditor.innerHTML;
  
  var oInput, oDiv, mode;
  for(var i=0,len=aModes.length; i<len; i+=1) {
    mode = aModes[i];
    oDiv = document.createElement("DIV");
    oDiv.id = mode + "_book_schedulingCronExpressionEditor";
    oInput = $(mode + "_book_schedulingCronExpression");
    if(oInput != null) {
      oInput.parentNode.appendChild(oDiv);
      var currHTML = HTML.replace(/( (id|for)=["']?)/g, "$1" + mode + "_");
      oDiv.innerHTML = currHTML;
    }
  }
	oSchedEditor.parentNode.removeChild(oSchedEditor);
	
    
  actualFileName = booksFile;
  importXml('xmlEditOpenXml.do?file=' + booksFile);
}

function modify() {
  var oEl = $('node' + currentID);
  
  var sNodeType = getRadioValue(oModForm.elements['node_type']);
  var storedNodeType = oEl.getAttribute('node_type');
  
  // remove unused attributes
  var bIsContentTypeDeleteable = false, storedContentType;
  if(sNodeType != storedNodeType) {
    for(sFieldName in aFormDefinitions[storedNodeType + 'Node'].fields) {
      oEl.removeAttribute(sFieldName);
    }
    if(sNodeType == "shelf") {
      bIsContentTypeDeleteable = true;
      storedContentType = oEl.getAttribute('contentType');
    }
  }
  else {
    if(sNodeType == "book") {
      var sContentType = getSelectValue(oModForm.elements['mod_book_contentType']);
      storedContentType = oEl.getAttribute('contentType');
      if(sContentType != storedContentType) {
        bIsContentTypeDeleteable = true;
      }
    }
  }
  
  if(bIsContentTypeDeleteable == true) {
    for(sFieldName in aFormDefinitions[storedContentType].fields) {
      oEl.removeAttribute(sFieldName);
    }
  }

  oNode = aFormDefinitions[sNodeType + 'Node'];

  // set basic values
  var sTitle = oModForm.elements['mod_' + oNode.prefix + '_' + 'title'].value;
  var sName = oModForm.elements['name'].value;
	var spanTitle = (sTitle != '') ? sTitle : sName;
  oEl.setAttribute('name', sName);
  if(spanTitle != oEl.firstChild.firstChild.nodeValue) {
    oEl.firstChild.firstChild.nodeValue = spanTitle;
  }
  
  for(sFieldName in oNode.fields) {
    var oFieldData = oNode.fields[sFieldName];
    var sFieldNameInForm = oNode.prefix + '_' + sFieldName;
    if(oFieldData.type == 'text') {
      sFieldValue = oModForm.elements['mod_' + sFieldNameInForm].value;
    }
    else if(oFieldData.type == 'radio') {
      sFieldValue = getRadioValue(oModForm.elements[sFieldNameInForm]);
    }
    else if(oFieldData.type == 'select') {
      sFieldValue = getSelectValue(oModForm.elements['mod_' + sFieldNameInForm]);
    }
    oEl.setAttribute(sFieldName, sFieldValue);
  }

  if(sNodeType == 'book') {
    var sContentType = oEl.getAttribute('contentType');
    if(sContentType != "") {
      oContentType = aFormDefinitions[sContentType];
      for(sFieldName in oContentType.fields) {
        var oFieldData = oContentType.fields[sFieldName];
        var sFieldNameInForm = oContentType.prefix + '_' + sFieldName;
        if(oFieldData.type == 'text') {
          sFieldValue = oModForm.elements[sFieldNameInForm].value;
        }
        else if(oFieldData.type == 'radio') {
          sFieldValue = getRadioValue(oModForm.elements[sFieldNameInForm]);
        }
        else if(oFieldData.type == 'select') {
          sFieldValue = getSelectValue(oModForm.elements[sFieldNameInForm]);
        }
        oEl.setAttribute(sFieldName, sFieldValue);
      }
    }
  }

  // set special values
	if(sContentType) {
    for(sFieldName in aFormDefinitions[sContentType].fields) {
      var field = aFormDefinitions[sContentType].prefix + '_' + sFieldName;
      oEl.setAttribute(field, oModForm.elements[field].value);
    }
  }

  hideActionPanel();
  return false;
}

function addOnClickHandler(id) {
  var oDiv = $('span' + id);
  if(oDiv != null) {
    $E.addListener(oDiv, 'click', showActionPanel);
  }
}

function hideActionPanel() {
  $D.setStyle('actionPanel', 'display', 'none');
	$D.setStyle('span' + currentID, "font-weight", "normal");
}

function getName() {
  return oAddForm.elements["name"].value;
}

function getType() {
  var oSel = oAddForm.elements["type"];
  return oSel.options[oSel.selectedIndex].value;
}

function getTypeForm(oForm) {
  var oSel = oForm.elements["type"];
  return oSel.options[oSel.selectedIndex].value;
}

function setSpecialForm(oForm, sValue, prefix) {
  // var oSel = oForm.elements[prefix + "_book_contentType"];
  setSelectValue(oForm.elements[prefix + "_book_contentType"], sValue);
}

function checkValues() {
}

function createEntry() {
  var oLI = document.createElement("LI");
  oLI.setAttribute('id', 'node' + ID);
  oLI.setAttribute('name', getName());
  
  var sNodeType = getRadioValue(oAddForm.elements['node_type']);
  oLI.setAttribute('node_type', sNodeType);
  oNode = aFormDefinitions[sNodeType + 'Node'];
	var data = getData(oAddForm);
  
  for(sFieldName in oNode.fields) {
    var oFieldData = oNode.fields[sFieldName];
    var sFieldNameInForm = oNode.prefix + '_' + sFieldName;
		sFieldValue = data[sFieldNameInForm];
    oLI.setAttribute(sFieldName, sFieldValue);
  }

  if(sNodeType == 'book') {
    var sContentType = oLI.getAttribute('contentType');
    if(sContentType != "") {
      oContentType = aFormDefinitions[sContentType];
      for(sFieldName in oContentType.fields) {
        var oFieldData = oContentType.fields[sFieldName];
        var sFieldNameInForm = oContentType.prefix + '_' + sFieldName;
				sFieldValue = data[sFieldNameInForm];
        oLI.setAttribute(sFieldName, sFieldValue);
      }
    }
  }
  
  var oSP = document.createElement("SPAN");
  oSP.setAttribute('id', 'span' + ID);
	var sSpanValue = (oLI.getAttribute('title') 
	                  && oLI.getAttribute('title') != null 
										&& oLI.getAttribute('title') != '')
								 ? oLI.getAttribute('title')
								 : oLI.getAttribute('name')
  oSP.appendChild(document.createTextNode(sSpanValue));
  oLI.appendChild(oSP);
  return oLI;
}

function deleteElement(e, obj) {
  var oTarget = $('node' + currentID);
	if(oTarget.getAttribute('name') == "root" || oTarget.parent.id == "nXMLtree") {
		alert("Root node can't be deleted!")
		return false;
	}
  var bDeletable = true;
  if(oTarget.lastChild.tagName && oTarget.lastChild.tagName == "UL") {
    bDeletable = confirm("The document has subdocument. The deletion will remove all these childrem. Are you sure?");
  }
  if(bDeletable) {
    if(oTarget.parentNode.id != 'nXMLtree' 
		   && oTarget.previousSibling == null 
			 && oTarget.nextSibling == null) {
      // this is the only child, remove the parent UL
      oTarget.parentNode.parentNode.removeChild(oTarget.parentNode);
    }
    else {
      oTarget.parentNode.removeChild(oTarget);
    }
  }
}

function TreeWalker(oDiv) {
  var text = "";

  if(oDiv.id != "XMLtree")
  {
    var sNodeType = oDiv.getAttribute('node_type');
    text += '<' + sNodeType 
		      + ' id="' + oDiv.getAttribute('id').replace(/^node/, '') + '"'; //"'
    text += ' name="' + oDiv.getAttribute('name') + '"';

    for(sFieldName in aFormDefinitions[sNodeType + 'Node'].fields) {
      // var field = aFormDefinitions[sNodeType].prefix + '_' + sFieldName;
      var value = oDiv.getAttribute(sFieldName);
      if(value != null) {
        text += ' ' + sFieldName + '="' + value + '"';
      }
    }

    var sContentType = oDiv.getAttribute('contentType');
		if(sContentType) {
  		for(sFieldName in aFormDefinitions[sContentType].fields) {
        var field = aFormDefinitions[sContentType].prefix + '_' + sFieldName;
        var value = oDiv.getAttribute(field);
        if(value != null) {
          text += ' ' + sFieldName + '="' + value + '"';
        }
      }
    }
  }

  if(oDiv.lastChild.tagName && oDiv.lastChild.tagName == "UL") {
    var len = oDiv.lastChild.childNodes.length;
    if(len > 0 && oDiv.id != "XMLtree") {
      text += '>' + "\n";
    }

    for(var i=0; i<len; i+=1) {
      text += TreeWalker(oDiv.lastChild.childNodes[i]);
    }

    if(len > 0 && oDiv.id != "XMLtree") {
      text += '</' + sNodeType + '>';
    }
  }
  else {
    if(oDiv.id != "XMLtree") {
      text += '/>';
    }
  }
  text += "\n";
  return text;
}

function exportXML(e) {
  var oDiv = $("XMLtree");
	setExportVariables();
  var exportXmlPos = [$E.getPageX(e)+10, $E.getPageY(e)];
  oDiv = $("exportXml");
  $D.setStyle(oDiv, 'display', 'block');
  $D.setX(oDiv, exportXmlPos[0]);
  $D.setY(oDiv, exportXmlPos[1]);
  var oXForm = $("exportXmlForm");
  oXForm.elements['fileName'].focus();
}

function setExportVariables() {
  var oDiv = $("XMLtree");
  var sXml = '<?xml version="1.0" encoding="utf-8"?>' + "\n" 
					 + '<xml-body>' + "\n"
	         + TreeWalker(oDiv) + "\n"
					 + '</xml-body>' + "\n";
  var oXForm = $("exportXmlForm");
  oXForm.elements['fileName'].value = actualFileName;
  oXForm.elements['content'].value = sXml;
}

function saveAndApply(e) {
	setExportVariables();
  var oXForm = $("exportXmlForm");
  oConn.setUrl('xmlEditSaveAndApply.do');
  oConn.setParam('fileName', oXForm.elements['fileName'].value);
  oConn.setParam('content', oXForm.elements['content'].value);
  var a = oConn.transact(null);
  return false;
}

function onExportXML(e) {
  var oDiv = $("exportXml");
  var oXForm = $("exportXmlForm");
  oConn.setUrl('xmlEditSaveXml.do');
  oConn.setParam('fileName', oXForm.elements['fileName'].value);
  oConn.setParam('content', oXForm.elements['content'].value);
  $D.setStyle(oDiv, 'display', 'none');
  var a = oConn.transact(null);
  return false;
}

function getFileList(e) {
  fileListPos = [$E.getPageX(e)+10, $E.getPageY(e)];
  oConn.setUrl('xmlEditListFiles.do?isAjaxian=1');
  var a = oConn.transact(showFileList);
}

function showFileList(sResp) {
  XMLfiles = eval(sResp);
  var len = XMLfiles.length;
  var oDiv = $('fileList');
  oDiv.innerHTML = '';
  var oUl = document.createElement('UL');
  oDiv.appendChild(oUl);
  var oLi, oLink;
  for(var i=0; i<len; i+=1) {
    oLi = document.createElement("LI");
    oUl.appendChild(oLi);
    oLink = document.createElement("A");
    oLi.appendChild(oLink);
    oLink.id = 'xmlEditOpenXml.do?file=' + XMLfiles[i];
    oLink.appendChild(document.createTextNode(XMLfiles[i]));
    $E.addListener(oLink, 'click', onSelectXml);
  }
  $D.setStyle(oDiv, 'display', 'block');
  $D.setX(oDiv, fileListPos[0]);
  $D.setY(oDiv, fileListPos[1]);
}

function onSelectXml(e, obj) {
  $D.setStyle('fileList', 'display', 'none');
  actualFileName = this.id.substr(this.id.indexOf('=')+1);
	importXml(this.id);
}

function importXml(url) {
  oConn.setUrl(url);
  var a = oConn.transact(onOpenXml);
  return false;
}

function onOpenXml(sResp) {
  var oXmlDom = XmlDommify();
  if(oXmlDom != null){
    oXmlDom.loadXML(sResp);
  } else {
    var oParser = new DOMParser();
    oXmlDom = oParser.parseFromString(sResp, "text/xml");
  }
  var oTree = $('nXMLtree');
  while (oTree.childNodes[0]) {
    oTree.removeChild(oTree.childNodes[0]);
  }

  var oRoot = oXmlDom.documentElement;
  IDs = [];
  
  for(var i=0,len=oRoot.childNodes.length; i<len; i+=1) {
    if(oRoot.childNodes[i].nodeType == 1){
      oTree.appendChild(drawHtml(oRoot.childNodes[i]));
    }
  }

	var max = 0;
  for(var i=0,len=IDs.length; i<len; i+=1) {
		if(IDs[i] > max) {
			max = IDs[i]; 
		}
    addOnClickHandler(IDs[i]);
  }
	ID = max;
}

function drawHtml(oNode) {
  var oEl = document.createElement("LI");
  if(oNode.tagName == 'book') {
    oEl.setAttribute('node_type', 'book');
  }
  else if(oNode.tagName == 'shelf') {
    oEl.setAttribute('node_type', 'shelf');
  }

  var myID;
	var bHaveId = false;
	for(var i=0,len=oNode.attributes.length; i<len; i+=1) {
    if(oNode.attributes[i].name == 'id') {
			bHaveId = true;
			myID = oNode.attributes[i].value;
      oEl.setAttribute(oNode.attributes[i].name, 'node' + oNode.attributes[i].value);
    } else {
      oEl.setAttribute(oNode.attributes[i].name, oNode.attributes[i].value);
    }
  }
	if(bHaveId == false) {
		myID = ID++;
		oEl.setAttribute('id', 'node' + myID);
	}
  IDs[IDs.length] = myID;

  var oSpan = document.createElement("SPAN");
  oEl.appendChild(oSpan);
  oSpan.id = 'span' + myID;
	var sSpanValue = (oNode.getAttribute('title') 
	                  && oNode.getAttribute('title') != null 
										&& oNode.getAttribute('title') != '')
								 ? oNode.getAttribute('title')
								 : oNode.getAttribute('name')
  oSpan.appendChild(document.createTextNode(sSpanValue));

  if(oNode.hasChildNodes()) {
    var oUl = document.createElement("UL");
    oUl.id = 'child'+ myID;
    oEl.appendChild(oUl);

    for(var i=0,len=oNode.childNodes.length; i<len; i+=1) {
      if(oNode.childNodes[i].nodeType == 1){
        oUl.appendChild(drawHtml(oNode.childNodes[i]));
      }
    }
  }
  return oEl;
}

function XmlDommify() {
  var aVersions = [ "MSXML2.DOMDocument.5.0", "MSXML2.DOMDocument.4.0", "MSXML2.DOMDocument.3.0", "MSXML2.DOMDocument", "Microsoft.XmlDom" ];
  for (var i = 0; i < aVersions.length; i++) {
    try {
      var oXmlDom = new ActiveXObject(aVersions[i]);
      return oXmlDom;
    } catch (oError) {
      //Do nothing
    }
  }
  return null;
}