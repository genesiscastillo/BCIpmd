package cl.bci.pmd.rule.politica;

import java.util.HashSet;


import java.util.Set;

import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.ImportWrapper;

public class ClasesMetodosProhibidosRule extends AbstractJavaRule {

    private static Set<ImportWrapper> imports = new HashSet<ImportWrapper>();
    
    static{
    	imports.add(new ImportWrapper("wcorp.util.DateUtil", "wcorp.util.DateUtil")); 
    	imports.add(new ImportWrapper("wcorp.util.MailClient","wcorp.util.MailClient"));
    	//imports.add(new ImportWrapper("java.lang.System.*.println()  printStackTrace()
    	//imports.add(new ImportWrapper("LogFile.writeToLog()","LogFile.writeToLog()"));
    	//imports.add(new ImportWrapper("Locale.setDefault()"));
        //new.InitialContext()
    	//imports.add(new ImportWrapper("wcorp.log4j.*
    	imports.add(new ImportWrapper("wcorp.util.DigitoVerificador","wcorp.util.DigitoVerificador"));
    	imports.add(new ImportWrapper("wcorp.util.InvestmentPortalKeys","wcorp.util.InvestmentPortalKeys"));
    	imports.add(new ImportWrapper("wcorp.util.NegUtl","wcorp.util.NegUtl"));
    	imports.add(new ImportWrapper("wcorp.util.RegistroValor","wcorp.util.RegistroValor"));
    	imports.add(new ImportWrapper("wcorp.util.RutFormatter","wcorp.util.RutFormatter"));
    	imports.add(new ImportWrapper("wcorp.util.formator.CurrencyFormator","wcorp.util.formator.CurrencyFormator"));
    	imports.add(new ImportWrapper("wcorp.util.formator.DateFormator","wcorp.util.formator.DateFormator"));
    	imports.add(new ImportWrapper("wcorp.util.formator.Formator","wcorp.util.formator.Formator"));
    	imports.add(new ImportWrapper("wcorp.util.tareas.EnviarMailTareas","wcorp.util.tareas.EnviarMailTareas"));
    	imports.add(new ImportWrapper("wcorp.serv.reporte.util.FechaFormator","wcorp.serv.reporte.util.FechaFormator"));
    	imports.add(new ImportWrapper("wcorp.serv.reporte.util.MailSender","wcorp.serv.reporte.util.MailSender"));
    	imports.add(new ImportWrapper("wcorp.model.etiquetas.DateUtils","wcorp.model.etiquetas.DateUtils"));
    	imports.add(new ImportWrapper("wcorp.model.seguridad.ControlerBCI","wcorp.model.seguridad.ControlerBCI"));
    	imports.add(new ImportWrapper("wcorp.model.seguridad.SessionBCI","wcorp.model.seguridad.SessionBCI"));
    	imports.add(new ImportWrapper("wcorp.util.StrUtl","wcorp.util.StrUtl"));
    	//imports.add(new ImportWrapper("java.util.logging.*"));
    	imports.add(new ImportWrapper("wcorp.aplicaciones.operacion.contabilizacion.transacciones.to.AitcUtil","wcorp.aplicaciones.operacion.contabilizacion.transacciones.to.AitcUtil"));
    	//imports.add(new ImportWrapper("wcorp.ent.portal.*"
    	
    }

	public Object visit(ASTImportDeclaration node, Object data) {
        ImportWrapper wrapper = new ImportWrapper(node.getImportedName(), node.getImportedName(), node.getImportedNameNode());
        
        if (node.isImportOnDemand()) {
        	addViolation(data, node.getImportedNameNode(), node.getImportedNameNode().getImage());
        }else{
        	if(imports.contains(wrapper))	{
        		addViolation(data, node.getImportedNameNode(), node.getImportedNameNode().getImage());
        	}
        	else if( wrapper.getName().startsWith("wcorp.log4j") 
        			 ||	wrapper.getName().startsWith("java.util.logging")
        			 ||	wrapper.getName().startsWith("wcorp.ent.portal")){
        		addViolation(data, node.getImportedNameNode(), node.getImportedNameNode().getImage());
        	}
        }
        return data;
    }
}
