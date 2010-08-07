<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoClaseSinonimo">
	<Portal:PaginaNombre titulo="Sinónimos de la Clase" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoClaseSinonimo' parametros='CveSector=${param.CveSector}&CveSubSector=${param.CveSubSector}&CveRama=${param.CveRama}&CveSubRama=${param.CveSubRama}&CveClase=${param.CveClase}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveSinonimo' controlvalor='${param.CveSinonimo}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSinonimo' controlvalor='${requestScope.registro.campos["NOM_SINONIMO"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
