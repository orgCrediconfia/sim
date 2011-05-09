<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteBuroCreditoG">

	<Portal:PaginaNombre titulo="Reporte de buro de credito individual y grupal" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteBuroCreditoG' url="ProcesaReporte?Funcion=SimReporteBuroCreditoG&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave del prestamo' control='Texto' controlnombre='CvePrestamoGrupo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
  
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Csv" onClick="javascript:fReporteCsv();">
        </Portal:FormaBotones>
		
	</Portal:Forma>       
     
     <script>
     	
         function fReporteXls(){
              url = '/portal/ProcesaReporte?Funcion=SimReporteBuroCreditoG&TipoReporte=Xls&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
         }
       
      
         function fReporteCsv(){
              url = '/portal/ProcesaReporte?Funcion=SimReporteBuroCreditoG&TipoReporte=Csv&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value;
              	MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }
     </script> 
     
</Portal:Pagina>     