/*
 * Copyright 2010 the original author or authors.
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
package org.codenarc.rule.concurrency

import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.rule.AbstractAstVisitor
import org.codehaus.groovy.ast.FieldNode
import java.lang.reflect.Modifier

/**
 * ThreadLocal fields should be static and final. In the most common case a java.lang.ThreadLocal 
 * instance associates state with a thread. A non-static non-final java.lang.ThreadLocal field
 * associates state with an instance-thread combination. This is seldom necessary and often a
 * bug which can cause memory leaks and possibly incorrect behavior.
 *
 * @author Hamlet D'Arcy
 * @version $Revision$ - $Date$
 */
class ThreadLocalNotStaticFinalRule extends AbstractAstVisitorRule {

     String name = 'ThreadLocalNotStaticFinal'
     int priority = 2
     Class astVisitorClass = ThreadLocalNotStaticFinalAstVisitor
}

class ThreadLocalNotStaticFinalAstVisitor extends AbstractAstVisitor {

    def void visitFieldEx(FieldNode node) {
        if (node?.type?.name == "ThreadLocal") {
            if ((node.modifiers & Modifier.STATIC) != Modifier.STATIC ||
                    (node.modifiers & Modifier.FINAL) != Modifier.FINAL) {
                addViolation(node)
            }
        }
        super.visitFieldEx(node);
    }
}
