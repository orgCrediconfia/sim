<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoNegocio">
	
	<Portal:PaginaNombre titulo="Cat&aacute;logo de tipo de negocio" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoTipoNegocio' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdTipoNegocio' controllongitud='22' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTipoNegocio' controllongitud='22' controllongitudmax='20'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimTipNegReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_TIPO_NEGOCIO"]}' funcion='SimCatalogoTipoNegocio' operacion='CR'parametros='IdTipoNegocio=${registro.campos["ID_TIPO_NEGOCIO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
