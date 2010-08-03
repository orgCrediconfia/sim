<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoFlujoEfectivo">
	<Portal:PaginaNombre titulo="Flujo de efectivo" subtitulo="Consulta de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoFlujoEfectivo' enviaarchivos='true'>
	
	<Portal:FormaSeparador nombre="Archivos"/>	 
			<Portal:FormaElemento etiqueta='foto' control='etiqueta-controloculto'  controlnombre='UrlAnterior11' controlvalor='${requestScope.registro.campos["ID_ARCHIVO_FLUJO"]}'  editarinicializado='false' obligatorio='true' />
			<Portal:FormaElemento etiqueta='Asignar foto' control='File' controlnombre='Url' controlvalor='' controllongitud='70' controllongitudmax='100' editarinicializado='true' obligatorio='false'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	
	</Portal:Forma>	
</Portal:Pagina>
