<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesUsuarioEmpresa"  precarga="GeneralesUsuarioEmpresaPerfil">
	<Portal:PaginaNombre titulo="Usuario" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='GeneralesUsuarioEmpresa'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveUsuario' controlvalor='${requestScope.registro.campos["CVE_USUARIO"]}' controllongitud='50' controllongitudmax='256' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Localidad' control='Texto' controlnombre='Localidad' controlvalor='${requestScope.registro.campos["LOCALIDAD"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='false' />
		
		<Portal:FormaElemento etiqueta='Password' control='etiqueta'>
			<input type="password" name="Password" size="50" maxlength="256"/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Re-ingrese Password' control='etiqueta'>
			<input type="password" name="PasswordConfirma" size="50" maxlength="256" onBlur="validarPasswd()"/>
		</Portal:FormaElemento>

		<Portal:FormaElemento etiqueta='Alias' control='Texto' controlnombre='NomAlias' controlvalor='${requestScope.registro.campos["NOM_ALIAS"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='false' />

		<Portal:FormaSeparador nombre="Datos persona"/>
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			<Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=IN&Filtro=Todos&CveGpoEmpresa=${param.CveGpoEmpresa}' nombreliga='Persona' nomventana='VentanaPersona'/>
		</Portal:FormaElemento>

		<c:if test='${(requestScope.registro != null)}'>
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <a id="DatosPersona" href="javascript:MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=CR&Consulta=SI&IdPersona=&Ventana=Si','VentanaCp','scrollbars=yes,resizable=yes,width=700,height=400') ">Consulta datos de la persona</a>			
		</Portal:FormaElemento>
		</c:if>	
		
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' />
		<Portal:FormaElemento etiqueta='Empresa' control='etiqueta-controlreferencia' controlnombre='CveEmpresa' controlvalor='${requestScope.registro.campos["CVE_EMPRESA"]}' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		<input type="hidden" name="IdPersona" value='<c:out value='${requestScope.registro.campos["ID_PERSONA"]}'/>' />
		<input type="hidden" name="CveEmpresa" value='<c:out value='${requestScope.registro.campos["CVE_EMPRESA"]}'/>' />
		<input type="hidden" name="CveGpoEmpresa" value='<c:out value='${param.CveGpoEmpresa}'/>' />
	</Portal:Forma>

	<Portal:TablaForma maestrodetallefuncion="GeneralesUsuarioEmpresaPerfil" nombre="Aplicaciones asignadas al usuario" funcion="GeneralesUsuarioEmpresaPerfil" operacion="BA" parametros='CveUsuario=${registro.campos["CVE_USUARIO"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}' >
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave Aplicaci&oacuten'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave Perfil'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Bloqueado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaUsuarioPerfil}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='AplicacionPerfil${registro.campos["CVE_APLICACION"]}___${registro.campos["CVE_PERFIL"]}'>
					<input type="hidden" name="ModificacionAplicacionPerfil" value='<c:out value='${registro.campos["CVE_APLICACION"]}___${registro.campos["CVE_PERFIL"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["CVE_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["CVE_PERFIL"]}'/>				
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BLOQUEADO"]}' control='checkbox' controlnombre='Bloqueado${registro.campos["CVE_APLICACION"]}___${registro.campos["CVE_PERFIL"]}' />
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnBaja' etiqueta='Eliminar aplicaciones seleccionadas' />
			<Portal:Boton tipo='submit' nombre='btnModificar' etiqueta='Modificar bloqueos' />			
			<Portal:Boton tipo='catalogo' etiqueta='Agregar nuevas aplicaciones' funcion='GeneralesUsuarioEmpresaPerfil' operacion='CT' filtro="PerfilesDisponibles" parametros='&CveUsuario=${requestScope.registro.campos["CVE_USUARIO"]}&CveGpoEmpresa=${param.CveGpoEmpresa}'/>
			<input type="button" name="Imprimir" value="Imprimir reporte" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=GeneralesReporteUsuarioAplicaciones&CveUsuario=<%=request.getParameter("CveUsuario")%>&CveGpoEmpresa=<%=request.getParameter("CveGpoEmpresa")%>&TipoReporte=Pdf','ReporteUsuarioAplicaciones','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">
		</Portal:FormaBotones>					
	</Portal:TablaForma>	

	<script> 
		fAgregaCampo('IdPersona', 'Persona');
	</script>	
	
	<script>
	//EJECUTA LA FUNCION
	if (frmRegistro.IdPersona.value != ""){	
	
		//Modificamos el onclick del link
		DatosPersona.onclick = fAgregaUrl;
		
		function fAgregaUrl(){
			sIdPersona = document.frmRegistro.IdPersona.value;
			sCadena = DatosPersona.href;
			//Agregamos a la cadena el valor de Id persona
			sPunto  = sCadena.indexOf("&IdPersona=");			
			sPrimeraCadena = sCadena.substring(0, sPunto+11);
			sCadenaValor = sPrimeraCadena + sIdPersona;
			sSegundaCadena = sCadena.substring(sCadena.indexOf("&Ventana"));
			sCadena = sCadenaValor + sSegundaCadena ;
			DatosPersona.href = sCadena;
		}	
	}	
		
	</script>	
	<script>
	function validarPasswd() {
	
	 var p1 = frmRegistro.Password.value;
	 var p2 = frmRegistro.PasswordConfirma.value;
	
	 var espacios = true;
	 var cont = 0;
	
	 // Este bucle recorre la cadena para comprobar que no todo son espacios
	 while (espacios && (cont < p1.length)) {
		if (p1.charAt(cont) != " ") {
		  espacios = false;
		}
		cont++;
	  }
	  if (p1 != p2) { 
		alert("La confirmacion no coincide con el password");
		frmRegistro.PasswordConfirma.value ="";
		frmRegistro.Password.focus();
		return false;
	  } else {
		return true; 
	  }
	}
	</script>


	
	
	
	
	
</Portal:Pagina>
