<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCorte">
	<Portal:PaginaNombre titulo="Corte de caja" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaCorte' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Corte de caja"/>
		<Portal:Calendario2 etiqueta='Fecha inicial' contenedor='frmRegistro' controlnombre='FechaInicial' controlvalor='${requestScope.registro.campos["FECHA_INICIAL"]}'  esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha final' contenedor='frmRegistro' controlnombre='FechaFinal' controlvalor='${requestScope.registro.campos["FECHA_FINAL"]}'  esfechasis='false'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimiento" value='<c:out value='${param.IdMovimiento}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
			<input type="button" name="Aceptar"  value="Imprimir" onclick='javascript:fImprimir()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="Corte de Caja" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='85' valor='Fecha'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Movimiento'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='300' valor='Cliente'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto'/>
		</Portal:TablaListaTitulos>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='85' valor='Saldo inicial'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaInicial.campos["SALDO_INICIAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
		<c:forEach var="registro" items="${requestScope.ListaMovimiento}">		
			<Portal:TablaListaRenglon>			
				<Portal:Columna tipovalor='texto' ancho='85' valor='${registro.campos["FECHA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_MOVIMIENTO_CAJA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='300' valor='${registro.campos["CLIENTE"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='85' valor='Saldo final'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaFinal.campos["SALDO_FINAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
	</Portal:TablaLista>
	
	<script>
	function fAceptar(){
		if (document.frmRegistro.FechaInicial.value == ""){
			alert ("Ingrese la fecha inicial");
		}else if (document.frmRegistro.FechaFinal.value == ""){
			alert ("Ingrese la fecha final");
		} else {
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCorte&OperacionCatalogo=CT&Filtro=Todos&IdCaja="+document.frmRegistro.IdCaja.value+"&FechaFinal="+document.frmRegistro.FechaFinal.value+"&FechaInicial="+document.frmRegistro.FechaInicial.value;
				document.frmRegistro.submit();
		}
	}
	
	function fImprimir(){
		if (document.frmRegistro.FechaInicial.value == ""){
			alert ("Ingrese la fecha inicial");
		}else if (document.frmRegistro.FechaFinal.value == ""){
			alert ("Ingrese la fecha final");
		} else {
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaCorte&TipoReporte=Txt&IdCaja='+document.frmRegistro.IdCaja.value+'&FechaFinal='+document.frmRegistro.FechaFinal.value+'&FechaInicial='+document.frmRegistro.FechaInicial.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	}
	</script>
	
</Portal:Pagina>