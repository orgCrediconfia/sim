<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaDesembolsoIndividual">
	<Portal:PaginaNombre titulo="Entrega de préstamo o Desembolso" subtitulo="Individual"/>

	<Portal:Forma tipo='busqueda' funcion='SimCajaDesembolsoIndividual' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.IdPrestamo}' controllongitud='19' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='80' controllongitudmax='100' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdTransaccion" value='<c:out value='${param.IdTransaccion}'/>' />
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Id Prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='20' valor='Ciclo'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto prestado'/>
		</Portal:TablaListaTitulos>
		
			<c:forEach var="registro" items="${requestScope.ListaDesembolsoIndividual}">		
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
						<input type="button" name="Aceptar"  value="Desembolso" onclick='javascript:fDesembolso(<c:out value='${registro.campos["ID_PRESTAMO"]}'/>)'>
					</Portal:Columna>		
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PRESTAMO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["NUM_CICLO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_PRESTADO"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<c:forEach var="registro" items="${requestScope.ListaDesembolsoIndividualTransaccion}">		
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
						<input type="button" name="Aceptar"  value="Reimpresión" onclick='javascript:fReimpresion(<c:out value='${registro.campos["ID_TRANSACCION"]}'/>)'>
					</Portal:Columna>		
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PRESTAMO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["NUM_CICLO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_PRESTADO"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
	
	</Portal:TablaLista>
	
		
	<script>
		
		function fDesembolso(sIdPrestamo){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaDesembolsoIndividual&OperacionCatalogo=AL&IdPrestamo="+sIdPrestamo+"&IdCaja="+document.frmRegistro.IdCaja.value;
			document.frmRegistro.submit();
		}
		
		function fReimpresion(sIdTransaccion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaEntregaPrestamo&TipoReporte=Pdf&Reimpresion=1&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+sIdTransaccion,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
		if (document.frmRegistro.IdTransaccion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaEntregaPrestamo&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+document.frmRegistro.IdTransaccion.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
			
	</script>
	
</Portal:Pagina>