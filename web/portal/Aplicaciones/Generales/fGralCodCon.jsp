<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesCodigoPostal">

	<Portal:PaginaNombre titulo="C&oacute;digo Postal" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='GeneralesCodigoPostal' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='C&oacute;digo postal' control='Texto' controlnombre='CodigoPostal' controlvalor='${param.CodigoPostal}' controllongitud='6' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Colonia/Asentamiento' control='Texto' controlnombre='NomAsentamiento' controlvalor='${param.NomAsentamiento}' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<input type="hidden" name="FiltroRegresaDatos" value='<c:out value='${registroFiltro.campos["FILTRO_DATOS"]}'/>' />
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta cat&aacute;logo de c&oacute;digos postales" botontipo="url" url='/ProcesaCatalogo?Funcion=GeneralesCodigoPostal&OperacionCatalogo=IN&Filtro=Alta&FiltroRegresaDatos=${registroFiltro.campos["FILTRO_DATOS"]}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='10%' valor='C&oacute;digo postal'/>
			<Portal:Columna tipovalor='texto' ancho='50' valor='Referencia'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Colonia/Asentamiento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Tipo asentamiento'/>		
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Delegaci&oacute;n/Municipio'/>		
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Ciudad'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Estado'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='10%' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CODIGO_POSTAL"]}' funcion='GeneralesCodigoPostal' operacion='CR' parametros='CodigoPostal=${registro.campos["CODIGO_POSTAL"]}&IdReferPost=${registro.campos["ID_REFER_POST"]}' parametrosregreso='\'${registro.campos["CODIGO_POSTAL"]}\', \'${registro.campos["ID_REFER_POST"]}\', \'${registro.campos["NOM_ASENTAMIENTO"]}\', \'${registro.campos["NOM_DELEGACION"]}\', \'${registro.campos["NOM_CIUDAD"]}\', \'${registro.campos["NOM_ESTADO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_REFER_POST"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO_ASENTAMIENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_DELEGACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_CIUDAD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<input type="hidden" name="FiltroRegresaDatos2" value='<c:out value='${registroFiltro.campos["FILTRO_DATOS"]}'/>' />
	</Portal:TablaLista>	
	
	<script languaje="javascript">
		
		function RegresaDatos(CodigoPostal, IdReferPost, Colonia, Municipio, Ciudad, Estado){
			
			var padre = window.opener;
			
			if(document.frmRegistro.FiltroRegresaDatos.value == 'DomicilioFactura'){
				padre.document.frmRegistro.CodigoPostalDomFactura.value = CodigoPostal;
				padre.document.frmRegistro.IdReferPostDomFactura.value = IdReferPost;
				padre.document.getElementById('ColoniaDomFactura').innerHTML = Colonia;
				padre.document.getElementById('CodigoPostalDomFactura').innerHTML = CodigoPostal;
				padre.document.getElementById('MunicipioDomFactura').innerHTML = Municipio;
				padre.document.getElementById('CiudadDomFactura').innerHTML = Ciudad;
				padre.document.getElementById('EstadoDomFactura').innerHTML = Estado;
			}
			else{
				if(document.frmRegistro.FiltroRegresaDatos.value == 'DomicilioEntrega'){
					padre.document.frmRegistro.CodigoPostalDomEntrega.value = CodigoPostal;
					padre.document.frmRegistro.IdReferPostDomEntrega.value = IdReferPost;
					padre.document.getElementById('ColoniaDomEntrega').innerHTML = Colonia;
					padre.document.getElementById('CodigoPostalDomEntrega').innerHTML = CodigoPostal;
					padre.document.getElementById('MunicipioDomEntrega').innerHTML = Municipio;
					padre.document.getElementById('CiudadDomEntrega').innerHTML = Ciudad;
					padre.document.getElementById('EstadoDomEntrega').innerHTML = Estado;
				}
				else{
					if(window.name == "SoloCodigoPostal"){
						padre.document.frmRegistro.CodigoPostal.value = CodigoPostal;
						padre.document.getElementById('CodigoPostal').innerHTML = CodigoPostal;
					}
					else{
						padre.document.frmRegistro.CodigoPostal.value = CodigoPostal;
						padre.document.frmRegistro.IdReferPost.value = IdReferPost;
						padre.document.getElementById('Colonia').innerHTML = Colonia;
						padre.document.getElementById('CodigoPostal').innerHTML = CodigoPostal;
						padre.document.getElementById('Municipio').innerHTML = Municipio;
						padre.document.getElementById('Ciudad').innerHTML = Ciudad;
						padre.document.getElementById('Estado').innerHTML = Estado;
					}
				}
		    }
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.CodigoPostal.focus();
	</script>
	
</Portal:Pagina>
