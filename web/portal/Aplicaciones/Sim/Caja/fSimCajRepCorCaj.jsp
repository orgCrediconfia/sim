<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaReporteCorteCaja">
	<Portal:PaginaNombre titulo="Reporte de Corte de caja" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaReporteCorteCaja' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Corte de caja"/>
		<Portal:FormaElemento etiqueta='Regional' control='selector' controlnombre='IdRegional' controlvalor='${param.IdRegional}' editarinicializado='true' obligatorio='false' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL" datosselector='${requestScope.ListaRegional}' evento="onchange=fRegional();"/>
		<Portal:FormaElemento etiqueta='Caja' control='selector' controlnombre='IdCaja' controlvalor='${requestScope.registro.campos["ID_CAJA"]}' editarinicializado='true' obligatorio='true' campoclave="ID_CAJA" campodescripcion="NOM_CAJA" datosselector='${requestScope.ListaSucursalCaja}'/>
		<Portal:Calendario2 etiqueta='Fecha inicial' contenedor='frmRegistro' controlnombre='FechaInicial' controlvalor='${requestScope.registro.campos["FECHA_INICIAL"]}'  esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha final' contenedor='frmRegistro' controlnombre='FechaFinal' controlvalor='${requestScope.registro.campos["FECHA_FINAL"]}'  esfechasis='false'/>
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Imprimir" onclick='javascript:fImprimir()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
	
	 function fRegional(){
            document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaReporteCorteCaja&OperacionCatalogo=IN&Filtro=Sucursal&IdRegional="+document.frmRegistro.IdRegional.value;
			document.frmRegistro.submit();
         }
	
	function fImprimir(){
		if (document.frmRegistro.IdCaja.value == ""){
			alert ("Seleccione la Sucursal-Caja");
		}else if (document.frmRegistro.FechaInicial.value == ""){
			alert ("Ingrese la fecha inicial");
		}else if (document.frmRegistro.FechaFinal.value == ""){
			alert ("Ingrese la fecha final");
		} else {
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaReporteCorteCaja&TipoReporte=Txt&IdCaja='+document.frmRegistro.IdCaja.value+'&FechaFinal='+document.frmRegistro.FechaFinal.value+'&FechaInicial='+document.frmRegistro.FechaInicial.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	}
	</script>
	
</Portal:Pagina>