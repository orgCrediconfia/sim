<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimPrestamoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Movimiento Extraordinario" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoMovimientoExtraordinario' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='20' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimPrestamoMovimientoExtraordinario' operacion='CR'parametros='Consulta=Registro&IdPrestamo=${registro.campos["ID_PRESTAMO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>