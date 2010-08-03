<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoSector">
	<Portal:PaginaNombre titulo="Catálogo de Sector" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoSector'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='CveSector' controlvalor='${param.CveSector}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSector' controlvalor='${requestScope.registro.campos["NOM_SECTOR"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoActividadEconomica" nombre="Actividad Econ&oacute;nomica" funcion="SimCatalogoActividadEconomica" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaActividadEconomica}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='200' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ACTIVIDAD_ECONOMICA"]}' funcion='SimCatalogoActividadEconomica' operacion='CR' parametros='IdActividadEconomica=${registro.campos["ID_ACTIVIDAD_ECONOMICA"]}&IdGiro=${registro.campos["ID_GIRO"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ACTIVIDAD_ECONOMICA"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimCatalogoActividadEconomica&OperacionCatalogo=IN&Filtro=Alta&IdGiro=${registro.campos["ID_GIRO"]}'/>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
	
</Portal:Pagina>
