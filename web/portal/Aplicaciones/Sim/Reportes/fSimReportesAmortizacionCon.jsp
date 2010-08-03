<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReportesAmortizacion">

	<Portal:PaginaNombre titulo="Reporte de tabla de amortización" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReportesAmortizacion' url="ProcesaReporte?Funcion=SimReportesAmortizacion&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave del grupo' control='Texto' controlnombre='CvePrestamoGrupo' controllongitud='40' controllongitudmax='20' editarinicializado='true' obligatorio='true' />
              
 
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	
     <script>
         function fReporteXls(){
      
      		if (document.frmRegistro.CvePrestamoGrupo.value == "" ){
                  alert ("La clave de préstamo del grupo es obligatoria para generar el reporte");
                  
      		}else{
      		
      
              url = '/portal/ProcesaReporte?Funcion=SimReportesAmortizacion&TipoReporte=Xls&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
           }
        }
      
       function fReportePdf(){
  
  		if (document.frmRegistro.CvePrestamoGrupo.value == "" ){
              alert ("La clave de préstamo del grupo es obligatoria para generar el reporte");
              
  		}else{
  		
  
          url = '/portal/ProcesaReporte?Funcion=SimReportesAmortizacion&TipoReporte=Pdf&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
          MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }
  }
     </script>
     
</Portal:Pagina>     

