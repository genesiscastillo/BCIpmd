package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoUsarCatchVacioRule extends AbstractJavaRule {
	
	public Object visit(ASTCatchStatement astCatchStatement , Object data){
		if(astCatchStatement.jjtGetChild(1).jjtGetNumChildren() == 0 ){
			addViolation(data, astCatchStatement , astCatchStatement.getImage() );
		}
		return data;
	}

}
