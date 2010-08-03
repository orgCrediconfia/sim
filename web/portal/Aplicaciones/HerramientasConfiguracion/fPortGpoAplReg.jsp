<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionGrupoEmpresasAplicacion" >
	<Portal:PaginaNombre titulo="Aplicaciones asignadas al grupo de empresas" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionGrupoEmpresasAplicacion' operacion='IN' filtro='AplicacionesDisponibles'>
		<Portal:FormaElemento etiqueta='Clave empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Aplicacion' control='Texto' controlnombre='NomAplicacion' controlvalor='${param.NomAplicacion}' controllongitud='50' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Aplicaciones disponibles para asignarlas a la empresa" funcion="HerramientasConfiguracionGrupoEmpresasAplicacion" operacion="AL" parametros='&CveAplicacion=${param.CveAplicacion}&CveGpoEmpresa=${param.CveGpoEmpresa}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='50' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaAplicaciones}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor='' control='checkbox' controlnombre='AplicacionBaja${registro.campos["CVE_APLICACION"]}'>
					<input type="hidden" name="CveAplicacion" value='<c:out value='${registro.campos["CVE_APLICACION"]}'/>'>		
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnAlta' etiqueta='Alta de aplicaciones' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>

	
</Portal:Pagina>