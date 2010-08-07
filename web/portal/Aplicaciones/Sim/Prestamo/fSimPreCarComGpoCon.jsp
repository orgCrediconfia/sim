<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCargoComisionGrupo">
	<Portal:PaginaNombre titulo="Cargos y comisiones" subtitulo="Consulta de datos"/>
		
	<Portal:TablaLista tipo="consulta" nombre="Consulta de cargos y comisiones de los integrantes un grupo">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Cliente'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_CLIENTE"]}' funcion='SimPrestamoCargoComisionCliente' operacion='CT' parametros='Filtro=Todos&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdCliente=${registro.campos["ID_CLIENTE"]}'/>																																																		
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
		
		<script>
		
	</script>
	
</Portal:Pagina>	
