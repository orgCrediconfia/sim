<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimClienteGarantia">
	<Portal:PaginaNombre titulo="Garant&iacute;as" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteGarantia' parametros='IdPersona=${param.IdPersona}&IdExConyuge${param.IdExConyuge}&IdGarantia=${param.IdGarantia}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/CatalogosGenerales/fSimTipGarCon.jsp' nombreliga='Asignar Tipo de garantía' nomventana='VentanaTipoGarantía'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Tipo de Garantía' control='etiqueta-controlreferencia' controlnombre='NomTipoGarantia' controlvalor='${requestScope.registro.campos["NOM_TIPO_GARANTIA"]}' />
		<input type="hidden" name="IdTipoGarantia" value='<c:out value='${requestScope.registro.campos["ID_TIPO_GARANTIA"]}'/>' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='80' controllongitudmax='100' editarinicializado='true' obligatorio='false'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de factura o escritura' control='Texto' controlnombre='NumeroFacturaEscritura' controlvalor='${requestScope.registro.campos["NUMERO_FACTURA_ESCRITURA"]}' controllongitud='25' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de factura o escritura' contenedor='frmRegistro' controlnombre='FechaFacturaEscritura' controlvalor='${requestScope.registro.campos["FECHA_FACTURA_ESCRITURA"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Valor comercial en pesos' control='Texto' controlnombre='ValorComercial' controlvalor='${requestScope.registro.campos["VALOR_COMERCIAL"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Valor a garantiza en pesos' control='Texto' controlnombre='ValorAGarantizar' controlvalor='${requestScope.registro.campos["VALOR_A_GARANTIZAR"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Personas/fSimGarDepCon.jsp' nombreliga='Asignar garante depositario' nomventana='VentanarAsignarGaranteDepositario'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Garante Depositario' control='etiqueta-controlreferencia' controlnombre='NomCompletoGarante' controlvalor='${requestScope.registro.campos["NOM_GARANTE"]}' />
		<input type="hidden" name="IdPersonaGarante" value='<c:out value='${requestScope.registro.campos["ID_GARANTE_DEPOSITARIO"]}'/>' />
		<Portal:FormaElemento etiqueta='Porcentaje que cubre la garant&iacute;a' control='Texto' controlnombre='PorcCubreGarantia' controlvalor='${requestScope.registro.campos["PORC_CUBRE_GARANTIA"]}' controllongitud='7' controllongitudmax='9' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	