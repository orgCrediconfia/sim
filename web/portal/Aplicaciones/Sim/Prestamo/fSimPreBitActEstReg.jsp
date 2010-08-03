<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoConsultaBitacoraActividadEstatus">
	<Portal:PaginaNombre titulo="Bitácora de etapa" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="consulta" nombre="Bitácora" botontipo="url" url='/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='150' valor='Actividad o Requisito'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Etapa del préstamo'/>	
			<Portal:Columna tipovalor='texto' ancho='150' valor='Fecha de registro'/>	
			<Portal:Columna tipovalor='texto' ancho='150' valor='Fecha de realización'/>	
			<Portal:Columna tipovalor='texto' ancho='80' valor='Estatus'/>	
			<!--Portal:Columna tipovalor='texto' ancho='200' valor='Comentario'/-->
			<Portal:Columna tipovalor='texto' ancho='200' valor='Usuario'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Orden de la etapa'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaHistorial}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["FECHA_REGISTRO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["FECHA_REALIZADA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ESTATUS"]}'/>
				<!--Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["COMENTARIO"]}'/-->
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_USUARIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["ORDEN_ETAPA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
</Portal:Pagina>
