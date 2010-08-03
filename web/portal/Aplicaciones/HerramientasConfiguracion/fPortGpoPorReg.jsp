<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortal" precarga= "HerramientasConfiguracionEstilo HerramientasConfiguracionPortalAplicacion">
	<Portal:PaginaNombre titulo="Portal" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPortal'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave Grupo Empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave Portal' control='Texto' controlnombre='CvePortal' controlvalor='${requestScope.registro.campos["CVE_PORTAL"]}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre Portal' control='Texto' controlnombre='NomPortal' controlvalor='${requestScope.registro.campos["NOM_PORTAL"]}' controllongitud='30' controllongitudmax='40' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL Encabezado' control='Texto' controlnombre='UrlEncabezado' controlvalor='${requestScope.registro.campos["URL_ENCABEZADO"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL Pie P&aacute;gina' control='Texto' controlnombre='UrlPiePagina' controlvalor='${requestScope.registro.campos["URL_PIE_PAGINA"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL Men&uacute; Aplicaci&oacute;n' control='Texto' controlnombre='UrlMenuAplicacion' controlvalor='${requestScope.registro.campos["URL_MENU_APLICACION"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL Contenido Inicio' control='Texto' controlnombre='UrlContenidoInicio' controlvalor='${requestScope.registro.campos["URL_CONTENIDO_INICIO"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL Contenido Fin' control='Texto' controlnombre='UrlContenidoFin' controlvalor='${requestScope.registro.campos["URL_CONTENIDO_FIN"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre Ventana' control='Texto' controlnombre='NomVentana' controlvalor='${requestScope.registro.campos["NOM_VENTANA"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Tipo de Letra</th>
			<td>
			<select name="TipoLetra" >
				<option value="DE" > Default </option>
				<option value="NO" > Normal </option>
				<option value="MA" > May&uacute;sculas</option>
			</select>
			</td>
		</tr>
		
		<c:if test='${(requestScope.registro.campos["URL_ENCABEZADO"] != null)}'>
			<Portal:FormaElemento etiqueta='Tipo de Menú' control='selector' controlnombre='TipoMenu' controlvalor='${requestScope.registro.campos["TIPO_MENU"]}' editarinicializado='true' obligatorio='true' campoclave="TIPO_MENU" campodescripcion="NOM_TIPO_MENU" datosselector='${requestScope.ListaMenu}' />
	    	<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    	<a id="CatalogoColores" href="javascript:fCatalogoColor('<c:out value='${registro.campos["CVE_GPO_EMPRESA"]}'/>','<c:out value='${registro.campos["CVE_PORTAL"]}'/>');">Catálogo de color</a>			
			</Portal:FormaElemento>
		</c:if>	
		
		<script> BuscaSelectOpcion(document.frmRegistro.TipoLetra,'<c:out value='${requestScope.registro.campos["TIPO_LETRA"]}'/>'); </script>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>

	<Portal:TablaLista maestrodetallefuncion="HerramientasConfiguracionEstilo" tipo="alta" nombre="Estilos" botontipo="url" url='/Aplicaciones/HerramientasConfiguracion/fPortGpoPorEstReg.jsp?OperacionCatalogo=AL&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}&CvePortal=${registro.campos["CVE_PORTAL"]}&CveEstilo=${registro.campos["CVE_ESTILO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Url'/>			
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPortalEstilo}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' >
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_ESTILO"]}' funcion='HerramientasConfiguracionEstilo' operacion='CR' parametros='CvePortal=${registro.campos["CVE_PORTAL"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}&CveEstilo=${registro.campos["CVE_ESTILO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTILO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["URL_ESTILO"]}'/>				
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>

	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionPortalAplicacion" nombre="Aplicaciones asignadas al portal" funcion='HerramientasConfiguracionPortalAplicacion' operacion='BA' parametros='&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}&CvePortal=${requestScope.registro.campos["CVE_PORTAL"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='40' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripcion'/>	
			<Portal:Columna tipovalor='texto' ancho='10' valor='May&uacute;sculas'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPortalAplicacion}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='Aplicacion${registro.campos["CVE_APLICACION"]}' >
						<input type="hidden" name="CveAplicacion" value='<c:out value='${registro.campos["CVE_APLICACION"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='40' valor='${registro.campos["CVE_APLICACION"]}' />
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>		
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["TIPO_LETRA"]}' control='checkbox' controlnombre='Mayusculas${registro.campos["CVE_APLICACION"]}' />
				</Portal:TablaListaRenglon>
				
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='submit' nombre='btnModificarMayusculas' etiqueta='Modificar may&uacute;sculas' />
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='HerramientasConfiguracionPortalAplicacion' operacion='IN' filtro='AplicacionesDisponibles' parametros='&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}&CvePortal=${requestScope.registro.campos["CVE_PORTAL"]}'/>
		</Portal:FormaBotones>				
	</Portal:TablaForma>

	<script> 
	function fCatalogoColor(sCveGpoEmpresa,sCvePortal){
		if(document.frmRegistro.TipoMenu.value == "Horizontal"){	
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=HerramientasConfiguracionCatalogoColor&OperacionCatalogo=CR&CveGpoEmpresa='+sCveGpoEmpresa+'&CvePortal='+sCvePortal+'&Ventana=Si','Acepta','scrollbars=yes,resizable=yes,width=600,height=850');	
		} 
		
		if(document.frmRegistro.TipoMenu.value == "Vertical"){	
			alert("Este catálogo sólo es válido para el menú horizontal");
		} 
	}
	</script> 	
		
</Portal:Pagina>