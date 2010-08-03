<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoAccesorio">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de accesorios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoAccesorio'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdAccesorio' controlvalor='${requestScope.registro.campos["ID_ACCESORIO"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomAccesorio' controlvalor='${requestScope.registro.campos["NOM_ACCESORIO"]}' controllongitud='50' controllongitudmax='100' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	