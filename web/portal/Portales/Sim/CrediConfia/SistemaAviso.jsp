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
</Portal:Pagina>