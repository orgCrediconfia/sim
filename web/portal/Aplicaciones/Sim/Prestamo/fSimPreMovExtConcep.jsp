<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Movimiento Extraordinario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoMovimientoExtraordinario' parametros='IdPrestamo=${param.IdPrestamo}&CveOperacion=${param.CveOperacion}&FechaMovimiento=${param.FechaMovimiento}&NumAmort=${param.NumAmort}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<c:forEach var="registro" items="${requestScope.ListaConcepto}">
			<tr>
			<th><c:out value='${registro.campos["DESC_LARGA"]}'/></th>
				<td>
					$ <input type='text' name='Importe<c:out value='${registro.campos["CVE_CONCEPTO"]}'/>' size='10' maxlength='10' value=''    onchange="fOnKeyUp();"  >
					<input type='hidden' name='CveConcepto<c:out value='${registro.campos["CVE_CONCEPTO"]}'/>' value='' >
				</td>
			</tr>
		</c:forEach>
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="CveOperacion" value='<c:out value='${param.CveOperacion}'/>' />
		<input type="hidden" name="FechaMovimiento" value='<c:out value='${param.FechaMovimiento}'/>' />
		<input type="hidden" name="NumAmort" value='<c:out value='${param.NumAmort}'/>' />
		
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&FechaMovimiento="+document.frmRegistro.FechaMovimiento.value+"&NumAmort="+document.frmRegistro.NumAmort.value+"&CveOperacion="+document.frmRegistro.CveOperacion.value;
			document.frmRegistro.submit();
		}
	</script>
</Portal:Pagina>


	