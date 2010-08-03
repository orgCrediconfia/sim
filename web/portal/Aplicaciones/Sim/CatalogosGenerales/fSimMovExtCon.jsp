<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Movimientos Extraordinarios" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoMovimientoExtraordinario' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdMovimientoExtra' controllongitud='10' controllongitudmax='10'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomMovimientoExtra' controllongitud='35' controllongitudmax='35'/>
	</Portal:Forma>	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimCatalogoMovimientoExtraordinario&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Tipo de movimiento'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_MOVIMIENTO_EXTRA"]}' funcion='SimCatalogoMovimientoExtraordinario' operacion='CR'parametros='IdMovimientoExtra=${registro.campos["ID_MOVIMIENTO_EXTRA"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_MOVIMIENTO_EXTRA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_MOVIMIENTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
