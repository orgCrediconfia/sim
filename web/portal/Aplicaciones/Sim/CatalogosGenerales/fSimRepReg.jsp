<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCatalogoReporte" >
	<Portal:PaginaNombre titulo="Catálogo de Reportes" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoReporte'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdReporte' controlvalor='${param.IdReporte}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomReporte' controlvalor='${requestScope.registro.campos["NOM_REPORTE"]}' controllongitud='75' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Aplica a</th>
			<td>
				<select name='AplicaA' size='1'  >
					<option value='Individual'>Individual</option>
					<option value='Grupo'>Grupo</option>
				</select>
			</td>
		</tr>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Función' control='Texto' controlnombre='FuncionReporte' controlvalor='${requestScope.registro.campos["CVE_FUNCION"]}' controllongitud='30' controllongitudmax='30' editarinicializado='false'/>
		</c:if>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	<script>
		<c:if test='${(requestScope.registro != null)}'>
			BuscaSelectOpcion(document.frmRegistro.AplicaA,'<c:out value='${requestScope.registro.campos["APLICA_A"]}'/>');  
		</c:if>
	</script>
</Portal:Pagina>	
