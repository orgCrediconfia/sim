<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimUsuarioEmpresa"  precarga="SimUsuarioDireccion SimUsuarioTelefono SimUsuarioRegional SimUsuarioSucursal">
	<Portal:PaginaNombre titulo="Usuarios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimUsuarioEmpresa' parametros='IdPersona=${param.IdPersona}&CveUsuario=${param.CveUsuario}'>

		<Portal:FormaSeparador nombre="Datos generales del Usuario"/>
		
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='ClaveUsuario' controlvalor='${requestScope.registro.campos["CVE_USUARIO"]}' controllongitud='50' controllongitudmax='256' editarinicializado='false' obligatorio='true' />
	  	
		<Portal:FormaElemento etiqueta='Password' control='etiqueta'>
			<input type="password" name="Password" size="50" maxlength="256"/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Re-ingrese Password' control='etiqueta'>
			<input type="password" name="PasswordConfirma" size="50" maxlength="256" onBlur="validarPasswd()"/>
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Puesto' control='selector' controlnombre='CvePuesto' controlvalor='${requestScope.registro.campos["CVE_PUESTO"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_PUESTO" campodescripcion="NOM_PUESTO" datosselector='${requestScope.ListaPuesto}'/>	
		<Portal:FormaElemento etiqueta='Perfil' control='selector' controlnombre='CvePerfil' controlvalor='${requestScope.registro.campos["CVE_PERFIL"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_PERFIL" campodescripcion="NOM_PERFIL" datosselector='${requestScope.ListaPerfil}'/>
		
		<Portal:FormaElemento etiqueta='N&uacute;mero de n&oacute;mina' control='Texto' controlnombre='NumNomina' controlvalor='${requestScope.registro.campos["NUM_NOMINA"]}' controllongitud='35' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:Calendario2 etiqueta='Fecha de Ingreso a la Empresa' contenedor='frmRegistro' controlnombre='FechaIngresoEmpresa' controlvalor='${requestScope.registro.campos["FECHA_INGRESO_EMPRESA"]}'  esfechasis='false'/>
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='Asignar a todas las regiones (Superusuario)' control='checkbox' controlnombre='BRegionales' controlvalor='${requestScope.registro.campos["B_REGIONALES"]}'/>	
		</c:if>
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["B_REGIONALES"] == "V")}'>
				<tr> 
				   	<th>Asignar a todas las regiones (Superusuario)</th>
					<td> 
						<input type='checkbox' name='BRegionales'/ checked >
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_REGIONALES"] == "F")}'>
				<tr> 
				   	<th>Asignar a todas las regiones (Superusuario)</th>
					<td> 
						<input type='checkbox' name='BRegionales'/>
					</td>
				</tr>
			</c:if>
		</c:if>
		<Portal:FormaElemento etiqueta='Sucursal a la que pertenece' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursalPertenece}'/>
	    	<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma maestrodetallefuncion="SimUsuarioDireccion" nombre="Direcciones del Usuario" funcion="SimUsuarioDireccion" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Calle y n&uacute;mero'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Colonia'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Delegaci&oacute;n o Municipio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='C&oacute;digo Postal'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaDireccion}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CALLE"]} ${registro.campos["NUMERO_INT"]}' funcion='SimUsuarioDireccion' operacion='CR' parametros='IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdPersona=${param.IdPersona}&CveUsuario=${param.CveUsuario}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_DELEGACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CODIGO_POSTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimUsuarioDireccion&OperacionCatalogo=IN&Filtro=Alta&CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimUsuarioTelefono" nombre="Teléfonos del Usuario" funcion="SimUsuarioTelefono" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaTelefono}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimUsuarioTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdPersona=${param.IdPersona}&CveUsuario=${param.CveUsuario}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimUsuarioTelefono&OperacionCatalogo=IN&Filtro=Alta&CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
    	
	<Portal:TablaForma maestrodetallefuncion="SimUsuarioRegional" nombre="Regionales a las que tiene acceso" funcion="SimUsuarioRegional" operacion="BA" parametros='CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaRegional}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='RegionBaja${registro.campos["ID_REGIONAL"]}' />
				<input type="hidden" name="IdRegional" value='<c:out value='${registro.campos["ID_REGIONAL"]}'/>'>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_REGIONAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_REGIONAL"]}'/>			
			</Portal:TablaListaRenglon>
			</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimUsuarioRegional&OperacionCatalogo=CT&Filtro=Todos&CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>
		
	<Portal:TablaForma maestrodetallefuncion="SimUsuarioSucursal" nombre="Sucursales a las que tiene acceso" funcion="SimUsuarioSucursal" operacion="BA" parametros='CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaSucursal}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='SucursalBaja${registro.campos["ID_SUCURSAL"]}' />
				<input type="hidden" name="IdSucursal" value='<c:out value='${registro.campos["ID_SUCURSAL"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimUsuarioSucursal&OperacionCatalogo=CT&Filtro=Todos&CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<script>
		document.frmRegistro.ApPaterno.onblur = fLlenaNombre;
		document.frmRegistro.ApMaterno.onblur = fLlenaNombre;
		document.frmRegistro.Nombre1.onblur = fLlenaNombre;
		document.frmRegistro.Nombre2.onblur = fLlenaNombre;
		function fLlenaNombre(){
			sNomCompleto = document.frmRegistro.Nombre1.value +' '+ document.frmRegistro.Nombre2.value +' '+ document.frmRegistro.ApPaterno.value +' '+ document.frmRegistro.ApMaterno.value;
			document.frmRegistro.NomCompleto.value = sNomCompleto;
		}
	
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
		