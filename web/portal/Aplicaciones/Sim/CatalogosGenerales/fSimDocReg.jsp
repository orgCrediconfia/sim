<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoDocumentacion">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Documentaci&oacute;n" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoDocumentacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdDocumento' controlvalor='${requestScope.registro.campos["ID_DOCUMENTO"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomDocumento' controlvalor='${requestScope.registro.campos["NOM_DOCUMENTO"]}' controllongitud='20' controllongitudmax='20' />	
		
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='80' controllongitudmax='100'/>
		
		<Portal:FormaElemento etiqueta='Tipo de Documentaci&oacute;n' control='selector' controlnombre='IdTipoDocumentacion' controlvalor='${requestScope.registro.campos["ID_TIPO_DOCUMENTACION"]}' editarinicializado='true' obligatorio='true' campoclave="ID_TIPO_DOCUMENTACION" campodescripcion="NOM_TIPO_DOCUMENTACION" datosselector='${requestScope.ListaTipoDocumentacion}'/>
		
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='Imprimir Reporte' control='checkbox' controlnombre='BReporte' controlvalor='${requestScope.registro.campos["B_REPORTE"]}'/>
		</c:if>
		
		
			
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["B_REPORTE"] == "V")}'>
					<tr> 
					   	<th>Imprimir Reporte</th>
						<td> 
							<input type='checkbox' name='BReporte'/ checked >
						</td>
					</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_REPORTE"] == "F")}'>
				<tr> 
				   	<th>Imprimir Reporte</th>
					<td> 
						<input type='checkbox' name='BReporte'/>
					</td>
				</tr>
			</c:if>
		</c:if>
		<Portal:FormaElemento etiqueta='Reporte' control='selector' controlnombre='IdReporte' controlvalor='${requestScope.registro.campos["ID_REPORTE"]}' editarinicializado='true' obligatorio='false' campoclave="ID_REPORTE" campodescripcion="NOM_REPORTE" datosselector='${requestScope.ListaReporte}'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	