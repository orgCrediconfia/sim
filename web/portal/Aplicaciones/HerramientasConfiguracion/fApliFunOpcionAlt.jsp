<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="HerramientasConfiguracionFuncionOperacion">
	<Portal:PaginaNombre titulo="Opciones de operaci&oacute;n" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionFuncionOperacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave funcion' control='etiqueta-controloculto' controlnombre='CveFuncion' controlvalor='${param.CveFuncion}'/>		
		<Portal:FormaElemento etiqueta='Operacion' control='selector' controlnombre='CveOperacion' controlvalor='${requestScope.registro.campos["CVE_OPERACION"]}' editarinicializado='false' obligatorio='true' campoclave="Clave" campodescripcion="Descripcion" datosselector='${requestScope.ListaOpciones}'/>
		<Portal:FormaElemento etiqueta='Codigo java' control='TextoArea' controlnombre='TxOperacion' controlvalor='${requestScope.registro.campos["TX_OPERACION"]}' controllongitud='135' controllongitudmax='30' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Depurar codigo' control='SelectorLogico' controlnombre='bDepurarCodigo' controlvalor='${requestScope.registro.campos["B_DEPURAR_CODIGO"]}' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		<input type="hidden" name="BajaIndividual" value='V'>
	</Portal:Forma>
</Portal:Pagina>