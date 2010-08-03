<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoPapel" precarga="SimCatalogoPapelDetalle">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Tasa de Referencia" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPapel'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdPapel' controlvalor='${param.IdPapel}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPapel' controlvalor='${requestScope.registro.campos["NOM_TASA_REFERENCIA"]}' controllongitud='30' controllongitudmax='20' />
		<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidad' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='60' controllongitudmax='100' />			
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>		
	<Portal:TablaForma maestrodetallefuncion="SimCatalogoPapelDetalle" nombre="Consulta del detalle del papel" funcion="SimCatalogoPapelDetalle" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de publicaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Valor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPapelDetalle}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_TASA_REFERENCIA_DETALLE"]}' funcion='SimCatalogoPapelDetalle' operacion='CR' parametros='IdPapel=${registro.campos["ID_TASA_REFERENCIA"]}&IdPapelDetalle=${registro.campos["ID_TASA_REFERENCIA_DETALLE"]}' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_PUBLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["VALOR"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimCatalogoPapelDetalle' operacion='IN' parametros='&IdPapel=${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' />
		</Portal:FormaBotones>	
	</Portal:TablaForma>	
</Portal:Pagina>	