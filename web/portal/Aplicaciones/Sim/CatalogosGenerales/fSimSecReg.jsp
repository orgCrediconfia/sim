<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoSector" precarga="SimCatalogoSubSector">
	<Portal:PaginaNombre titulo="Catálogo de Sector" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoSector'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveSector' controlvalor='${param.CveSector}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSector' controlvalor='${requestScope.registro.campos["NOM_SECTOR"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoSubSector" nombre="SubSectores" funcion="SimCatalogoSubSector" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaSubSector}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_SUB_SECTOR"]}' funcion='SimCatalogoSubSector' operacion='CR' parametros='CveSubSector=${registro.campos["CVE_SUB_SECTOR"]}&CveSector=${registro.campos["CVE_SECTOR"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUB_SECTOR"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimCatalogoSubSector&OperacionCatalogo=IN&Filtro=Alta&CveSector=${registro.campos["CVE_SECTOR"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
