<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="CataUsuPerEmpresa">
	<Portal:PaginaNombre titulo="Perfil empresa" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta cat&aacute;logo de perfiles" botontipo="url" url='/ProcesaCatalogo?Funcion=CataUsuPerEmpresa&OperacionCatalogo=CR&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='10%' valor='Clave perfil '/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre perfil'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Descripcion aplicacion'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='10%' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PERFIL"]}' funcion='CataUsuPerEmpresa' operacion='CR' parametros='CvePerfil=${registro.campos["CVE_PERFIL"]}&CveAplicacion=${registro.campos["CVE_APLICACION"]}}'parametrosregreso='\'${registro.campos["CVE_PERFIL"]}\', \'${registro.campos["CVE_APLICACION"]}\', \'${registro.campos["TX_DESC_APLICACION"]}\', \'${registro.campos["NOM_PERFIL"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_PERFIL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>
	<script languaje="javascript">
		function RegresaDatos(CvePerfil,  CveAplicacion, TxDescAplicacion, NomPerfil){
			var padre = window.opener;
			padre.document.frmRegistro.CvePerfil.value = CvePerfil;
			padre.document.frmRegistro.CveAplicacion.value = CveAplicacion;
			padre.document.getElementById('TxDescAplicacion').innerHTML = TxDescAplicacion;
			padre.document.getElementById('NomPerfil').innerHTML = NomPerfil;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.CveAplicacion.focus();
	</script>	
</Portal:Pagina>
