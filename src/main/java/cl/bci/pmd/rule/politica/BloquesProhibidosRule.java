package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class BloquesProhibidosRule extends AbstractJavaRule	{
	
	public Object visit(ASTPrimaryExpression block, Object data)	{
		for(int i = 0 ; i < block.jjtGetNumChildren() ; i++){
			if(block.jjtGetChild(i) instanceof ASTPrimaryPrefix){
				ASTPrimaryPrefix astPrimaryPrefix = (ASTPrimaryPrefix)block.jjtGetChild(i);
				if(astPrimaryPrefix.jjtGetChild(0) instanceof ASTAllocationExpression){
					ASTAllocationExpression allocationExpression = (ASTAllocationExpression)astPrimaryPrefix.jjtGetChild(0);
					for(int j = 0 ; j < allocationExpression.jjtGetNumChildren() ; j++){
						if(allocationExpression.jjtGetChild(j) instanceof ASTClassOrInterfaceType ){
							ASTClassOrInterfaceType astClassOrInterfaceType = (ASTClassOrInterfaceType) allocationExpression.jjtGetChild(j);
							if("InitialContext".equals(astClassOrInterfaceType.getImage())){
								addViolation(data, astClassOrInterfaceType , astClassOrInterfaceType.getImage() );
							}
						}
					}
				}
			}
		}
		return data;
	}

}
