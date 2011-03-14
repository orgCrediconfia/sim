<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoCodigoPostal">

	<Portal:PaginaNombre titulo="C&oacute;digo Postal" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoCodigoPostal' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='C&oacute;digo postal' control='Texto' controlnombre='CodigoPostal' controlvalor='${param.CodigoPostal}' controllongitud='6' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Colonia/Asentamiento' control='Texto' controlnombre='NomAsentamiento' controlvalor='${param.NomAsentamiento}' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<input type="hidden" name="Ventana" value='<c:out value='${param.Ventana}'/>' />
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta cat&aacute;logo de c&oacute;digos postales" funcion='SimCatalogoCodigoPostal' operacion='AL' parametros='CodigoPostal=${param.CodigoPostal}'>
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
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CODIGO_POSTAL"]}' funcion='SimCatalogoCodigoPostal' operacion='CR' parametros='Filtro=Consulta&CodigoPostal=${registro.campos["CODIGO_POSTAL"]}&IdReferPost=${registro.campos["ID_REFER_POST"]}' parametrosregreso='\'${registro.campos["CODIGO_POSTAL"]}\', \'${registro.campos["ID_REFER_POST"]}\', \'${registro.campos["NOM_ASENTAMIENTO"]}\', \'${registro.campos["NOM_DELEGACION"]}\', \'${registro.campos["NOM_CIUDAD"]}\', \'${registro.campos["NOM_ESTADO"]}\', \'${registro.campos["TIPO_ASENTAMIENTO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_REFER_POST"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ASENTAMIENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO_ASENTAMIENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_DELEGACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_CIUDAD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		
		
		<Portal:FormaBotones>
			<input type='button' name='Alta' value='Alta' onClick='fAltaColonia()'/>
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	
	
	<script>
	
		function fAltaColonia(){
			if (document.frmRegistro.CodigoPostal.value == ""){
				alert("Ingrese el Código Postal para dar de alta la colonia");
			}else{ 
				document.frmTablaForma.action="ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=CR&Filtro=AltaColonia&Ventana="+document.frmRegistro.Ventana.value+"&CodigoPostal="+document.frmRegistro.CodigoPostal.value;
				document.frmTablaForma.submit();
			}
		}
		
		function RegresaDatos(CodigoPostal, IdReferPost, Colonia, Municipio, Ciudad, Estado, TipoAsentamiento){
			var padre = window.opener;
			
			padre.document.getElementById('CodigoPostal').innerHTML = CodigoPostal;
			padre.document.getElementById('Colonia').innerHTML = Colonia;
			padre.document.getElementById('Ciudad').innerHTML = Ciudad;
			padre.document.getElementById('Municipio').innerHTML = Municipio;
			padre.document.getElementById('Estado').innerHTML = Estado;
		   
			padre.document.frmRegistro.CodigoPostal.value  = CodigoPostal;
			padre.document.frmRegistro.IdReferPost.value = IdReferPost;
	   		padre.document.frmRegistro.Colonia.value = Colonia;
	   		padre.document.frmRegistro.Ciudad.value = Ciudad;
	   		padre.document.frmRegistro.Municipio.value = Municipio;
	   		padre.document.frmRegistro.Estado.value = Estado;
	   		padre.document.frmRegistro.TipoAsentamiento.value = TipoAsentamiento;

			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.CodigoPostal.focus();

	</script>
	
</Portal:Pagina>
