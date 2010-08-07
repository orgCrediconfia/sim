<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoFondeador">
	<Portal:PaginaNombre titulo="Catálogo de Fondeador" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoFondeador' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveFondeador' controllongitud='20' controllongitudmax='20' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomFondeador'  controllongitud='75' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta de Fondeador" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimFonReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_FONDEADOR"]}' funcion='SimCatalogoFondeador' operacion='CR' parametros='CveFondeador=${registro.campos["CVE_FONDEADOR"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_FONDEADOR"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
