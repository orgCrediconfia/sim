<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaSalidaCajaDesembolso">
	<Portal:PaginaNombre titulo="Salida de caja por desembolso" subtitulo=""/>
	<Portal:Forma tipo='busqueda' funcion='SimCajaSalidaCajaDesembolso' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamoGrupo' controlvalor='${param.CvePrestamoGrupo}' controllongitud='18' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre del Grupo' control='Texto' controlnombre='NomGrupo' controlvalor='${param.NomGrupo}' controllongitud='20' controllongitudmax='19'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdTransaccion" value='<c:out value='${param.IdTransaccion}'/>' />
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Salida de caja por desembolso" botontipo="url" url="/Aplicaciones/Sim/Caja/fSimCajaSalCajDesReg.jsp?OperacionCatalogo=AL&IdCaja=${param.IdCaja}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='45' valor='Ciclo'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Fecha de aplicación'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto pagado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>	
						<input type="button" name="Aceptar"  value="Reimpresión" onclick='javascript:fReimpresion(<c:out value='${registro.campos["ID_TRANSACCION"]}'/>)'>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO_GRUPO"]}' funcion='SimCajaSalidaCajaDesembolso' operacion='IN' parametros='Filtro=Alta&IdPrestamoGrupo=${registro.campos["ID_PRESTAMO_GRUPO"]}&IdCaja=${param.IdCaja}&IdTransaccionGrupo=${registro.campos["ID_TRANSACCION_GRUPO"]}&IdGrupo=${registro.campos["ID_GRUPO"]}&IdProducto=${registro.campos["ID_PRODUCTO"]}&NumCiclo=${registro.campos["NUM_CICLO"]}'/>
				</Portal:Columna>		
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='45' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["FECHA_TRANSACCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
	
		function fReimpresion(sIdTransaccion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaRetiro&TipoReporte=Pdf&Reimpresion=1&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+sIdTransaccion,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	
		if (document.frmRegistro.IdTransaccion.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaSalidaCajaDesembolso&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccion='+document.frmRegistro.IdTransaccion.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	</script>
</Portal:Pagina>