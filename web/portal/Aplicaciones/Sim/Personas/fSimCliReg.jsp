<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClientes" precarga="SimPersonaRoles SimClienteDireccion SimClienteTelefono SimClienteExConyugeDireccion SimClienteExConyugeTelefono SimClienteCuentaBancaria SimClienteDocumentacion SimClienteReferencia SimClienteNegocio SimClienteIntegranteUEF SimClienteAdeudo SimClienteGarantia">
	<Portal:PaginaNombre titulo="Personas" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimClientes' parametros='IdPersona=${param.IdPersona}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos generales de la persona"/>
		
			<Portal:FormaElemento etiqueta='Clave Persona' control='etiqueta-controloculto' controlnombre='IdPersona' controlvalor='${requestScope.registro.campos["ID_PERSONA"]}' />
		
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre Alterno' control='Texto' controlnombre='NombreAlterno' controlvalor='${requestScope.registro.campos["NOMBRE_ALTERNO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Sexo' control='SelectorGenero' controlnombre='Sexo' controlvalor='${requestScope.registro.campos["SEXO"]}' editarinicializado='true' obligatorio='false' />	
		<tr>
			<th>Estado Civil</th>
			<td>
				<select name='EstadoCivil' size='1' onchange='fRegimenMarital();'>
					<option value='Casado'   >Casado</option>
					<option value='Divorciado'   >Divorciado</option>
					<option value='Soltero'   >Soltero</option>
					<option value='Union Libre'   >Unión Libre</option>
					<option value='Viudo'   >Viudo</option>
				</select>
			</td>
		</tr>
		<script> BuscaSelectOpcion(document.frmRegistro.EstadoCivil,'<c:out value='${requestScope.registro.campos["ESTADO_CIVIL"]}'/>'); </script>
		
		<tr>
			<th>Regimen Marital</th>
			<td>
				<select name='RegimenMarital' size='1'>
					<option value='null'></option>
					<option value='Bienes Mancomunados'   >Bienes Mancomunados</option>
					<option value='Bienes Separados'   >Bienes Separados</option>
				</select>
			</td>
		</tr>
		<script> BuscaSelectOpcion(document.frmRegistro.RegimenMarital,'<c:out value='${requestScope.registro.campos["REGIMEN_MARITAL"]}'/>'); </script>
		
		
		<Portal:Calendario2 etiqueta='Fecha de Nacimiento' contenedor='frmRegistro' controlnombre='FechaNacimiento' controlvalor='${requestScope.registro.campos["FECHA_NACIMIENTO"]}'  esfechasis='true'/>
		
		<Portal:FormaElemento etiqueta='Identificaci&oacute;n Oficial' control='selector' controlnombre='IdentificacionOficial' controlvalor='${requestScope.registro.campos["IDENTIFICACION_OFICIAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_DOCUMENTO" campodescripcion="NOM_DOCUMENTO" datosselector='${requestScope.ListaIdentificacionOficial}'/>
		
		<Portal:FormaElemento etiqueta='No. Identificaci&oacute;n Oficial' control='Texto' controlnombre='NumIdentificacionOficial' controlvalor='${requestScope.registro.campos["NUM_IDENTIFICACION_OFICIAL"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='17' controllongitudmax='15' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='CURP' control='Texto' controlnombre='Curp' controlvalor='${requestScope.registro.campos["CURP"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='No. Dependientes econ&oacute;micos' control='Texto' controlnombre='NumDependientesEconomicos' controlvalor='${requestScope.registro.campos["NUM_DEPENDIENTES_ECONOMICOS"]}' controllongitud='1' controllongitudmax='1' editarinicializado='true' obligatorio='false' validadato='numerico' />
		<Portal:FormaElemento etiqueta='Escolaridad' control='selector' controlnombre='IdEscolaridad' controlvalor='${requestScope.registro.campos["ID_ESCOLARIDAD"]}' editarinicializado='true' obligatorio='true' campoclave="ID_ESCOLARIDAD" campodescripcion="NOM_ESCOLARIDAD" datosselector='${requestScope.ListaEscolaridad}'/>			
	
	    <c:if test='${requestScope.registro.campos["LISTA_NEGRA"] == "V"}'>
	    	<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='ListaNegraSi'/ checked >S&iacute
			    </td>
			</tr>
	    </c:if>	
	    <c:if test='${requestScope.registro.campos["LISTA_NEGRA"] == "F"}'>
	    	<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='ListaNegraSi'/>S&iacute
			    </td>
			</tr>
	    </c:if>
	    
	<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
	    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Prestamo/fSimPreAseCred.jsp' nombreliga='Asigna asesor de crédito' nomventana='VentanaAsesorCredito'/>
	</Portal:FormaElemento>
	<Portal:FormaElemento etiqueta='Asesor crédito' control='etiqueta-controlreferencia' controlnombre='NomAsesorCredito' controlvalor='${requestScope.registro.campos["NOMBRE_ASESOR"]}' />
	<input type="hidden" name="IdAsesorCredito" value='<c:out value='${requestScope.registro.campos["CVE_ASESOR_CREDITO"]}'/>' />
	
	<!--Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/-->
	
	<Portal:FormaElemento etiqueta='Sucursal' control='etiqueta-controlreferencia' controlnombre='NomSucursal' controlvalor='${requestScope.registro.campos["NOM_SUCURSAL"]}' />
	<input type="hidden" name="IdSucursal" value='<c:out value='${requestScope.registro.campos["ID_SUCURSAL"]}'/>' />
	
	    <c:if test='${(requestScope.registro.campos["ESTADO_CIVIL"] == "Casado") or (requestScope.registro.campos["ESTADO_CIVIL"] == "Divorciado")}'>
			<Portal:FormaSeparador nombre="Datos generales del Cónyuge o Exconyuge"/>
			<c:if test='${(requestScope.registro != null)}'>
				<Portal:FormaElemento etiqueta='Clave Persona' control='etiqueta-controloculto' controlnombre='IdPersonaExConyuge' controlvalor='${requestScope.registroExConyuge.campos["ID_PERSONA"]}' />
			</c:if>
			<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaternoExConyuge' controlvalor='${requestScope.registroExConyuge.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaternoExConyuge' controlvalor='${requestScope.registroExConyuge.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1ExConyuge' controlvalor='${requestScope.registroExConyuge.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2ExConyuge' controlvalor='${requestScope.registroExConyuge.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompletoExConyuge' controlvalor='${requestScope.registroExConyuge.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Sexo' control='SelectorGenero' controlnombre='SexoExConyuge' controlvalor='${requestScope.registroExConyuge.campos["SEXO"]}' editarinicializado='true' obligatorio='false' />	
			<Portal:Calendario2 etiqueta='Fecha de Nacimiento' contenedor='frmRegistro' controlnombre='FechaNacimientoExConyuge' controlvalor='${requestScope.registroExConyuge.campos["FECHA_NACIMIENTO"]}'  esfechasis='true'/>
			
			<Portal:FormaElemento etiqueta='Identificaci&oacute;n Oficial' control='selector' controlnombre='IdentificacionOficialExConyuge' controlvalor='${requestScope.registroExConyuge.campos["IDENTIFICACION_OFICIAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_DOCUMENTO" campodescripcion="NOM_DOCUMENTO" datosselector='${requestScope.ListaIdentificacionOficial}'/>
			
			<Portal:FormaElemento etiqueta='No. Identificaci&oacute;n Oficial' control='Texto' controlnombre='NumIdentificacionOficialExConyuge' controlvalor='${requestScope.registroExConyuge.campos["NUM_IDENTIFICACION_OFICIAL"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />		
		</c:if>
	    	
		<Portal:FormaBotones>
			<input type="button" name="Aceptar" value="Aceptar" onClick="javascript:fAceptar();" >
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimPersonaRoles" nombre="Roles de la persona" funcion="SimPersonaRoles" operacion="BA" parametros='IdPersona=${param.IdPersona}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Roles'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaRoles}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_TIPO_PERSONA"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_PERSONA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimPersonaRoles&OperacionCatalogo=CT&Filtro=Todos&Roles=Disponibles&IdPersona=${requestScope.registro.campos["ID_PERSONA"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteTelefono" nombre="Teléfonos de la persona" funcion="SimClienteTelefono" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaTelefono}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimClienteTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteTelefono&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
		
	<Portal:TablaForma maestrodetallefuncion="SimClienteDireccion" nombre="Direcciones de la persona" funcion="SimClienteDireccion" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Calle y n&uacute;mero'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Colonia'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Delegaci&oacute;n o Municipio'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='C&oacute;digo Postal'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor=''/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaDireccion}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CALLE"]} ${registro.campos["NUMERO_INT"]}' funcion='SimClienteDireccion' operacion='CR' parametros='IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdPersona=${param.IdPersona}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_DELEGACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CODIGO_POSTAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DOMICILIO_FISCAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteDireccion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<c:if test='${(requestScope.registro.campos["ESTADO_CIVIL"] == "Casado") or (requestScope.registro.campos["ESTADO_CIVIL"] == "Divorciado")}'>
		<Portal:TablaForma maestrodetallefuncion="SimClienteExConyugeTelefono" nombre="Teléfonos de Cónyuge o Excónyuge" funcion="SimClienteExConyugeTelefono" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaTelefonoExConyuge}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimClienteExConyugeTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteExConyugeTelefono&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimClienteExConyugeDireccion" nombre="Direcciones del Cónyuge o Excónyuge" funcion="SimClienteExConyugeDireccion" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Calle y n&uacute;mero'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Colonia'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Delegaci&oacute;n o Municipio'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='C&oacute;digo Postal'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaDireccionExConyuge}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CALLE"]} ${registro.campos["NUMERO_INT"]}' funcion='SimClienteExConyugeDireccion' operacion='CR' parametros='IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdPersona=${param.IdPersona}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>			
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_DELEGACION"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CODIGO_POSTAL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteExConyugeDireccion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	</c:if>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteCuentaBancaria" nombre="Cuentas bancarias" funcion="SimClienteCuentaBancaria" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Banco'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='N&uacute;mero de cuenta'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clabe'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaCuentaBancaria}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_BANCO"]}' funcion='SimClienteCuentaBancaria' operacion='CR' parametros='IdCuenta=${registro.campos["ID_CUENTA"]}&IdPersona=${param.IdPersona}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NUMERO_CUENTA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CLABE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteCuentaBancaria&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteDocumentacion" nombre="Documentaci&oacute;n de la persona" funcion="SimClienteDocumentacion" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Documento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha de recibido'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaDocumentacion}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_DOCUMENTO"]}' funcion='SimClienteDocumentacion' operacion='CR' parametros='IdDocumento=${registro.campos["ID_DOCUMENTO"]}&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_RECIBIDO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteDocumentacion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteReferencia" nombre="Referencias de la persona" funcion="SimClienteReferencia" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de verificaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Resultado de verificaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Tipo de referencia'/>	
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaReferencia}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_COMPLETO"]}' funcion='SimClienteReferencia' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdReferencia=${registro.campos["ID_REFERENCIA"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_VERIFICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_RESULTADO_VERIFICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TIPO_REFERENCIA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteReferencia&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteNegocio" nombre="Negocios de la persona" funcion="SimClienteNegocio" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo del negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Negocio'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaNegocio}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_NEGOCIO"]}' funcion='SimClienteNegocio' operacion='CR' parametros='IdNegocio=${registro.campos["ID_NEGOCIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='500' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["B_PRINCIPAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteNegocio&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteIntegranteUEF" nombre="Integrantes de la UEF" funcion="SimClienteIntegranteUEF" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='300' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Parentesco'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaIntegranteUEF}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_COMPLETO"]}' funcion='SimClienteIntegranteUEF' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdIntegrante=${registro.campos["ID_INTEGRANTE"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PARENTESCO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteIntegranteUEF&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteAdeudo" nombre="Adeudos de la persona" funcion="SimClienteAdeudo" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Persona o Institución a quién adeuda'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Saldo actual'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaAdeudo}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ADEUDO_A"]}' funcion='SimClienteAdeudo' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdAdeudo=${registro.campos["ID_ADEUDO"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["SALDO_ACTUAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteAdeudo&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimClienteGarantia" nombre="Garant&iacute;as de la persona" funcion="SimClienteGarantia" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Valor comercial en pesos'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='N&uacute;mero de factura o escritura'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Garante Depositario'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaGarantia}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_GARANTIA"]}' funcion='SimClienteGarantia' operacion='CR' parametros='IdGarantia=${registro.campos["ID_GARANTIA"]}&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${registroExConyuge.campos["ID_PERSONA"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["VALOR_COMERCIAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NUMERO_FACTURA_ESCRITURA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_GARANTE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/Aplicaciones/Sim/Personas/fSimCliGarReg.jsp?OperacionCatalogo=AL&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	
	<script>
		if (document.frmRegistro.EstadoCivil.value != 'Casado'){
			document.frmRegistro.RegimenMarital.disabled = true;
			document.frmRegistro.RegimenMarital.style.backgroundColor = '#CCCCCC';
		}else if (document.frmRegistro.EstadoCivil.value == 'Casado'){
			document.frmRegistro.RegimenMarital.disabled = false;
			document.frmRegistro.RegimenMarital.style.backgroundColor = 'white';
		}
		
		document.frmRegistro.ApPaterno.onblur = fLlenaNombre;
		document.frmRegistro.ApMaterno.onblur = fLlenaNombre;
		document.frmRegistro.Nombre1.onblur = fLlenaNombre;
		document.frmRegistro.Nombre2.onblur = fLlenaNombre;
		function fLlenaNombre(){
			sNomCompleto = document.frmRegistro.Nombre1.value +' '+ document.frmRegistro.Nombre2.value +' '+ document.frmRegistro.ApPaterno.value +' '+ document.frmRegistro.ApMaterno.value;
			document.frmRegistro.NomCompleto.value = sNomCompleto;
		}
		
		
		if (document.frmRegistro.NomCompleto.value != ''){
			if (document.frmRegistro.EstadoCivil.value == 'Casado' || document.frmRegistro.EstadoCivil.value == 'Divorciado'){
				document.frmRegistro.ApPaternoExConyuge.onblur = fLlenaNombreExConyuge;
				document.frmRegistro.ApMaternoExConyuge.onblur = fLlenaNombreExConyuge;
				document.frmRegistro.Nombre1ExConyuge.onblur = fLlenaNombreExConyuge;
				document.frmRegistro.Nombre2ExConyuge.onblur = fLlenaNombreExConyuge;
				function fLlenaNombreExConyuge(){
					sNomCompletoExConyuge = document.frmRegistro.Nombre1ExConyuge.value +' '+ document.frmRegistro.Nombre2ExConyuge.value +' '+ document.frmRegistro.ApPaternoExConyuge.value +' '+ document.frmRegistro.ApMaternoExConyuge.value;
					document.frmRegistro.NomCompletoExConyuge.value = sNomCompletoExConyuge;
				}
			}
		}
		
		function fRegimenMarital(){
			if (document.frmRegistro.EstadoCivil.value != 'Casado'){
				document.frmRegistro.RegimenMarital.disabled = true;
				document.frmRegistro.RegimenMarital.style.backgroundColor = '#CCCCCC';
			}else if (document.frmRegistro.EstadoCivil.value == 'Casado'){
				document.frmRegistro.RegimenMarital.disabled = false;
				document.frmRegistro.RegimenMarital.style.backgroundColor = 'white';
			}
		}
		
		function fAceptar(){
		        bExito = fRevisaCampos();
		        if (!bExito){
		              return;
		        }
			if (document.frmRegistro.IdAsesorCredito.value != ''){
				if (document.frmRegistro.IdPersona.value != ''){
					if (document.frmRegistro.EstadoCivil.value == 'Casado' || document.frmRegistro.EstadoCivil.value == 'Divorciado'){
						if (document.frmRegistro.NomCompletoExConyuge.value != ''){
			         		document.frmRegistro.submit();
			         	}else{
			         		alert("Ingrese el nombre del Conyuge o Exconyuge");
			         	}
			         }else{
			         	document.frmRegistro.submit();
			         }
			    }else{
			         	document.frmRegistro.submit();
			    }
		    }else{
		         	alert("Asigne un Asesor de Crédito");
		    }
		}
		
	</script>
</Portal:Pagina>
