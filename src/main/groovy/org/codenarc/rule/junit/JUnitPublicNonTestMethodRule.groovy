/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.junit

import org.codehaus.groovy.ast.MethodNode
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Rule that checks if a JUnit test class contains public methods other than:
 * <ul>
 *   <li>Zero-argument methods with names starting with "test"</li>
 *   <li>The setUp() and tearDown() methods</li>
 *   <li>Methods annotated with @Test</li>
 *   <li>Methods annotated with @Before and @After</li>
 *   <li>Methods annotated with @BeforeClass and @AfterClass</li>
 * </ul>
 * Public, non-test methods on a test class violate conventional usage of test classes,
 * and can be confusing.
 * <p/>
 * Public, non-test methods may also hide unintentional 'Lost Tests'. For instance, the test method
 * declaration may accidentally include methods parameters, and thus be ignored by JUnit. Or the
 * method may accidentally not follow the "test.." naming convention and not have the @Test annotation,
 * and thus be ignored by JUnit.
 * <p/>
 * This rule sets the default value of <code>applyToFilesMatching</code> to only match source code file
 * paths ending in 'Test.groovy' or 'Tests.groovy'.
 *
 * @author Chris Mair
 * @version $Revision: 97 $ - $Date: 2009-03-25 20:36:13 -0400 (Wed, 25 Mar 2009) $
 */
class JUnitPublicNonTestMethodRule extends AbstractAstVisitorRule {
    String name = 'JUnitPublicNonTestMethod'
    int priority = 2
    Class astVisitorClass = JUnitPublicNonTestMethodAstVisitor
    String applyToFilesMatching = DEFAULT_TEST_FILES
}

class JUnitPublicNonTestMethodAstVisitor extends AbstractAstVisitor  {

    void visitMethod(MethodNode methodNode) {
        if ((methodNode.modifiers & MethodNode.ACC_PUBLIC)
            && !(methodNode.modifiers & MethodNode.ACC_STATIC)
            && !isTestMethod(methodNode)
            && !isZeroArgumentMethod(methodNode, 'setUp')
            && !isZeroArgumentMethod(methodNode, 'tearDown')
            && !AstUtil.getAnnotation(methodNode, 'Test')
            && !AstUtil.getAnnotation(methodNode, 'Before')
            && !AstUtil.getAnnotation(methodNode, 'After')
            && !AstUtil.getAnnotation(methodNode, 'BeforeClass')
            && !AstUtil.getAnnotation(methodNode, 'AfterClass') ) {

                addViolation(methodNode)
        }
        super.visitMethod(methodNode)
    }

    private boolean isZeroArgumentMethod(MethodNode methodNode, String methodName) {
        return methodNode.name == methodName &&
               methodNode.parameters.size() == 0
    }

    private boolean isTestMethod(MethodNode methodNode) {
        return (methodNode.modifiers & MethodNode.ACC_PUBLIC) &&
                methodNode.name.startsWith('test') &&
                methodNode.parameters.size() == 0
    }

}