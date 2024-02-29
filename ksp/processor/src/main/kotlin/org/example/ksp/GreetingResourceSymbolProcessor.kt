package org.example.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class GreetingResourceSymbolProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        return emptyList()
    }

    override fun finish() {
        // Non-kt file gets generated as a resource
        codeGenerator.createNewFileByPath(
            Dependencies(false),
            "org/example/ksp/Greeting",
            "txt",
        ).writer().use { writer ->
            writer.write("World")
        }
    }
}

class GreetingResourceSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GreetingResourceSymbolProcessor(environment.codeGenerator)
    }
}
