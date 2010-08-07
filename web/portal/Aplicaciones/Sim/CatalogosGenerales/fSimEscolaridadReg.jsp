<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoEscolaridad">
	<Portal:PaginaNombre titulo="Catálogo de Escolaridad" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoEscolaridad'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Id Escolaridad' control='etiqueta-controloculto' controlnombre='IdEscolaridad' controlvalor='${param.IdEscolaridad}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomEscolaridad' controlvalor='${requestScope.registro.campos["NOM_ESCOLARIDAD"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
</Portal:Pagina>
