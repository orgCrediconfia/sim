<%@ taglib uri="Portal" prefix="Portal" %>
<Portal:Pagina menu="false">
	<Portal:PaginaNombre titulo="Mensajes del sistema"/>	
	<table width='100%' border='0' cellspacing='0' cellpadding='0' dwcopytype="CopyTableRow">
		<tr> 
			<td width='70' nowrap align="right">&nbsp; </td>
			
	    <td width='15' nowrap>&nbsp;</td>
			<td width='100%'><font color="#FF0000"><%=request.getParameter("Mensaje")%></font></td>
		</tr>		
	</table>
	
	<br>
	<br>
<%if(request.getParameter("Ventana")!=null){
	    if(!request.getParameter("Ventana").equals("null")){%>
			<center><input type="button" value="Cerrar" onclick="window.close();"></center>
 <%}
 		else{	%>
			<center><input type="button" value="Regresar" onclick="javascript:history.go(-1);"></center>	
 <%}
    }else{
		if(request.getAttribute("UrlOriginal")!=null){%>
			<center><input type="button" value="   Ok   " onclick="location.href ='<%=(String)request.getAttribute("UrlOriginal")%>'"></center>	
		<%}else{%>
			<center><input type="button" value="Regresar" onclick="javascript:history.go(-1);"></center>	
		<%}%>
	<%}%>
	<br>
	<br>	 
</Portal:Pagina>