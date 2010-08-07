<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionGrupoEmpresas" precarga="HerramientasConfiguracionPortal HerramientasConfiguracionGrupoEmpresasAplicacion">
	<Portal:PaginaNombre titulo="Grupo de empresas" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionGrupoEmpresas'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveGpoEmpresa' controlvalor='${requestScope.registro.campos["CVE_GPO_EMPRESA"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomGpoEmpresa' controlvalor='${requestScope.registro.campos["NOM_GPO_EMPRESA"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Tipo de letra</th>
			<td>
			<select name="TipoLetra" >
				<option value="DE" > Default </option>
				<option value="NO" > Normal </option>
				<option value="MA" > May&uacute;sculas</option>
			</select>
			</td>
		</tr> 
		<script> BuscaSelectOpcion(document.frmRegistro.TipoLetra,'<c:out value='${requestScope.registro.campos["TIPO_LETRA"]}'/>'); </script>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>		
	</Portal:Forma>

	<Portal:TablaLista maestrodetallefuncion="HerramientasConfiguracionPortal" tipo="alta" nombre="Portales " botontipo="url" url='/Aplicaciones/HerramientasConfiguracion/fPortGpoPorReg.jsp?OperacionCatalogo=AL&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Url encabezado'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Url pie pagina'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Url men&uacute;'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre ventana'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPortales}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='' >
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PORTAL"]}' funcion='HerramientasConfiguracionPortal' operacion='CR' parametros='CvePortal=${registro.campos["CVE_PORTAL"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["URL_ENCABEZADO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["URL_PIE_PAGINA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["URL_MENU_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_VENTANA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>
	
	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionGrupoEmpresasAplicacion" nombre="Aplicaciones asignadas al grupo de empresas" funcion="HerramientasConfiguracionGrupoEmpresasAplicacion" operacion="BA" parametros='CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='40' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='60%' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='10' valor='Bloqueado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaAplicaciones}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='AplicacionAlta${registro.campos["CVE_APLICACION"]}'>
					<input type="hidden" name="CveAplicacion" value='<c:out value='${registro.campos["CVE_APLICACION"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='60%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BLOQUEADO"]}' control='checkbox' controlnombre='Bloqueado${registro.campos["CVE_APLICACION"]}' />
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnBaja' etiqueta='Eliminar aplicaciones seleccionadas' />
			<Portal:Boton tipo='submit' nombre='btnModificar' etiqueta='Modificar bloqueo' />
			<Portal:Boton tipo='catalogo' etiqueta='Agregar nuevas aplicaciones' funcion='HerramientasConfiguracionGrupoEmpresasAplicacion' operacion='IN' filtro='AplicacionesDisponibles' parametros='&CveGpoEmpresa=${param.CveGpoEmpresa}&CvePortal=${requestScope.registro.campos["CVE_PORTAL"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:Url tipo='catalogo' nombreliga='Ver personas asignadas al grupo de empresas' funcion='GeneralesPersona' operacion='IN' filtro='Todos' parametros='CveGpoEmpresa=${requestScope.registro.campos["CVE_GPO_EMPRESA"]}'/>
	<br/>
	<br/>	
	<Portal:Url tipo='url' nombreliga='Ver usuarios asignados al grupo de empresas' url='/Aplicaciones/Generales/fGralUsuCon.jsp?CveGpoEmpresa=${requestScope.registro.campos["CVE_GPO_EMPRESA"]}'/>
</Portal:Pagina>
