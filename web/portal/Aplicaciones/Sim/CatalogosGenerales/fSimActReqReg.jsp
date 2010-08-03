<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoActividadRequisito">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de actividades o requisitos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoActividadRequisito' parametros='IdEstatusPrestamo=${param.IdEstatusPrestamo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdActividadRequisito' controlvalor='${requestScope.registro.campos["ID_ACTIVIDAD_REQUISITO"]}' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomActividadRequisito' controlvalor='${requestScope.registro.campos["NOM_ACTIVIDAD_REQUISITO"]}' controllongitud='30' controllongitudmax='30' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='80' controllongitudmax='100'/>			
		<tr>
			<th>Aplica a</th>
			<td>
				<select name='AplicaA' size='1'  >
					<option value='null'></option>
					<option value='INDIVIDUAL'   >INDIVIDUAL</option>
					<option value='GRUPO'   >GRUPO</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Tipo</th>
			<td>
				<select name='Tipo' size='1'  >
					<option value='null'></option>
					<option value='ACTIVIDAD'   >ACTIVIDAD</option>
					<option value='REQUISITO'   >REQUISITO</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Aplicable en ciclo</th>
			<td>
				<select name='AplicaCiclo' size='1'  >
					<option value='null'></option>
					<option value='TODOS'   >TODOS</option>
					<option value='PAR'   >PAR</option>
					<option value='IMPAR'   >IMPAR</option>
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
			BuscaSelectOpcion(document.frmRegistro.AplicaA,'<c:out value='${requestScope.registro.campos["APLICA_A"]}'/>'); 
			BuscaSelectOpcion(document.frmRegistro.Tipo,'<c:out value='${requestScope.registro.campos["TIPO"]}'/>'); 
			BuscaSelectOpcion(document.frmRegistro.AplicaCiclo,'<c:out value='${requestScope.registro.campos["APLICA_CICLO"]}'/>'); 
		</c:if>
	</script>
</Portal:Pagina>	
