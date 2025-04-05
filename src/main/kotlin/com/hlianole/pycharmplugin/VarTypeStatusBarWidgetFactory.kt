package com.hlianole.pycharmplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import org.jetbrains.annotations.Nls

class VarTypeStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "VarTypeStatusBarWidget"

    @Nls
    override fun getDisplayName(): String = "Variable Type"

    override fun createWidget(project: Project): StatusBarWidget {
        return VarTypeStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {}

    override fun isAvailable(project: Project): Boolean = true

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}