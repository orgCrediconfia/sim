<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimTablaAmortizacionGrupo">

	<Portal:PaginaNombre titulo="Reporte de tabla amortización por grupo" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimTablaAmortizacionGrupo' url="ProcesaReporte?Funcion=SimTablaAmortizacionGrupo&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave del préstamo' control='Texto' controlnombre='CvePrestamoGrupo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
  
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en PDF" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
		
	</Portal:Forma>  
     
     <script>
      function fReporteXls(){
      		  
      		  if (document.frmRegistro.CvePrestamoGrupo.value == "" ){
      		    alert ("La clave de préstamo del grupo es obligatoria para generar el reporte");
      		  }else{
              
              url = '/portal/ProcesaReporte?Funcion=SimTablaAmortizacionGrupo&TipoReporte=Xls&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
             
             }
      }
     </script>   
     
     <script>
      function fReportePdf(){
      		  
      		  if (document.frmRegistro.CvePrestamoGrupo.value == "" ){
      		    alert ("La clave de préstamo del grupo es obligatoria para generar el reporte");
      		  }else{
              
              url = '/portal/ProcesaReporte?Funcion=SimTablaAmortizacionGrupo&TipoReporte=Pdf&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
             
             }
      }
     </script>          
     
</Portal:Pagina>     