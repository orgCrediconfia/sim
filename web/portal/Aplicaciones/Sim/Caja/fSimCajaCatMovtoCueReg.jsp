<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaCatalogoMovimientoCuenta">
	<Portal:PaginaNombre titulo="Catálogo de Movimiento de la Cuenta" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaCatalogoMovimientoCuenta'>
		<Portal:FormaSeparador nombre="Datos del Movimiento"/>
		<Portal:FormaElemento etiqueta='Clave del Movimiento' control='Texto' controlnombre='CveOperacion' controlvalor='${param.CveOperacion}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Movimiento' control='Texto' controlnombre='DescLarga' controlvalor='${requestScope.registro.campos["DESC_LARGA"]}' controllongitud='45' controllongitudmax='60' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Afecta al Saldo</th>
			<td>
				<select name='CveAfectaSaldo' size='1'  >
					<option value='D'>Decrementa</option>
					<option value='I'>Incrementa</option>
				</select>
			</td>
		</tr>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	<script>
		<c:if test='${(requestScope.registro != null)}'>
			BuscaSelectOpcion(document.frmRegistro.CveAfectaSaldo,'<c:out value='${requestScope.registro.campos["CVE_AFECTA_SALDO"]}'/>');  
			document.frmRegistro.CveAfectaSaldo.disabled = true;
		</c:if>
	</script>
</Portal:Pagina>
