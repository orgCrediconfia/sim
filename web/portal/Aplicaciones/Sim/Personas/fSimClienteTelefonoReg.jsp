<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteTelefono">
	<Portal:PaginaNombre titulo="Telefono Cliente" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteTelefono'>
		<Portal:FormaSeparador nombre="Datos"/>
		<Portal:FormaElemento etiqueta='Clave  persona' control='etiqueta-controloculto' controlnombre='IdPersona' controlvalor='${param.IdPersona}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave GpoEmpresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Id Teléfono' control='etiqueta-controloculto' controlnombre='IdTelefono' controlvalor='${param.IdTelefono}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Teléfono' control='Texto' controlnombre='Telefono' controlvalor='${requestScope.registro.campos["TELEFONO"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' validadato='numerico' />
		
		<Portal:FormaElemento etiqueta='Tipo Identificador' control='selector' controlnombre='IdTipoIdentificador' controlvalor='${requestScope.registro.campos["CVE_TIPO_IDENTIFICADOR"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_TIPO_IDENTIFICADOR" campodescripcion="DESC_TIPO_IDENTIFICADOR" datosselector='${requestScope.ListaTipoIdentificador}'/>
		<Portal:FormaElemento etiqueta='Descripción Telefono' control='selector' controlnombre='IdDescTel' controlvalor='${requestScope.registro.campos["ID_DESC_TEL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_DESC_TEL" campodescripcion="NOM_DESC_TEL" datosselector='${requestScope.ListaDecripcionTelefono}'/>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
