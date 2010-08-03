<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimParametroGlobalGrupo">
	<Portal:PaginaNombre titulo="Par&aacute;metros Globales del Sistema" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimParametroGlobalGrupo'>
		<Portal:FormaSeparador nombre="Par&aacute;metros Globales"/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de integrantes' control='Texto' controlnombre='NumIntegrante' controlvalor='${requestScope.registro.campos["NUM_INTEGRANTE"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='false' />
		<Portal:FormaElemento etiqueta='M&aacute;ximo de Tipo Ambulantes' control='Texto' controlnombre='MaxAmbulante' controlvalor='${requestScope.registro.campos["MAX_AMBULANTE"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='M&aacute;ximo de Tipo Cat&aacute;logo' control='Texto' controlnombre='MaxCatalogo' controlvalor='${requestScope.registro.campos["MAX_CATALOGO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='M&aacute;ximo de Integrantes en Riesgo' control='Texto' controlnombre='MaximoRiesgo' controlvalor='${requestScope.registro.campos["MAXIMO_RIESGO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>
