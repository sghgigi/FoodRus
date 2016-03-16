/**
 * 
 */
function checkqty(node)
{ 
	
	var ok = true;
	var id = "msg_" + node.id;

	document.getElementById(id).innerHTML = "";
	
	var p = node.value;
	if (isNaN(p)||p<0)
	{
		var msg = "quantity invalid!";
		document.getElementById(id).innerHTML = msg;
		ok = false;
	}
	
	return ok;
}

function getItemId(itemid)
{
//	alert(node.name + ":" + node.value + ";" + node.type + ":" + node.id);
	document.getElementById("addedItemID").value = itemid;
	
}


function setKeywordUrl()
{
	var keyword = document.getElementById("keyword").value;
	var x = document.getElementById("kwAnchor");
//	alert(x.href);
	var url = x.href + "?keyword=" + keyword;
//	var url = "https://www.google.com";
	window.location.href = url;
	return false;
//	alert(url);
//	document.getElementById("kwAnchor").setAttribute("href", url);
//	document.createAttribute("keyword").value = keyword;      
	
}

