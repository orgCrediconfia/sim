<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimEtapaPerfil">

	<Portal:PaginaNombre titulo="Perfiles" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Perfiles" funcion="SimEtapaPerfil" operacion="AL" parametros='IdEtapaPrestamo=${param.IdEtapaPrestamo}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Perfil'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPerfiles}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_PERFIL"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PERFIL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	