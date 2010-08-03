<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoVerificacionPrestamo">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de verificaci&oacute;n del pr&eacute;stamo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoVerificacionPrestamo' operacion='CT' filtro='Todos'>								   
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdVerificacionPrestamo' controllongitud='22' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomVerificacionPrestamo' controllongitud='22' controllongitudmax='20'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimVerPrestReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_VERIFICACION_PRESTAMO"]}' funcion='SimCatalogoVerificacionPrestamo' operacion='CR'parametros='IdVerificacionPrestamo=${registro.campos["ID_VERIFICACION_PRESTAMO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_VERIFICACION_PRESTAMO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
