<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoRecuperador">
	<Portal:PaginaNombre titulo="Recuperador" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoRecuperador' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='ClaveUsuario' controllongitud='20' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='35' controllongitudmax='35'/>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoRecuperador" operacion="AL" parametros='ClaveUsuario=${param.ClaveUsuario}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaRecuperador}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_USUARIO"]}' funcion='SimPrestamoRecuperador' operacion='CR' parametros='ClaveUsuario=${registro.campos["CVE_USUARIO"]}' parametrosregreso='\'${registro.campos["CVE_USUARIO"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	<script>
		function RegresaDatos(ClaveUsuario, NomCompleto){
			var padre = window.opener;
			padre.document.frmRegistro.IdRecuperador.value = ClaveUsuario;
			padre.document.getElementById('NomRecuperador').innerHTML = NomCompleto;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.NomCompleto.focus();
	</script>	
</Portal:Pagina>
