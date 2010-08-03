<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTabla" precarga="HerramientasConfiguracionTablaBitacora HerramientasConfiguracionTablaDependiente HerramientasConfiguracionTablaCampo HerramientasConfiguracionTablaFiltro HerramientasConfiguracionTablaDependienteCampos">
	<Portal:PaginaNombre titulo="Tablas del Sistema" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionTabla'>
		<Portal:FormaSeparador nombre="Datos Generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveTabla' controlvalor='${requestScope.registro.campos["CVE_TABLA"]}' controllongitud='40' controllongitudmax='80' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre en BD' control='Texto' controlnombre='NomTabla' controlvalor='${requestScope.registro.campos["NOM_TABLA"]}' controllongitud='40' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='DescTabla' controlvalor='${requestScope.registro.campos["DESC_TABLA"]}' controllongitud='50' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Baja l&oacute;gica' control='checkbox' controlnombre='bBajaLogica' controlvalor='${requestScope.registro.campos["B_BAJA_LOGICA"]}'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>

	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionTablaCampo" nombre="Campos de la tabla" funcion="HerramientasConfiguracionTablaCampo" operacion="BA" parametros='&CveTabla=${param.CveTabla}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre campo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Auto-incrementable'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre secuencia'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='CampoLlave'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaCampos}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='CampoBaja${registro.campos["NOM_CAMPO"]}' />
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_CAMPO"]}' funcion='HerramientasConfiguracionTablaCampo' operacion='CR' parametros='CveTabla=${param.CveTabla}&NomCampo=${registro.campos["NOM_CAMPO"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["B_AUTO_INCREMENTABLE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SECUENCIA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["B_CAMPO_LLAVE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='url' etiqueta='Alta' url='/Aplicaciones/HerramientasConfiguracion/fTablTabCamReg.jsp?OperacionCatalogo=AL&CveTabla=${param.CveTabla}'/>
		</Portal:FormaBotones>				
	</Portal:TablaForma>

	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionTablaFiltro" nombre="Filtros de b&uacute;squeda" funcion="HerramientasConfiguracionTablaFiltro" operacion="BA" parametros='&CveTabla=${param.CveTabla}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clave filtro'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Muestra Sql en consola'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaFiltros}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='CampoBaja${registro.campos["CVE_FILTRO"]}' />
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_FILTRO"]}' funcion='HerramientasConfiguracionTablaFiltro' operacion='CR' parametros='CveTabla=${param.CveTabla}&CveFiltro=${registro.campos["CVE_FILTRO"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["B_MUESTRA_CODIGO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='url' etiqueta='Alta' url='/Aplicaciones/HerramientasConfiguracion/fTablTabFilReg.jsp?OperacionCatalogo=AL&CveTabla=${param.CveTabla}'/>
		</Portal:FormaBotones>				
	</Portal:TablaForma>
 
	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionTablaDependiente" nombre="Tablas dependientes" funcion="HerramientasConfiguracionTablaDependiente" operacion="BA" parametros='CveTabla=${param.CveTabla}'>         
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave tabla'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Campos diferentes'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaTablasDependientes}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='Tabla${registro.campos["CVE_TABLA_DEPENDIENTE"]}' />
				<Portal:Columna tipovalor='texto' ancho='100' valor='' >
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_TABLA_DEPENDIENTE"]}' funcion='HerramientasConfiguracionTablaDependienteCampos' operacion='CT' filtro='Todos' parametros='CveTabla=${param.CveTabla}&CveTablaDependiente=${registro.campos["CVE_TABLA_DEPENDIENTE"]}' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["B_CAMPOS_DIFERENTES"]}' control='checkbox' controlnombre='Modificacion${registro.campos["CVE_TABLA_DEPENDIENTE"]}'>
					<input type="hidden" name="CveTablaDependiente" value='<c:out value='${registro.campos["CVE_TABLA_DEPENDIENTE"]}'/>'>		
				</Portal:Columna>				
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' nombre='btnBaja' />
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='HerramientasConfiguracionTablaDependiente' operacion='CT' filtro="TablasDisponibles" parametros='&CveTabla=${param.CveTabla}'/>
			<Portal:Boton tipo='submit' nombre='btnModificar' etiqueta='Modificar campos diferentes' />			
		</Portal:FormaBotones>	
	</Portal:TablaForma>

	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionTablaBitacora" nombre="Tablas relacionadas para bitacorar" funcion="HerramientasConfiguracionTablaBitacora" operacion="BA" parametros='CveTabla=${param.CveTabla}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Clave tabla'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaTablasBitacora}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='Tabla${registro.campos["CVE_TABLA_BITACORA"]}' />
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CVE_TABLA_BITACORA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='HerramientasConfiguracionTablaBitacora' operacion='CT' filtro="TablasDisponibles" parametros='&CveTabla=${param.CveTabla}'/>
		</Portal:FormaBotones>				
	</Portal:TablaForma>	
	
</Portal:Pagina>

