<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionEmpresa">
	<Portal:PaginaNombre titulo="Empresa" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionEmpresa'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave Grupo Empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave Empresa' control='Texto' controlnombre='CveEmpresa' controlvalor='${requestScope.registro.campos["CVE_EMPRESA"]}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre de la Empresa' control='Texto' controlnombre='NomEmpresa' controlvalor='${requestScope.registro.campos["NOM_EMPRESA"]}' controllongitud='30' controllongitudmax='40' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n de la Empresa' control='TextoArea' controlnombre='TxDescEmpresa' controlvalor='${requestScope.registro.campos["TX_DESC_EMPRESA"]}' controllongitud='50' controllongitudmax='3' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>		
</Portal:Pagina>