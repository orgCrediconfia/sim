<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoActividadRequisito">
	<Portal:PaginaNombre titulo="Actividades o requisito del préstamo" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='250' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Actividad o requisito'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de registro'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha de realizada'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' funcion='SimPrestamoActividadRequisito' operacion='CR' parametros='IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${param.IdProducto}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_REGISTRO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_REALIZADA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
