<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaConsultaPagarCredito">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<%
	String sSaldoCuenta = ((request.getParameter("SaldoCliente")!=null) ? request.getParameter("SaldoCliente"):"0");
	float fSaldoCuenta = (Float.parseFloat(sSaldoCuenta));
	%>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaConsultaPagarCredito'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${registro.campos["CVE_PRESTAMO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='IdGrupo' controlvalor='${registro.campos["NOMBRE"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='IdProducto' controlvalor='${registro.campos["ID_PRODUCTO"]} - ${registro.campos["NOM_PRODUCTO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${registro.campos["NUM_CICLO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Importe' control='Texto' controlnombre='Importe' controlvalor='${param.Importe}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		
		<c:if test='${(param.FechaMovimiento != "null")}'>
			<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${param.FechaMovimiento}'  esfechasis='true'/>
		</c:if>
		<c:if test='${(param.FechaMovimiento == "null")}'>
			<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor=''  esfechasis='true'/>
		</c:if>
		
		
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="IdMovimientoOperacion" value='<c:out value='${param.IdMovimientoOperacion}'/>' />
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.TxRespuesta}'/>' />
		<input type="hidden" name="TxPregunta" value='<c:out value='${param.TxPregunta}'/>' />
		<input type="hidden" name="Respuesta" value='<c:out value='${param.Respuesta}'/>' />
		<input type="hidden" name="PagoTotal" value='<c:out value='${param.PagoTotal}'/>' />
		<input type="hidden" name="Saldo" value='<c:out value='${param.Saldo}'/>' />
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="Excedente" value='<c:out value='${param.Excedente}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	
	<script>
		function fAceptar(){
			if (document.frmRegistro.Importe.value == ''){
				alert("Ingrese el importe a pagar");
			}else {
				<% if(fSaldoCuenta > 0){%>
					alert ("No se puede hacer pagos ya que existe saldo positivo a la cuenta del cliente");
				<%} else {%>
					document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupalSaldo&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&Importe="+document.frmRegistro.Importe.value+"&FechaMovimiento="+document.frmRegistro.FechaMovimiento.value;
					document.frmRegistro.submit();
				<%}%>
			} 
		}	
		
		function compare_dates(f1, f2){  
			var xMonth=f1.substring(3, 5);  
			var xDay=f1.substring(0, 2);  
			var xYear=f1.substring(6,10);
			var yMonth=f2.substring(3, 5);  
			var yDay=f2.substring(0, 2); 
			var yYear=f2.substring(6,10); 
			
			if (xYear> yYear){  
			  	return(true)  
			} else { 
				if (xYear == yYear){  
					if (xMonth> yMonth){  
			             return(true)  
			        } else { 
			        	if (xMonth == yMonth){  
			        		if (xDay> yDay){ 
					          	return(true);  
					        }else { 
					          return(false);
					        }  
			        	}else{
			        		return(false);
			        	}
			        }
				}else {
					return(false); 
				}
			}
			 
		 }  
		
		if (document.frmRegistro.IdMovimientoOperacion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaPagoGrupal&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdMovimientoOperacion='+document.frmRegistro.IdMovimientoOperacion.value+'&Importe='+document.frmRegistro.Importe.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
		if (document.frmRegistro.TxRespuesta.value != "0"){
			var answer = confirm('<%=request.getParameter("TxRespuesta")%>');
					
			if (answer){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupal&OperacionCatalogo=CR&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&Respuesta="+document.frmRegistro.Respuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value+"&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value+"&Excedente="+document.frmRegistro.Excedente.value+"&FechaMovimiento="+document.frmRegistro.FechaMovimiento.value;
				document.frmRegistro.submit();	
			}
		}else if (document.frmRegistro.TxRespuesta.value == "0"){
			if (document.frmRegistro.TxPregunta.value != "0"){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoGrupal&OperacionCatalogo=CR&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&TxPregunta="+document.frmRegistro.TxPregunta.value+"&TxRespuesta="+document.frmRegistro.TxRespuesta.value+"&Respuesta="+document.frmRegistro.Respuesta.value+"&PagoTotal="+document.frmRegistro.PagoTotal.value+"&Saldo="+document.frmRegistro.Saldo.value+"&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&Importe="+document.frmRegistro.Importe.value+"&Excedente="+document.frmRegistro.Excedente.value+"&FechaMovimiento="+document.frmRegistro.FechaMovimiento.value;
				document.frmRegistro.submit();	
			}
		}
		
		if (document.frmRegistro.Respuesta.value != "null"){
			alert('<%=request.getParameter("Respuesta")%>');
		}
		
		<% if(fSaldoCuenta > 0){%>
			alert ("No se puede hacer pagos ya que existe saldo positivo a la cuenta del cliente");
		<%}%>
		
	</script>
	
</Portal:Pagina>
