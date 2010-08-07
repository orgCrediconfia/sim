<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoClase" precarga="SimCatalogoClaseSinonimo">
	<Portal:PaginaNombre titulo="Catálogo de Clase" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoClase' parametros='CveSector=${param.CveSector}&CveSubSector=${param.CveSubSector}&CveRama=${param.CveRama}&CveSubRama=${param.CveSubRama}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveClase' controlvalor='${param.CveClase}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomClase' controlvalor='${requestScope.registro.campos["NOM_CLASE"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoClaseSinonimo" nombre="Sinónimos" funcion="SimCatalogoClaseSinonimo" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaClaseSinonimo}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_SINONIMO"]}' funcion='SimCatalogoClaseSinonimo' operacion='CR' parametros='CveClase=${registro.campos["CVE_CLASE"]}&CveSinonimo=${registro.campos["CVE_SINONIMO"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SINONIMO"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimCatalogoClaseSinonimo&OperacionCatalogo=IN&Filtro=Alta&CveSector=${registro.campos["CVE_SECTOR"]}&CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}&CveRama=${registro.campos["CVE_RAMA"]}&CveSubRama=${registro.campos["CVE_SUB_RAMA"]}&CveClase=${registro.campos["CVE_CLASE"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
