<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoRama" precarga="SimCatalogoSubRama">
	<Portal:PaginaNombre titulo="Catálogo de Rama" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoRama' parametros='CveSector=${param.CveSector}&CveSubSector=${param.CveSubSector}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveRama' controlvalor='${param.CveRama}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomRama' controlvalor='${requestScope.registro.campos["NOM_RAMA"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoSubRama" nombre="SubRamas" funcion="SimCatalogoSubRama" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaSubRama}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_SUB_RAMA"]}' funcion='SimCatalogoSubRama' operacion='CR' parametros='CveSector=${registro.campos["CVE_SECTOR"]}&CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}&CveRama=${registro.campos["CVE_RAMA"]}&CveSubRama=${registro.campos["CVE_SUB_RAMA"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUB_RAMA"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimCatalogoSubRama&OperacionCatalogo=IN&Filtro=Alta&CveSector=${registro.campos["CVE_SECTOR"]}&CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}&CveRama=${registro.campos["CVE_RAMA"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
