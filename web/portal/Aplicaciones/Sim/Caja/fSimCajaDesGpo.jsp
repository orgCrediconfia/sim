<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaDesembolsoGrupal">
	<Portal:PaginaNombre titulo="Entrega de préstamo o Desembolso" subtitulo="Grupal"/>

	<Portal:Forma tipo='busqueda' funcion='SimCajaDesembolsoGrupal' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18'/>
		<Portal:FormaElemento etiqueta='Nombre del grupo' control='Texto' controlnombre='NomGrupo' controlvalor='${param.NomGrupo}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdTransaccionGrupo" value='<c:out value='${param.IdTransaccionGrupo}'/>' />
	</Portal:Forma>
	
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del grupo'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Producto'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='20' valor='Ciclo'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto prestado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaDesembolsoGrupal}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<a id="IdPrestamo" href="javascript:fConsulta('<c:out value='${registro.campos["ID_PRESTAMO_GRUPO"]}'/>');"><c:out value='${registro.campos["CVE_PRESTAMO_GRUPO"]}'/></a>	
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_PRESTADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.ListaDesembolsoGrupalTransaccion}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>	
					<input type="button" name="Aceptar"  value="Reimpresión" onclick='javascript:fReimpresion(<c:out value='${registro.campos["ID_TRANSACCION_GRUPO"]}'/>)'>
				</Portal:Columna>		
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='20' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_PRESTADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		
	</Portal:TablaLista>
		
	<script>
	
		function fConsulta(sIdPrestamoGrupo){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaDesembolsoGrupal&OperacionCatalogo=CT&Filtro=Individual&IdCaja="+document.frmRegistro.IdCaja.value+"&IdPrestamoGrupo="+sIdPrestamoGrupo;
			document.frmRegistro.submit();
		}
		
		function fReimpresion(sIdTransaccionGrupo){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaDesembolsoGrupal&TipoReporte=Pdf&Reimpresion=1&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccionGrupo='+sIdTransaccionGrupo,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
		if (document.frmRegistro.IdTransaccionGrupo.value != "null"){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaDesembolsoGrupal&TipoReporte=Pdf&Reimpresion=0&IdCaja='+document.frmRegistro.IdCaja.value+'&IdTransaccionGrupo='+document.frmRegistro.IdTransaccionGrupo.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	
	</script>
	
</Portal:Pagina>