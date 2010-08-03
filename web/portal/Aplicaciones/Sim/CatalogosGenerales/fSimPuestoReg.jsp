<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoPuesto">
	
	<Portal:PaginaNombre titulo="Catálogo de Puesto" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPuesto'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del Puesto' control='Texto' controlnombre='CvePuesto' controlvalor='${requestScope.registro.campos["CVE_PUESTO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPuesto' controlvalor='${requestScope.registro.campos["NOM_PUESTO"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave del Puesto al que depende' control='selector' controlnombre='CvePuestoPadre' controlvalor='${requestScope.registro.campos["CVE_PUESTO_PADRE"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_PUESTO" campodescripcion="NOM_PUESTO" datosselector='${requestScope.ListaPuesto}' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='false' />				
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
