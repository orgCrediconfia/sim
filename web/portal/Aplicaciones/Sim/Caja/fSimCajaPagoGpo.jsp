<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaPagoGrupal">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Grupal"/>
	
	<Portal:TablaForma nombre="Pago grupal" funcion='SimCajaPagoGrupal' operacion='AL' parametros='TxRespuesta=${param.TxRespuesta}&TxPregunta=${param.TxPregunta}&PagoTotal=${param.PagoTotal}&Saldo=${param.Saldo}&IdCaja=${param.IdCaja}&FechaMovimiento=${param.FechaMovimiento}&IdProducto=${param.IdProducto}&IdGrupo=${param.IdGrupo}&NumCiclo=${param.NumCiclo}&Importe=${param.Importe}&IdPrestamoGrupo=${param.IdPrestamoGrupo}&Rango=${param.Rango}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Importe'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaImportes}">
			<Portal:TablaListaRenglon>
				<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_CLIENTE"]}'/>'>
				<input type='hidden' name='IdPrestamoIndividual' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>		
					$ <input type='text' name='MontoAutorizado' size='22' maxlength='22' value='<c:out value='${registro.campos["IMP_DEUDA"]}'/>'>
				</Portal:Columna>
				
			</Portal:TablaListaRenglon>
		</c:forEach>
		
		
		<Portal:FormaBotones>
			<!--Portal:Boton tipo='submit' etiqueta='Realizar el pago' /-->
			<input type="button" name="PagoGrupal" value="Realizar el pago" onclick='javascript:pagoGrupal();'>

		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
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
		<c:forEach var="registro" items="${requestScope.ListaEstadoCuentaResumenGrupo}">		
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
		function pagoGrupal(){
			document.frmTablaForma.PagoGrupal.disabled = true; 
			document.frmTablaForma.PagoGrupal.value = "Pago realizado"; 
			document.frmTablaForma.submit();
		}
	</script>
		
</Portal:Pagina>
