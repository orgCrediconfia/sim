<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaRecuperacionCuentaIncobrable">
	<Portal:PaginaNombre titulo="Recuperación de Cuenta Incobrable" subtitulo=""/>
	<Portal:Forma tipo='busqueda' funcion='SimCajaRecuperacionCuentaIncobrable' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='20' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
		
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimientoOperacion" value='<c:out value='${param.IdMovimientoOperacion}'/>' />
	</Portal:Forma>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>	
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimCajaRecuperacionCuentaIncobrable' operacion='IN' parametros='Filtro=Alta&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdCliente=${registro.campos["CVE_NOMBRE"]}&IdCaja=${param.IdCaja}&IdGrupo=${registro.campos["ID_GRUPO"]}&IdProducto=${registro.campos["ID_PRODUCTO"]}&NumCiclo=${registro.campos["NUM_CICLO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>