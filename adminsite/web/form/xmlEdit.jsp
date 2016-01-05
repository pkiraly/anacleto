<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title><bean:message key="header.xmledit" /></title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css" />
  <link rel="stylesheet" type="text/css" href="css/editor.css" />
  <script type="text/javascript" src="js/lib/yui/yahoo/yahoo.js"></script>
  <script type="text/javascript" src="js/lib/yui/event/event.js"></script>
  <script type="text/javascript" src="js/lib/yui/dom/dom.js"></script>
  <script type="text/javascript" src="js/lib/yui/animation/animation.js"></script>
  <script type="text/javascript" src="js/lib/yui/connection/connection.js"></script>
  <script type="text/javascript" src="js/fConnection.js"></script>
  <script type="text/javascript" src="js/lib/scheduler/scheduler.js"></script>
  <script type="text/javascript" src="js/lib/scheduler/util.js"></script>
  <script type="text/javascript" src="js/lib/scheduler/component.js"></script>
  <script type="text/javascript" src="js/editor.js"></script>
  <script type="text/javascript" src="js/configuration.js"></script>
  <script type="text/javascript" src="js/move_items_fn.js"></script>
  <script type="text/javascript" src="js/util.js"></script>
  <script type="text/javascript" src="js/form.js"></script>
  <script type="text/javascript" src="js/getData.js"></script>
  <script type="text/javascript" src="js/arrayUtil.js"></script>
<script type="text/javascript">
var booksFile = "<bean:write name="xmlEditForm" property="booksFile" />";
</script>
</head>

<body onload="init();">

<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="header.xmledit" /></td>
  </tr>
</table>

<div id="admin">
  <ul>
    <li id="importXMLlink"><bean:message key="xmledit.importXml" /></li>
    <li id="exportXMLlink"><bean:message key="xmledit.exportXml" /></li>
    <li id="saveAndApplylink"><bean:message key="xmledit.saveAndApply" /></li>
  </ul>
</div>

<div id="XMLtree"><ul id="nXMLtree"><li id="node1" name="first" node_type="book"
  contentType="FileSystemContent" path="c:\doku"
  titlepage="c:\doku" scheduled="false"><span id="span1">first</span></li></ul></div>

<div id="actionPanel">

  <!-- menu -->
  <div id="panelCommand">
    <span id="cmd_panel_add"  onclick="showPanel('panel_add');" class="selected cmd_panel"><bean:message key="xmledit.new" /></span>
    <span id="cmd_panel_move" onclick="showPanel('panel_move');" class="cmd_panel"><bean:message key="xmledit.move" /></span>
    <span id="cmd_panel_modify" onclick="showPanel('panel_modify');" class="cmd_panel"><bean:message key="xmledit.modify" /></span>
    <span onclick="deleteElement(); hideActionPanel();" class="cmd_panel"><bean:message key="xmledit.delete" /></span>
    <span onclick="hideActionPanel();" class="cmd_panel"><bean:message key="xmledit.close" /></span>
  </div>

  <!-- add form -->
  <div id="panel_add">
    <h1><bean:message key="xmledit.createNew" /></h1>
    <form id="add_form" action="editor.html">
      <div id="add_basic_container"></div>
      <div id="add_shelf_container"></div>
      <div id="add_book_container"></div>
      <div id="add_special_container"></div>
    </form>

    <fieldset>
      <legend><bean:message key="xmledit.createNew" /></legend>
      <ul type="square">
        <li onclick="addElementBefore();"><bean:message key="xmledit.addElementBefore" /></li>
        <li onclick="addElementAfter();"><bean:message key="xmledit.addElementAfter" /></li>
        <li onclick="addLastSubelement();"><bean:message key="xmledit.addLastSubelement" /></li>
        <li onclick="addFirstSubelement();"><bean:message key="xmledit.addFirstSubelement" /></li>
      </ul>
    </fieldset>
  </div>

  <!-- 'move' form -->
  <div id="panel_move">
    <h1>move the element</h1>
    <fieldset>
      <legend>actions</legend>
      <ul type="square">
        <li onclick="moveElementFirst();"><bean:message key="xmledit.moveElementFirst" /></li>
        <li onclick="moveElementUp();"><bean:message key="xmledit.moveElementUp" /></li>
        <li onclick="moveElementDown();"><bean:message key="xmledit.moveElementDown" /></li>
        <li onclick="moveElementLast();"><bean:message key="xmledit.moveElementLast" /></li>
      </ul>
    </fieldset>

    <fieldset>
      <legend><bean:message key="xmledit.moveParent.title" /></legend>
      <ul type="square">
        <li onclick="moveElementParentFirst();"><bean:message key="xmledit.moveParent.First" /></li>
        <li onclick="moveElementParentUp();"><bean:message key="xmledit.moveParent.Up" /></li>
        <li onclick="moveElementParentDown();"><bean:message key="xmledit.moveParent.Down" /></li>
        <li onclick="moveElementParentLast();"><bean:message key="xmledit.moveParent.Last" /></li>
      </ul>
    </fieldset>

    <fieldset>
      <legend><bean:message key="xmledit.moveDown.title" /></legend>
      <ul type="square">
        <li onclick="moveElementUpChildFirst();"><bean:message key="xmledit.moveDown.UpChildFirst" /></li>
        <li onclick="moveElementUpChildLast();"><bean:message key="xmledit.moveDown.UpChildLast" /></li>
        <li onclick="moveElementDownChildFirst();"><bean:message key="xmledit.moveDown.DownChildFirst" /></li>
        <li onclick="moveElementDownChildLast();"><bean:message key="xmledit.moveDown.DownChildLast" /></li>
      </ul>
    </fieldset>
  </div>

  <!-- 'modify' form -->
  <div id="panel_modify">
    <h1>modify element</h1>
    <form id="modify_form" action="editor.html" onsubmit="modify(); return false;">
      <div id="mod_basic_container"></div>
      <div id="mod_shelf_container"></div>
      <div id="mod_book_container"></div>
      <div id="mod_special_container"></div>
      <div style="text-align:right;"><input type="button" value="modify" onclick="return modify();" /></div>
    </form>
  </div>
</div>

<div id="basicValues">
  <fieldset>
    <legend>Basic values</legend>
    <label for="name">name:</label>
    <input type="text" name="name" id="name" />
    <br />

    <label for="name">type:</label>

    <input type="radio" name="node_type" id="ntShelf" value="shelf"
           class="radio" onclick="onNodeTypeChange(this.form)" />
    <label for="ntShelf" class="radio">shelf</label>

    <input type="radio" name="node_type" id="ntBook" value="book"
           class="radio" onclick="onNodeTypeChange(this.form)" />
    <label for="ntBook" class="radio">book</label>
  </fieldset>
</div>

<div id="schedulerEditor">
  <div class="schedTypeSelector">
    <input type="radio" id="schedTypeSimple" name="schedType" class="radio"  
      onclick="s.show('type', 'SEsimple')" value="simple" checked="checked" />
    <label for="schedTypeSimple" class="radio">
      simple setup
    </label>
    <input type="radio" id="schedTypeAdvanced" name="schedType" class="radio" 
      onclick="s.show('type', 'SEadvanced')" value="advanced" />
    <label for="schedTypeAdvanced" class="radio">
      advanced setup
    </label>
  </div>

  <div id="SEsimple">
    <fieldset>
      <legend>set up predefinited values</legend>
      <select id="predefinited" name="predefinited">
        <option value="0 * * * *">Hourly</option>
        <option value="0 0 * * *">Daily (at midnight)</option>
        <option value="0 0 * * 0">Weekly (on Sunday)</option>
        <option value="0 0 1 * *">Monthly (on the 1st)</option>
        <option value="0 0 1 1 *">Yearly (on 1st Jan)</option>
      </select>

      <input type="button" onclick="s.setPredefinited()" value="OK" />
    </fieldset>
  </div>

  <div id="SEadvanced" style="display: none;">
    <fieldset>
      <legend>set up components</legend>

      <span id="minutesSelector" onclick="s.show('component', 'minutes')" style="margin: 0; padding: 0; color: #000;">min</span> |
      <span id="hoursSelector" onclick="s.show('component', 'hours')" style="margin: 0; padding: 0; color: #666;">hour</span> |
      <span id="daysSelector" onclick="s.show('component', 'days')" style="margin: 0; padding: 0; color: #666;">day</span> |
      <span id="monthsSelector" onclick="s.show('component', 'months')" style="margin: 0; padding: 0; color: #666;">month</span> |
      <span id="wdaysSelector" onclick="s.show('component', 'wdays')" style="margin: 0; padding: 0; color: #666;">week day</span>

      <div id="minutes" class="container" style="display: block;">
        <p>set up minute component</p>
        <div id="minutesContainer" class="componentContainer"></div>
        <label for="minutesPeriodMode" title="The task will run on every nth minutes">
          <input id="minutesPeriodMode" type="checkbox" onclick="s.togglePeriodMode('minutes')" />
          periodic mode
        </label>
        <input id="clearMinutesBtn" type="button" onclick="s.clear('m')" value="clear" />
      </div>

      <div id="hours" class="container" style="display: none;">
        <p>set up hour component</p>
        <div id="hoursContainer" class="componentContainer"></div>
        <label for="hoursPeriodMode" title="The task will run on every nth hour">
          <input id="hoursPeriodMode" type="checkbox" onclick="s.togglePeriodMode('hours')" />
          periodic mode
        </label>
        <input id="clearHoursBtn" type="button" onclick="s.clear('h')" value="clear" />
      </div>

      <div id="days" class="container" style="display: none;">
        <p>set up day of month component</p>
        <div id="daysContainer" class="componentContainer"></div>
        <label for="daysPeriodMode" title="The task will run on every nth day">
          <input id="daysPeriodMode" type="checkbox" onclick="s.togglePeriodMode('days')" />
          periodic mode
        </label>
        <input id="clearDaysBtn" type="button" onclick="s.clear('d')" value="clear" />
      </div>

      <div id="months" class="container" style="display: none;">
        <p>set up month component</p>
        <div id="monthsContainer" class="componentContainer"></div>
        <input id="clearMonthsBtn" type="button" onclick="s.clear('n')" value="clear" />
      </div>

      <div id="wdays" class="container" style="display: none;">
        <p>set up day of week component</p>
        <div id="wdaysContainer" class="componentContainer"></div>
        <input id="clearWdaysBtn" type="button" onclick="s.clear('w')" value="clear" />
      </div>
    </fieldset>
  </div>
</div>

<div id="fileList"></div>

<div id="exportXml">
 <form id="exportXmlForm" action="editor.html" onsubmit="return onExportXML();">
  <fieldset>
   <legend>export</legend>
   <label for="fileName" style="display: inline; float: none">file name:</label>
   <input id="fileName" type="text" name="fileName" />
   <input type="hidden" name="content" />
   <input type="submit" value="OK"/>
  </fieldset>
 </form>
</div>

</body>
</html>
