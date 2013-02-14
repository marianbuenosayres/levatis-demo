<%
  String designerUrl = (String) request.getAttribute("designerUrl");
  String key = (String) request.getAttribute("key");
%>
<html>
  <body>
    <table>
    <tr><td>
      <iframe id="designerFrame" src="<%=designerUrl%>" width="1000" height="700"></iframe>
    </td></tr>
    <tr><td>
      <script type="text/javascript">
        function save() {
    	  if (confirm("ARE YOU SURE?")) {
      	    var designerFrame = document.getElementById("designerFrame");
    	    var bpmn2 = designerFrame.contentWindow.document.defaultView.ORYX.EDITOR.getSerializedJSON();
    	    var http = new XMLHttpRequest();
    	    var url = "save?key=<%=key%>";
    	    http.open("POST", url, true);

		    //Send the proper header information along with the request
    	    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    	    http.setRequestHeader("Content-length", bpmn2.length);
    	    http.setRequestHeader("Connection", "close");
            http.onreadystatechange = function() {//Call a function when the state changes.
    		  if(http.readyState == 4 && http.status == 200) {
    			  alert(http.responseText);
    			  document.location.reload();
    		  }
    	    }
            http.send(bpmn2);
      	  }
        }
      </script>
      <button onclick="save();">SAVE CHANGES</button>
    </td></tr>
    </table>
  </body>
</html>