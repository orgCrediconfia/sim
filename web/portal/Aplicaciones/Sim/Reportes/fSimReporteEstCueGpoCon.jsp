<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteEstadoCuentaGrupo">

	<Portal:PaginaNombre titulo="Reporte de estado de cuenta grupal" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteEstadoCuentaGpo' url="ProcesaReporte?Funcion=SimReporteEstadoCuentaGrupo&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
	          <Portal:FormaElemento etiqueta='Clave del préstamo' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
	</Portal:Forma>
	
     <script>
         function fReporteXls(){
      		if (document.frmRegistro.CvePrestamo.value == "" ){
                  alert ("La clave del grupo es obligatoria para generar el reporte");  
      		}else{
              url = '/portal/ProcesaReporte?Funcion=SimReporteEstadoCuentaGrupo&TipoReporte=Xls&CvePrestamo='+document.frmRegistro.CvePrestamo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
           }
        }
      
       function fReportePdf(){
  		if (document.frmRegistro.CvePrestamo.value == "" ){
              alert ("La clave del grupo es obligatoria para generar el reporte");
              
  		}else{
          url = '/portal/ProcesaReporte?Funcion=SimReporteEstadoCuentaGrupo&TipoReporte=Pdf&CvePrestamo='+document.frmRegistro.CvePrestamo.value;
          MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }
  }
     </script>
     
</Portal:Pagina>     

