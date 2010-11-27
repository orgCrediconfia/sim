<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimEstadoCuentaResumenGrupo">

	<Portal:PaginaNombre titulo="Reporte de estado de cuenta" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimEstadoCuentaResumenGrupo' url="ProcesaReporte?Funcion=SimEstadoCuentaResumenGrupo&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave del prestamo grupal' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
 
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	

      <script>
         function fReporteXls(){
      
      		if (document.frmRegistro.CvePrestamo.value == "" ){
                  alert ("La clave del prestamo es obligatorio para generar el reporte");
                  
      		}else{
      		
      
              url = '/portal/ProcesaReporte?Funcion=SimEstadoCuentaResumenGrupo&TipoReporte=Xls&CvePrestamo='+document.frmRegistro.CvePrestamo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
           }
        }
      
       function fReportePdf(){
  
  		if (document.frmRegistro.CvePrestamo.value == "" ){
              alert ("El id del prestamo es obligatorio para generar el reporte");
              
  		}else{
  		
  
          url = '/portal/ProcesaReporte?Funcion=SimEstadoCuentaResumenGrupo&TipoReporte=Pdf&CvePrestamo='+document.frmRegistro.CvePrestamo.value;
          MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }
  }
     </script>
     
</Portal:Pagina>     

   