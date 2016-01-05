function addElementBefore(e, obj) {
  if(checkValues() == false) {
    return false;
  }
  ID++;
  var oLI = createEntry();
  var oTarget = $('node' + currentID);
  oTarget.parentNode.insertBefore(oLI, oTarget);
  addOnClickHandler(ID);
  hideActionPanel();
}

function addElementAfter(e, obj) {
  if(checkValues() == false) {
    return false;
  }
  ID++;
  var oLI = createEntry();
  var oTarget = $('node' + currentID);
  var oTarget = oTarget.parentNode.replaceChild(oLI, oTarget);
  oLI.parentNode.insertBefore(oTarget, oLI);
  addOnClickHandler(ID);
  hideActionPanel();
}

function addLastSubelement(e, obj) {
  if(checkValues() == false) {
    return false;
  }
  ID++;
  var oUL;
  var oTarget = $('node' + currentID);
  if(!oTarget.lastChild.tagName || oTarget.lastChild.tagName != "UL") {
    oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oTarget.appendChild(oUL);
  }
  var oLI = createEntry();
  oUL = oTarget.lastChild;
  oUL.appendChild(oLI);
  addOnClickHandler(ID);
  hideActionPanel();
}

function addFirstSubelement(e, obj) {
  if(checkValues() == false) {
    return false;
  }
  ID++;
  var oTarget = $('node' + currentID);
  if(!oTarget.lastChild.tagName || oTarget.lastChild.tagName != "UL") {
    var oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oTarget.appendChild(oUL);
  }
  var oLI = createEntry();
  var oUL = oTarget.lastChild;
  if(oUL.childNodes.length == 0){
    oUL.appendChild(oLI);
  }
  else {
    oUL.insertBefore(oLI, oUL.childNodes[0]);
  }
  addOnClickHandler(ID);
  hideActionPanel();
}

function moveElementFirst(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget != oTarget.parentNode.firstChild) {
    oTarget.parentNode.insertBefore(oTarget, oTarget.parentNode.firstChild);
  }
}

function moveElementUp(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget != oTarget.parentNode.firstChild) {
    oTarget.parentNode.insertBefore(oTarget, oTarget.previousSibling);
  }
}

function moveElementDown(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget != oTarget.parentNode.lastChild) {
    oTarget.parentNode.insertBefore(oTarget.nextSibling, oTarget);
  }
}

function moveElementLast(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget != oTarget.parentNode.lastChild) {
    oTarget.parentNode.appendChild(oTarget);
  }
}

function moveElementParentFirst(e, obj) {
  var oTarget = $('node' + currentID);
  var oOuterUL = oTarget.parentNode.parentNode.parentNode; //LI->UL->LI->UL
  if(oOuterUL.tagName == "UL") {
    oOuterUL.insertBefore(oTarget, oOuterUL.firstChild);
  }
}

function moveElementParentUp(e, obj) {
  var oTarget = $('node' + currentID);
  var oOuterUL = oTarget.parentNode.parentNode.parentNode; //LI->UL->LI->UL
  if(oOuterUL.tagName == "UL") {
    oOuterUL.insertBefore(oTarget, oTarget.parentNode.parentNode);
  }
}

function moveElementParentDown(e, obj) {
  var oTarget = $('node' + currentID);
  var oOuterUL = oTarget.parentNode.parentNode.parentNode; //LI->UL->LI->UL
  var oNextSibling = oTarget.parentNode.parentNode.nextSibling;
  if(oOuterUL.tagName == "UL") {
    oOuterUL.insertBefore(oTarget, oNextSibling);
  }
}

function moveElementParentLast(e, obj) {
  var oTarget = $('node' + currentID);
  var oOuterUL = oTarget.parentNode.parentNode.parentNode; //LI->UL->LI->UL
  if(oOuterUL.tagName == "UL") {
    oOuterUL.appendChild(oTarget);
  }
}

function moveElementUpChildFirst(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget.previousSibling == null) {
    return;
  }
  var oParent = oTarget.previousSibling;
  if(!oParent.lastChild.tagName || oParent.lastChild.tagName != "UL") {
    var oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oParent.appendChild(oUL);
  }
  if(oParent.lastChild.hasChildNodes()) {
    oParent.lastChild.insertBefore(oTarget, oParent.lastChild.firstChild);
  }
  else {
    oParent.lastChild.appendChild(oTarget);
  }
}

function moveElementUpChildLast(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget.previousSibling == null) {
    return;
  }
  var oParent = oTarget.previousSibling;
  if(!oParent.lastChild.tagName || oParent.lastChild.tagName != "UL") {
    var oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oParent.appendChild(oUL);
  }
  oParent.lastChild.appendChild(oTarget);
}

function moveElementDownChildFirst(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget.nextSibling == null) {
    return;
  }
  var oParent = oTarget.nextSibling;
  if(!oParent.lastChild.tagName || oParent.lastChild.tagName != "UL") {
    var oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oParent.appendChild(oUL);
  }
  if(oParent.lastChild.hasChildNodes()) {
    oParent.lastChild.insertBefore(oTarget, oParent.lastChild.firstChild);
  }
  else {
    oParent.lastChild.appendChild(oTarget);
  }
}

function moveElementDownChildLast(e, obj) {
  var oTarget = $('node' + currentID);
  if(oTarget.nextSibling == null) {
    return;
  }
  var oParent = oTarget.nextSibling;
  if(!oParent.lastChild.tagName || oParent.lastChild.tagName != "UL") {
    var oUL = document.createElement("UL");
    oUL.setAttribute('id', 'children' + currentID);
    oParent.appendChild(oUL);
  }
  oParent.lastChild.appendChild(oTarget);
}
