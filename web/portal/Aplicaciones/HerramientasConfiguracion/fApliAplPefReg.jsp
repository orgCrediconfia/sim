<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPerfil" precarga="HerramientasConfiguracionPerfilConfiguracion">
	<Portal:PaginaNombre titulo="Perfiles" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPerfil'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave aplicaci&oacute;n' control='etiqueta-controloculto' controlnombre='CveAplicacion' controlvalor='${param.CveAplicacion}' controllongitud='40' controllongitudmax='80' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave perfil' control='Texto' controlnombre='CvePerfil' controlvalor='${requestScope.registro.campos["CVE_PERFIL"]}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPerfil' controlvalor='${requestScope.registro.campos["NOM_PERFIL"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='DescPerfil' controlvalor='${requestScope.registro.campos["DESC_PERFIL"]}' controllongitud='30' controllongitudmax='5' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Bloqueado' control='SelectorLogico' controlnombre='Bloqueado' controlvalor='${requestScope.registro.campos["B_BLOQUEADO"]}' editarinicializado='true' obligatorio='true' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<input type="button" name="Imprimir" value="Generar Reporte" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=HerramientasConfiguracionPerfilFuncionesReporte&CveAplicacion=<%=request.getParameter("CveAplicacion")%>&CvePerfil=<%=request.getParameter("CvePerfil")%>&TipoReporte=Pdf','ReporteFuncionesPerfil','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">
	
	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionPerfilConfiguracion" nombre="Permisos para las funciones asignadas al perfil" funcion="HerramientasConfiguracionPerfilConfiguracion" operacion="BA" parametros='&CvePerfil=${registro.campos["CVE_PERFIL"]}&CveAplicacion=${registro.campos["CVE_APLICACION"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Consulta'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Alta'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Baja'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Modificaci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPermisos}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='FuncionBaja${registro.campos["CVE_FUNCION"]}'>
					<input type="hidden" name="CveFuncion" value='<c:out value='${registro.campos["CVE_FUNCION"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_CONSULTA"]}' control='checkbox' controlnombre='Consulta${registro.campos["CVE_FUNCION"]}' />
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_ALTA"]}' control='checkbox' controlnombre='Alta${registro.campos["CVE_FUNCION"]}' />
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BAJA"]}' control='checkbox' controlnombre='Baja${registro.campos["CVE_FUNCION"]}' />
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["B_MODIF"]}' control='checkbox' controlnombre='Modificacion${registro.campos["CVE_FUNCION"]}' />
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnBaja' etiqueta='Eliminar funciones seleccionadas' />
			<Portal:Boton tipo='submit' nombre='btnModificar' etiqueta='Modificar permisos' />
			<Portal:Boton tipo='catalogo' etiqueta='Agregar nuevas funciones' funcion='HerramientasConfiguracionPerfilConfiguracion' operacion='CT' filtro='FuncionesDisponibles' parametros='&CveAplicacion=${param.CveAplicacion}&CvePerfil=${requestScope.registro.campos["CVE_PERFIL"]}'/>
		</Portal:FormaBotones>
	</Portal:TablaForma>
</Portal:Pagina>