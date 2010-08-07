<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Movimiento Extraordinario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoMovimientoExtraordinario' >
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO"]}' controllongitud='20' controllongitudmax='18' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='30' controllongitudmax='30' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Movimiento extraordinario' control='selector' controlnombre='CveOperacion' controlvalor='${requestScope.registro.campos["CVE_OPERACION"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_OPERACION" campodescripcion="DESC_LARGA" datosselector='${requestScope.ListaOperaciones}'/>
		<Portal:Calendario2 etiqueta='Fecha del movimiento' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Número de amortización' control='Texto' controlnombre='NumAmort' controlvalor='' controllongitud='2' controllongitudmax='2' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		
		
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=CR&Consulta=Concepto&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&FechaMovimiento="+document.frmRegistro.FechaMovimiento.value+"&NumAmort="+document.frmRegistro.NumAmort.value+"&CveOperacion="+document.frmRegistro.CveOperacion.value;
			document.frmRegistro.submit();
		}
	</script>
</Portal:Pagina>


	