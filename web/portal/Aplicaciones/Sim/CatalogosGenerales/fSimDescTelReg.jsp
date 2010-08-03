<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoDescripcionTelefono">
	<Portal:PaginaNombre titulo="Catálogo Descripción del Teléfono" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoDescripcionTelefono'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Id Descripción Tel.' control='etiqueta-controloculto' controlnombre='IdDescTel' controlvalor='${param.IdDescTel}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomDescTel' controlvalor='${requestScope.registro.campos["NOM_DESC_TEL"]}' controllongitud='50' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
</Portal:Pagina>
