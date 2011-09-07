<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaTranspasoBanco">
	<Portal:PaginaNombre titulo="Transpaso a banco" subtitulo=""/>
	<Portal:Forma tipo='busqueda' funcion='SimCajaTranspasoBanco' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}'>
		<Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' esfechasis='false'/>
		<Portal:FormaElemento etiqueta='Banco' control='selector' controlnombre='CveBanco' controlvalor='${requestScope.registro.campos["CVE_BANCO"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_BANCO" campodescripcion="NOM_BANCO" datosselector='${requestScope.ListaBanco}'/>
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controllongitud='22' controllongitudmax='22' />
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimientoOperacion" value='<c:out value='${param.IdMovimientoOperacion}'/>' />
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Transpaso a banco" botontipo="url" url="/ProcesaCatalogo?Funcion=SimCajaTranspasoBanco&OperacionCatalogo=IN&Filtro=Alta&IdCaja=${param.IdCaja}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor=''/>	
			<Portal:Columna tipovalor='texto' ancho='80' valor='Fecha'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Banco'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
						<input type="button" name="Aceptar"  value="Reimpresi�n" onclick='javascript:fReimpresion(<c:out value='${registro.campos["ID_MOVIMIENTO_OPERACION"]}'/>)'>
				</Portal:Columna>			
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["FECHA_TRANSACCION"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_BANCO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		
		function fReimpresion(sIdMovimientoOperacion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaTranspasoBanco&TipoReporte=Pdf&Reimpresion=1&IdCaja='+document.frmRegistro.IdCaja.value+'&IdMovimientoOperacion='+sIdMovimientoOperacion,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	
		if (document.frmRegistro.IdMovimientoOperacion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaTranspasoBanco&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdMovimientoOperacion='+document.frmRegistro.IdMovimientoOperacion.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	</script>
</Portal:Pagina>