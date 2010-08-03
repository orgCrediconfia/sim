<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimObligadoSolidario" precarga="SimObligadoSolidarioDireccion SimObligadoSolidarioTelefono SimObligadoSolidarioExConyugeDireccion SimObligadoSolidarioExConyugeTelefono">
	<Portal:PaginaNombre titulo="Obligado Solidario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimObligadoSolidario' parametros='IdPersona=${param.IdPersona}&IdExConyuge=${param.IdExConyuge}'>

		<Portal:FormaSeparador nombre="Datos generales del Obligado Solidario"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Clave Persona' control='etiqueta-controloculto' controlnombre='IdPersona' controlvalor='${requestScope.registro.campos["ID_PERSONA"]}' />
		</c:if>
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Alterno' control='Texto' controlnombre='NombreAlterno' controlvalor='${requestScope.registro.campos["NOMBRE_ALTERNO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Sexo' control='SelectorGenero' controlnombre='Sexo' controlvalor='${requestScope.registro.campos["SEXO"]}' editarinicializado='true' obligatorio='false' />	
		<tr>
			<th>Estado Civil</th>
			<td>
				<select name='EstadoCivil' size='1' onchange='fRegimenMarital();'>
					<option value='null'></option>
					<option value='Casado'   >Casado</option>
					<option value='Divorciado'   >Divorciado</option>
					<option value='Soltero'   >Soltero</option>
					<option value='Unión Libre'   >Unión Libre</option>
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
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='20' controllongitudmax='17' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='CURP' control='Texto' controlnombre='Curp' controlvalor='${requestScope.registro.campos["CURP"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='No. Dependientes econ&oacute;micos' control='Texto' controlnombre='NumDependientesEconomicos' controlvalor='${requestScope.registro.campos["NUM_DEPENDIENTES_ECONOMICOS"]}' controllongitud='1' controllongitudmax='1' editarinicializado='true' obligatorio='false' validadato='numerico' />
		<Portal:FormaElemento etiqueta='Escolaridad' control='selector' controlnombre='IdEscolaridad' controlvalor='${requestScope.registro.campos["ID_ESCOLARIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_ESCOLARIDAD" campodescripcion="NOM_ESCOLARIDAD" datosselector='${requestScope.ListaEscolaridad}'/>			
		
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='ListaNegraSi'/>S&iacute &nbsp;&nbsp; <input type='checkbox' name='ListaNegraNo'/>No
			    </td>
			</tr>
		</c:if>
	    	<c:if test='${requestScope.registro.campos["LISTA_NEGRA"] == "V"}'>
	    		<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='ListaNegraSi'/ checked >S&iacute &nbsp;&nbsp; <input type='checkbox' name='ListaNegraNo'/>No
			    </td>
			</tr>
	    	</c:if>	
	    	<c:if test='${requestScope.registro.campos["LISTA_NEGRA"] == "F"}'>
	    		<tr> 
			<th>Lista Negra</th>
			    <td> 
				<input type='checkbox' name='ListaNegraSi'/>S&iacute &nbsp;&nbsp; <input type='checkbox' name='ListaNegraNo'/ checked >No
			    </td>
			</tr>
	    	</c:if>	
	    	
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
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
		</Portal:Forma>	
		
		<Portal:TablaForma maestrodetallefuncion="SimObligadoSolidarioTelefono" nombre="Teléfonos del Obligado Solidario" funcion="SimObligadoSolidarioTelefono" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaTelefono}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimObligadoSolidarioTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimObligadoSolidarioTelefono&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimObligadoSolidarioDireccion" nombre="Direcciones del Obligado Solidario" funcion="SimObligadoSolidarioDireccion" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Calle y n&uacute;mero'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Colonia'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Delegaci&oacute;n o Municipio'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='C&oacute;digo Postal'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaDireccion}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CALLE"]} ${registro.campos["NUMERO_INT"]}' funcion='SimObligadoSolidarioDireccion' operacion='CR' parametros='IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdPersona=${param.IdPersona}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>			
					<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_DELEGACION"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CODIGO_POSTAL"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimObligadoSolidarioDireccion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<c:if test='${(requestScope.registro.campos["ESTADO_CIVIL"] == "Casado") or (requestScope.registro.campos["ESTADO_CIVIL"] == "Divorciado")}'>
			<Portal:TablaForma maestrodetallefuncion="SimObligadoSolidarioExConyugeTelefono" nombre="Teléfonos de Cónyuge o Excónyuge" funcion="SimObligadoSolidarioExConyugeTelefono" operacion="BA">
				<Portal:TablaListaTitulos>
					<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
				</Portal:TablaListaTitulos>	
				<c:forEach var="registro" items="${requestScope.ListaTelefonoExConyuge}">
					<Portal:TablaListaRenglon>
						<Portal:Columna tipovalor='texto' ancho='150' valor=''>
							<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimObligadoSolidarioExConyugeTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}'/>
						</Portal:Columna>
						<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
					</Portal:TablaListaRenglon>
				</c:forEach>
				<Portal:FormaBotones>
					<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimObligadoSolidarioExConyugeTelefono&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}'/>
				</Portal:FormaBotones>		
			</Portal:TablaForma>
			
			<Portal:TablaForma maestrodetallefuncion="SimObligadoSolidarioExConyugeDireccion" nombre="Direcciones del Cónyuge o Excónyuge" funcion="SimObligadoSolidarioExConyugeDireccion" operacion="BA">
				<Portal:TablaListaTitulos>
					<Portal:Columna tipovalor='texto' ancho='200' valor='Calle y n&uacute;mero'/>
					<Portal:Columna tipovalor='texto' ancho='150' valor='Colonia'/>
					<Portal:Columna tipovalor='texto' ancho='150' valor='Delegaci&oacute;n o Municipio'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='C&oacute;digo Postal'/>
				</Portal:TablaListaTitulos>	
				<c:forEach var="registro" items="${requestScope.ListaDireccionExConyuge}">
					<Portal:TablaListaRenglon>
						<Portal:Columna tipovalor='texto' ancho='200' valor=''>
							<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CALLE"]} ${registro.campos["NUMERO_INT"]}' funcion='SimExConyugeDireccion' operacion='CR' parametros='IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdPersona=${param.IdPersona}'/>
						</Portal:Columna>
						<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>			
						<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_DELEGACION"]}'/>
						<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CODIGO_POSTAL"]}'/>
					</Portal:TablaListaRenglon>
				</c:forEach>
				<Portal:FormaBotones>
					<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimObligadoSolidarioExConyugeDireccion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${param.IdExConyuge}'/>
				</Portal:FormaBotones>		
			</Portal:TablaForma>
		</c:if>
		
		
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
		</script>
		
</Portal:Pagina>
