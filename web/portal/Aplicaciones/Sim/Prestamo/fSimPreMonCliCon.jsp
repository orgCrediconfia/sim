<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoMontoCliente">
	<Portal:PaginaNombre titulo="Montos por cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoMontoCliente" operacion="AL" parametros='IdPrestamo=${param.IdPrestamo}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Monto solicitado'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Monto autorizado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_CLIENTE"]}' funcion='SimPrestamoActividadRequisito' operacion='CR' parametros='IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${param.IdProducto}'/>
				</Portal:Columna>
				<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_CLIENTE"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>		
					<input type='text' name='MontoSolicitado' size='22' maxlength='22' value='<c:out value='${registro.campos["MONTO_SOLICITADO"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["MONTO_AUTORIZADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Aceptar' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>

</Portal:Pagina>	
