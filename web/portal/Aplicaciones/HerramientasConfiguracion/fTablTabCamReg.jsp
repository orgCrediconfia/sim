<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTablaCampo">
	<Portal:PaginaNombre titulo="Campos de la tabla" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionTablaCampo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave tabla' control='etiqueta-controloculto' controlnombre='CveTabla' controlvalor='${param.CveTabla}'/>		
		<Portal:FormaElemento etiqueta='Nombre campo' control='Texto' controlnombre='NomCampo' controlvalor='${requestScope.registro.campos["NOM_CAMPO"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Auto incrementable' control='SelectorLogico' controlnombre='bAutoIncrementable' controlvalor='${requestScope.registro.campos["B_AUTO_INCREMENTABLE"]}' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre secuencia' control='Texto' controlnombre='NomSecuencia' controlvalor='${requestScope.registro.campos["NOM_SECUENCIA"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Campo llave' control='SelectorLogico' controlnombre='bCampoLlave' controlvalor='${requestScope.registro.campos["B_CAMPO_LLAVE"]}' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>