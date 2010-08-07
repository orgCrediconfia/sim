<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<script src='/portal/comun/lib/bFuncionesPersona.js'></script>
<Portal:Pagina funcion="GeneralesPersona">
	<Portal:PaginaNombre titulo="Persona" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='GeneralesPersona'>

		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave Persona' control='etiqueta-controloculto' controlnombre='IdPersona' controlvalor='${requestScope.registro.campos["ID_PERSONA"]}' />
		
		
		<Portal:FormaElemento etiqueta='Personalidad Fiscal' control='selector' controlnombre='PersonaFisica' controlvalor='${requestScope.registro.campos["B_PERSONA_FISICA"]}' editarinicializado='true' obligatorio='true' campoclave="Clave" campodescripcion="Descripcion" datosselector='${requestScope.ListaPersonalidad}' evento="onchange=fDeshabilitaPersonaFisica();"/>
		
		<Portal:FormaElemento etiqueta='EMail' control='Texto' controlnombre='EMail' controlvalor='${requestScope.registro.campos["EMAIL"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='20' controllongitudmax='17' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Tel&eacute;fono' control='Texto' controlnombre='NumTel' controlvalor='${requestScope.registro.campos["NUM_TEL"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />

		<c:if test='${(requestScope.registro == null) or (requestScope.registro.campos["B_PERSONA_FISICA"] == "V")}'>
			<Portal:FormaSeparador nombre="Persona F&iacute;sica"/>
			<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPersona' controlvalor='${requestScope.registro.campos["NOM_PERSONA"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='CURP' control='Texto' controlnombre='Curp' controlvalor='${requestScope.registro.campos["CURP"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		</c:if>	
		
		<c:if test='${(requestScope.registro == null) or (requestScope.registro.campos["B_PERSONA_FISICA"] == "F")}'>
			<Portal:FormaSeparador nombre="Persona Moral"/>
			<Portal:FormaElemento etiqueta='Raz&oacute;n Social' control='Texto' controlnombre='RazonSocial' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		</c:if>	
		
		<Portal:FormaSeparador nombre="Direcci&oacute;n"/>
		<Portal:FormaElemento etiqueta='Pais' control='selector' controlnombre='CvePais' controlvalor='${requestScope.registro.campos["CVE_PAIS"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_PAIS" campodescripcion="NOM_PAIS" datosselector='${requestScope.ListaPais}' evento="onchange=fDeshabilitaDireccion();" />
		<Portal:FormaElemento etiqueta='Calle' control='Texto' controlnombre='Calle' controlvalor='${requestScope.registro.campos["CALLE"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='N&uacute;mero' control='Texto' controlnombre='Numero' controlvalor='${requestScope.registro.campos["NUMERO"]}' controllongitud='16' controllongitudmax='16' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=GeneralesCodigoPostal&OperacionCatalogo=CT&Filtro=Todos&CveGpoEmpresa=${param.CveGpoEmpresa}' nombreliga='C&oacute;digo Postal' nomventana='VentanaCodigoPostal'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='C.P.' control='etiqueta-controlreferencia' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}' />
		<Portal:FormaElemento etiqueta='Asentamiento o Colonia' control='etiqueta-controlreferencia' controlnombre='Colonia' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />
		<Portal:FormaElemento etiqueta='Delegaci&oacute;n  o Municipio' control='etiqueta-controlreferencia' controlnombre='Municipio' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}' />
		<Portal:FormaElemento etiqueta='Ciudad' control='etiqueta-controlreferencia' controlnombre='Ciudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}' />
		<Portal:FormaElemento etiqueta='Estado' control='etiqueta-controlreferencia' controlnombre='Estado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}' />
		
		
		<!-- ESTOS CAMPOS SE LLENAN SOLO SI LA CLAVE DE PAIS NO ES MEX -->
		<Portal:FormaSeparador nombre="Direcci&oacute;n (si no es M&eacute;xico)"/>
		<Portal:FormaElemento etiqueta='C&oacute;digo Postal' control='Texto' controlnombre='CveCodigoPostal' controlvalor='${requestScope.registro.campos["CVE_CODIGO_POSTAL"]}' controllongitud='15' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Asentamiento' control='Texto' controlnombre='NomAsentamientoExt' controlvalor='${requestScope.registro.campos["EX_NOM_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Tipo Asentamiento' control='Texto' controlnombre='TipoAsentamientoExt' controlvalor='${requestScope.registro.campos["EX_TIPO_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Ciudad' control='Texto' controlnombre='NomCiudadExt' controlvalor='${requestScope.registro.campos["EX_NOM_CIUDAD"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Delegaci&oacute;n' control='Texto' controlnombre='NomDelegacionExt' controlvalor='${requestScope.registro.campos["EX_NOM_DELEGACION"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Estado' control='Texto' controlnombre='NomEstadoExt' controlvalor='${requestScope.registro.campos["EX_NOM_ESTADO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion>
			
			//MENSAJES DE SISTEMA
			MENSAJE_01 = "El RFC no tiene la longitud correcta";
			MENSAJE_02 = "El RFC no es valido";
			MENSAJE_03 = "Fecha de RFC no valida";
			
			MENSAJE_04 = "La CURP no tiene la longitud correcta";
			MENSAJE_05 = "La CURP no es valida";
			MENSAJE_06 = "Fecha de CURP no valida";
			
			MENSAJE_07 = "La razon social no puede ir vacia";
			MENSAJE_08 = "El nombre no puede ir vacio";
			
			
			//VERFICA QUE EL RFC NO ESTE VACIO
			//TRAER EL DATO DE BASE
			combo = document.frmRegistro.PersonaFisica;
			sValue = combo.options[combo.selectedIndex].value
				if (document.frmRegistro.Rfc.value !=""){
					//VERIFICA SI LA PERSONALIDAD FISCAL ES FISICA
					if (sValue == 'V'){
						//VERIFICA QUE EL TAMANIO DEL RFC SEA 13
						if (document.frmRegistro.Rfc.value.length != 13){
							alert(MENSAJE_01);
							return;
						}
						else{
						// VALIDACION DE LETRAS Y NUMEROS
							if ((!SonLetras(0, 4, document.frmRegistro.Rfc, false)) || (!SonNumeros(4, 10, document.frmRegistro.Rfc)) || (!SonLetrasNumeros(10, 13, document.frmRegistro.Rfc))) {
								alert(MENSAJE_02);
								return;
							}
							else {
								//OBTIENE EL ANIO, MES Y DIA DEL RFC
								var sAnio = document.frmRegistro.Rfc.value.substring(4,6);
								var sMes = document.frmRegistro.Rfc.value.substring(6,8);
								var sDia = document.frmRegistro.Rfc.value.substring(8,10);
			
								//ELIMINA EL POSIBLE CARACTER CERO A LA IZQUIERDA DEL NUMERO
								if (sMes.charAt(0) == "0"){
									sMes = sMes.charAt(1);
								}
								if (sDia.charAt(0) == "0"){
									sDia = sDia.charAt(1);
								}
								// VERIFICA SI LA FECHA DEL RFC ES VALIDA							
								if (!isDate(sAnio, sMes, sDia)){
									alert(MENSAJE_03);
									return;
								}//VALIDA FECHA
							}// VALIDACION DE LETRAS Y NUMEROS	
						}// RFC = 13
					}
					else{
						//INCIA VALIDACIONES PARA PERSONA MORAL
						//VERIFICA QUE EL TAMANIO DEL RFC SEA 12
						if (document.frmRegistro.Rfc.value.length != 12){
							alert(MENSAJE_01);
							return;
						}
						else {
							// VALIDACION DE LETRAS Y NUMEROS
							if ((!SonLetrasCaracteresValidos(0, 3, document.frmRegistro.Rfc)) || (!SonNumeros(3, 9, document.frmRegistro.Rfc)) || (!SonLetrasNumeros(9, 12, document.frmRegistro.Rfc))) {
								alert(MENSAJE_02);
								return;
							}
							else {
								//OBTIENE EL ANIO, MES Y DIA DEL RFC
								var sAnio = document.frmRegistro.Rfc.value.substring(3,5);
								var sMes = document.frmRegistro.Rfc.value.substring(5,7);
								var sDia = document.frmRegistro.Rfc.value.substring(7,9);
								//ELIMINA EL POSIBLE CARACTER CERO A LA IZQUIERDA DEL NUMERO
								if (sMes.charAt(0) == "0"){
									sMes = sMes.charAt(1);
								}
								if (sDia.charAt(0) == "0"){
									sDia = sDia.charAt(1);
								}
								// VERIFICA SI LA FECHA DEL RFC ES VALIDA							
								if (!isDate(sAnio, sMes, sDia)){
									alert(MENSAJE_03);
									return;
								}//VALIDA FECHA
							}// VALIDACION DE LETRAS Y NUMEROS
						}// RFC = 12
					}//VERIFICA SI LA PERSONALIDAD FISCAL ES FISICA
				}//RFC NO VACIO
					
				//VERIFICA SI APLICA VALIDACIONES PARA PERSONA FISICA
				if (sValue == 'V'){
					//VALIDA QUE EL CURP NO ESTE VACIO
					if (document.frmRegistro.Curp.value !=""){
						//VERIFICA QUE EL TAMANIO SEA DE 18 CARACTERES
						if (document.frmRegistro.Curp.value.length < 18){
							alert(MENSAJE_04);
							return;
						}
						else {
							// Valida que los primeros caracteres sean alfabeticos y los siguientes numericos segun la nomenclatura del CURP
							if ((!SonLetras(0, 4, document.frmRegistro.Curp, false)) || (!SonNumeros(4, 10, document.frmRegistro.Curp)) || (!SonLetras(10, 11, document.frmRegistro.Curp, false))) {
								alert(MENSAJE_05);
								return;
							}
							else {
								// Verifica si la fecha del CURP es correcta
								//OBTIENE EL ANIO, MES Y DIA DEL CURP
								sAnio = document.frmRegistro.Curp.value.substring(4,6);
								sMes = document.frmRegistro.Curp.value.substring(6,8);
								sDia = document.frmRegistro.Curp.value.substring(8,10);
								//ELIMINA EL POSIBLE CARACTER CERO A LA IZQUIERDA DEL NUMERO
								if (sMes.charAt(0) == "0"){
									sMes = sMes.charAt(1);
								}
								if (sDia.charAt(0) == "0"){
									sDia = sDia.charAt(1);
								}
								// VERIFICA SI LA FECHA DEL RFC ES VALIDA							
								if (!isDate(sAnio, sMes, sDia)){
									alert(MENSAJE_06);
									return;
								}//VALIDA FECHA
								else {
									if ((document.frmRegistro.Curp.value.substring(10,11)!="H") && (document.frmRegistro.Curp.value.substring(10,11)!="M")){
										alert(MENSAJE_06);
										return;
									}
								}
							} //VALIDACION DE LOS PRIMEROS 4 CARACTERES
						} //LONGITUD IGUAL A 18 CARACTERES
					} //CURP VACIO
				}//VERIFICA SI APLICA VALIDACIONES PARA PERSONA FISICA
				//VALIDA CAMPOS VACIOS
				if (sValue == 'F'){
				   if (document.frmRegistro.RazonSocial.value ==""){
				   alert(MENSAJE_07);
				   return;
				   }
				}else{
					if (sValue == 'V'){
						if (document.frmRegistro.NomPersona.value ==""){
				   		alert(MENSAJE_08);
				   		return;
				   		}
					}
				}
				
							
			</Portal:FormaBotonAltaModificacion>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		
		<input type="hidden" name="IdReferPost" value='<c:out value='${requestScope.registro.campos["ID_REFER_POST"]}'/>' />
		<input type="hidden" name="CodigoPostal" value='<c:out value='${requestScope.registro.campos["CODIGO_POSTAL"]}'/>' />
				                  
		<input type="hidden" name="NomAsentamiento" value='<c:out value='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}'/>' />
		<input type="hidden" name="TipoAsentamiento" value='<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}'/>' />
		<input type="hidden" name="NomCiudad" value='<c:out value='${requestScope.registro.campos["NOM_CIUDAD"]}'/>' />
		<input type="hidden" name="NomEstado" value='<c:out value='${requestScope.registro.campos["NOM_ESTADO"]}'/>' />
		<input type="hidden" name="NomDelegacion" value='<c:out value='${requestScope.registro.campos["NOM_DELEGACION"]}'/>' />
		
		
		<input type="hidden" name="CveGpoEmpresa" value='<c:out value='${param.CveGpoEmpresa}'/>' />		
		<input type="hidden" name="PersonaFisicaDatos" value='<c:out value='${requestScope.registro.campos["B_PERSONA_FISICA"]}'/>' />
		<input type="hidden" name="NomCompletoPersona" value='<c:out value='${requestScope.registro.campos["NOM_COMPLETO"]}'/>' />
		
		<input type="hidden" name="IdDomicilio" value='<c:out value='${requestScope.registro.campos["ID_DOMICILIO"]}'/>' />
		<input type="hidden" name="IdPersona" value='<c:out value='${requestScope.registro.campos["ID_PERSONA"]}'/>' />
		
		
		
	</Portal:Forma>
	
	<script>	
		function fDeshabilitaDireccion(){
			
				formulario = document.frmRegistro;				
				comboPais = document.frmRegistro.CvePais;
					
				sValorPais = comboPais.options[comboPais.selectedIndex].value;
				
				if (sValorPais == 'MEX '){
			
					formulario.CveCodigoPostal.disabled = true;
					formulario.NomAsentamientoExt.disabled = true;
					formulario.TipoAsentamientoExt.disabled = true;
					formulario.NomCiudadExt.disabled = true;
					formulario.NomDelegacionExt.disabled = true;
					formulario.NomEstadoExt.disabled = true;
					
					formulario.CveCodigoPostal.runtimeStyle.background  = '#CCCCCC';
					formulario.NomAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.TipoAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomCiudadExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomDelegacionExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomEstadoExt.runtimeStyle.background  = '#CCCCCC';
					
					
					
		
				}else{
					formulario.CveCodigoPostal.disabled = false;
					formulario.NomAsentamientoExt.disabled = false;
					formulario.TipoAsentamientoExt.disabled = false;
					formulario.NomCiudadExt.disabled = false;
					formulario.NomDelegacionExt.disabled = false;
					formulario.NomEstadoExt.disabled = false;
					
					formulario.CveCodigoPostal.runtimeStyle.background  = 'white';
					formulario.NomAsentamientoExt.runtimeStyle.background  = 'white';
					formulario.TipoAsentamientoExt.runtimeStyle.background  = 'white';
					formulario.NomCiudadExt.runtimeStyle.background  = 'white';
					formulario.NomDelegacionExt.runtimeStyle.background  = 'white';
					formulario.NomEstadoExt.runtimeStyle.background  = 'white';
					
					
					document.getElementById('CodigoPostal').innerHTML='';
					document.getElementById('Colonia').innerHTML='';
					document.getElementById('Estado').innerHTML='';
					document.getElementById('Municipio').innerHTML='';
					document.getElementById('Ciudad').innerHTML='';
					
					document.frmRegistro.CodigoPostal.value= '';
					document.frmRegistro.IdReferPost.value= '';
					document.frmRegistro.NomAsentamiento.value = '';
					document.frmRegistro.TipoAsentamiento.value= '';
					document.frmRegistro.NomCiudad.value= '';
					document.frmRegistro.NomEstado.value= '';
					document.frmRegistro.NomDelegacion.value= '';
					
					//LEEMOS EL CONTENIDO DE LA LIGA
					var Links = document.getElementsByTagName("a");
			
					//DESHABILITAMOS TAMBIEN LA LIGA DEL CP		
					
					
					//Links[13].removeAttribute("href");
					
					
				}
		}
					
		//FUNCION ASOCIADA AL EVENTO
		function fDeshabilitaPersonaFisica(){
			fDeshabilita('PersonaFisica', 'ApPaterno, ApMaterno, NomPersona, Curp, NomCompleto', 'V', 'F', '');
		}

		//FUNCION PARA HABILITAR Y DESHABILITAR CAMPOS DE ACUERDO AL 
		//CONTENIDO DE UN COMBO
		function fDeshabilita(sCombo, sControles, sHabilita, sDeshabilita, sColorSombreado){
		
			//VARIABLES 	
			sColor     = 'white';
			formulario = document.frmRegistro;
			objCombo   = eval("formulario." +sCombo);
		
			//COLOR
			if (sColorSombreado == ""){
				sColorSombreado = '#CCCCCC';
			}
			
			//CREAMOS Y LLENAMOS EL ARREGLO DE CONTROLES
			arregloControles = new Array();
			arregloControles = sControles.split(',');		
	
			
			//EJECUTAMOS SOBRE TODO EL ARREGLO
			for(i=0; i<arregloControles.length; i++){
				//OBJ ES UN CONTROL DE TEXTO
				obj = eval("formulario." + arregloControles[i]);			
				
				//DESHABILITAR
				if (objCombo.value == sDeshabilita){
					obj.disabled = true;
					obj.runtimeStyle.background  = sColorSombreado;
					formulario.RazonSocial.disabled = false;
					formulario.RazonSocial.runtimeStyle.background  = sColor;
				}
				
				//HABILITAR
				if (objCombo.value == sHabilita){
					obj.disabled = false;
					obj.runtimeStyle.background  = sColor;
					formulario.RazonSocial.disabled = true;
					formulario.RazonSocial.runtimeStyle.background  = sColorSombreado;
					formulario.NomCompleto.disabled = true;
				}			
			}
		}
		combo = document.frmRegistro.PersonaFisica;
		sValue = combo.options[combo.selectedIndex].value;
		sPersona = document.frmRegistro.PersonaFisicaDatos.value;		
		
		if ((sPersona== '')||(sPersona=='V')){
			if (sValue == 'V'){

			//FUNCION PARA LLENAR EL CAMPO DE NOMBRE COMPLETO		
			formulario = document.frmRegistro;
			formulario.ApPaterno.onblur = fLlenaNombre;
			formulario.ApMaterno.onblur = fLlenaNombre;
			formulario.NomPersona.onblur = fLlenaNombre;
			function fLlenaNombre(){
				sNomCompleto = formulario.NomPersona.value +' '+ formulario.ApPaterno.value +' '+ formulario.ApMaterno.value;
				formulario.NomCompleto.value = sNomCompleto;
				formulario.NomCompletoPersona.value = sNomCompleto;
			}
		  }
			
		}
		

		//FUNCION PARA VALIDAD LOS CAMPOS DISPONIBLES DE CAPTURA
		formulario = document.frmRegistro;				
		combo = document.frmRegistro.PersonaFisica;
		comboPais = document.frmRegistro.CvePais
		
		function fCarga(){
		
			
			sValue = combo.options[combo.selectedIndex].value;
			sPersona = document.frmRegistro.PersonaFisicaDatos.value;
			
			sPais = comboPais.options[comboPais.selectedIndex].value;
							
			
			if (sPersona== ''){
			
							
				for(i=0; i<frmRegistro.CvePais.options.length; i++){
					if(frmRegistro.CvePais.options[i].value == 'MEX ')
					frmRegistro.CvePais.selectedIndex = i;
														
				}
				    formulario.CveCodigoPostal.disabled = true;
					formulario.NomAsentamientoExt.disabled = true;
					formulario.TipoAsentamientoExt.disabled = true;
					formulario.NomCiudadExt.disabled = true;
					formulario.NomDelegacionExt.disabled = true;
					formulario.NomEstadoExt.disabled = true;
					
					formulario.CveCodigoPostal.runtimeStyle.background  = '#CCCCCC';
					formulario.NomAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.TipoAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomCiudadExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomDelegacionExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomEstadoExt.runtimeStyle.background  = '#CCCCCC';
					
				
				if (sPais == 'MEX '){
				
				
				    formulario.CveCodigoPostal.disabled = true;
					formulario.NomAsentamientoExt.disabled = true;
					formulario.TipoAsentamientoExt.disabled = true;
					formulario.NomCiudadExt.disabled = true;
					formulario.NomDelegacionExt.disabled = true;
					formulario.NomEstadoExt.disabled = true;
					
					formulario.CveCodigoPostal.runtimeStyle.background  = '#CCCCCC';
					formulario.NomAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.TipoAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomCiudadExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomDelegacionExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomEstadoExt.runtimeStyle.background  = '#CCCCCC';
				
				
				}
				
				 
		
		 		if (sValue == 'F'){
		  			
		  			formulario.NomPersona.disabled = true;
					formulario.ApPaterno.disabled = true;
					formulario.ApMaterno.disabled = true;
					formulario.Curp.disabled = true;
				
					formulario.NomPersona.runtimeStyle.background  = '#CCCCCC';
					formulario.ApPaterno.runtimeStyle.background  = '#CCCCCC';
					formulario.ApMaterno.runtimeStyle.background  = '#CCCCCC';
					formulario.Curp.runtimeStyle.background  = '#CCCCCC';
					formulario.NomCompleto.runtimeStyle.background  = '#CCCCCC';
		 
		 		}else{
		 		 	if (sValue == 'V'){
		 		 		formulario.RazonSocial.disabled = true;
		 		 		formulario.RazonSocial.runtimeStyle.background  = '#CCCCCC';
		 		 		formulario.NomCompleto.disabled = true;
		 		 	}
		 	 	}
		 	}else{
		 	
		 		if (sPais== 'MEX '){
		 	
		 	        formulario.CveCodigoPostal.disabled = true;
					formulario.NomAsentamientoExt.disabled = true;
					formulario.TipoAsentamientoExt.disabled = true;
					formulario.NomCiudadExt.disabled = true;
					formulario.NomDelegacionExt.disabled = true;
					formulario.NomEstadoExt.disabled = true;
					
					formulario.CveCodigoPostal.runtimeStyle.background  = '#CCCCCC';
					formulario.NomAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.TipoAsentamientoExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomCiudadExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomDelegacionExt.runtimeStyle.background  = '#CCCCCC';
					formulario.NomEstadoExt.runtimeStyle.background  = '#CCCCCC';
				}
		 	
		 	
		 		formulario.PersonaFisica.disabled = true;
		 	
		 		if (sPersona== 'F'){
		 		
				}else{
		 		 	if (sPersona == 'V'){
		 		 		formulario.NomCompleto.disabled = true;
		 		 	}
		 	 	}
		 	
		 	
		 	}
		 	
		}	
		//EJECUTA LA FUNCION
		fCarga();
	</script>	

</Portal:Pagina>
