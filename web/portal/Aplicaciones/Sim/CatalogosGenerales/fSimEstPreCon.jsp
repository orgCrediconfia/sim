<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoEstatusPrestamo">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Etapas del pr&eacute;stamo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoEstatusPrestamo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdEstatusPrestamo' controllongitud='5' controllongitudmax='5' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomEstatusPrestamo' controllongitud='20' controllongitudmax='20'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimEstPreReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ETAPA_PRESTAMO"]}' funcion='SimCatalogoEstatusPrestamo' operacion='CR'parametros='IdEstatusPrestamo=${registro.campos["ID_ETAPA_PRESTAMO"]}&CveFuncion=${registro.campos["CVE_FUNCION"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
