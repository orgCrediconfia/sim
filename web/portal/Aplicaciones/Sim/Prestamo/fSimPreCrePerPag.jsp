<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCreditosPersonales">
	<Portal:PaginaNombre titulo="Créditos del cliente" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Créditos del cliente" funcion="SimPrestamoCreditosPersonales" operacion="CT" parametros="&Filtro=Todos">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Producto'/>	
			<Portal:Columna tipovalor='texto' ancho='50' valor='Ciclo'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de entrega'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de real'/>	
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Estatus del pr&eacute;stamo'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del Asesor'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clave de la Sucursal'/>
			
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamoCreditosPersonalesPaginacion}">		
			<Portal:TablaListaRenglon>
				<c:if test='${(registro.campos["ID_ETAPA_PRESTAMO"] == "7")}'>
					<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
						<input type="button" name="Liquidacion"  value="Liquidación por defunción" onclick='javascript:fLiquidacionDefuncion(<c:out value='${registro.campos["ID_PRESTAMO"]}'/>)'>
					</Portal:Columna>
				</c:if>
				<c:if test='${(registro.campos["ID_ETAPA_PRESTAMO"] != "7")}'>
					<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
					</Portal:Columna>
				</c:if>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ENTREGA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_REAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOMBRE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_ASESOR_CREDITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["ID_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<c:if test='${(param.Paginas != 0)}'>
				<input type="button" name="Siguiente" value="Siguiente" onclick="javascript:MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamoCreditosPersonalesPaginacion&OperacionCatalogo=CT&Filtro=Todos&Paginas=<c:out value='${param.Paginas}'/>&Superior=<c:out value='${param.Superior}'/>&CveNombre=<c:out value='${param.CveNombre}'/>&Nombre=<c:out value='${param.Nombre}'/>');" >
			</c:if>	
			<input type="button" name="Regresar" value="Regresar" onClick="MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamoCreditosPersonales&OperacionCatalogo=IN&Filtro=Inicio')" >
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<script>
	
		function fLiquidacionDefuncion(sIdPrestamo){
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimPrestamoLiquidacionDefuncion&OperacionCatalogo=IN&Filtro=Alta&Ventana=Si&GoBackPl=No&IdPrestamo='+sIdPrestamo,'VentanaLp','scrollbars=yes,resizable=yes,width=500,height=300');
		}
		
	</script>
		
</Portal:Pagina>
