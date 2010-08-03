<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoPapel">
	<Portal:PaginaNombre titulo="Cat&aacute;logo Tasa de Referencia" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoPapel' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPapel' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPapel' controllongitud='20' controllongitudmax='20'/>
	</Portal:Forma>	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimCatalogoPapel&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Periodicidad'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_TASA_REFERENCIA"]}' funcion='SimCatalogoPapel' operacion='CR'parametros='IdPapel=${registro.campos["ID_TASA_REFERENCIA"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TASA_REFERENCIA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PERIODICIDAD"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
