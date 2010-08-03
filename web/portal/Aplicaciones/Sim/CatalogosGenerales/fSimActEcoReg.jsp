<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoActividadEconomica" precarga="SimCatalogoActividadEconomica">
	<Portal:PaginaNombre titulo="Catálogo de Actividad Economica" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoActividadEconomica' parametros='IdGiro=${param.IdGiro}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdActividadEconomica' controlvalor='${param.IdActividadEconomica}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomActividadEconomica' controlvalor='${requestScope.registro.campos["NOM_ACTIVIDAD_ECONOMICA"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
