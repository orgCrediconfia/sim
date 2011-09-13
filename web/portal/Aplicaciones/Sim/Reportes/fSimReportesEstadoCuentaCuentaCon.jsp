<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimReportesEstadoCuentaCuenta">
	<Portal:PaginaNombre titulo="Reporte de Estado de Cuenta" subtitulo="Consulta de Saldos en la Cuenta"/>
	<Portal:Forma tipo='url' funcion='SimReportesEstadoCuentaCuenta' url="/Aplicaciones/Sim/Reportes/fSimReportesEstadoCuentaCuentaCon.jsp" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
	    <Portal:FormaElemento etiqueta='Clave del prestamo' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
		<Portal:FormaBotones>
        	<input type="button" name="Imprimir" value="Reporte en Pdf" onClick="javascript:fReportePdf();">
        </Portal:FormaBotones>
	</Portal:Forma>       
     
     <script>
        function fReportePdf(){
        	if (document.frmRegistro.CvePrestamo.value == "" ){
	      		alert ("Ingrese la clave del préstamo");
	      	}else{
	             url = '/portal/ProcesaReporte?Funcion=SimReportesEstadoCuentaCuenta&TipoReporte=Pdf&CvePrestamo='+document.frmRegistro.CvePrestamo.value;
	             MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
        }
     </script> 
</Portal:Pagina>     
