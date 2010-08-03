<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortalDefault">
	<Portal:PaginaNombre titulo="Portales Default" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="alta" nombre="Consulta portales default" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fPortPorAlt.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='300' valor='Clave Portal Default'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clave Grupo Empresa'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave Portal '/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PORTAL_DEFAULT"]}' funcion='HerramientasConfiguracionPortalDefault' operacion='CR' parametros='CvePortalDefault=${registro.campos["CVE_PORTAL_DEFAULT"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}' clonarregistro='true' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CVE_GPO_EMPRESA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["CVE_PORTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>
</Portal:Pagina>