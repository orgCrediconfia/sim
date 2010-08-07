<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoRegional">
	<Portal:PaginaNombre titulo="Catálogo Regional" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoRegional' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Id Regional' control='Texto' controlnombre='IdRegional' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomRegional'  controllongitud='20' controllongitudmax='30' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimRegionalReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Regional'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_REGIONAL"]}' funcion='SimCatalogoRegional' operacion='CR' parametros='IdRegional=${registro.campos["ID_REGIONAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_REGIONAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
