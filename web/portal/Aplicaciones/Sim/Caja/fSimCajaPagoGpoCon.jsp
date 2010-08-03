<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaPagoGrupal">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Grupal"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaPagoIndividual'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del grupo' control='Texto' controlnombre='IdGrupo' controlvalor='${param.IdGrupo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Clave del producto' control='Texto' controlnombre='IdProducto' controlvalor='${param.IdProducto}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${param.NumCiclo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Importe' control='Texto' controlnombre='Importe' controlvalor='${param.Importe}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdTransaccion" value='<c:out value='${param.IdTransaccion}'/>' />
		<input type="hidden" name="TotalImporte" value='<c:out value='${param.Importe}'/>' />
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.TxRespuesta}'/>' />
		<input type="hidden" name="TxPregunta" value='<c:out value='${param.TxPregunta}'/>' />
		<input type="hidden" name="PagoTotal" value='<c:out value='${param.PagoTotal}'/>' />
		<input type="hidden" name="Saldo" value='<c:out value='${param.Saldo}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>
		
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupalSaldo&OperacionCatalogo=AL&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value;
			document.frmRegistro.submit();
		}
		
		if (document.frmRegistro.IdTransaccion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaPagoGrupal&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+document.frmRegistro.IdTransaccion.value+'&Importe='+document.frmRegistro.TotalImporte.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
		
		if (document.frmRegistro.TxRespuesta.value != "0"){
			var answer = confirm('<%=request.getParameter("TxRespuesta")%>');
		
			if (answer){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupal&OperacionCatalogo=CR&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value;
				document.frmRegistro.submit();	
			}
		}else if (document.frmRegistro.TxRespuesta.value == "0"){
			if (document.frmRegistro.TxPregunta.value != "0"){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupal&OperacionCatalogo=CR&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value;
				document.frmRegistro.submit();	
			}
		}
		
	</script>		
		
</Portal:Pagina>
