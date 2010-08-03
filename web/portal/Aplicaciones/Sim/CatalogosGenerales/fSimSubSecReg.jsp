<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoSubSector" precarga="SimCatalogoRama">
	<Portal:PaginaNombre titulo="Catálogo de SubSector" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoSubSector' parametros='CveSector=${param.CveSector}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveSubSector' controlvalor='${param.CveSubSector}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSubSector' controlvalor='${requestScope.registro.campos["NOM_SUB_SECTOR"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoRama" nombre="Ramas" funcion="SimCatalogoRama" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaRama}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_RAMA"]}' funcion='SimCatalogoRama' operacion='CR' parametros='CveSector=${registro.campos["CVE_SECTOR"]}&CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}&CveRama=${registro.campos["CVE_RAMA"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_RAMA"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimCatalogoRama&OperacionCatalogo=IN&Filtro=Alta&CveSector=${registro.campos["CVE_SECTOR"]}&CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
