package com.hlianole.pycharmplugin

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyReferenceExpression
import com.jetbrains.python.psi.types.TypeEvalContext
import com.intellij.util.Consumer
import com.jetbrains.python.psi.PyTargetExpression
import java.awt.event.MouseEvent

class VarTypeStatusBarWidget(
    private val project: Project
) : StatusBarWidget, StatusBarWidget.TextPresentation {
    private var statusBar: StatusBar? = null
    private var currentText: String = ""
    private val caretListener = MyCaretListener()

    inner class MyCaretListener : CaretListener {
        override fun caretPositionChanged(event: CaretEvent) {
            val editor = event.editor
            updateStatusBar(editor)
        }
    }

    private fun updateStatusBar(editor: Editor) {
        val document = editor.document
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return

        if (psiFile !is PyFile) {
            currentText = "Not a Python file"
            statusBar?.updateWidget(ID())
            return
        }

        val offset = editor.caretModel.offset
        val element = psiFile.findElementAt(offset) ?: return

        val varType = getVarType(element)
        currentText = varType ?: "No type info"

        if (statusBar != null) {
            statusBar!!.updateWidget(ID())
        }
    }

    private fun getVarType(element: PsiElement): String? {
        if (element.parent is PyTargetExpression) {
            val typeEvalContext = TypeEvalContext.codeAnalysis(project, element.parent.containingFile)
            try {
                val type = typeEvalContext.getType(element.parent as PyTargetExpression)
                if (type != null) {
                    return type.name ?: "Unknown"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Plugin error"
            }
        }

        if (element.parent is PyReferenceExpression) {
            val typeEvalContext = TypeEvalContext.codeAnalysis(project, element.parent.containingFile)
            try {
                val type = typeEvalContext.getType(element.parent as PyReferenceExpression)
                if (type != null) {
                    return type.name ?: "Unknown"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Plugin error"
            }
        }
        return null
    }

    override fun ID(): String = "VarTypeStatusBarWidget"

    override fun getPresentation(): StatusBarWidget.WidgetPresentation {
        return this
    }

    override fun getAlignment(): Float = 0f

    override fun getText(): String {
        if (currentText.isEmpty()) {
            return ""
        } else {
            return "Type: $currentText"
        }
    }

    override fun getTooltipText(): String? = null

    override fun getClickConsumer(): Consumer<MouseEvent>? = null

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        EditorFactory.getInstance().eventMulticaster.addCaretListener(caretListener, this)

        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        if (editor != null) {
            updateStatusBar(editor)
        }
    }

    override fun dispose() {
        EditorFactory.getInstance().eventMulticaster.removeCaretListener(caretListener)
        statusBar = null
    }
}