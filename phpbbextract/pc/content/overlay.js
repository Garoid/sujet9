

$('a',content.document);



var pc = {
	ready: function(){
			alert($(document));
	} ,

	onLoad: function() {
		alert('salut');
	

	/*
	var ni = content.document.getElementsByTagName("postbody");
	var newdiv = content.document.createElement('div');
	alert('ni' +ni);
	alert('newdiv'+newdiv)
	
	newdiv.innerHTML = "<p>salut les cocos</p>";
	ni.appendChild(newdiv);
	alert('tamere');*/
	}
};
//window.addEventListener("dblclick", function(e) { pc.onLoad(e); }, false);
window.addEventListener("dblclick", function(e) { pc.ready(e); }, false);
//pageshow