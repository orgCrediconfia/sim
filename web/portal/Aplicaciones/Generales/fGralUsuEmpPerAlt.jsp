<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesUsuarioEmpresaPerfil">
	<Portal:PaginaNombre titulo="Perfiles de usuario" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='CataAplPortal'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave usuario' control='etiqueta' controlvalor='${param.CveUsuario}'/>
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>

	<Portal:TablaForma nombre="Aplicaciones y perfiles disponibles para asignar al usuario" funcion='GeneralesUsuarioEmpresaPerfil' operacion='AL' parametros='CveUsuario=${param.CveUsuario}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave aplicacion'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave perfil'/>			
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaAplicaciones}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='AplicacionPerfil${registro.campos["CVE_APLICACION"]}___${registro.campos["CVE_PERFIL"]}'>
					<input type="hidden" name='ModificacionAplicacionPerfil' value='<c:out value='${registro.campos["CVE_APLICACION"]}___${registro.campos["CVE_PERFIL"]}'/>'>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["CVE_APLICACION"]}' />
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["CVE_PERFIL"]}'/>				
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<input type="hidden" name="CveGpoEmpresa" value='<c:out value='${param.CveGpoEmpresa}'/>' />
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>

	
</Portal:Pagina>