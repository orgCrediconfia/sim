<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionAplicacion" precarga="HerramientasConfiguracionPerfil HerramientasConfiguracionAplicacionFuncion HerramientasConfiguracionMenu">
	<Portal:PaginaNombre titulo="Aplicaciones" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionAplicacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveAplicacion' controlvalor='${requestScope.registro.campos["CVE_APLICACION"]}' controllongitud='40' controllongitudmax='80' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomAplicacion' controlvalor='${requestScope.registro.campos["NOM_APLICACION"]}' controllongitud='40' controllongitudmax='80' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='TxDescAplicacion' controlvalor='${requestScope.registro.campos["TX_DESC_APLICACION"]}' controllongitud='50' controllongitudmax='10' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Url' control='Texto' controlnombre='Url' controlvalor='${requestScope.registro.campos["URL_APLICACION"]}' controllongitud='40' controllongitudmax='256' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Etiqueta Componente Men&uacute;' control='Texto' controlnombre='NomEtiquetaMenu' controlvalor='${requestScope.registro.campos["NOM_ETIQUETA_MENU"]}' controllongitud='40' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Bloqueado' control='SelectorLogico' controlnombre='Bloqueado' controlvalor='${requestScope.registro.campos["B_BLOQUEADO"]}' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Archivo Aplicaci&oacute;n Java' control='Texto' controlnombre='ArchivoAplicacion' controlvalor='${requestScope.registro.campos["ARCHIVO_APLICACION"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>

	<Portal:TablaLista maestrodetallefuncion="HerramientasConfiguracionMenu" tipo="alta" nombre="Menu" botontipo="url" url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionMenu&OperacionCatalogo=IN&CveAplicacion=${registro.campos["CVE_APLICACION"]}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Funcion'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Url funcion'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaMenu}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_OPCION"]}' funcion='HerramientasConfiguracionMenu' operacion='CR' parametros='CveOpcion=${registro.campos["CVE_OPCION"]}&CveAplicacion=${registro.campos["CVE_APLICACION"]}'/>
				</Portal:Columna>					
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["BLOQUE_INDENTADO"]}${registro.campos["NOM_OPCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["URL_FUNCION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>	
 
	<Portal:TablaLista maestrodetallefuncion="HerramientasConfiguracionPerfil" tipo="alta" nombre="Perfiles" botontipo="url" url='/Aplicaciones/HerramientasConfiguracion/fApliAplPefReg.jsp?OperacionCatalogo=AL&CveAplicacion=${registro.campos["CVE_APLICACION"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='50' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Bloqueado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPerfiles}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["CVE_PERFIL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_PERFIL"]}' funcion='HerramientasConfiguracionPerfil' operacion='CR' parametros='CvePerfil=${registro.campos["CVE_PERFIL"]}&CveAplicacion=${registro.campos["CVE_APLICACION"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BLOQUEADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>

	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionAplicacionFuncion" nombre="Funciones asignadas a la aplicaci&oacute;n" funcion="HerramientasConfiguracionAplicacionFuncion" operacion="BA" parametros='&CveAplicacion=${param.CveAplicacion}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='URL funci&oacuten'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaFunciones}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_FUNCION"]}' />
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["NOM_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["URL_FUNCION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='url' etiqueta='Alta' url='/Aplicaciones/HerramientasConfiguracion/fApliAplFunFil.jsp?OperacionCatalogo=CT&CveAplicacion=${registro.campos["CVE_APLICACION"]}'/>
			<input type="button" name="Imprimir" value="Generar Reporte" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=HerramientasConfiguracionAplicacionFuncionesReporte&CveAplicacion=<%=request.getParameter("CveAplicacion")%>&NomAplicacion=<%=request.getParameter("NomAplicacion")%>&TipoReporte=Pdf','ReporteEstadisticaFuncionAplicacion','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">
		</Portal:FormaBotones>				
	</Portal:TablaForma>	
</Portal:Pagina>
