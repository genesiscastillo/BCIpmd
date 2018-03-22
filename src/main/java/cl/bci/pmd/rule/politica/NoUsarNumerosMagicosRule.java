package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTRelationalExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoUsarNumerosMagicosRule extends AbstractJavaRule {
	
	public Object visit(ASTRelationalExpression astRelationalExpression ,  Object data){
		int numero = astRelationalExpression.jjtGetNumChildren();
		for(int i = 0 ; i < numero ; i++){
			if( astRelationalExpression.jjtGetChild(i) instanceof ASTPrimaryExpression){
				ASTPrimaryExpression astPrimaryExpression = (ASTPrimaryExpression)astRelationalExpression.jjtGetChild(i);
				if(astPrimaryExpression.jjtGetChild(0) instanceof ASTPrimaryPrefix){
					ASTPrimaryPrefix astPrimaryPrefix = (ASTPrimaryPrefix)astPrimaryExpression.jjtGetChild(0);
					
					if(astPrimaryPrefix.getFirstChildOfType(ASTLiteral.class) != null ){
						addViolation(data, astPrimaryPrefix , astPrimaryPrefix.getImage() );
					}
				}
			}
		}
		return data;
	}

}
