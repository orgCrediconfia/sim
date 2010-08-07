<%@ taglib uri="Portal" prefix="Portal" %>
<Portal:Pagina menu="false">

	<Portal:PaginaNombre titulo="Sistema Integral de Microfinanciamiento" subtitulo="Acceso al sistema"/>

	<table width='100%' border='0' cellspacing='0' cellpadding='0'>
		<tr> 
			<td width='70' nowrap align="right"></td>
			<td width='15' nowrap>&nbsp;</td>
	    	<td width='100%'>La informaci&oacute;n para accesar al sistema es incorrecta</td>
		</tr>
	</table>
	<br>
	<br>
	<table width='100%' border='0' cellspacing='0' cellpadding='0'>
		<tr> 
			<td width='70' nowrap align="right"></td>
			<td width='15' nowrap>&nbsp;</td>		
		    <td width='100%'><a href="<%=request.getContextPath()%>/Autentificacion">Intentar otra vez</a></td>
		</tr>
	</table>
</Portal:Pagina>