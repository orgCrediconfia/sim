<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteNegocioSecundario" precarga="SimNegocioSecundarioTelefono">
	<Portal:PaginaNombre titulo="Negocio Secundario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimClienteNegocioSecundario' parametros='IdPersona=${param.IdPersona}&IdNegocio=${param.IdNegocio}&IdPersona=${param.IdPersona}'>

		<Portal:FormaSeparador nombre="Datos generales del Negocio Secundario"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdNegocio' controlvalor='${requestScope.registro.campos["ID_NEGOCIO"]}' />
		</c:if>		
		<Portal:FormaElemento etiqueta='Tipo de negocio' control='selector' controlnombre='IdTipoNegocio' controlvalor='${requestScope.registro.campos["ID_TIPO_NEGOCIO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TIPO_NEGOCIO" campodescripcion="NOM_TIPO_NEGOCIO" datosselector='${requestScope.ListaTipoNegocio}'/>			
		<Portal:FormaElemento etiqueta='Nombre de negocio' control='Texto' controlnombre='NomNegocio' controlvalor='${requestScope.registro.campos["NOM_NEGOCIO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Giro' control='selector' controlnombre='IdGiro' controlvalor='${requestScope.registro.campos["CVE_CLASE"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_CLASE" campodescripcion="NOM_CLASE" datosselector='${requestScope.ListaGiro}'/>			
		<Portal:Calendario2 etiqueta='Fecha en que empez&oacute; el negocio' contenedor='frmRegistro' controlnombre='FechaInicioOperacion' controlvalor='${requestScope.registro.campos["FECHA_INICIO_OPERACION"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Negocio principal' control='Texto' controlnombre='NegocioPrincipal' controlvalor='${requestScope.registro.campos["NEGOCIO_PRINCIPAL"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		
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
		
		<Portal:TablaForma maestrodetallefuncion="SimNegocioSecundarioTelefono" nombre="Teléfonos del Negocio" funcion="SimNegocioSecundarioTelefono" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaTelefono}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimNegocioSecundarioTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimNegocioSecundarioTelefono&OperacionCatalogo=IN&Filtro=Alta&IdNegocioSecundario=${registro.campos["ID_NEGOCIO_SECUNDARIO"]}&IdNegocio=${registro.campos["ID_NEGOCIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
</Portal:Pagina>
