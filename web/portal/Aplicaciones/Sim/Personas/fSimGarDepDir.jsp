<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimGaranteDepositarioDireccion">
	<Portal:PaginaNombre titulo="Direcci&oacute;n" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimGaranteDepositarioDireccion' parametros='IdPersona=${param.IdPersona}&IdDomicilio=${param.IdDomicilio}'>
	
		<Portal:FormaSeparador nombre="Direcci&oacute;n del Garante Depositario"/>				
		<Portal:FormaElemento etiqueta='Calle' control='Texto' controlnombre='Calle' controlvalor='${requestScope.registro.campos["CALLE"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='N&uacute;mero interior' control='Texto' controlnombre='NumeroInt' controlvalor='${requestScope.registro.campos["NUMERO_INT"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='N&uacute;mero exterior' control='Texto' controlnombre='NumeroExt' controlvalor='${requestScope.registro.campos["NUMERO_EXT"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=IN&Filtro=Inicio' nombreliga='C&oacute;digo Postal' nomventana='VentanaCodigoPostal'/>
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='C.P.' control='etiqueta-controlreferencia' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}'/>
		<Portal:FormaElemento etiqueta='Colonia' control='etiqueta-controlreferencia' controlnombre='Colonia' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}'/>
		<Portal:FormaElemento etiqueta='Municipio' control='etiqueta-controlreferencia' controlnombre='Municipio' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}'/>
		<Portal:FormaElemento etiqueta='Ciudad' control='etiqueta-controlreferencia' controlnombre='Ciudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}'/>
		<Portal:FormaElemento etiqueta='Estado' control='etiqueta-controlreferencia' controlnombre='Estado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}'/>
		<Portal:FormaElemento etiqueta='Tipo Domicilio' control='selector' controlnombre='IdTipoDomicilio' controlvalor='${requestScope.registro.campos["ID_TIPO_DOMICILIO"]}' editarinicializado='true' obligatorio='true' campoclave="ID_TIPO_DOMICILIO" campodescripcion="NOM_TIPO_DOMICILIO" datosselector='${requestScope.ListaTipoDomicilio}'/>
		<Portal:Calendario2 etiqueta='Fecha en que empez&oacute; habitar' contenedor='frmRegistro' controlnombre='FechaInicioResidencia' controlvalor='${requestScope.registro.campos["FECHA_INICIO_RESIDENCIA"]}'  esfechasis='true'/>
		
		<input type="hidden" name="CodigoPostal" value='<c:out value='${registro.campos["CODIGO_POSTAL"]}' />' />
		<input type="hidden" name="IdReferPost" value='<c:out value='${requestScope.registro.campos["ID_REFER_POST"]}' />' />
		<input type="hidden" name="Colonia" value='<c:out value='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />' />
		<input type="hidden" name="Municipio" value='<c:out value='${requestScope.registro.campos["NOM_DELEGACION"]}' />' />
		<input type="hidden" name="Ciudad" value='<c:out value='${requestScope.registro.campos["NOM_CIUDAD"]}' />' />
		<input type="hidden" name="Estado" value='<c:out value='${requestScope.registro.campos["NOM_ESTADO"]}' />' />
		<input type="hidden" name="TipoAsentamiento" value='<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}' />' />
	    	
	    	<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			<th>Domicilio fiscal</th>
			    <td> 
				<input type='checkbox' name='DomicilioFiscalSi'/>S&iacute &nbsp;&nbsp; <input type='checkbox' name='DomicilioFiscalNo'/>No
			    </td>
			</tr>
		</c:if>
	    	<c:if test='${requestScope.registro.campos["DOMICILIO_FISCAL"] == "V"}'>
	    		<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='DomicilioFiscalSi'/ checked >S&iacute &nbsp;&nbsp; <input type='checkbox' name='DomicilioFiscalNo'/>No
			    </td>
			</tr>
	    	</c:if>	
	    	<c:if test='${requestScope.registro.campos["DOMICILIO_FISCAL"] == "F"}'>
	    		<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='DomicilioFiscalSi'/>S&iacute &nbsp;&nbsp; <input type='checkbox' name='DomicilioFiscalNo'/ checked >No
			    </td>
			</tr>
	    	</c:if>	
	    	
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>
