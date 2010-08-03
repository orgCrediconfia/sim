<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoGarantia">
	<Portal:PaginaNombre titulo="Garant&iacute;as" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/Prestamo/fSimPreGarReg.jsp?IdPrestamo=${param.IdPrestamo}&IdProducto=${param.IdProducto}&OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Garantía'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Porcentaje que cubre la garantía'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_GARANTIA"]}' funcion='SimPrestamoGarantia' operacion='CR' parametros='IdGarantia=${registro.campos["ID_GARANTIA"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${param.IdProducto}' />
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_TIPO_GARANTIA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["PORC_CUBRE_GARANTIA"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
