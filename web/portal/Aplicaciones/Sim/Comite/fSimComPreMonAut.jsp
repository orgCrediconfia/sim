<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimComitePrestamoMontoAutorizado">
	<Portal:PaginaNombre titulo="Montos Autorizados y Fecha de Desembolso" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimComitePrestamoMontoAutorizado' parametros='IdPrestamo=${param.IdPrestamo}&IdComite=${param.IdComite}&Prestamo=${param.Prestamo}'>
		<table border='0' cellspacing='2' cellpadding='3'>
		<Portal:Calendario2 etiqueta='Fecha de desembolso' contenedor='frmRegistro' controlnombre='FechaDesembolso' controlvalor='${requestScope.registro.campos["FECHA_ENTREGA"]}'  esfechasis='true'/>
		<c:if test='${(registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "7") or (registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "8")}'>
			<Portal:FormaElemento etiqueta='Día de pago' control='selector' controlnombre='DiaSemanaPago' controlvalor='${registro.campos["DIA_SEMANA_PAGO"]}' editarinicializado='true' obligatorio='true' campoclave="DIA_SEMANA_PAGO" campodescripcion="NOM_DIA_SEMANA_PAGO" datosselector='${requestScope.ListaDiaSemanaPago}'/>
		</c:if>	
		<c:if test='${(registro.campos["ID_PERIODICIDAD_PRODUCTO"] != "7") && (registro.campos["ID_PERIODICIDAD_PRODUCTO"] != "8")}'>
			<input type='hidden' name='DiaSemanaPago' value='ninguno'>
		</c:if>
		<input type='hidden' name='IdPrestamo' value='<c:out value='${param.IdPrestamo}'/>' />
		<input type='hidden' name='IdComite' value='<c:out value='${param.IdComite}'/>' />
		<input type='hidden' name='Prestamo' value='<c:out value='${param.Prestamo}'/>' />
	</Portal:Forma>	
	
	
	<Portal:TablaForma nombre="Consulta" funcion="SimComitePrestamoMontoAutorizado" operacion="MO" parametros='IdPrestamo=${param.IdPrestamo}&IdComite=${param.IdComite}&Prestamo=${param.Prestamo}'>
	
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='250' valor='Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Monto solicitado'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Monto autorizado'/>
			<input type='hidden' name='Fecha' value=''>
			<input type='hidden' name='Dia' value=''>
			<input type='hidden' name='MontoMinimo' value='<c:out value='${registro.campos["MONTO_MINIMO"]}'/>'>
			<input type='hidden' name='MontoMaximo' value='<c:out value='${registro.campos["MONTO_MAXIMO"]}'/>'>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaClienteMonto}">		
			<Portal:TablaListaRenglon>
				<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_CLIENTE"]}'/>'>
				<input type='hidden' name='IdPrestamoIndividual' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='$ ${registro.campos["MONTO_SOLICITADO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>		
					$ <input type='text' name='MontoAutorizado' size='22' maxlength='22' value='<c:out value='${registro.campos["MONTO_AUTORIZADO"]}'/>'>
				</Portal:Columna>
				
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<c:if test='${(requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "F")}'>  
				<input type='button' name='Aceptar' value='Aceptar' onClick='fAceptar()'/>
			</c:if>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<script>

	function fAceptar(){
			document.frmTablaForma.action="ProcesaCatalogo?Funcion=SimComitePrestamoMontoAutorizado&OperacionCatalogo=MO&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdComite="+document.frmRegistro.IdComite.value+"&Prestamo="+document.frmRegistro.Prestamo.value+"&FechaDesembolso="+document.frmRegistro.FechaDesembolso.value+"&DiaSemanaPago="+document.frmRegistro.DiaSemanaPago.value;
			document.frmTablaForma.submit();	
		}
	
	</script>
	
</Portal:Pagina>	
