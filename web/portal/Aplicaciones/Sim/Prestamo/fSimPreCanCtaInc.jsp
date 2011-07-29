<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCancelarCuentaIncobrable">
	<Portal:PaginaNombre titulo="Cuentas Incobrables" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoCancelarCuentaIncobrable' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del Préstamo' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='18' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='Nombre' controllongitud='40' controllongitudmax='60' editarinicializado='true'/>
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.Respuesta}'/>' />
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>	
				    <input type="button" name="CancelarCuentaIncobrable"  value="Cancelar Cuenta Incobrable" onclick="javascript:fCancelarCuentaIncobrable('<c:out value='${registro.campos["ID_PRESTAMO"]}'/>','<c:out value='${registro.campos["APLICA_A"]}'/>')">
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
	</Portal:TablaLista>
	
	<script>
	
		if(document.frmRegistro.TxRespuesta.value != 'null' && document.frmRegistro.TxRespuesta.value != ''){
			alert(document.frmRegistro.TxRespuesta.value);
		} 
	
		function fCancelarCuentaIncobrable(sIdPrestamo,sAplicaA){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoCancelarCuentaIncobrable&OperacionCatalogo=AL&IdPrestamo="+sIdPrestamo+"&AplicaA="+sAplicaA;
			document.frmRegistro.submit();		
		}
		
	</script>
		
</Portal:Pagina>
