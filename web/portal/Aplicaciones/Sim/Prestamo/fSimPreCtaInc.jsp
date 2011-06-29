<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCuentaIncobrable">
	<Portal:PaginaNombre titulo="Liquidación por cuenta incobrable" subtitulo=""/>
	
	
<Portal:Forma tipo='catalogo' funcion='SimPrestamoCuentaIncobrable' >

		<Portal:FormaSeparador nombre="Liquidación por cuenta incobrable"/>
		<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor=''  esfechasis='true'/>
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.Respuesta}'/>' />
		<input type="hidden" name="GoBackPl" value='<c:out value='${param.GoBackPl}'/>' />
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="AplicaA" value='<c:out value='${param.AplicaA}'/>' />
		<Portal:FormaBotones>
			<input type='button' name='Liquidacion' value='Liquidación' onClick='fLiquidacionCuentaIncobrable()'/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
	
		function fLiquidacionCuentaIncobrable(){
			document.frmRegistro.Liquidacion.disabled = true; 
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoCuentaIncobrable&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&Fecha="+document.frmRegistro.FechaMovimiento.value+"&AplicaA="+document.frmRegistro.AplicaA.value;
			document.frmRegistro.submit();
			
		}
		
		if(document.frmRegistro.TxRespuesta.value != 'null' && document.frmRegistro.TxRespuesta.value != ''){
			alert(document.frmRegistro.TxRespuesta.value);
		} 
		
		if(document.frmRegistro.GoBackPl.value == 'Si'){
			window.opener.document.frmRegistro.submit();
			window.close();
		} 	
	</script>
</Portal:Pagina>



