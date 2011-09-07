<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaPagoIndividual">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Individual"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaPagoIndividual'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Importe' control='Texto' controlnombre='Importe' controlvalor='${param.Importe}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="IdMovimientoOperacion" value='<c:out value='${param.IdMovimientoOperacion}'/>' />
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.TxRespuesta}'/>' />
		<input type="hidden" name="TxPregunta" value='<c:out value='${param.TxPregunta}'/>' />
		<input type="hidden" name="PagoTotal" value='<c:out value='${param.PagoTotal}'/>' />
		<input type="hidden" name="Saldo" value='<c:out value='${param.Saldo}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Realizar el pago" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Estado de Cuenta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='200' valor='Fecha Operaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='moneda' ancho='200' valor='Importe'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Desglose/Saldo Total'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaEstadoCuenta}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["FECHA_OPERACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["IMPORTE"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMP_DESGLOSE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.SaldoFecha}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMP_DESGLOSE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<Portal:TablaLista tipo="consulta" nombre="Resumen">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='200' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='moneda' ancho='200' valor='Importe'/>
			<Portal:Columna tipovalor='moneda' ancho='200' valor='Pagado'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Saldo'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaEstadoCuentaResumen}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["IMPORTE"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["PAGADO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<c:forEach var="registro" items="${requestScope.SaldoTotal}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='TOTAL'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["IMPORTE_TOTAL"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["PAGO_TOTAL"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDO_TOTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
	
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoIndividualSaldo&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value;
			document.frmRegistro.submit();
		}
		
		
		if (document.frmRegistro.TxRespuesta.value != "0"){
			var answer = confirm('<%=request.getParameter("TxRespuesta")%>');
		
			if (answer){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoIndividual&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value+"&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value;
				document.frmRegistro.submit();	
			}
		}else if (document.frmRegistro.TxRespuesta.value == "0"){
			if (document.frmRegistro.TxPregunta.value != "0"){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoIndividual&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value+"&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value;
				document.frmRegistro.submit();	
			}
		}
		
		if (document.frmRegistro.IdMovimientoOperacion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaPagoIndividual&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdMovimientoOperacion='+document.frmRegistro.IdMovimientoOperacion.value+'&IdPrestamo='+document.frmRegistro.IdPrestamo.value+'&Importe='+document.frmRegistro.Importe.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	</script>		
		
</Portal:Pagina>
