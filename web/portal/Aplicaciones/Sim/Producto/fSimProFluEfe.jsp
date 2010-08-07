<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoFlujoEfectivo">
	<Portal:PaginaNombre titulo="Archivos de flujo de efectivo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoFlujoEfectivo' parametros='IdProducto=${param.IdProducto}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdArchivoFlujo' controlvalor='${requestScope.registro.campos["ID_ARCHIVO_FLUJO"]}' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomArchivo' controlvalor='${requestScope.registro.campos["NOM_ARCHIVO"]}' controllongitud='30' controllongitudmax='30' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	