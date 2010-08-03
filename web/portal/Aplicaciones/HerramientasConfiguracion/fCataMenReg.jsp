<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionMensaje">
	<Portal:PaginaNombre titulo="Mensajes del Sistema" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionMensaje'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveMensaje' controlvalor='${requestScope.registro.campos["CVE_MENSAJE"]}' controllongitud='50' controllongitudmax='80' editarinicializado='false' evento='onKeydown="SoloNumerosLetras()"' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Tipo' control='Texto' controlnombre='TipoMensaje' controlvalor='${requestScope.registro.campos["TIPO_MENSAJE"]}' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='TxMensaje' controlvalor='${requestScope.registro.campos["TX_MENSAJE"]}' controllongitud='50' controllongitudmax='10' controltexarealongmax='256' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>