<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaRecepcionSucursal">
	<Portal:PaginaNombre titulo="Recepción de otra sucursal" subtitulo=""/>
	<Portal:Forma tipo='busqueda' funcion='SimCajaRecepcionSucursal' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}'>
		<Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' esfechasis='false'/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controllongitud='22' controllongitudmax='22' />
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdTransaccion" value='<c:out value='${param.IdTransaccion}'/>' />
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Recepción de otra sucursal" botontipo="url" url="/ProcesaCatalogo?Funcion=SimCajaRecepcionSucursal&OperacionCatalogo=IN&Filtro=Alta&IdCaja=${param.IdCaja}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Fecha'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Sucursal'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
						<input type="button" name="Aceptar"  value="Reimpresión" onclick='javascript:fReimpresion(<c:out value='${registro.campos["ID_TRANSACCION"]}'/>)'>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["FECHA"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
	
		function fReimpresion(sIdTransaccion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaRecepcionSucursal&TipoReporte=Pdf&Reimpresion=1&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+sIdTransaccion,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	
		if (document.frmRegistro.IdTransaccion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaRecepcionSucursal&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+document.frmRegistro.IdTransaccion.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	</script>
</Portal:Pagina>