<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteSeguimiento">

	<Portal:PaginaNombre titulo="Reporte de seguimiento de cobranza" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteSeguimiento' url="ProcesaReporte?Funcion=SimReporteSeguimiento&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
              <Portal:SelectorCadena etiqueta1='Regional' etiqueta2='Sucursal' etiqueta3='Asesor' control='selector' cveacto='comun' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL"/>
              <Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' esfechasis='false'/>  
	
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	
     <script>
	         function fReporteXls(){
	         
		        if (document.frmRegistro.Fecha.value == ""){
		     		alert("Ingrese un fecha");
		     	}else{
	            url = '/portal/ProcesaReporte?Funcion=SimReporteSeguimiento&TipoReporte=Xls&IdRegional='+document.frmRegistro.IdRegional.value+'&IdSucursal='+document.frmRegistro.IdSucursal.value+'&CveUsuario='+document.frmRegistro.CveUsuario.value+'&Fecha='+document.frmRegistro.Fecha.value;
	          	MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
	         	}
	         }
	       
	      
	         function fReportePdf(){
	         
	         	if (document.frmRegistro.Fecha.value == ""){
		     		alert("Ingrese un fecha");
		     	}else{
	              url = '/portal/ProcesaReporte?Funcion=SimReporteSeguimiento&TipoReporte=Pdf&IdRegional='+document.frmRegistro.IdRegional.value+'&IdSucursal='+document.frmRegistro.IdSucursal.value+'&CveUsuario='+document.frmRegistro.CveUsuario.value+'&Fecha='+document.frmRegistro.Fecha.value;
	              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
	         	}
	         }
        
     </script>
     
</Portal:Pagina>     

