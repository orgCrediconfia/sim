<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoReporteLineaFondeo">
	<Portal:PaginaNombre titulo="Reporte de Líneas de fondeo" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoReporteLineaFondeo'>
		<Portal:FormaSeparador nombre="Reporte"/>
		<Portal:FormaElemento etiqueta='# de Línea' control='selector' controlnombre='IdLinea' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="NUM_LINEA" campodescripcion="NUM_LINEA" datosselector='${requestScope.ListaLinea}'/>
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fAceptar(){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimPrestamoReporteLineaFondeo&TipoReporte=Pdf&IdLinea='+document.frmRegistro.IdLinea.value+'&Toolbar=Si&Ventana=Si','Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
	</script>
	
	
</Portal:Pagina>
