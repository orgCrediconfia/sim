<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoPeriodicidad">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Periodicidad" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoPeriodicidad' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPeriodicidad' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPeriodicidad' controllongitud='20' controllongitudmax='20'/>
	</Portal:Forma>	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimPeriodReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cantidad de pagos'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='N&uacute;mero de dias que comprende'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERIODICIDAD"]}' funcion='SimCatalogoPeriodicidad' operacion='CR'parametros='IdPeriodicidad=${registro.campos["ID_PERIODICIDAD"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_PERIODICIDAD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CANTIDAD_PAGOS"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DIAS"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
