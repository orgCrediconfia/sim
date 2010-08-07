<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoIdentificador">
	<Portal:PaginaNombre titulo="Catálogo Tipo de Identificador" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoTipoIdentificador' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Id Tipo Identificador' control='Texto' controlnombre='IdTipoIdentificador' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='DescTipoIdentificador'  controllongitud='20' controllongitudmax='30' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimTipoIdentificadorReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Tipo Identificador'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_TIPO_IDENTIFICADOR"]}' funcion='SimCatalogoTipoIdentificador' operacion='CR' parametros='IdTipoIdentificador=${registro.campos["CVE_TIPO_IDENTIFICADOR"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_TIPO_IDENTIFICADOR"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
