<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteSinRenovar">

	<Portal:PaginaNombre titulo="Reporte de créditos terminados sin renovar" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteSinRenovar' url="ProcesaReporte?Funcion=SimReporteSinRenovar&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamoGpo' controllongitud='40' controllongitudmax='20' editarinicializado='true' />
              
 
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
                      <input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	
     <script>
         function fReporteXls(){
      
              url = '/portal/ProcesaReporte?Funcion=SimReporteSinRenovar&TipoReporte=Xls&CvePrestamoGpo=';
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
           }
      
       function fReportePdf(){
  
          url = '/portal/ProcesaReporte?Funcion=SimReporteSinRenovar&TipoReporte=Pdf&CvePrestamoGpo=';
          MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
        }

     </script>
     
</Portal:Pagina>     

