<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimSucursalAsesor">

	<Portal:PaginaNombre titulo="Asesores" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Asesores" funcion="SimSucursalAsesor" operacion="AL" parametros='IdSucursal=${param.IdSucursal}&IdDomicilio=${param.IdDomicilio}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_ASESOR"]}_${registro.campos["CVE_USUARIO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_USUARIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	