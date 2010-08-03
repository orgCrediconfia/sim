<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoCatalogoConcepto">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Conceptos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoCatalogoConcepto'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveConcepto' controlvalor='${requestScope.registro.campos["CVE_CONCEPTO"]}' controllongitud='8' controllongitudmax='8' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='DescCorta' controlvalor='${requestScope.registro.campos["DESC_CORTA"]}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='DescLarga' controlvalor='${requestScope.registro.campos["DESC_LARGA"]}' controllongitud='60' controllongitudmax='60' editarinicializado='true' obligatorio='true' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>	