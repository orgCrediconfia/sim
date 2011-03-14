<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoCodigoPostal">
	<Portal:PaginaNombre titulo="C&oacute;digo Postal" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='SimCatalogoCodigoPostal'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<input type="hidden" name="Ventana" value='<c:out value='${param.Ventana}'/>' />
		<Portal:FormaElemento etiqueta='C&oacute;digo postal' control='Texto' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}' editarinicializado='false'/>
		<c:if test='${(requestScope.registro.campos["ID_REFER_POST"] != null)}'>
			<Portal:FormaElemento etiqueta='Referencia' control='Texto' controlnombre='IdReferPost' controlvalor='${requestScope.registro.campos["ID_REFER_POST"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true' />
		</c:if>
		<Portal:FormaElemento etiqueta='Colonia/Asentamiento' control='Texto' controlnombre='NomAsentamiento' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Tipo asentamiento</th>
			<td>
				<select name='TipoAsentamiento' size='1'>
					<option value='null'></option>
					<option value='AMPLIACION'>AMPLIACION</option>
					<option value='BARRIO'>BARRIO</option>
					<option value='CENTRO URBANO'>CENTRO URBANO</option>
					<option value='CIUDAD'>CIUDAD</option>
					<option value='COLONIA'>COLONIA</option>
					<option value='CONJUNTO HABITACIONAL'>CONJUNTO HABITACIONAL</option>
					<option value='EJIDO'>EJIDO</option>
					<option value='EXHACIENDA'>EXHACIENDA</option>
					<option value='FRACCIONAMIENTO'>FRACCIONAMIENTO</option>
					<option value='GRANJA'>GRANJA</option>
					<option value='HACIENDA'>HACIENDA</option>
					<option value='INGENIO'>INGENIO</option>
					<option value='PARQUE INDUSTRIAL'>PARQUE INDUSTRIAL</option>
					<option value='POBLADO COMUNAL'>POBLADO COMUNAL</option>
					<option value='PUEBLO'>PUEBLO</option>
					<option value='RANCHO O RANCHERIA'>RANCHO O RANCHERIA</option>
					<option value='URBANA'>URBANA</option>
					<option value='UNIDAD HABITACIONAL'>UNIDAD HABITACIONAL</option>
					<option value='VILLA'>VILLA</option>
					<option value='ZONA COMERCIAL'>ZONA COMERCIAL</option>
					<option value='ZONA FEDERAL'>ZONA FEDERAL</option>
					<option value='ZONA INDUSTRIAL'>ZONA INDUSTRIAL</option>
					<option value='ZONA URBANA'>ZONA URBANA</option>
				</select>
			</td>
		</tr>
		<script> BuscaSelectOpcion(document.frmRegistro.TipoAsentamiento,'<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}'/>'); </script>
		
		<Portal:FormaElemento etiqueta='Delegaci&oacute;n/Municipio' control='Texto' controlnombre='NomDelegacion' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}' editarinicializado='false'/>
		<Portal:FormaElemento etiqueta='Ciudad' control='Texto' controlnombre='NomCiudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}' editarinicializado='false'/>
		<Portal:FormaElemento etiqueta='Estado' control='Texto' controlnombre='NomEstado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}' editarinicializado='false'/>
		<Portal:FormaBotones>
			<c:if test='${(requestScope.registro.campos["ID_REFER_POST"] == null)}'>
				<input type='button' name='Alta' value='Alta' onClick='fAltaColonia()'/>
			</c:if>
			<c:if test='${(requestScope.registro.campos["ID_REFER_POST"] != null)}'>
				<input type='button' name='Aceptar' value='Aceptar' onClick='fModificacion()'/>
				<input type='button' name='Baja' value='Baja' onClick='fBaja()'/>
			</c:if>
		</Portal:FormaBotones>	
	</Portal:Forma>
	
	<script>
		function fAltaColonia(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=AL&Ventana="+document.frmRegistro.Ventana.value+"&CodigoPostal="+document.frmRegistro.CodigoPostal.value+"&NomAsentamiento="+document.frmRegistro.NomAsentamiento.value+"&TipoAsentamiento="+document.frmRegistro.TipoAsentamiento.value+"&NomDelegacion="+document.frmRegistro.NomDelegacion.value+"&NomCiudad="+document.frmRegistro.NomCiudad.value+"&NomEstado="+document.frmRegistro.NomEstado.value;
			document.frmRegistro.submit();
		}
		
		function fModificacion(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=MO&Ventana="+document.frmRegistro.Ventana.value+"&CodigoPostal="+document.frmRegistro.CodigoPostal.value+"&IdReferPost="+document.frmRegistro.IdReferPost.value;
			document.frmRegistro.submit();
		}
		
		function fBaja(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=BA&Ventana="+document.frmRegistro.Ventana.value+"&CodigoPostal="+document.frmRegistro.CodigoPostal.value+"&IdReferPost="+document.frmRegistro.IdReferPost.value;
			document.frmRegistro.submit();
		}
	</script>	
	
</Portal:Pagina>