<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaPagoIndividual">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Individual"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaPagoIndividual'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>
		
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPagoIndividual&OperacionCatalogo=IN&Filtro=Alta&TxRespuesta=0&TxPregunta=0&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&IdCaja="+document.frmRegistro.IdCaja.value+"&IdTransaccion="+null;
			document.frmRegistro.submit();
		}
	</script>		
		
</Portal:Pagina>
