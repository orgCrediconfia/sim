<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortalAplicacion" >
	<Portal:PaginaNombre titulo="Aplicaciones asignadas al portal" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPortalAplicacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave grupo empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}'/>
		<Portal:FormaElemento etiqueta='Clave portal' control='etiqueta-controloculto' controlnombre='CvePortal' controlvalor='${param.CvePortal}'/>
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Aplicaciones disponibles para asignar al portal" funcion='HerramientasConfiguracionPortalAplicacion' operacion='AL' parametros='CveGpoEmpresa=${param.CveGpoEmpresa}&CvePortal=${param.CvePortal}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='40' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripcion'/>			
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaAplicacionesEmpresa}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='Aplicacion${registro.campos["CVE_APLICACION"]}' />
				<Portal:Columna tipovalor='texto' ancho='40' valor='${registro.campos["CVE_APLICACION"]}' />
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>				
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
</Portal:Pagina>