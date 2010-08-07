<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoRechazoComite">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de razones de rechazo del comit&eacute;" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoRechazoComite'>
	
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdRechazo' controlvalor='${param.IdRechazo}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre de rechazo' control='Texto' controlnombre='NomRechazo' controlvalor='${requestScope.registro.campos["NOM_RECHAZO"]}' controllongitud='20' controllongitudmax='20' />
		<Portal:FormaElemento etiqueta='Descripción' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='75' controllongitudmax='100' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	
	</Portal:Forma>
</Portal:Pagina>	
