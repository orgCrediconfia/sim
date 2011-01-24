<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoActividadRequisito">
	<Portal:PaginaNombre titulo="Actividades o requisitos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoActividadRequisito' parametros='IdPrestamo=${param.IdPrestamo}&IdActividadRequisito=${param.IdActividadRequisito}&IdPrestamo=${param.IdPrestamo}&AplicaA=${param.AplicaA}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controlreferencia' controlnombre='IdActividadRequisito' controlvalor='${requestScope.registro.campos["ID_ACTIVIDAD_REQUISITO"]}' />
		<Portal:FormaElemento etiqueta='Actividad o requisito' control='selector' controlnombre='IdActividadRequisito' controlvalor='${requestScope.registro.campos["ID_ACTIVIDAD_REQUISITO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_ACTIVIDAD_REQUISITO" campodescripcion="NOM_ACTIVIDAD_REQUISITO" datosselector='${requestScope.ListaActividadRequisito}'/>
		<Portal:FormaElemento etiqueta='Etapa de verificaci&oacute;n' control='selector' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_ETAPA_PRESTAMO" campodescripcion="NOM_ESTATUS_PRESTAMO" datosselector='${requestScope.ListaEstatusPrestamo}'/>
		<Portal:FormaElemento etiqueta='Fecha de registro' control='Texto' controlnombre='FechaRegistro' controlvalor='${requestScope.registro.campos["FECHA_REGISTRO"]}' controllongitud='15' controllongitudmax='15' editarinicializado='false' obligatorio='false'/>
		<tr>
			<th>Estatus</th>
			<td>
				<select name='Estatus' size='1'  >
					<option value='null'></option>
					<option value='Registrada'   >Registrada</option>
					<option value='Completada'   >Completada</option>
				</select>
			</td>
		</tr>
		<Portal:FormaElemento etiqueta='Comentario' control='Texto' controlnombre='Comentario' controlvalor='${requestScope.registro.campos["COMENTARIO"]}' controllongitud='80' controllongitudmax='100' editarinicializado='true' obligatorio='false'/>
		
		<Portal:FormaBotones>
			<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "V"}'>
				<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] != "V"}'>
					<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] != "V"}'>
						<Portal:FormaBotonAltaModificacion/>
					</c:if>
				</c:if>
			</c:if>
				<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>
	
	<script>
		<c:if test='${(requestScope.registro != null)}'>
			BuscaSelectOpcion(document.frmRegistro.Estatus,'<c:out value='${requestScope.registro.campos["ESTATUS"]}'/>'); 
		</c:if>	
	</script>
</Portal:Pagina>	