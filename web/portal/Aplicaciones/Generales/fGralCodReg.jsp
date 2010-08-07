<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesCodigoPostal">
	<Portal:PaginaNombre titulo="C&oacute;digo Postal" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='GeneralesCodigoPostal'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='C&oacute;digo postal' control='Texto' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}' controllongitud='6' controllongitudmax='6' editarinicializado='false' obligatorio='true' evento="onKeyDown='SoloNumeros();'" />
		<Portal:FormaElemento etiqueta='Referencia' control='Texto' controlnombre='IdReferPost' controlvalor='${requestScope.registro.campos["ID_REFER_POST"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true' evento="onKeyDown='SoloNumeros();'" />
		<Portal:FormaElemento etiqueta='Colonia/Asentamiento' control='Texto' controlnombre='NomAsentamiento' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Tipo asentamiento' control='Texto' controlnombre='TipoAsentamiento' controlvalor='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Delegaci&oacute;n/Municipio' control='Texto' controlnombre='NomDelegacion' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Ciudad' control='Texto' controlnombre='NomCiudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Estado' control='Texto' controlnombre='NomEstado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		<input type="hidden" name="FiltroRegresaDatos" value='<c:out value='${registroFiltro.campos["FILTRO_DATOS"]}'/>' />
	</Portal:Forma>
</Portal:Pagina>