<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimProcesoCierre">
	<Portal:PaginaNombre titulo="Proceso de cierre" subtitulo=""/>
	
	
<Portal:Forma tipo='catalogo' funcion='SimProcesoCierre'>

		<Portal:FormaSeparador nombre="Cierre del día"/>
		<Portal:FormaElemento etiqueta='Fecha del medio' control='etiqueta-controlreferencia' controlnombre='FMedio' controlvalor='${requestScope.registro.campos["F_MEDIO"]}' controllongitud='19' controllongitudmax='18'/>
		<Portal:FormaBotones>
			<input type='button' name='Aceptar' value='Cierre del día' onClick='fCierreDia()'/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fCierreDia(){
			document.frmRegistro.Aceptar.disabled = true; 
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR&Modulo=Cierre&Ventana=Si','VentanaCp','scrollbars=yes,resizable=yes,width=500,height=300');
		}	
	</script>
</Portal:Pagina>



