<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoTipoDocumentacion">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Tipo de Documentaci&oacute;n" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoTipoDocumentacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdTipoDocumentacion' controlvalor='${requestScope.registro.campos["ID_TIPO_DOCUMENTACION"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTipoDocumentacion' controlvalor='${requestScope.registro.campos["NOM_TIPO_DOCUMENTACION"]}' controllongitud='30' controllongitudmax='30' obligatorio='true'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	