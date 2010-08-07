<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteDocumentacion">

	<Portal:PaginaNombre titulo="Documentaci&oacute;n" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteDocumentacion' parametros='IdPersona=${param.IdPersona}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/CatalogosGenerales/fSimDocCon.jsp' nombreliga='Asignar documento' nomventana='VentanaDocumento'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Documento' control='etiqueta-controlreferencia' controlnombre='NomDocumento' controlvalor='${requestScope.registro.campos["NOM_DOCUMENTO"]}' />
		<input type="hidden" name="IdDocumento" value='<c:out value='${requestScope.registro.campos["ID_DOCUMENTO"]}'/>' />
		<Portal:Calendario2 etiqueta='Fecha de Recibido' contenedor='frmRegistro' controlnombre='FechaRecibido' controlvalor='${requestScope.registro.campos["FECHA_RECIBIDO"]}'  esfechasis='true'/>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Usuarios/fSimUsuSucCoo.jsp' nombreliga='Asignar Coordinador de la Sucursal' nomventana='VentanaAsignarCoordinadorSucursal'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Verificado por' control='etiqueta-controlreferencia' controlnombre='NomCompletoCoordinador' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' />
		<input type="hidden" name="CveUsuarioCoordinador" value='<c:out value='${requestScope.registro.campos["CVE_VERIFICADOR"]}'/>' />
		
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			<th>Documentaci&oacute;n correcta</th>
				<td> 
					<input type='checkbox' name='DocCorSi' />S&iacute
				</td>
			</tr>
		</c:if>
	    	<c:if test='${requestScope.registro.campos["DOCUMENTO_CORRECTO"] == "V"}'>
	    		<tr> 
			<th>Documentaci&oacute;n correcta</th>
			    <td> 
				<input type='checkbox' name='DocCorSi'/ checked >S&iacute
			    </td>
			</tr>
	    	</c:if>	
	    	<c:if test='${requestScope.registro.campos["DOCUMENTO_CORRECTO"] == "F"}'>
	    		<tr> 
			<th>Documentaci&oacute;n correcta</th>
			    <td> 
				<input type='checkbox' name='DocCorSi'/>S&iacute
			    </td>
			</tr>
	    	</c:if>	
		
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
