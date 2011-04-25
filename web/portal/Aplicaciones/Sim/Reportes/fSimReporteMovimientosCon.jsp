<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteMovimientos">

	<Portal:PaginaNombre titulo="Reporte de movimientos" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteMovimientos' url="ProcesaReporte?Funcion=SimReporteMovimientos&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Id del Prestamo' control='Texto' controlnombre='IdPrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
  
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
        </Portal:FormaBotones>
		
	</Portal:Forma>       
     
     <script>
     	
         function fReporteXls(){
              url = '/portal/ProcesaReporte?Funcion=SimReporteMovimientos&TipoReporte=Xls&IdPrestamo='+document.frmRegistro.IdPrestamo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
         }

     </script> 
     
</Portal:Pagina>     