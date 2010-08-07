<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoParentesco">
	<Portal:PaginaNombre titulo="Catálogo de Parentesco" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoParentesco'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdParentesco' controlvalor='${param.IdParentesco}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomParentesco' controlvalor='${requestScope.registro.campos["NOM_PARENTESCO"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
</Portal:Pagina>
