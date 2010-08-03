<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<%@ page language="java" import="com.rapidsist.comun.bd.Registro"%>
<%
    Registro registroHtml = null;
	registroHtml = (Registro)request.getAttribute("registro");
%>


<Portal:Pagina funcion="HerramientasConfiguracionFuncionHtml">
	<Portal:PaginaNombre titulo="Editor HTML"  subtituloalta="Captura de texto con formato"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionFuncionHtml'>
	
		<Portal:FormaSeparador nombre="C&oacute;digo HTML para funciones"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveHtml' controlvalor='${requestScope.registro.campos["CVE_HTML"]}' controllongitud='20' controllongitudmax='40' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomHtml' controlvalor='${requestScope.registro.campos["NOM_HTML"]}' controllongitud='30' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		</table>
		<BR>
		
		<Portal:editor id="CodigoHtml" basePath="/portal/comun/lib/FCKeditor2.4.3/"
			height="450">
			<% if(registroHtml!=null) { %>
				<%=registroHtml.getDefCampo("CODIGO_HTML")%>
			<% } %>
		</Portal:editor>

		<Portal:FormaBotonAltaModificacion/>
		<Portal:FormaBotonBaja/>
	</Portal:Forma>
	
	<script type="text/javascript">
		function FCKeditor_OnComplete( editorInstance )
		{
			window.status = editorInstance.Description ;
		}
	</script>
</Portal:Pagina>
