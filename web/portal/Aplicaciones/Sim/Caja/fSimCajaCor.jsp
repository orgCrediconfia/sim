<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCorte">
	<Portal:PaginaNombre titulo="Reporte de Movimientos de Caja" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaCorte' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Movimientos de Caja"/>
		<Portal:FormaElemento etiqueta='Regional' control='selector' controlnombre='IdRegional' controlvalor='${param.IdRegional}' editarinicializado='true' obligatorio='false' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL" datosselector='${requestScope.ListaRegional}' evento="onchange=fRegional();"/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${param.IdSucursal}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}' />
		<Portal:Calendario2 etiqueta='Fecha inicial' contenedor='frmRegistro' controlnombre='FechaInicial' controlvalor='${param.FechaInicial}'  esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha final' contenedor='frmRegistro' controlnombre='FechaFinal' controlvalor='${param.FechaFinal}'  esfechasis='false'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimiento" value='<c:out value='${param.IdMovimiento}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Consultar"  value="Consultar" onclick='javascript:fConsultar()'>
			<input type="button" name="Imprimir"  value="Imprimir" onclick='javascript:fImprimir()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="Corte de Caja" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave Región'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre Región'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave Sucursal'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre Sucursal'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Caja'/>
			<Portal:Columna tipovalor='texto' ancho='85' valor='Fecha'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Movimiento'/>
			<Portal:Columna tipovalor='texto' ancho='20' valor='Clave del Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre del Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='20' valor='Clave del Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre del Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='10' valor='Ciclo'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='No. Factura'/>
			<Portal:Columna tipovalor='moneda' ancho='100' valor='Monto'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto Acumulado'/>
		</Portal:TablaListaTitulos>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='85' valor='${param.FechaInicial}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Saldo inicial'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='20' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='10' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ 0.00'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaInicial.campos["SALDO_INICIAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
		<c:forEach var="registro" items="${requestScope.ListaMovimiento}">		
			<Portal:TablaListaRenglon>		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_REGIONAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_REGIONAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_CAJA"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='85' valor='${registro.campos["FECHA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_MOVIMIENTO_CAJA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["ID_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["ID_CLIENTE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='10' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_MOVIMIENTO_OPERACION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_ACUMULADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='85' valor='${param.FechaFinal}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Saldo final'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='20' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='10' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ 0.00'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaFinal.campos["SALDO_FINAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
	</Portal:TablaLista>
	
	<script>
		function fRegional(){
		 	if (document.frmRegistro.IdRegional.value == "null"){
		 		alert("Seleccione una Regional para mostrar sus Sucursales");
		 	}else {
		        document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCorte&OperacionCatalogo=IN&Filtro=Sucursal&IdRegional="+document.frmRegistro.IdRegional.value;
				document.frmRegistro.submit();
			}
	    }
		
		function fConsultar(){
			if (document.frmRegistro.FechaInicial.value == ""){
				alert ("Ingrese la fecha inicial");
			}else if (document.frmRegistro.FechaFinal.value == ""){
				alert ("Ingrese la fecha final");
			} else {
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCorte&OperacionCatalogo=CT&Filtro=Todos&IdRegional="+document.frmRegistro.IdRegional.value+"&IdSucursal="+document.frmRegistro.IdSucursal.value+"&FechaFinal="+document.frmRegistro.FechaFinal.value+"&FechaInicial="+document.frmRegistro.FechaInicial.value;
				document.frmRegistro.submit();
			}
		}
		
		function fImprimir(){
			if (document.frmRegistro.FechaInicial.value == ""){
				alert ("Ingrese la fecha inicial");
			}else if (document.frmRegistro.FechaFinal.value == ""){
				alert ("Ingrese la fecha final");
			} else {
				MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaReporteCorteCaja&TipoReporte=Xls&IdRegional='+document.frmRegistro.IdRegional.value+'&IdSucursal='+document.frmRegistro.IdSucursal.value+'&FechaFinal='+document.frmRegistro.FechaFinal.value+'&FechaInicial='+document.frmRegistro.FechaInicial.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
		}
	</script>
	
</Portal:Pagina>