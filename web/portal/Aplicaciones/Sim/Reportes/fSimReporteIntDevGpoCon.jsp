<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteInteresDevengadoGrupo">

	<Portal:PaginaNombre titulo="Intereses Devengados" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteInteresDevengadoGrupo' url="ProcesaReporte?Funcion=SimReporteInteresDevengadoGrupo&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
			   <Portal:FormaElemento etiqueta='Regional' control='selector' controlnombre='IdRegional' controlvalor='${param.IdRegional}' editarinicializado='true' obligatorio='false' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL" datosselector='${requestScope.ListaRegional}' evento="onchange=fRegional();"/>
			   <Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${param.IdSucursal}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}' evento="onchange=fSucursal();"/>
			   <Portal:FormaElemento etiqueta='Asesor' control='selector' controlnombre='CveUsuario' controlvalor='${param.CveUsuario}' editarinicializado='true' obligatorio='false' campoclave="ID_PERSONA" campodescripcion="NOM_COMPLETO" datosselector='${requestScope.ListaAsesor}'/>
			   <Portal:FormaElemento etiqueta='Clave del prestamo' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
	           <Portal:Calendario2 etiqueta='Fecha inicio' contenedor='frmRegistro' controlnombre='FechaInicio' esfechasis='false'/>  
			   <Portal:Calendario2 etiqueta='Fecha fin' contenedor='frmRegistro' controlnombre='FechaFin' esfechasis='false'/>  
	
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
	</Portal:Forma>
	
     <script>
     	function fRegional(){
            document.frmRegistro.action="ProcesaCatalogo?Funcion=SimReporteInteresDevengadoGrupo&OperacionCatalogo=IN&Filtro=Sucursal&IdRegional="+document.frmRegistro.IdRegional.value;
			document.frmRegistro.submit();
         }
     
     	function fSucursal(){
            document.frmRegistro.action="ProcesaCatalogo?Funcion=SimReporteInteresDevengadoGrupo&OperacionCatalogo=IN&Filtro=Asesor&IdRegional="+document.frmRegistro.IdRegional.value+"&IdSucursal="+document.frmRegistro.IdSucursal.value;
			document.frmRegistro.submit();
         }
     
         function fReporteXls(){
      		if (document.frmRegistro.FechaInicio.value == "" || document.frmRegistro.FechaFin.value == ""){
                  alert ("Ingrese las fecha de búsqueda");  
      		}else{
              url = '/portal/ProcesaReporte?Funcion=SimReporteInteresDevengadoGrupo&TipoReporte=Xls&IdRegional='+document.frmRegistro.IdRegional.value+'&IdSucursal='+document.frmRegistro.IdSucursal.value+'&CveUsuario='+document.frmRegistro.CveUsuario.value+'&CvePrestamo='+document.frmRegistro.CvePrestamo.value+'&FechaInicio='+document.frmRegistro.FechaInicio.value+'&FechaFin='+document.frmRegistro.FechaFin.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
           }
        }
      
        function fReportePdf(){
  		if (document.frmRegistro.FechaInicio.value == "" || document.frmRegistro.FechaFin.value == ""){
              alert ("Ingrese las fecha de búsqueda");  
  		}else{
          url = '/portal/ProcesaReporte?Funcion=SimReporteInteresDevengadoGrupo&TipoReporte=Pdf&Bd=MySql&FechaInicio='+document.frmRegistro.FechaInicio.value+'&FechaFin='+document.frmRegistro.FechaFin.value;
          MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }
  }
     </script>
     
</Portal:Pagina>     

