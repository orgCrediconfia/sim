<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteCuentaBancaria">
	<Portal:PaginaNombre titulo="Cuenta bancaria del Cliente" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteCuentaBancaria' parametros='IdPersona=${param.IdPersona}&IdCuenta=${param.IdCuenta}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos"/>
		
		<Portal:FormaElemento etiqueta='Banco' control='selector' controlnombre='CveBanco' controlvalor='${requestScope.registro.campos["CVE_BANCO"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_BANCO" campodescripcion="NOM_BANCO" datosselector='${requestScope.ListaBanco}'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de cuenta' control='Texto' controlnombre='NumeroCuenta' controlvalor='${requestScope.registro.campos["NUMERO_CUENTA"]}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Clabe' control='Texto' controlnombre='Clabe' controlvalor='${requestScope.registro.campos["CLABE"]}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' validadato='numerico'/>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
