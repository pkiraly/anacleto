      function selectAll(boxname)
      {
      	var el = document.forms[0].elements;
    
        for (i = 0; i < el.length; i++) {
          if (el[i].name == boxname){
            el[i].checked = true;
          }
        }
      }
      
      function deSelectAll(boxname)
      {
        var el = document.forms[0].elements;
    
        for (i = 0; i < el.length; i++) {
          if (el[i].name == boxname){
            el[i].checked = false;
          }
        }
      }