<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReportePresidentes">

	<Portal:PaginaNombre titulo="Reporte de presidentes" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReportePresidentes' url="ProcesaReporte?Funcion=SimReportePresidentes&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Id del prestamo' control='Texto' controlnombre='IdPrestamoGrupo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
  
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
        </Portal:FormaBotones>
		
	</Portal:Forma>       
     
     <script>
     	
         function fReporteXls(){
              url = '/portal/ProcesaReporte?Funcion=SimReportePresidentes&TipoReporte=Xls&IdPrestamoGrupo='+document.frmRegistro.IdPrestamoGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
         }
       
     </script> 
     
</Portal:Pagina>     