<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaMovimientoCuenta">
	<Portal:PaginaNombre titulo='<%=request.getParameter("NomMovimientoCaja")%>' subtitulo=""/>
		
	<Portal:Forma tipo='busqueda' funcion='SimCajaMovimientoCuenta' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}&NomMovimientoCaja=${param.NomMovimientoCaja}&CveMovimientoCaja=${param.CveOperacion}'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='20' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Grupo / Cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
		
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimientoOperacion" value='<c:out value='${param.IdMovimientoOperacion}'/>' />
	</Portal:Forma>
	<Portal:TablaLista tipo="consulta" nombre="<%=request.getParameter("NomMovimientoCaja")%>">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='300' valor='Grupo / Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Ciclo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimCajaMovimientoCuenta' operacion='IN' parametros='Filtro=Alta&IdCaja=${param.IdCaja}&CveMovimientoCaja=${param.CveMovimientoCaja}&NomMovimientoCaja=${param.NomMovimientoCaja}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&AplicaA=${registro.campos["APLICA_A"]}&CveNombre=${registro.campos["CVE_NOMBRE"]}&Nombre=${registro.campos["NOMBRE"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='300' valor='${registro.campos["NOMBRE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NUM_CICLO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>