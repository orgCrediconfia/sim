<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesUsuario">
	<Portal:PaginaNombre titulo="Usuarios" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='GeneralesUsuario' operacion='CT'>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false'>
			<input type="hidden" name="CveGpoEmpresa" value='<c:out value='${param.CveGpoEmpresa}'/>' />
			<input type="hidden" name="CveAplicacion" value='<c:out value='${param.CveAplicacion}'/>' />
			<input type="hidden" name="Filtro" value='<c:out value='${param.Filtro}'/>' />
		</Portal:FormaElemento>		
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveUsuario' controlvalor='${param.CveUsuario}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<c:if test='${(param.Ventana eq "null") or (param.Ventana == null)}'>
			<input type="hidden" name="Ventana" value=null>
		</c:if>
		<c:if test='${(param.Ventana != null)}'>
			<input type="hidden" name="Situacion" value='AC' />
			<input type="hidden" name="Ventana" value='<%=request.getParameter("Ventana")%>'>
		</c:if>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta cat&aacute;logo usuarios" botontipo="url" url="/Aplicaciones/Generales/fGralUsuReg.jsp?OperacionCatalogo=AL&CveGpoEmpresa=${param.CveGpoEmpresa}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='10%' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='80%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Localidad'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Alias'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='10%' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_USUARIO"]}' funcion='GeneralesUsuario' operacion='CR' parametros='CveUsuario=${registro.campos["CVE_USUARIO"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}' parametrosregreso='\'${registro.campos["CVE_USUARIO"]}\', \'${registro.campos["NOM_COMPLETO"]}\'' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='80%' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["LOCALIDAD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ALIAS"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	<script>
		function RegresaDatos(CveUsuario,  NomCompleto){
			var padre = window.opener;
			padre.document.frmRegistro.CveUsuario.value = CveUsuario;
			padre.document.getElementById('CveUsuario').innerHTML = CveUsuario;
			padre.document.getElementById('NomCompleto').innerHTML = NomCompleto;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		if(document.frmRegistro.Ventana.value == 'Si' ){
			btnOperaciones.disabled = true
		}
	</script>
</Portal:Pagina>
