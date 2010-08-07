<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaReporteMovimiento">
	
	<Portal:PaginaNombre titulo="Reporte de Movimientos de caja" subtitulo=""/>
	<Portal:Forma tipo='url' funcion='SimCajaReporteMovimiento' url="ProcesaReporte?Funcion=SimCajaReporteMovimiento&TipoReporte=Pdf"agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
		<Portal:FormaElemento etiqueta='Regional' control='selector' controlnombre='IdRegional' controlvalor='${requestScope.registro.campos["ID_REGIONAL"]}' editarinicializado='true' obligatorio='false' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL" datosselector='${requestScope.ListaRegional}'/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		<Portal:FormaElemento etiqueta='Caja' control='selector' controlnombre='IdCaja' controlvalor='${requestScope.registro.campos["ID_CAJA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_CAJA" campodescripcion="NOM_CAJA" datosselector='${requestScope.ListaCaja}'/>
		<Portal:Calendario2 etiqueta='Desde' contenedor='frmRegistro' controlnombre='FechaIni' esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Hasta' contenedor='frmRegistro' controlnombre='FechaFin' esfechasis='false'/>
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Imprimir" onclick="javascript:fAceptar();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
		function fAceptar(){
			url = '/portal/ProcesaReporte?Funcion=SimCajaReporteMovimiento&TipoReporte=Pdf&IdRegional='+document.frmRegistro.IdRegional.value+'&IdSucursal='+document.frmRegistro.IdSucursal.value+'&IdCaja='+document.frmRegistro.IdCaja.value+'&FechaIni='+document.frmRegistro.FechaIni.value+'&FechaFin='+document.frmRegistro.FechaFin.value;
			MM_openBrWindow(url,'ReporteMovimientos','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}	
	</script>
	
</Portal:Pagina>