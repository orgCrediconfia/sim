<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<script src='/portal/Portales/HerramientasConfiguracion/lib/bFuncionesPersona.js'></script>
<Portal:Pagina funcion="GeneralesPersonaEmpresa">
	<Portal:PaginaNombre titulo="Persona" subtitulo="Consulta de datos" />

	<Portal:Forma tipo='catalogo' funcion='GeneralesPersonaEmpresa'>

		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave persona' control='etiqueta-controloculto' controlnombre='IdPersona' controlvalor='${requestScope.registro.campos["ID_PERSONA"]}' />
		<Portal:FormaElemento etiqueta='Personalidad fiscal' control='selector' controlnombre='PersonaFisica' controlvalor='${requestScope.registro.campos["B_PERSONA_FISICA"]}' editarinicializado='false' obligatorio='true' campoclave="Clave" campodescripcion="Descripcion" datosselector='${requestScope.ListaPersonalidad}' evento="onchange=fDeshabilitaPersonaFisica();"/>
		<Portal:FormaElemento etiqueta='EMail' control='etiqueta-controlreferencia' controlnombre='EMail' controlvalor='${requestScope.registro.campos["EMAIL"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='RFC' control='etiqueta-controlreferencia' controlnombre='Rfc' controlvalor='${requestScope.registro.campos["RFC"]}' controllongitud='20' controllongitudmax='17' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Telefono' control='etiqueta-controlreferencia' controlnombre='NumTel' controlvalor='${requestScope.registro.campos["NUM_TEL"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Empresa' control='selector' controlnombre='CveEmpresa' controlvalor='${requestScope.registro.campos["CVE_EMPRESA"]}' editarinicializado='false' obligatorio='true' campoclave="CVE_EMPRESA" campodescripcion="NOM_EMPRESA" datosselector='${requestScope.ListaEmpresas}'/>	

		<c:if test='${(requestScope.registro == null) or (requestScope.registro.campos["B_PERSONA_FISICA"] == "V")}'>
			<Portal:FormaSeparador nombre="Persona f&iacute;sica"/>
			<Portal:FormaElemento etiqueta='Apellido Paterno' control='etiqueta-controlreferencia' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Apellido Materno' control='etiqueta-controlreferencia' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomPersona' controlvalor='${requestScope.registro.campos["NOM_PERSONA"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='CURP' control='etiqueta-controlreferencia' controlnombre='Curp' controlvalor='${requestScope.registro.campos["CURP"]}' controllongitud='25' controllongitudmax='20' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Nombre Completo' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		</c:if>	

		<c:if test='${(requestScope.registro == null) or (requestScope.registro.campos["B_PERSONA_FISICA"] == "F")}'>
			<Portal:FormaSeparador nombre="Persona Moral"/>
			<Portal:FormaElemento etiqueta='Raz&oacute;n Social' control='etiqueta-controlreferencia' controlnombre='RazonSocial' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		</c:if>	
		
		<c:if test='${(requestScope.registro.campos["CVE_PAIS"] == "MEX ")}'>
			<Portal:FormaSeparador nombre="Direcci&oacute;n"/>
			<Portal:FormaElemento etiqueta='Pais' control='selector' controlnombre='CvePais' controlvalor='${requestScope.registro.campos["CVE_PAIS"]}' editarinicializado='false' obligatorio='true' campoclave="CVE_PAIS" campodescripcion="NOM_PAIS" datosselector='${requestScope.ListaPais}' evento="onchange=fDeshabilitaDireccion();" />
			<Portal:FormaElemento etiqueta='Calle' control='etiqueta-controlreferencia' controlnombre='Calle' controlvalor='${requestScope.registro.campos["CALLE"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
			<Portal:FormaElemento etiqueta='N&uacute;mero' control='etiqueta-controlreferencia' controlnombre='Numero' controlvalor='${requestScope.registro.campos["NUMERO"]}' controllongitud='16' controllongitudmax='16' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='C.P.' control='etiqueta-controlreferencia' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}' />
			<Portal:FormaElemento etiqueta='Asentamiento o colonia' control='etiqueta-controlreferencia' controlnombre='Colonia' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />
			<Portal:FormaElemento etiqueta='Delegaci&oacute;n  o municipio' control='etiqueta-controlreferencia' controlnombre='Municipio' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}' />
			<Portal:FormaElemento etiqueta='Ciudad' control='etiqueta-controlreferencia' controlnombre='Ciudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}' />
			<Portal:FormaElemento etiqueta='Estado' control='etiqueta-controlreferencia' controlnombre='Estado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}' />
		</c:if>	
		
		<c:if test='${(requestScope.registro.campos["CVE_PAIS"] != "MEX ")}'>				
			<!-- ESTOS CAMPOS SE LLENAN SOLO SI LA CLAVE DE PAIS NO ES MEX -->
			<Portal:FormaSeparador nombre="Direcci&oacute;n"/>
			<Portal:FormaElemento etiqueta='CodigoPostal' control='etiqueta-controlreferencia' controlnombre='CveCodigoPostal' controlvalor='${requestScope.registro.campos["CVE_CODIGO_POSTAL"]}' controllongitud='15' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Asentamiento o colonia' control='etiqueta-controlreferencia' controlnombre='NomAsentamientoExt' controlvalor='${requestScope.registro.campos["EX_NOM_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Tipo asentamiento' control='etiqueta-controlreferencia' controlnombre='TipoAsentamientoExt' controlvalor='${requestScope.registro.campos["EX_TIPO_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Ciudad' control='etiqueta-controlreferencia' controlnombre='NomCiudadExt' controlvalor='${requestScope.registro.campos["EX_NOM_CIUDAD"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Delegacion o municipio' control='etiqueta-controlreferencia' controlnombre='NomDelegacionExt' controlvalor='${requestScope.registro.campos["EX_NOM_DELEGACION"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Estado' control='etiqueta-controlreferencia' controlnombre='NomEstadoExt' controlvalor='${requestScope.registro.campos["EX_NOM_ESTADO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		</c:if>			

	</Portal:Forma>

</Portal:Pagina>