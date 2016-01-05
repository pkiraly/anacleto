function getData(oForm) {
	var data = {};

	if (oForm) {
		for (var i=0;i<oForm.elements.length;i++) {
			var formItem = oForm.elements[i];
			if (formItem) {
				if (formItem.tagName) { // Got a single form item
					switch (formItem.tagName) {
						case "INPUT":
							switch (formItem.type) {
								case "checkbox":
									data[formItem.name] = formItem.checked;
									break;
								case "textbox":
								case "text":
								case "hidden":
									data[formItem.name] = formItem.value;
									break;
							}
							break;
						case "TEXTAREA":
							data[formItem.name] = formItem.value;
							break;
						case "SELECT":
							var val = [];
							for (var x=0;x<formItem.options.length;x++)	{
								var option = formItem.options[x];
								if (option.selected) {
									var selval = option.value;
									if (! selval || selval === "") {
										selval = option.text;
									}
									val[val.length] = selval;
								}
							}
							data[formItem.name] = val;
							break;
					}
				} else if (formItem[0] && formItem[0].tagName) { // this is an array of form items
					if (formItem[0].tagName == "INPUT") {
						switch (formItem[0].type) {
							case "radio":
								for (var r=0; r<formItem.length; r++) {
									var radio = formItem[r];
									if (radio.checked) {
										data[radio.name] = radio.value;
										break;
									}
								}
								break;
							case "checkbox":
								var cbArray = [];
								for (var c=0; c<formItem.length; c++) {
									var check = formItem[c];
									if (check.checked) {
										cbArray[cbArray.length] = check.value;
									}
								}
								data[formItem[0].name] = cbArray;
								break;
						}
					}
				}
			}
		}
	}
	return data;
};
