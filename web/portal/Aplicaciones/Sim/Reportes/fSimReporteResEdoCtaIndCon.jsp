<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimReporteResEdoCtaInd">

	<Portal:PaginaNombre titulo="Reporte del resumen del estado de cuenta individual" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimReporteResEdoCtaInd' url="ProcesaReporte?Funcion=SimReporteResEdoCtaInd&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamo' controllongitud='40' controllongitudmax='20' editarinicializado='true' obligatorio='true' />
              <Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controllongitud='40' controllongitudmax='100' editarinicializado='true'/>
 
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	
	

	 <script>
      function fReporteXls(){
      
      		if (document.frmRegistro.CvePrestamo.value == "" && document.frmRegistro.NomCompleto.value=="" ){
                  alert ("La clave de préstamo o Nombre del cliente es obligatorio para generar el reporte");
                  
      		}else{
      		
      
              url = '/portal/ProcesaReporte?Funcion=SimReporteResEdoCtaInd&TipoReporte=Xls&CvePrestamo='+document.frmRegistro.CvePrestamo.value+'&NomCompleto='+document.frmRegistro.NomCompleto.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
            }
      }
     </script>
     
</Portal:Pagina>     

