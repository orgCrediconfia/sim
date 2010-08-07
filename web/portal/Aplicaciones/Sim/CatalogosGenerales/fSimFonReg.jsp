<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCatalogoFondeador" precarga="SimFondeadorLinea">
	<Portal:PaginaNombre titulo="Catálogo de Fondeador" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoFondeador'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveFondeador' controlvalor='${param.CveFondeador}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomFondeador' controlvalor='${requestScope.registro.campos["NOM_FONDEADOR"]}' controllongitud='75' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	<Portal:TablaForma maestrodetallefuncion="SimFondeadorLinea" nombre="Consulta de líneas de fondeo" funcion="SimFondeadorLinea" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='# Línea'/>
			<Portal:Columna tipovalor='texto' ancho='120' valor='Monto'/>
			<Portal:Columna tipovalor='texto' ancho='120' valor='Monto Disponible'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Tasa'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaLineas}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NUM_LINEA"]}' funcion='SimFondeadorLinea' operacion='CR' parametros='NumLinea=${registro.campos["NUM_LINEA"]}&CveFondeador=${registro.campos["CVE_FONDEADOR"]}' />
				</Portal:Columna>
				<Portal:Columna tipovalor='moneda' ancho='120' valor='$ ${registro.campos["MONTO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='120' valor='$ ${registro.campos["MONTO_DISPONIBLE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TASA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimFondeadorLinea' operacion='IN' parametros='&CveFondeador=${requestScope.registro.campos["CVE_FONDEADOR"]}' />
		</Portal:FormaBotones>	
	</Portal:TablaForma>	
</Portal:Pagina>	
