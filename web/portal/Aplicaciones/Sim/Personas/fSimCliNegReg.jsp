<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteNegocio" precarga="SimNegocioTelefono">
	<Portal:PaginaNombre titulo="Negocio" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimClienteNegocio' parametros='IdPersona=${param.IdPersona}&IdNegocio=${param.IdNegocio}&IdDomicilio=${param.IdDomicilio}&IdExConyuge=${param.IdExConyuge}&NegocioPrincipal=${param.NegocioPrincipal}'>

		<Portal:FormaSeparador nombre="Datos generales del Negocio"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdNegocio' controlvalor='${requestScope.registro.campos["ID_NEGOCIO"]}' />
		</c:if>		
		<Portal:FormaElemento etiqueta='Tipo de negocio' control='selector' controlnombre='IdTipoNegocio' controlvalor='${requestScope.registro.campos["ID_TIPO_NEGOCIO"]}' editarinicializado='true' obligatorio='true' campoclave="ID_TIPO_NEGOCIO" campodescripcion="NOM_TIPO_NEGOCIO" datosselector='${requestScope.ListaTipoNegocio}'/>			
		<Portal:FormaElemento etiqueta='Nombre de negocio' control='Texto' controlnombre='NomNegocio' controlvalor='${requestScope.registro.campos["NOM_NEGOCIO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='15' controllongitudmax='15' editarinicializado='true' obligatorio='false' />
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Personas/fSimNegGiroCon.jsp' nombreliga='Asignar Giro' nomventana='VentanaAsignarGiro'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Giro' control='etiqueta-controlreferencia' controlnombre='NomClase' controlvalor='${requestScope.registro.campos["NOM_CLASE"]}' obligatorio='true'/>
		<input type="hidden" name="CveClase" value='<c:out value='${requestScope.registro.campos["CVE_CLASE"]}'/>' />
		
		<Portal:Calendario2 etiqueta='Fecha en que empez&oacute; el negocio' contenedor='frmRegistro' controlnombre='FechaInicioOperacion' controlvalor='${requestScope.registro.campos["FECHA_INICIO_OPERACION"]}'  esfechasis='true'/>
		
		<c:if test='${requestScope.registro.campos["B_PRINCIPAL"] == "V"}'>
	    		<tr> 
				<th>Negocio Principal</th>
				<td> 
					<input type='checkbox' name='BPrincipalSi'/ checked >S&iacute
				</td>
			</tr>
	    	</c:if>
		
		<c:if test='${param.NegocioPrincipal == "F"}'>
			<c:if test='${(requestScope.registro == null)}'>
				<tr> 
				   	<th>Negocio Principal</th>
					<td> 
						<input type='checkbox' name='BPrincipalSi' />S&iacute
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro != null)}'>
				<c:if test='${(requestScope.registro.campos["B_PRINCIPAL"] == "V")}'>
					<tr> 
					   	<th>Negocio Principal</th>
						<td> 
							<input type='checkbox' name='BPrincipalSi'/ checked >S&iacute
						</td>
					</tr>
				</c:if>
				<c:if test='${(requestScope.registro.campos["B_PRINCIPAL"] == "F")}'>
					<tr> 
					   	<th>Negocio Principal</th>
						<td> 
							<input type='checkbox' name='BPrincipalSi' />S&iacute
						</td>
					</tr>
				</c:if>
			</c:if>
		</c:if>
		<Portal:FormaSeparador nombre="Direcci&oacute;n"/>				
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
		
		<input type="hidden" name="CodigoPostal" value='<c:out value='${registro.campos["CODIGO_POSTAL"]}' />' />
		<input type="hidden" name="IdReferPost" value='<c:out value='${requestScope.registro.campos["ID_REFER_POST"]}' />' />
		<input type="hidden" name="Colonia" value='<c:out value='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />' />
		<input type="hidden" name="Municipio" value='<c:out value='${requestScope.registro.campos["NOM_DELEGACION"]}' />' />
		<input type="hidden" name="Ciudad" value='<c:out value='${requestScope.registro.campos["NOM_CIUDAD"]}' />' />
		<input type="hidden" name="Estado" value='<c:out value='${requestScope.registro.campos["NOM_ESTADO"]}' />' />
		<input type="hidden" name="TipoAsentamiento" value='<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}' />' />
	    	
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
		</Portal:Forma>	
		
		<Portal:TablaForma maestrodetallefuncion="SimNegocioTelefono" nombre="Teléfonos del Negocio" funcion="SimNegocioTelefono" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaTelefono}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimNegocioTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}&IdNegocio=${param.IdNegocio}&IdDomicilio=${param.IdDomicilio}&IdExConyuge=${param.IdExConyuge}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimNegocioTelefono&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}&IdExConyuge=${param.IdExConyuge}&IdNegocio=${param.IdNegocio}&IdDomicilio=${param.IdDomicilio}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
