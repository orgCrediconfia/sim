<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina>
	<%
	String sNumeroPagina = ((request.getParameter("NumeroPagina")!=null) ? request.getParameter("NumeroPagina"):"1");
	String sNumeroRegistrosPagina = ((request.getParameter("NumeroRegistrosPagina")!=null) ? request.getParameter("NumeroRegistrosPagina"): "5");
	String sNumeroSetPaginas = ((request.getParameter("NumSet")!=null) ? request.getParameter("NumSet"): "0");
	int iNumeroRegistrosPagina = (int)Float.valueOf(sNumeroRegistrosPagina).floatValue();
	int iNumeroPagina = (int)Float.valueOf(sNumeroPagina).floatValue();
	int iRegistroInicial = (iNumeroPagina * iNumeroRegistrosPagina) - (iNumeroRegistrosPagina - 1);
	int iTotRegistros = (int)Float.valueOf(request.getParameter("TotRegistros")).floatValue();
	int showRegistros = 0;
	if ((iRegistroInicial + (iNumeroRegistrosPagina - 1)) >= iTotRegistros){
 		showRegistros = iTotRegistros;
	}
 	else{
 		showRegistros = iRegistroInicial + (iNumeroRegistrosPagina -1);
 	}
 	request.getParameter("registroPaginas");
	%>
	<Portal:PaginaNombre titulo="Bit&aacute;cora" subtitulo="Consulta de datos"/>
	<Portal:BitacoraPagina numeropagina="<%=sNumeroPagina%>" numeroregistrospagina="<%=sNumeroRegistrosPagina%>" numerosetpaginas="<%=sNumeroSetPaginas%>"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta bit&aacute;cora">
	<c:forEach var="registro" items="${requestScope.Lista}">
		<Portal:TablaListaRenglon>
			<Portal:Columna tipovalor='texto' ancho='10%' valor='${registro.campos["0"]}'/>
			<% for(int i = iRegistroInicial; i<=showRegistros; i++){
					String sRegistro = "${registro.campos[\""+ String.valueOf(i) +"\"]}";%>
					<Portal:Columna tipovalor='texto' ancho='18%' valor='<%=sRegistro%>'/>
			<%}%>
		</Portal:TablaListaRenglon>
	</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
 