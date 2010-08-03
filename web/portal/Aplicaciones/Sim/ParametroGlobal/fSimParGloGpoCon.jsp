<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimParametroGlobalGrupo">
	<Portal:PaginaNombre titulo="Par&aacute;metros Globales del Grupo" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="alta" nombre="Par&aacute;metros Globales del Grupo" botontipo="url" url="/Aplicaciones/Sim/ParametroGlobal/fSimParGloGpoReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='N&uacute;mero de integrantes'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='M&aacute;ximo de Tipo Ambulantes'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='M&aacute;ximo de Tipo Cat&aacute;logo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='M&aacute;ximo de Integrantes en Riesgo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>	
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NUM_INTEGRANTE"]}' funcion='SimParametroGlobalGrupo' operacion='CR' parametros='NumIntegrante=${registro.campos["NUM_INTEGRANTE"]}'/>
				</Portal:Columna>			
				<Portal:Columna tipovalor='cantidad' ancho='150' valor='${registro.campos["MAX_AMBULANTE"]}'/>
				<Portal:Columna tipovalor='cantidad' ancho='150' valor='${registro.campos["MAX_CATALOGO"]}'/>
				<Portal:Columna tipovalor='cantidad' ancho='100%' valor='${registro.campos["MAXIMO_RIESGO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
